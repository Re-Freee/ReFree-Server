package refree.backend.IngredientTest;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import refree.backend.module.Ingredient.Ingredient;
import refree.backend.module.Ingredient.IngredientService;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class CategorizeTest {
    @Autowired
    IngredientService ingredientService;

    @Test
    public void Category() throws Exception{
        Ingredient ingredient=new Ingredient();
        Ingredient ingredient2=new Ingredient();
        ingredient.setName("땅콩밥");
        ingredient2.setName("현미밥");

        String category=ingredientService.ingredientToCategory(ingredient.getName());
        String category2=ingredientService.ingredientToCategory(ingredient2.getName());

        assertEquals(category,category2);
    }


}
//package backend.refree;

//import org.json.simple.JSONArray;
//        import org.json.simple.JSONObject;
//        import org.json.simple.parser.JSONParser;
//        import org.json.simple.parser.ParseException;
//        import org.springframework.boot.test.context.SpringBootTest;
//        import org.springframework.core.io.ClassPathResource;
//        import org.springframework.core.io.DefaultResourceLoader;

//import javax.annotation.Resource;
//import javax.transaction.Transactional;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@SpringBootTest
//@Transactional
//public class Test {
//
//
//    private static String ingredientToCategory(String inputs)  throws IOException, ParseException{
//        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1390802/AgriFood/FdFood/getKoreanFoodFdFoodList"); /*URL*/
//        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=JFijwchWJy3Nc6ZJO%2BLrky1imhAw43gItboKn2EuJbkrJJhPd13DziheNccSaKwmHiPc%2FDz4Jx8Z5mvH5JG5bg%3D%3D"); /*Service Key*/
//        urlBuilder.append("&" + URLEncoder.encode("service_Type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml 과 json 형식 지원*/
//        urlBuilder.append("&" + URLEncoder.encode("Page_No","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
//        urlBuilder.append("&" + URLEncoder.encode("Page_Size","UTF-8") + "=" + URLEncoder.encode("20", "UTF-8")); /*한 페이지 결과 수*/
//        urlBuilder.append("&" + URLEncoder.encode("food_Name","UTF-8") + "=" + URLEncoder.encode(inputs, "UTF-8")); /*음식 명 (검색어 입력값 포함 검색)*/
//        URL url = new URL(urlBuilder.toString());
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Content-type", "application/json");
//        System.out.println("Response code: " + conn.getResponseCode());
//        BufferedReader rd;
//        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
//        } else {
//            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
//        }
//
//
//        JSONParser parser = new JSONParser();
//        JSONObject jsonObject = (JSONObject) parser.parse(rd);
//
//        rd.close();
//        conn.disconnect();
//        JSONObject cookObject=(JSONObject) jsonObject.get("response");
//        JSONArray recipeArray=(JSONArray) cookObject.get("list");
//        String index;
//        List<String> confirm=new ArrayList<>();
//        for(int i=0;i<recipeArray.size();i++){
//            JSONObject recipeObject=(JSONObject) recipeArray.get(i); //no 1을 가지고 온다
//            JSONArray recipeArray1=(JSONArray) recipeObject.get("food_List");
//            for(int j=0;j<recipeArray1.size();j++){
//                JSONObject checks=(JSONObject) recipeArray1.get(j);
//                index=(String)checks.get("fd_Eng_Nm");
//                confirm.add(index);
//            }
//        }
//        List<String> outside=new ArrayList<>();
//        for(int i=0;i<confirm.size();i++){
//            String inside=confirm.get(i);
//            String[] insideArray=inside.split(",");
//            outside.add(insideArray[0]);
//        }
//
//        // 각 원소의 빈도를 계산하여 Map에 저장
//        Map<String, Integer> frequencyMap = new HashMap<>();
//        for (String element : outside) {
//            frequencyMap.put(element, frequencyMap.getOrDefault(element, 0) + 1);
//        }
//
//        // 최댓값을 찾기 위해 최댓값을 초기화
//        int maxFrequency = 0;
//        String mostFrequentElement = null;
//
//        // Map의 엔트리들을 순회하면서 최댓값을 찾음
//        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
//            String element = entry.getKey();
//            int frequency = entry.getValue();
//            if (frequency > maxFrequency) {
//                maxFrequency = frequency;
//                mostFrequentElement = element;
//            }
//        }
//        return mostFrequentElement;
//    }
//
//    public static void main(String[] args) throws IOException, ParseException {
//        String answer= ingredientToCategory("연두부");
//        System.out.println(answer);
//        final String inputs="케이크";//땅콩밥, 방울토마토, 흑토마토, 롤케이크
//        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1390802/AgriFood/FdFood/getKoreanFoodFdFoodList"); /*URL*/
//        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=JFijwchWJy3Nc6ZJO%2BLrky1imhAw43gItboKn2EuJbkrJJhPd13DziheNccSaKwmHiPc%2FDz4Jx8Z5mvH5JG5bg%3D%3D"); /*Service Key*/
//        urlBuilder.append("&" + URLEncoder.encode("service_Type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml 과 json 형식 지원*/
//        urlBuilder.append("&" + URLEncoder.encode("Page_No","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
//        urlBuilder.append("&" + URLEncoder.encode("Page_Size","UTF-8") + "=" + URLEncoder.encode("20", "UTF-8")); /*한 페이지 결과 수*/
//        urlBuilder.append("&" + URLEncoder.encode("food_Name","UTF-8") + "=" + URLEncoder.encode(inputs, "UTF-8")); /*음식 명 (검색어 입력값 포함 검색)*/
//        URL url = new URL(urlBuilder.toString());
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Content-type", "application/json");
//        System.out.println("Response code: " + conn.getResponseCode());
//        BufferedReader rd;
//        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
//        } else {
//            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
//        }
//
//
//        JSONParser parser = new JSONParser();
//        JSONObject jsonObject = (JSONObject) parser.parse(rd);
//
//        rd.close();
//        conn.disconnect();
//        JSONObject cookObject=(JSONObject) jsonObject.get("response");
//        JSONArray recipeArray=(JSONArray) cookObject.get("list");
//        String index;
//        List<String> confirm=new ArrayList<>();
//        for(int i=0;i<recipeArray.size();i++){
//            JSONObject recipeObject=(JSONObject) recipeArray.get(i); //no 1을 가지고 온다
//            JSONArray recipeArray1=(JSONArray) recipeObject.get("food_List");
//            for(int j=0;j<recipeArray1.size();j++){
//                JSONObject checks=(JSONObject) recipeArray1.get(j);
//                index=(String)checks.get("fd_Eng_Nm");
//                confirm.add(index);
//            }
//        }
//        List<String> outside=new ArrayList<>();
//        for(int i=0;i<confirm.size();i++){
//            String inside=confirm.get(i);
//            String[] insideArray=inside.split(",");
//            outside.add(insideArray[0]);
//        }
//
//        // 각 원소의 빈도를 계산하여 Map에 저장
//        Map<String, Integer> frequencyMap = new HashMap<>();
//        for (String element : outside) {
//            frequencyMap.put(element, frequencyMap.getOrDefault(element, 0) + 1);
//        }
//
//        // 최댓값을 찾기 위해 최댓값을 초기화
//        int maxFrequency = 0;
//        String mostFrequentElement = null;
//
//        // Map의 엔트리들을 순회하면서 최댓값을 찾음
//        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
//            String element = entry.getKey();
//            int frequency = entry.getValue();
//            if (frequency > maxFrequency) {
//                maxFrequency = frequency;
//                mostFrequentElement = element;
//            }
//        }
//        System.out.println(mostFrequentElement);
//        ClassPathResource resource=new ClassPathResource("recipe_data1.json");
//        Map<String,String> food=new HashMap<>();
//
//        JSONParser parser = new JSONParser();
//        JSONObject jsonObject = (JSONObject) parser
//                .parse(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
//
//        JSONObject cookObject=(JSONObject) jsonObject.get("COOKRCP01");
//        JSONArray recipeArray=(JSONArray) cookObject.get("row");
//        String index;
//        for(int i=0;i<recipeArray.size();i++){
//            JSONObject recipeObject=(JSONObject) recipeArray.get(i);
//            index=(String) recipeObject.get("RCP_PARTS_DTLS");
//            System.out.println(index);
//        }


