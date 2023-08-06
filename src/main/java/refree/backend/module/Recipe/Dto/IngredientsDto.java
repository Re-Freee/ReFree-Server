package refree.backend.module.Recipe.Dto;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.Size;
import java.util.List;

@Data
public class IngredientsDto {

    @UniqueElements(message = "고유 요소만 포함해야 합니다")
    @Size(min = 3, max = 5, message = "크기가 3에서 5 사이여야 합니다")
    private List<String> ingredients;
}

