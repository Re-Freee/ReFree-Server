package refree.backend.module.recipe.RecommendStrategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import refree.backend.infra.exception.BadRequestException;
import refree.backend.module.ingredient.Ingredient;
import refree.backend.module.recipe.Dto.RecipeRecommendDto;
import refree.backend.module.recipe.Recipe;
import refree.backend.module.recipe.RecipeCount;
import refree.backend.module.recipe.RecipeRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NormalRecommendationStrategy implements RecipeRecommendationStrategy {

    private final RecipeRepository recipeRepository;
    private static final int MAX_RECIPE_COUNT = 1115;
    private static final int MAX_RECOMMEND_SIZE = 3;

    @Override
    public List<RecipeRecommendDto> recommend(List<Ingredient> ingredients) {
        ArrayList<RecipeCount> recipeCounts = new ArrayList<>();
        for (long i = 0; i < MAX_RECIPE_COUNT; i++) {
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
        for (int i = 0; i < MAX_RECOMMEND_SIZE; i++) {
            RecipeCount recipeCount = recipeCounts.get(i);
            if (recipeCount.getCount() == 0) { // 추천 개수가 3개가 안되면 종료
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
}
