package refree.backend.module.recipeLike;

import java.util.List;
import java.util.Set;

public interface RecipeLikeRepositoryCustom {

    List<RecipeLike> findByMemberFetchJoinRecipe(Long memberId, int offset);

    Set<Long> likedRecipe(Long memberId);
}
