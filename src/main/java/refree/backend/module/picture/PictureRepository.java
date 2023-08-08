package refree.backend.module.picture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface PictureRepository extends JpaRepository<Picture, Long> {

    Optional<Picture> findByStoragePictureName(String storageName);
}