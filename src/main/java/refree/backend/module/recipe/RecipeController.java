package refree.backend.module.recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import refree.backend.infra.config.CurrentUser;
import refree.backend.infra.response.BasicResponse;
import refree.backend.infra.response.GeneralResponse;
import refree.backend.infra.response.SingleResponse;
import refree.backend.module.recipe.Dto.RecipeRecommendDto;
import refree.backend.module.recipe.Dto.RecipeSearch;
import refree.backend.module.recipeLike.RecipeLikeService;
import refree.backend.module.member.Member;

import java.util.List;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeLikeService recipeLikeService;

    @GetMapping("/recommend")
    public ResponseEntity<? extends BasicResponse> recommend(@CurrentUser Member member) {
        List<RecipeRecommendDto> recipeDtos = recipeService.recommend(member.getId());
        return ResponseEntity.ok().body(new GeneralResponse<>(recipeDtos, "RECOMMEND_RECIPE_RESULT"));
    }

    @GetMapping("/view/{recipeId}")
    public ResponseEntity<? extends BasicResponse> viewRecipe(@CurrentUser Member member,
                                                              @PathVariable Long recipeId) {
        return ResponseEntity.ok()
                .body(new GeneralResponse<>(recipeService.recipeView(member.getId(), recipeId), "RECIPE_DETAIL"));
    }

    @GetMapping("/search")
    public ResponseEntity<? extends BasicResponse> search(@CurrentUser Member member,
                                                          @ModelAttribute RecipeSearch recipeSearch) {
        return ResponseEntity.ok()
                .body(new GeneralResponse<>(recipeService.search(member.getId(), recipeSearch), "RECIPE_SEARCH_RESULT"));
    }

    @PostMapping("/like/{recipeId}")
    public ResponseEntity<? extends BasicResponse> clickRecipeLike(@CurrentUser Member member,
                                                                   @PathVariable("recipeId") Long recipeId) {
        recipeLikeService.clickRecipeLike(member.getId(), recipeId);
        return ResponseEntity.ok().body(new SingleResponse("LIKE_COMPLETED"));
    }
}
