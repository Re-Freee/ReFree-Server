package refree.backend.module.Recipe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface RecipeRepository extends JpaRepository<Recipe, Long>, RecipeRepositoryCustom {

    List<Recipe> findByIngredientContains(String ingredient);
}

