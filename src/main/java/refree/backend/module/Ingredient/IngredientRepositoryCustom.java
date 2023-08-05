package refree.backend.module.Ingredient;

import java.util.List;

public interface IngredientRepositoryCustom {
    //    public List<Ingredient> findAllIngredient(int mem_id);
//    public List<Ingredient> search(String searchKey,int mem_id);
//    public void delete(int ingredient_id,int cnt);
//    Optional<Ingredient> findByIngred(int ingredient);
    void delete(int ingredient_id,int cnt,String memo);
    List<Ingredient> findAllIngredient(int mem_id);
    List<Ingredient> search(String searchKey,int mem_id);
}
