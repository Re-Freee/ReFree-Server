package refree.backend.module.ingredient.Dto;

import lombok.Builder;
import lombok.Data;
import refree.backend.module.ingredient.Ingredient;

import java.time.LocalDate;

@Data
@Builder
public class IngredientViewDto {

    private Long id;
    private String name;
    private LocalDate period;
    private int quantity;
    private String content;
    private int options;
    private String image;

    public static IngredientViewDto getIngredientViewDto(Ingredient ingredient) {
        return IngredientViewDto.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .period(ingredient.getPeriod())
                .quantity(ingredient.getQuantity())
                .content(ingredient.getContent())
                .options(ingredient.getOptions())
                .image(ingredient.getPicture() != null ? ingredient.getPicture().getPictureUrl() : null)
                .build();
    }
}
