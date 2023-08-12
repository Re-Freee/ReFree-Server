package refree.backend.module.recipeLike;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;

import static refree.backend.module.recipe.QRecipe.recipe;
import static refree.backend.module.recipeLike.QRecipeLike.recipeLike;

@RequiredArgsConstructor
public class RecipeLikeRepositoryImpl implements RecipeLikeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RecipeLike> findByMemberFetchJoinRecipe(Long memberId, int offset) {
        return jpaQueryFactory
                .selectFrom(recipeLike)
                .join(recipeLike.recipe, recipe).fetchJoin()
                .where(recipeLike.member.id.eq(memberId))
                .offset(offset)
                .limit(10)
                .fetch();
    }

    // 회원이 좋아요한 recipe - key : recipeId, value : memberId
    @Override
    public HashMap<Long, Long> likedRecipe(Long memberId) {
        List<RecipeLike> recipeLikes = jpaQueryFactory
                .selectFrom(recipeLike)
                .where(recipeLike.member.id.eq(memberId))
                .fetch();

        HashMap<Long, Long> hashMap = new HashMap<>();
        recipeLikes.forEach(r -> hashMap.put(r.getRecipe().getId(), r.getMember().getId()));
        return hashMap;
    }
}