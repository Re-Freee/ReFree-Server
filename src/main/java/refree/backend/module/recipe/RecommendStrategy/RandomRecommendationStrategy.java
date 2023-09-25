package refree.backend.module.recipe.RecommendStrategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import refree.backend.module.ingredient.Ingredient;
import refree.backend.module.recipe.Dto.RecipeRecommendDto;
import refree.backend.module.recipe.RecipeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RandomRecommendationStrategy implements RecipeRecommendationStrategy {

    private final RecipeRepository recipeRepository;
    private static final int MAX_RANDOM_VALUE = 1114;
    private static final int MAX_RECOMMEND_SIZE = 3;


    @Override
    public List<RecipeRecommendDto> recommend(List<Ingredient> ingredients) {
        Random random = new Random();
        List<Long> ids = new ArrayList<>();
        while (ids.size() < MAX_RECOMMEND_SIZE) {
            long randomNum = (long) random.nextInt(MAX_RANDOM_VALUE) + 1;
            if (!ids.contains(randomNum))
                ids.add(randomNum);
        }
        return recipeRepository.findByIn(ids).stream()
                .map(RecipeRecommendDto::getRecipeRecommendDto).collect(Collectors.toList());
    }
}
