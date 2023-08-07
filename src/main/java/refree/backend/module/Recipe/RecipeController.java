package refree.backend.module.Recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import refree.backend.infra.config.CurrentUser;
import refree.backend.infra.response.BasicResponse;
import refree.backend.infra.response.GeneralResponse;
import refree.backend.infra.response.SingleResponse;
import refree.backend.module.Recipe.Dto.IngredientsDto;
import refree.backend.module.Recipe.Dto.RecipeDto;
import refree.backend.module.Recipe.Dto.RecipeSearch;
import refree.backend.module.RecipeLike.RecipeLikeService;
import refree.backend.module.member.Member;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeLikeService recipeLikeService;

    @GetMapping("/recommend")
    public ResponseEntity<? extends BasicResponse> recommend(@ModelAttribute @Valid IngredientsDto ingredientsDto) {
        List<RecipeDto> recipeDtos = recipeService.recommend(ingredientsDto.getIngredients());
        return ResponseEntity.ok().body(new GeneralResponse<>(recipeDtos, "RECOMMEND_RECIPE_RESULT"));
    }

    @GetMapping("/view/{recipeId}")
    public ResponseEntity<? extends BasicResponse> viewRecipe(@PathVariable Long recipeId) {
        return ResponseEntity.ok()
                .body(new GeneralResponse<>(recipeService.recipeView(recipeId), "RECIPE_DETAIL"));
    }

    @GetMapping("/search")
    public ResponseEntity<? extends BasicResponse> search(@ModelAttribute RecipeSearch recipeSearch) {
        return ResponseEntity.ok()
                .body(new GeneralResponse<>(recipeService.search(recipeSearch), "RECIPE_SEARCH_RESULT"));
    }

    @PostMapping("/like/{recipeId}")
    public ResponseEntity<? extends BasicResponse> clickRecipeLike(@CurrentUser Member member,
                                                                   @PathVariable("recipeId") Long recipeId) {
        recipeLikeService.clickRecipeLike(member.getId(), recipeId);
        return ResponseEntity.ok().body(new SingleResponse("LIKE_COMPLETED"));
    }
}
