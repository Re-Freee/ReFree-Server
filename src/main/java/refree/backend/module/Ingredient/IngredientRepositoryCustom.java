package refree.backend.module.Ingredient;

import refree.backend.module.Ingredient.Dto.IngredientSearch;

import java.util.List;

public interface IngredientRepositoryCustom {
    List<Ingredient> search(IngredientSearch ingredientSearch, Long memberId);
    Ingredient findByIdFetchJoinImage(Long ingredientId);
    List<Ingredient> findByIdFetchJoinMember(Long memId);
}
