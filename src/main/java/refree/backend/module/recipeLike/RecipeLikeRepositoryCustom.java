package refree.backend.module.recipeLike;

import java.util.List;

public interface RecipeLikeRepositoryCustom {

    List<RecipeLike> findByMemberFetchJoinRecipe(Long memberId);
}