//        String jsonString=sb.toString();
//        System.out.println(jsonString);
//        int endIndex=jsonString.length()-2;
//        String modified=jsonString.substring(0,endIndex);
//        String[] input=modified.split("\"fd_Eng_Nm\":\"");
//        String[] input2;
//        if(input.length>2){
//            input2=input[2].split(",");
//        }
//        else{
//            input2=input[1].split(",");
//        }
//        System.out.println(input2[0]);

//input2[0] : 이 재료의 최종 카테고리로 추가
//        for(int i=0;i<checking.length;i++){
//            String inputs=checking[i];
//            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1390802/AgriFood/FdFood/getKoreanFoodFdFoodList"); /*URL*/
//            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=JFijwchWJy3Nc6ZJO%2BLrky1imhAw43gItboKn2EuJbkrJJhPd13DziheNccSaKwmHiPc%2FDz4Jx8Z5mvH5JG5bg%3D%3D"); /*Service Key*/
//            urlBuilder.append("&" + URLEncoder.encode("service_Type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml 과 json 형식 지원*/
//            urlBuilder.append("&" + URLEncoder.encode("Page_No","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
//            urlBuilder.append("&" + URLEncoder.encode("Page_Size","UTF-8") + "=" + URLEncoder.encode("20", "UTF-8")); /*한 페이지 결과 수*/
//            urlBuilder.append("&" + URLEncoder.encode("food_Name","UTF-8") + "=" + URLEncoder.encode(inputs, "UTF-8")); /*음식 명 (검색어 입력값 포함 검색)*/
//            URL url = new URL(urlBuilder.toString());
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Content-type", "application/json");
//            System.out.println("Response code: " + conn.getResponseCode());
//            BufferedReader rd;
//            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
//            } else {
//                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
//            }
//
//            StringBuilder sb = new StringBuilder();
//            String line;
//            while ((line = rd.readLine()) != null) {
//                sb.append(line);
//            }
//            rd.close();
//            conn.disconnect();
//
//
//            String jsonString=sb.toString();
//            int endIndex=jsonString.length()-2;
//            String modified=jsonString.substring(0,endIndex);
//            String[] input=modified.split("\"fd_Eng_Nm\":\""); //두번째 나오는 재료로 하기 input[1]를 고치기 (input[input.length
//            //System.out.println(input.length);
//            String[] input2;
//            if(input.length>2){
//                input2=input[2].split(",");
//            }
//            else{
//                if(input.length!=0)
//                    input2=input[1].split(",");
//                else
//                    input2="Null,Null".split(",");
//            }
//
//            food.put(inputs,input2[0]);
//        }
//
//        for (Map.Entry<String, String> entry : food.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            System.out.println(key + ": " + value);
//        }
//
//    }
//
//
//
//}

