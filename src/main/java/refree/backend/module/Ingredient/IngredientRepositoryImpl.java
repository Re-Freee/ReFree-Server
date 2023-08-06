package refree.backend.module.Ingredient;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import refree.backend.module.Ingredient.Dto.IngredientDto;
import refree.backend.module.Ingredient.Dto.IngredientSearch;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.List;

import static refree.backend.module.Ingredient.QIngredient.*;


@RequiredArgsConstructor
public class IngredientRepositoryImpl implements  IngredientRepositoryCustom{

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
    public void delete(int ingredient_id,int cnt,String memo){
        /*jpaQueryFactory
                .update(ingredient)
                .set(ingredient.quantity,cnt)
                .set(ingredient.content,memo)
                .where(ingredient.ingredient_id.eq(ingredient_id))
                .execute();*/
        return;
    }

    private BooleanExpression getOptionsEqual(String options) {
        return options != null ? ingredient.options.eq(Integer.parseInt(options)) : null;
    }

    private BooleanExpression getNameContains(String searchKey) {
        return searchKey != null ? ingredient.name.contains(searchKey) : null;
    }
}
