package refree.backend.module.Recipe;

import refree.backend.module.Recipe.Dto.RecipeSearch;

import java.util.List;

public interface RecipeRepositoryCustom {

    List<Recipe> findByIn(List<Long> ids);

    List<Recipe> searchRecipe(RecipeSearch recipeSearch);
}
