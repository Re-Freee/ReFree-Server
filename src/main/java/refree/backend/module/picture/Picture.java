package refree.backend.module.picture;


import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "picture_id")
    private Long id;
    private String pictureUrl;
    private String originalPictureName;
    private String storagePictureName;

    public static Picture createPicture(String pictureUrl, String originalPictureName, String storagePictureName) {
        Picture picture = new Picture();
        picture.pictureUrl = pictureUrl;
        picture.originalPictureName = originalPictureName;
        picture.storagePictureName = storagePictureName;
        return picture;
    }
}
