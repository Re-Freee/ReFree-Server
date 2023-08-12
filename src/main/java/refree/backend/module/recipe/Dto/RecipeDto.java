package refree.backend.module.recipe.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeDto {

    private Long id;
    private String name;
    private Double calorie;
    private String ingredient;
    private String image;
    private Integer isHeart;
}