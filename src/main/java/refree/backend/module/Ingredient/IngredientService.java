package refree.backend.module.Ingredient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import refree.backend.infra.exception.NotFoundException;
import refree.backend.infra.exception.ParsingException;
import refree.backend.module.Category.Category;
import refree.backend.module.Category.CategoryRepository;
import refree.backend.module.Ingredient.Dto.IngredientDto;
import refree.backend.module.Ingredient.Dto.IngredientResponseDto;
import refree.backend.module.Ingredient.Dto.IngredientSearch;
import refree.backend.module.Picture.Picture;
import refree.backend.module.Picture.PictureService;
import refree.backend.module.member.Member;
import refree.backend.module.member.MemberRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final PictureService pictureService;

    public void create(IngredientDto ingredientDto, MultipartFile file, Long memberId) {
        Picture picture = null;
        if (file != null) {
            String storageImageName = pictureService.saveImage(file);
            picture = pictureService.getPicture(storageImageName);
        }

        LocalDate localDateFromString = getLocalDateFromString(ingredientDto.getPeriod());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 계정입니다."));
        Category category = categoryRepository.findByName(ingredientDto.getCategory())
                .orElseThrow(() -> new NotFoundException("NOT_VALID_CATEGORY"));
        Ingredient ingredient = Ingredient.createIngredient(member, category, localDateFromString, ingredientDto, picture);
        ingredientRepository.save(ingredient);
    }

    @Transactional(readOnly = true)
    public List<IngredientResponseDto> view(Long ingredientId){
        Ingredient ingredient = ingredientRepository.findByIdFetchJoinImage(ingredientId);
        if (ingredient == null)
            throw new NotFoundException("존재하지 않는 재료");
        return List.of(IngredientResponseDto.getIngredientResponseDto(ingredient));
    }

    public void update(IngredientDto ingredientDto, MultipartFile file, Long ingredientId){
        Ingredient ingredient = ingredientRepository.findByIdFetchJoinImage(ingredientId);
        if (ingredient == null)
            throw new  NotFoundException("존재하지 않는 재료");

        Picture savePicture = null;
        if (file != null) { // 저장하려는 이미지 있음
            pictureService.updateImageCheck(file, ingredient);
            String storageImageName = pictureService.saveImage(file);
            savePicture = pictureService.getPicture(storageImageName);
        } else { // 저장하려는 이미지 : null
            pictureService.updateImageCheck(null, ingredient);
        }

        LocalDate localDateFromString = getLocalDateFromString(ingredientDto.getPeriod());
        Category category = categoryRepository.findByName(ingredientDto.getCategory())
                .orElseThrow(() -> new NotFoundException("NOT_VALID_CATEGORY"));
        ingredient.update(localDateFromString, category, ingredientDto, savePicture);
    }
    public List<Ingredient> findAllIngredient(int mem_id){
        return ingredientRepository.findAllIngredient(mem_id);
    }

    /*public List<Ingredient> closure(int mem_id){ //유통기한이 3일 남은 경우
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
    }*/

    /*public List<Ingredient> end(int mem_id){
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
    }*/

    @Transactional(readOnly = true)
    public List<IngredientResponseDto> search(IngredientSearch ingredientSearch, Member member) {
        List<Ingredient> ingredients = ingredientRepository.search(ingredientSearch, member.getId());
        return ingredients.stream()
                .map(IngredientResponseDto::getIngredientResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LocalDate getLocalDateFromString(String period) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        try {
            return LocalDate.parse(period, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            throw new ParsingException("PARSING_ERROR");
        }
    }
}
