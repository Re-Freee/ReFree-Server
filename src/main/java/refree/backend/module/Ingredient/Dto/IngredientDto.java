package refree.backend.module.Ingredient.Dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class IngredientDto {

    @NotEmpty(message = "작성을 완료해주세요")
    private String name;
    @NotEmpty(message = "작성을 완료해주세요")
    private String category;
    @NotEmpty(message = "작성을 완료해주세요")
    private String period;
    @NotNull(message = "작성을 완료해주세요")
    private int quantity;
    @Length(max = 100, message = "메모 글자 제한 100자")
    private String content;
    @NotNull(message = "작성을 완료해주세요")
    private int options;
}
