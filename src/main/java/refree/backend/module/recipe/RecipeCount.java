package refree.backend.module.recipe;

import lombok.Data;

@Data
public class RecipeCount {

    private Long recipeId;
    private int count;

    public static RecipeCount createRecipeCount(Long recipeId) {
        RecipeCount recipeCount = new RecipeCount();
        recipeCount.setRecipeId(recipeId);
        recipeCount.setCount(0);
        return recipeCount;
    }

    public void plusCount() {
        count += 1;
    }
}

