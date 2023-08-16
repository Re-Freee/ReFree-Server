package refree.backend.module.recipeLike;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public Set<Long> likedRecipe(Long memberId) {
        List<RecipeLike> recipeLikes = jpaQueryFactory
                .selectFrom(recipeLike)
                .where(recipeLike.member.id.eq(memberId))
                .fetch();

        Set<Long> set = new HashSet<>();
        recipeLikes.forEach(r -> set.add(r.getRecipe().getId()));
        return set;
    }
}