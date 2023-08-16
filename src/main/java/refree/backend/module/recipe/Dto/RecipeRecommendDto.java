package refree.backend.module.recipe.Dto;

import lombok.Builder;
import lombok.Data;
import refree.backend.module.recipe.Recipe;

@Data
@Builder
public class RecipeRecommendDto {

    private Long id;
    private String name;
    private Double calorie;
    private String ingredient;
    private String image;

    public static RecipeRecommendDto getRecipeRecommendDto(Recipe recipe) {
        return RecipeRecommendDto.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .calorie(recipe.getCalorie())
                .ingredient(recipe.getIngredient())
                .image(recipe.getImageUrl())
                .build();
    }
}
