package refree.backend.module.recipe;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import refree.backend.infra.exception.BadRequestException;
import refree.backend.infra.exception.NotFoundException;
import refree.backend.module.ingredient.Ingredient;
import refree.backend.module.ingredient.IngredientRepository;
import refree.backend.module.recipe.Dto.*;
import refree.backend.module.recipeLike.RecipeLikeRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeLikeRepository recipeLikeRepository;
    private final IngredientRepository ingredientRepository;

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

    private List<RecipeRecommendDto> recommendAlgorithm(List<Ingredient> ingredients) {
        ArrayList<RecipeCount> recipeCounts = new ArrayList<>();
        for (long i = 0; i < 1115; i++) {
            recipeCounts.add(RecipeCount.createRecipeCount(i));
        }

        // 특정 재료(String)를 가진 recipe으로 count
        for (Ingredient ingredient : ingredients) {
            recipeRepository.findByIngredientContains(ingredient.getCategory().getName()).forEach(recipe ->
                    recipeCounts.get(recipe.getId().intValue()).plusCount()
            );
        }

        // count를 기준으로 내림차순 정렬
        recipeCounts.sort(new Comparator<RecipeCount>() {
            @Override
            public int compare(RecipeCount o1, RecipeCount o2) {
                return Integer.compare(o2.getCount(), o1.getCount());
            }
        });

        List<Long> recipeIds = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            RecipeCount recipeCount = recipeCounts.get(i);
            if (recipeCount.getCount() == 0) {
                break;
            }
            recipeIds.add(recipeCount.getRecipeId());
        }
        if (!recipeIds.isEmpty()) {
            return recipeIds.stream() // 우선 순위대로 내려주는 방식
                    .map(recipeId -> {
                        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(()
                                -> new BadRequestException("BAD_REQUEST"));
                        return RecipeRecommendDto.getRecipeRecommendDto(recipe);
                    }).collect(Collectors.toList());
            /*List<Recipe> recipeResultList = recipeRepository.findByIn(recipeIds); // 우선 순위를 고려하지 않은 방식
            return recipeResultList.stream()
                    .map(recipe -> RecipeRecommendDto.getRecipeRecommendDto(recipe)).collect(Collectors.toList());*/
        }
        return List.of();
    }

    private List<Long> recommendRandomIds() {
        Random random = new Random();
        List<Long> ids = new ArrayList<>();
        while (ids.size() < 3) {
            long randomNum = (long)random.nextInt(1114) + 1;
            if (!ids.contains(randomNum))
                ids.add(randomNum);
        }
        return ids;
    }

    public List<RecipeRecommendDto> recommend(Long memberId) {
        List<Ingredient> ingredients = ingredientRepository.findAllByMemberFetchJoinCategory(memberId);
        if (ingredients.isEmpty()) {
            return recipeRepository.findByIn(recommendRandomIds()).stream()
                    .map(RecipeRecommendDto::getRecipeRecommendDto).collect(Collectors.toList());
        }
        List<Ingredient> filteredIngredients = ingredients.stream()
                .filter(ingredient -> {
                    LocalDate expire = ingredient.getPeriod();
                    LocalDate now = LocalDate.now();
                    int days = Period.between(now, expire).getDays();
                    return days >= 0 && days <= 3;
                }).collect(Collectors.toList());

        List<Ingredient> resultIngredients;
        if (filteredIngredients.isEmpty()) {
            // 임박 재료 없음 최대 8개
            resultIngredients = new ArrayList<>(ingredients.subList(0, Math.min(ingredients.size(), 8)));
        } else {
            // 임박 재료 있음 최대 8개
            resultIngredients = new ArrayList<>(filteredIngredients.subList(0, Math.min(filteredIngredients.size(), 8)));
        }
        return recommendAlgorithm(resultIngredients);
    }

    @Transactional(readOnly = true)
    public List<RecipeDto> search(Long memberId, RecipeSearch recipeSearch) {
        List<Recipe> recipes = recipeRepository.searchRecipe(recipeSearch);
        // Recipe_like 테이블에서 member_id로 RecipeLike리스트 가져와서 recipe_id랑 같은지 비교
        Set<Long> set = recipeLikeRepository.likedRecipe(memberId);

        return recipes.stream()
                .map(recipe -> RecipeDto.builder()
                        .id(recipe.getId())
                        .name(recipe.getName())
                        .calorie(recipe.getCalorie())
                        .ingredient(recipe.getIngredient())
                        .image(recipe.getImageUrl())
                        .isHeart(set.contains(recipe.getId()) ? 1 : 0)
                        .build()).collect(Collectors.toList());
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
