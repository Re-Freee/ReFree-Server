package refree.backend.module.recipe.Dto;

import lombok.Builder;
import lombok.Data;
import refree.backend.module.recipe.Recipe;

import java.util.Set;

@Data
@Builder
public class RecipeDto {

    private Long id;
    private String name;
    private Double calorie;
    private String ingredient;
    private String image;
    private Integer isHeart;

    public static RecipeDto getRecipeDto(Recipe recipe, Set<Long> set) {
        return RecipeDto.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .calorie(recipe.getCalorie())
                .ingredient(recipe.getIngredient())
                .image(recipe.getImageUrl())
                .isHeart(set.contains(recipe.getId()) ? 1 : 0)
                .build();
    }
}