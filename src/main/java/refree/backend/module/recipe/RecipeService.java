package refree.backend.module.recipe;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import refree.backend.infra.exception.NotFoundException;
import refree.backend.module.ingredient.Ingredient;
import refree.backend.module.ingredient.IngredientRepository;
import refree.backend.module.recipe.Dto.*;
import refree.backend.module.recipe.RecommendStrategy.RecipeRecommendationStrategy;
import refree.backend.module.recipeLike.RecipeLikeRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeLikeRepository recipeLikeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeRecommendationStrategy normalRecommendationStrategy;
    private final RecipeRecommendationStrategy randomRecommendationStrategy;
    private static final int MAX_RECIPE_SIZE = 8;

    @PostConstruct
    public void initRecipeData() throws IOException, ParseException {
        if (recipeRepository.count() == 0) {
            ClassPathResource classPathResource1 = new ClassPathResource("recipe_data1.json");
            ClassPathResource classPathResource2 = new ClassPathResource("recipe_data2.json");
            ClassPathResource classPathResource3 = new ClassPathResource("recipe_data3.json");
            ClassPathResource classPathResource4 = new ClassPathResource("recipe_data4.json");
            ClassPathResource classPathResource5 = new ClassPathResource("recipe_data5.json");
            ClassPathResource classPathResource6 = new ClassPathResource("recipe_data6.json");
            ClassPathResource classPathResource7 = new ClassPathResource("recipe_data7.json");
            ClassPathResource classPathResource8 = new ClassPathResource("recipe_data8.json");
            ClassPathResource classPathResource9 = new ClassPathResource("recipe_data9.json");
            ClassPathResource classPathResource10 = new ClassPathResource("recipe_data10.json");
            ClassPathResource classPathResource11 = new ClassPathResource("recipe_data11.json");

            saveRecipeData(classPathResource1);
            saveRecipeData(classPathResource2);
            saveRecipeData(classPathResource3);
            saveRecipeData(classPathResource4);
            saveRecipeData(classPathResource5);
            saveRecipeData(classPathResource6);
            saveRecipeData(classPathResource7);
            saveRecipeData(classPathResource8);
            saveRecipeData(classPathResource9);
            saveRecipeData(classPathResource10);
            saveRecipeData(classPathResource11);
        }
    }

    private void saveRecipeData(ClassPathResource classPathResource) throws IOException, ParseException {
        List<Recipe> recipeList = new ArrayList<>();
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser
                .parse(new InputStreamReader(classPathResource.getInputStream(), StandardCharsets.UTF_8));

        JSONObject cookObject = (JSONObject) jsonObject.get("COOKRCP01");
        JSONArray recipeArray = (JSONArray) cookObject.get("row");
        for (int i = 0; i < recipeArray.size(); i++) {
            JSONObject recipeObject = (JSONObject) recipeArray.get(i);
            Recipe recipe = Recipe.builder()
                    .name((String) recipeObject.get("RCP_NM"))
                    .type((String) recipeObject.get("RCP_PAT2"))
                    .calorie(Double.parseDouble((String) recipeObject.get("INFO_ENG")))
                    .imageUrl((String) recipeObject.get("ATT_FILE_NO_MAIN"))
                    .ingredient((String) recipeObject.get("RCP_PARTS_DTLS"))
                    .manual1((String) recipeObject.get("MANUAL01"))
                    .manualUrl1((String) recipeObject.get("MANUAL_IMG01"))
                    .manual2((String) recipeObject.get("MANUAL02"))
                    .manualUrl2((String) recipeObject.get("MANUAL_IMG02"))
                    .manual3((String) recipeObject.get("MANUAL03"))
                    .manualUrl3((String) recipeObject.get("MANUAL_IMG03"))
                    .manual4((String) recipeObject.get("MANUAL04"))
                    .manualUrl4((String) recipeObject.get("MANUAL_IMG04"))
                    .manual5((String) recipeObject.get("MANUAL05"))
                    .manualUrl5((String) recipeObject.get("MANUAL_IMG05"))
                    .manual6((String) recipeObject.get("MANUAL06"))
                    .manualUrl6((String) recipeObject.get("MANUAL_IMG06"))
                    .build();
            recipeList.add(recipe);
        }
        recipeRepository.saveAll(recipeList);
    }

    public List<RecipeRecommendDto> recommend(Long memberId, RecommendRequest recommendRequest) {
        List<Ingredient> ingredients = ingredientRepository.findAllByMemberFetchJoinCategory(memberId);
        // 저장한 재료가 하나도 없다면 랜덤 추천
        if (ingredients.isEmpty()) {
            return randomRecommendationStrategy.recommend(ingredients);
        }
        // 저장한 재료들 중에서 소비기한 임박한 재료 찾기
        /*List<Ingredient> filteredIngredients = ingredients.stream()
                .filter(ingredient -> {
                    LocalDate expire = ingredient.getPeriod();
                    LocalDate now = LocalDate.now();
                    int days = Period.between(now, expire).getDays();
                    return days >= 0 && days <= 3;
                }).collect(Collectors.toList());*/
        List<Ingredient> filteredIngredients = new ArrayList<>();
        List<Ingredient> notOutdatedIngredients = new ArrayList<>();
        ingredients.forEach(ingredient -> {
            LocalDate expire = ingredient.getPeriod();
            LocalDate now = LocalDate.now();
            int days = Period.between(now, expire).getDays();
            if (days >= 0 && days <= 3) { // 임박 재료일 경우
                filteredIngredients.add(ingredient);
            }
            if (days > 3) { // 일반 재료일 경우
                notOutdatedIngredients.add(ingredient);
            }
        });

        List<Ingredient> resultIngredients;
        if (filteredIngredients.isEmpty()) {
            // 임박 재료 없음 최대 8개
            if (recommendRequest.getIsOutdatedOk()) { // 소비기한 지나도 포함 ok
                log.info("outdated ok");
                resultIngredients = new ArrayList<>(ingredients.subList(0, Math.min(ingredients.size(), MAX_RECIPE_SIZE)));
            } else {
                log.info("outdated not ok");
                if (notOutdatedIngredients.isEmpty()) {
                    log.info("but empty");
                    return randomRecommendationStrategy.recommend(ingredients);
                }
                resultIngredients = new ArrayList<>(notOutdatedIngredients.subList(0, Math.min(notOutdatedIngredients.size(), MAX_RECIPE_SIZE)));
            }
        } else {
            // 임박 재료 있음 최대 8개
            resultIngredients = new ArrayList<>(filteredIngredients.subList(0, Math.min(filteredIngredients.size(), MAX_RECIPE_SIZE)));
        }
        return normalRecommendationStrategy.recommend(resultIngredients);
    }

    @Transactional(readOnly = true)
    public List<RecipeDto> search(Long memberId, RecipeSearch recipeSearch) {
        List<Recipe> recipes = recipeRepository.searchRecipe(recipeSearch);
        // Recipe_like 테이블에서 member_id로 RecipeLike리스트 가져와서 recipe_id랑 같은지 비교
        Set<Long> set = recipeLikeRepository.likedRecipe(memberId);
        return recipes.stream()
                .map(recipe -> RecipeDto.getRecipeDto(recipe, set)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecipeViewDto> recipeView(Long memberId, Long recipeId) {
        // 레시피 존재하는지 확인
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new NotFoundException("NO_RECIPE_EXIST"));
        // 좋아요 여부 확인
        Boolean isHeart = recipeLikeRepository.existsByMemberIdAndRecipeId(memberId, recipeId);
        return List.of(RecipeViewDto.getRecipeViewDto(recipe, createManuelDtos(recipe), isHeart));
    }

    @Transactional(readOnly = true)
    public List<ManualDto> createManuelDtos(Recipe recipe) {
        List<ManualDto> manualDtos = new ArrayList<>();
        if (recipe.getManual1() != null && !recipe.getManual1().isEmpty()) {
            manualDtos.add(ManualDto.getManualDto(recipe.getManual1(), recipe.getManualUrl1()));
        }
        if (recipe.getManual2() != null && !recipe.getManual2().isEmpty()) {
            manualDtos.add(ManualDto.getManualDto(recipe.getManual2(), recipe.getManualUrl2()));
        }
        if (recipe.getManual3() != null && !recipe.getManual3().isEmpty()) {
            manualDtos.add(ManualDto.getManualDto(recipe.getManual3(), recipe.getManualUrl3()));
        }
        if (recipe.getManual4() != null && !recipe.getManual4().isEmpty()) {
            manualDtos.add(ManualDto.getManualDto(recipe.getManual4(), recipe.getManualUrl4()));
        }
        if (recipe.getManual5() != null && !recipe.getManual5().isEmpty()) {
            manualDtos.add(ManualDto.getManualDto(recipe.getManual5(), recipe.getManualUrl5()));
        }
        if (recipe.getManual6() != null && !recipe.getManual6().isEmpty()) {
            manualDtos.add(ManualDto.getManualDto(recipe.getManual6(), recipe.getManualUrl6()));
        }
        return manualDtos;
    }
}
