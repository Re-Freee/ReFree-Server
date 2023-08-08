package refree.backend.module.recipe;

import refree.backend.module.recipe.Dto.RecipeSearch;

import java.util.List;

public interface RecipeRepositoryCustom {

    List<Recipe> findByIn(List<Long> ids);

    List<Recipe> searchRecipe(RecipeSearch recipeSearch);
}
