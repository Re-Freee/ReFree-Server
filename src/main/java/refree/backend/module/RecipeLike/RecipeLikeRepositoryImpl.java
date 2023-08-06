package refree.backend.module.RecipeLike;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static refree.backend.module.Recipe.QRecipe.recipe;
import static refree.backend.module.RecipeLike.QRecipeLike.recipeLike;

@RequiredArgsConstructor
public class RecipeLikeRepositoryImpl implements RecipeLikeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RecipeLike> findByMemberFetchJoinRecipe(Long memberId) {
        return jpaQueryFactory
                .selectFrom(recipeLike)
                .join(recipeLike.recipe, recipe).fetchJoin()
                .where(recipeLike.member.id.eq(memberId))
                .fetch();
    }
}