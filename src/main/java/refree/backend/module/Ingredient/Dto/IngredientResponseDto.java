package refree.backend.module.Ingredient.Dto;

import lombok.Builder;
import lombok.Data;
import refree.backend.module.Ingredient.Ingredient;

import java.time.LocalDate;

@Data
@Builder
public class IngredientResponseDto {

    private Long id;
    private String name;
    private LocalDate period;
    private int quantity;
    private String content;
    private int options;
    private String image; //TODO: 이미지 url

    public static IngredientResponseDto getIngredientResponseDto(Ingredient ingredient) {
        return IngredientResponseDto.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .period(ingredient.getPeriod())
                .quantity(ingredient.getQuantity())
                .content(ingredient.getContent())
                .options(ingredient.getOptions())
                //TODO: 이미지 url
                .build();
    }
}