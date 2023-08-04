package refree.backend.module.Recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import refree.backend.infra.response.BasicResponse;
import refree.backend.infra.response.GeneralResponse;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/recommend")
    public ResponseEntity<? extends BasicResponse> recommend(@ModelAttribute @Valid IngredientsDto ingredientsDto) {
        List<RecipeDto> recipeDtos = recipeService.recommend(ingredientsDto.getIngredients());
        return ResponseEntity.ok().body(new GeneralResponse<>(recipeDtos, "RECOMMEND_RECIPE_RESULT"));
    }
}
