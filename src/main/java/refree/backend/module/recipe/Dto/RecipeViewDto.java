package refree.backend.module.recipe.Dto;

import lombok.Builder;
import lombok.Data;
import refree.backend.module.recipe.Recipe;

import java.util.List;

@Data
@Builder
public class RecipeViewDto {

    private Long id;
    private String name;
    private Double calorie;
    private String ingredient;
    private Boolean isHeart;
    private String image;
    private List<ManualDto> manual;

    public static RecipeViewDto getRecipeViewDto(Recipe recipe, List<ManualDto> manual, Boolean isHeart) {
        return RecipeViewDto.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .calorie(recipe.getCalorie())
                .ingredient(recipe.getIngredient())
                .isHeart(isHeart)
                .image(recipe.getImageUrl())
                .manual(manual)
                .build();
    }
}
