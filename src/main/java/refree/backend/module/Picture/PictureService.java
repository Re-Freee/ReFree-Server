package refree.backend.module.Picture;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PictureService {
    private final AmazonS3Client amazonS3Client;
    private final PictureRepository pictureRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Autowired
    public PictureService(PictureRepository pictureRepository,AmazonS3Client amazonS3Client){
        this.pictureRepository=pictureRepository;
        this.amazonS3Client=amazonS3Client;
    }

    public Picture view(int id){
        Optional<Picture> ingredient2= pictureRepository.findById(id);
        Picture ingredient1=ingredient2.get();
        return ingredient1;
    }

    public String uploadImg(MultipartFile file) throws IOException {
        Picture picture=new Picture();

        String fileName=file.getOriginalFilename();
        String fileUrl= "https://" + bucket + "/test" +fileName;
        picture.setPicture_url(fileUrl);
        picture.setOriginal_picture_name(file.getOriginalFilename());
        picture.setStorage_picture_name(bucket);
        pictureRepository.save(picture);

        ObjectMetadata metadata= new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        amazonS3Client.putObject(bucket,fileName,file.getInputStream(),metadata);

        return fileUrl;
    }

}
