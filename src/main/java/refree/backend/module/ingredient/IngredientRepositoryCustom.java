package refree.backend.module.ingredient;

import refree.backend.module.ingredient.Dto.IngredientSearch;

import java.util.List;

public interface IngredientRepositoryCustom {
    List<Ingredient> search(IngredientSearch ingredientSearch, Long memberId);
    Ingredient findByIdFetchJoinImage(Long ingredientId);
    Ingredient findByIdFetchJoinImageAndCategory(Long ingredientId);
    List<Ingredient> findAllByMemberFetchJoinImage(Long memberId);
    List<Ingredient> findAllByMemberFetchJoinCategory(Long memberId);
}
