package refree.backend.module.ingredient;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import refree.backend.module.ingredient.Dto.IngredientSearch;

import java.util.List;

import static refree.backend.module.category.QCategory.category;
import static refree.backend.module.ingredient.QIngredient.ingredient;
import static refree.backend.module.picture.QPicture.picture;


@RequiredArgsConstructor
public class IngredientRepositoryImpl implements IngredientRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Ingredient> search(IngredientSearch ingredientSearch, Long memberId) {
        return jpaQueryFactory
                .selectFrom(ingredient)
                .where(ingredient.member.id.eq(memberId),
                        getOptionsEqual(ingredientSearch.getOptions()),
                        getNameContains(ingredientSearch.getSearchKey()))
                .fetch();
    }

    @Override
    public Ingredient findByIdFetchJoinImage(Long ingredientId) {
        return jpaQueryFactory
                .selectFrom(ingredient)
                .leftJoin(ingredient.picture, picture).fetchJoin()
                .where(ingredient.id.eq(ingredientId))
                .fetchOne();
    }

    @Override
    public Ingredient findByIdFetchJoinImageAndCategory(Long ingredientId) {
        return jpaQueryFactory
                .selectFrom(ingredient)
                .leftJoin(ingredient.picture, picture).fetchJoin()
                .join(ingredient.category, category).fetchJoin()
                .where(ingredient.id.eq(ingredientId))
                .fetchOne();
    }

    @Override
    public List<Ingredient> findAllByMemberFetchJoinImage(Long memberId) {
        return jpaQueryFactory
                .selectFrom(ingredient)
                .leftJoin(ingredient.picture, picture).fetchJoin()
                .where(ingredient.member.id.eq(memberId))
                .fetch();
    }

    @Override
    public List<Ingredient> findAllByMemberFetchJoinCategory(Long memberId) {
        return jpaQueryFactory
                .selectFrom(ingredient)
                .join(ingredient.category, category).fetchJoin()
                .where(ingredient.member.id.eq(memberId))
                .fetch();
    }

    private BooleanExpression getOptionsEqual(String options) {
        return options != null ? ingredient.options.eq(Integer.parseInt(options)) : null;
    }

    private BooleanExpression getNameContains(String searchKey) {
        return searchKey != null ? ingredient.name.contains(searchKey) : null;
    }
}
