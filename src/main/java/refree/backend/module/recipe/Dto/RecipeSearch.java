package refree.backend.module.recipe.Dto;

import lombok.Data;

@Data
public class RecipeSearch {

    private String type;
    private String title;
    private int offset;

    public void setOffset(int offset) {
        this.offset = Math.max(offset, 0);
    }
}
