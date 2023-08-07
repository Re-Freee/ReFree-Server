package refree.backend.module.Picture;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import refree.backend.infra.exception.ImageException;
import refree.backend.module.Ingredient.Dto.IngredientResponseDto;
import refree.backend.module.Ingredient.Ingredient;
import refree.backend.module.Ingredient.IngredientRepository;
import refree.backend.module.member.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class PictureService {

    private final S3Service s3Service;
    private final PictureRepository pictureRepository;
    private final MemberRepository memberRepository;

    public String saveImage(MultipartFile file) {
        if (file.getSize() > 2097152) // 2MB보다 큰 경우
            throw new MaxUploadSizeExceededException(file.getSize());

        String storageImageName = s3Service.uploadImg(file);
        String imgUrl = s3Service.getFileUrl(storageImageName);
        pictureRepository.save(Picture.createPicture(imgUrl, file.getOriginalFilename(), storageImageName));
        return storageImageName;
    }

    public void updateImageCheck(MultipartFile file, Ingredient ingredient) {
        Picture picture = ingredient.getPicture();
        if (file != null && file.getSize() > 2097152) // 2MB보다 큰 경우
            throw new MaxUploadSizeExceededException(file.getSize());

        // 기존 이미지있다면 삭제
        if (picture != null) {
            ingredient.deletePicture(); // 연관관계 삭제
            pictureRepository.delete(picture);
            s3Service.deleteFile(picture.getStoragePictureName());
        }
    }

    @Transactional(readOnly = true)
    public Picture getPicture(String storageName) {
        return pictureRepository.findByStoragePictureName(storageName)
                .orElseThrow(() -> new ImageException("존재하지 않는 이미지"));
    }
}
