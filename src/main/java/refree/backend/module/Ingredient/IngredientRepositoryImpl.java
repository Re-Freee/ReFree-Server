package refree.backend.module.Ingredient;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import refree.backend.module.Ingredient.Dto.IngredientSearch;

import java.util.List;

import static refree.backend.module.Ingredient.QIngredient.ingredient;
import static refree.backend.module.Picture.QPicture.picture;


@RequiredArgsConstructor
public class IngredientRepositoryImpl implements IngredientRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Ingredient> findAllIngredient(int mem_id) {
       /* return jpaQueryFactory
                .selectFrom(ingredient)
                .where(ingredient.member_id.eq(mem_id))
                .fetch();*/
        return null;
    }

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
    public void delete(int ingredient_id, int cnt, String memo) {
        /*jpaQueryFactory
                .update(ingredient)
                .set(ingredient.quantity,cnt)
                .set(ingredient.content,memo)
                .where(ingredient.ingredient_id.eq(ingredient_id))
                .execute();*/
        return;
    }

    @Override
    public Ingredient findByIdFetchJoinImage(Long ingredientId) {
        return jpaQueryFactory
                .selectFrom(ingredient)
                .leftJoin(ingredient.picture, picture)
                .where(ingredient.id.eq(ingredientId))
                .fetchOne();
    }

    private BooleanExpression getOptionsEqual(String options) {
        return options != null ? ingredient.options.eq(Integer.parseInt(options)) : null;
    }

    private BooleanExpression getNameContains(String searchKey) {
        return searchKey != null ? ingredient.name.contains(searchKey) : null;
    }
}
