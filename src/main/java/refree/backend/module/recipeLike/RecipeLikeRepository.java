package refree.backend.module.recipeLike;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RecipeLikeRepository extends JpaRepository<RecipeLike, Long>, RecipeLikeRepositoryCustom {

    Optional<RecipeLike> findByMemberIdAndRecipeId(Long memberId, Long recipeId);

    List<RecipeLike> findByMemberId(Long memberId);

    Boolean existsByMemberIdAndRecipeId(Long memberId, Long recipeId);
}
