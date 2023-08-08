package refree.backend.module.ingredient.Dto;

import lombok.Builder;
import lombok.Data;
import refree.backend.module.ingredient.Ingredient;

import java.time.LocalDate;

@Data
@Builder
public class IngredientShortResponse {

    private Long id;
    private String name;
    private LocalDate period;
    private String image;

    public static IngredientShortResponse getIngredientShortResponse(Ingredient ingredient) {
        return IngredientShortResponse.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .period(ingredient.getPeriod())
                .image(ingredient.getPicture() != null ? ingredient.getPicture().getPictureUrl() : null)
                .build();
    }
}
