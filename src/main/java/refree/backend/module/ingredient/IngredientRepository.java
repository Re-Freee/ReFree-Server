package refree.backend.module.ingredient;


import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long>,IngredientRepositoryCustom {

}
