package refree.backend.module.RecipeLike;

import java.util.List;

public interface RecipeLikeRepositoryCustom {

    List<RecipeLike> findByMemberFetchJoinRecipe(Long memberId);
}
