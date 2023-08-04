package refree.backend.module.Recipe;

import java.util.List;

public interface RecipeRepositoryCustom {

    List<Recipe> findByIn(List<Long> ids);
}
