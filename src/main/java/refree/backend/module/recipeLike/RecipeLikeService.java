package refree.backend.module.recipeLike;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import refree.backend.infra.exception.MemberException;
import refree.backend.infra.exception.NotFoundException;
import refree.backend.module.recipe.Recipe;
import refree.backend.module.recipe.RecipeRepository;
import refree.backend.module.member.Member;
import refree.backend.module.member.MemberRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecipeLikeService {

    private final RecipeLikeRepository recipeLikeRepository;
    private final MemberRepository memberRepository;
    private final RecipeRepository recipeRepository;

    public void clickRecipeLike(Long memberId, Long recipeId) {
        Optional<RecipeLike> recipeLikeOptional = recipeLikeRepository.findByMemberIdAndRecipeId(memberId, recipeId);
        if (recipeLikeOptional.isPresent()) {
            // 이미 좋아요 누른 상태
            deleteRecipeLike(recipeLikeOptional.get(), memberId);
        } else {
            // 좋아요 누르지 않은 상태
            saveRecipeLike(memberId, recipeId);
        }
    }

    private void saveRecipeLike(Long memberId, Long recipeId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException("존재하지 않는 계정입니다."));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new NotFoundException("NO_RECIPE_EXIST"));
        RecipeLike recipeLike = RecipeLike.createRecipeLike(member, recipe);
        recipeLikeRepository.save(recipeLike);
    }

    private void deleteRecipeLike(RecipeLike recipeLike, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException("존재하지 않는 계정입니다."));
        member.deleteRecipeFromLikes(recipeLike);
        recipeLikeRepository.deleteById(recipeLike.getId());
    }
}
