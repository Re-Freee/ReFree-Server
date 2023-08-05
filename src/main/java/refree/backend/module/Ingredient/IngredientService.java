package refree.backend.module.Ingredient;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Transactional
@Component
public class IngredientService {
    @Value("${serviceKey}")
    private String serviceKey;
    private final IngredientRepository ingredientRepository;
    public IngredientService(IngredientRepository ingredientRepository){
        this.ingredientRepository=ingredientRepository;
    }
    public void create(Ingredient ingredient) throws IOException {
        ingredientRepository.save(ingredient);
    }
    public List<Ingredient> view(int ingredient_id){
        Optional<Ingredient> ingredient2= ingredientRepository.findById(ingredient_id);
        Ingredient ingredient1=ingredient2.get();
        List<Ingredient> checked=new ArrayList<>();
        checked.add(ingredient1);
        return checked;
    }
    public List<Ingredient> delete(int ingred_id, int cnt, String memo){
        ingredientRepository.delete(ingred_id,cnt,memo);
        Ingredient check=view(ingred_id).get(0);
        check.setQuantity(cnt);
        check.setContent(memo);
        List<Ingredient> checked=new ArrayList<>();
        checked.add(check);

        return checked;
    }
    public List<Ingredient> findAllIngredient(int mem_id){
        return ingredientRepository.findAllIngredient(mem_id);
    }
    public List<Ingredient> closure(int mem_id){ //유통기한이 3일 남은 경우
        List<Ingredient> check=findAllIngredient(mem_id);
        List<Ingredient> confirm = new ArrayList<>();
        int[] month={0,31,29,31,30,31,30,31,30,31,30,31,30};
        for(int i=0;i<check.size();i++){
            java.sql.Date expire=check.get(i).getPeriod();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formatted= dateFormat.format(expire);
            String[] format=formatted.split("-");

            // 현재 시간을 milliseconds로 얻어옵니다.
            long currentTimeMillis = System.currentTimeMillis();
            java.sql.Date currentDate = new java.sql.Date(currentTimeMillis);
            String formatted2=dateFormat.format(currentDate);
            String[] format2=formatted2.split("-");

            if(format[0].equals(format2[0])){//같은 년도, 같은 달
                if (format[1].equals(format2[1])){//같은 달
                    if(Integer.parseInt(format[2])-Integer.parseInt(format2[2])<=3 && Integer.parseInt(format[2])-Integer.parseInt(format2[2])>=0) {
                        confirm.add(check.get(i));
                    }
                }
                else {//다른 달
                    if(Integer.parseInt(format[0])-Integer.parseInt(format2[0])==1){
                        int checked=month[Integer.parseInt(format2[1])]-Integer.parseInt(format2[2])+1;
                        int checking=checked+Integer.parseInt(format[2]);
                        if (checking<=3 && checking>=0){
                            confirm.add(check.get(i));
                        }
                    }
                }
            }
            else if(Integer.parseInt(format[0])-Integer.parseInt(format2[0])==1){//다른 년도 12, 1월인 경우
                if(Integer.parseInt(format[1])==1&&Integer.parseInt(format2[1])==12){
                    int checked=month[Integer.parseInt(format2[1])]-Integer.parseInt(format2[2])+1;
                    int checking=checked+Integer.parseInt(format[2]);
                    if (checking<=3 && checking>=0){
                        confirm.add(check.get(i));
                    }
                }
            }

        }
        return confirm;
    }
    public List<Ingredient> end(int mem_id){
        List<Ingredient> check=findAllIngredient(mem_id);
        List<Ingredient> confirm =new ArrayList<>();
        for(int i=0;i<check.size();i++){
            java.sql.Date expire=check.get(i).getPeriod();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formatted= dateFormat.format(expire);
            String[] format=formatted.split("-");

            // 현재 시간을 milliseconds로 얻어옵니다.
            long currentTimeMillis = System.currentTimeMillis();
            java.sql.Date currentDate = new Date(currentTimeMillis);
            String formatted2=dateFormat.format(currentDate);
            String[] format2=formatted2.split("-");

            if(Integer.parseInt(format2[0])-Integer.parseInt(format[0])>0){
                confirm.add(check.get(i));
            }
            else if(format2[0].equals(format[0])&&Integer.parseInt(format2[1])-Integer.parseInt(format[1])>0){
                confirm.add(check.get(i));
            }
            else if(format2[0].equals(format[0])&&format2[1].equals(format[1])&&Integer.parseInt(format2[2])-Integer.parseInt(format[2])>0){
                confirm.add(check.get(i));
            }
        }
        return confirm;
    }

    public List<Ingredient> search(String searchKey,int mem_id){
        String searchKey1="%"+searchKey+"%";
        return ingredientRepository.search(searchKey1,mem_id);
    }

    public String ingredientToCategory(String check)  throws IOException, ParseException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1390802/AgriFood/FdFood/getKoreanFoodFdFoodList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "="+serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("service_Type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml 과 json 형식 지원*/
        urlBuilder.append("&" + URLEncoder.encode("Page_No","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("Page_Size","UTF-8") + "=" + URLEncoder.encode("20", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("food_Name","UTF-8") + "=" + URLEncoder.encode(check, "UTF-8")); /*음식 명 (검색어 입력값 포함 검색)*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
        }


        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(rd);

        rd.close();
        conn.disconnect();
        JSONObject cookObject=(JSONObject) jsonObject.get("response");
        JSONArray recipeArray=(JSONArray) cookObject.get("list");
        String index;
        List<String> confirm=new ArrayList<>();
        for(int i=0;i<recipeArray.size();i++){
            JSONObject recipeObject=(JSONObject) recipeArray.get(i); //no 1을 가지고 온다
            JSONArray recipeArray1=(JSONArray) recipeObject.get("food_List");
            for(int j=0;j<recipeArray1.size();j++){
                JSONObject checks=(JSONObject) recipeArray1.get(j);
                index=(String)checks.get("fd_Eng_Nm");
                confirm.add(index);
            }
        }
        List<String> outside=new ArrayList<>();
        for(int i=0;i<confirm.size();i++){
            String inside=confirm.get(i);
            String[] insideArray=inside.split(",");
            outside.add(insideArray[0]);
        }

        // 각 원소의 빈도를 계산하여 Map에 저장
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String element : outside) {
            frequencyMap.put(element, frequencyMap.getOrDefault(element, 0) + 1);
        }

        // 최댓값을 찾기 위해 최댓값을 초기화
        int maxFrequency = 0;
        String mostFrequentElement = null;

        // Map의 엔트리들을 순회하면서 최댓값을 찾음
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            String element = entry.getKey();
            int frequency = entry.getValue();
            if (frequency > maxFrequency) {
                maxFrequency = frequency;
                mostFrequentElement = element;
            }
        }
        return mostFrequentElement;
    }
}
