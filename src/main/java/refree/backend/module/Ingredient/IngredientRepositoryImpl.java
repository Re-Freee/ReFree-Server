package refree.backend.module.Ingredient;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.List;
import static refree.backend.module.Ingredient.QIngredient.ingredient;

@RequiredArgsConstructor
public class IngredientRepositoryImpl implements  IngredientRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    //    @Override
//    Optional<Ingredient> findByIngred(int ingredient_id){
//        return jpaQueryFactory
//                .selectFrom(ingredient)
//                .where(ingredient.ingredient_id.eq(ingredient_id))
//                .fetch();
//
//    }
    @Override
    public List<Ingredient> findAllIngredient(int mem_id) {
        return jpaQueryFactory
                .selectFrom(ingredient)
                .where(ingredient.member_id.eq(mem_id))
                .fetch();
    }
    @Override
    public List<Ingredient> search(String searchKey,int mem_id){
//        System.out.println(jpaQueryFactory
//                .selectFrom(ingredient)
//                .where(ingredient.member_id.eq(mem_id))
//                .fetch());
//      ingredient.member_id.eq(mem_id).and(ingredient.name.like(searchKey))
        return jpaQueryFactory
                .selectFrom(ingredient)
                .where(ingredient.member_id.eq(mem_id).and(ingredient.name.like(searchKey)))
                .fetch();//,ingredient.ingredient_id.eq(mem_id)ingredient.name.contains(searchKey),
    }
    //        List<Ingredient> check=em.createQuery("select v from Ingredient v where u.member_id:=id",Ingredient.class)
//                .setParameter("id",mem_id)
//                .getResultList();
//        List<Ingredient> confirm=new ArrayList<>();
//        for(int i=0;i<check.size();i++){
//            if(check.get(i).getName().contains(searchKey)){
//                confirm.add(check.get(i));
//            }
//        }
//        return confirm;
    @Override
    public void delete(int ingredient_id,int cnt,String memo){
        jpaQueryFactory
                .update(ingredient)
                .set(ingredient.quantity,cnt)
                .set(ingredient.content,memo)
                .where(ingredient.ingredient_id.eq(ingredient_id))
                .execute();
//        em.createQuery("update Ingredient u set u.quantity=:quantity")
//                .setParameter("quantity",cnt)
//                .executeUpdate();
    }

//    private final EntityManager em;
//    public IngredientRepositoryImpl(JPAQueryFactory jpaQueryFactory, EntityManager em){
//        this.jpaQueryFactory = jpaQueryFactory;
//        this.em=em;
//    }
//    @Override
//    public void create(Ingredient ingredient){
//        em.persist(ingredient);
//    }
//    @Override
//    public Optional<Ingredient> view(int ingredient_id){
//        Ingredient ingredient=em.find(Ingredient.class,ingredient_id);
//        return Optional.ofNullable(ingredient);
//    }
//    @Override
//    public List<Ingredient> findAllIngredient(int mem_id){
//        return em.createQuery("select u from Ingredient u where u.member_id=:id", Ingredient.class)
//                .setParameter("id",mem_id)
//                .getResultList();
//    }

}
