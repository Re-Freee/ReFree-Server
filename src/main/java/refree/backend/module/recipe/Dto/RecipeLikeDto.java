package refree.backend.module.recipe.Dto;

import lombok.Data;
import refree.backend.module.recipe.Recipe;

@Data
public class RecipeLikeDto {

    private Long id;
    private String name;
    private String image;

    public static RecipeLikeDto getRecipeLikeDto (Recipe recipe) {
        RecipeLikeDto recipeLikeDto = new RecipeLikeDto();
        recipeLikeDto.id = recipe.getId();
        recipeLikeDto.name = recipe.getName();
        recipeLikeDto.image = recipe.getImageUrl();
        return recipeLikeDto;
    }
}
