package refree.backend.module.ingredient;

import refree.backend.module.ingredient.Dto.IngredientSearch;

import java.util.List;

public interface IngredientRepositoryCustom {
    //    public List<Ingredient> findAllIngredient(int mem_id);
//    public List<Ingredient> search(String searchKey,int mem_id);
//    public void delete(int ingredient_id,int cnt);
//    Optional<Ingredient> findByIngred(int ingredient);
    List<Ingredient> findAllIngredient(int mem_id);
    List<Ingredient> search(IngredientSearch ingredientSearch, Long memberId);

    Ingredient findByIdFetchJoinImage(Long ingredientId);

    List<Ingredient> findAllByFetchJoinImage(Long memberId);
}
