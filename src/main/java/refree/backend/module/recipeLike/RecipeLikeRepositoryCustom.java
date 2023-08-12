package refree.backend.module.recipeLike;

import java.util.HashMap;
import java.util.List;

public interface RecipeLikeRepositoryCustom {

    List<RecipeLike> findByMemberFetchJoinRecipe(Long memberId, int offset);

    HashMap<Long, Long> likedRecipe(Long memberId);
}
