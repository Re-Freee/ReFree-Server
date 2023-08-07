package refree.backend.module.recipe;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import refree.backend.module.recipe.Dto.RecipeSearch;

import java.util.List;

import static refree.backend.module.recipe.QRecipe.*;


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

    @Override
    public List<Recipe> searchRecipe(RecipeSearch recipeSearch) {
        return jpaQueryFactory
                .selectFrom(recipe)
                .where(getTypeContains(recipeSearch.getType()),
                        getNameContains(recipeSearch.getTitle()))
                .limit(10)
                .fetch();
    }

    private BooleanExpression getTypeContains(String type) {
        return type != null ? recipe.type.contains(type) : null;
    }

    private BooleanExpression getNameContains(String title) {
        return title != null ? recipe.name.contains(title) : null;
    }
}
