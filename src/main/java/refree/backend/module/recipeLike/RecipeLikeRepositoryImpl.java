package refree.backend.module.recipeLike;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

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
}