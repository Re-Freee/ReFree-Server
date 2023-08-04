package refree.backend.module.Recipe;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static refree.backend.module.Recipe.QRecipe.*;


@RequiredArgsConstructor
public class RecipeRepositoryImpl implements RecipeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Recipe> findByIn(List<Long> ids) {
        return jpaQueryFactory
                .selectFrom(recipe)
                .where(recipe.id.in(ids))
                .fetch();
    }
}
