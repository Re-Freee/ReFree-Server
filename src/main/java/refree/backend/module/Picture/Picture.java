package refree.backend.module.Picture;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int picture;
    private String picture_url;
    private String original_picture_name;
    private String storage_picture_name;

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getOriginal_picture_name() {
        return original_picture_name;
    }

    public void setOriginal_picture_name(String original_picture_name) {
        this.original_picture_name = original_picture_name;
    }

    public String getStorage_picture_name() {
        return storage_picture_name;
    }

    public void setStorage_picture_name(String storage_picture_name) {
        this.storage_picture_name = storage_picture_name;
    }
}
