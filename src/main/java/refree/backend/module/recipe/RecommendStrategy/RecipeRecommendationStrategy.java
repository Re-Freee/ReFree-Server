package refree.backend.module.recipe.RecommendStrategy;

import refree.backend.module.ingredient.Ingredient;
import refree.backend.module.recipe.Dto.RecipeRecommendDto;

import java.util.List;

public interface RecipeRecommendationStrategy {
    List<RecipeRecommendDto> recommend(List<Ingredient> ingredients);
}
