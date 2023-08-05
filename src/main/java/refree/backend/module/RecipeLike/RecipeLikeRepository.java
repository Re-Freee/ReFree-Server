package refree.backend.module.RecipeLike;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface RecipeLikeRepository extends JpaRepository<RecipeLike, Long> {

    Optional<RecipeLike> findByMemberIdAndRecipeId(Long memberId, Long recipeId);
}