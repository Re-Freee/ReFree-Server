package refree.backend.module.recipe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id", nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private Double calorie;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(length = 1000, nullable = false)
    private String ingredient;
    private String manual1;
    @Column(name = "manual_url1")
    private String manualUrl1;
    private String manual2;
    @Column(name = "manual_url2")
    private String manualUrl2;
    private String manual3;
    @Column(name = "manual_url3")
    private String manualUrl3;
    private String manual4;
    @Column(name = "manual_url4")
    private String manualUrl4;
    private String manual5;
    @Column(name = "manual_url5")
    private String manualUrl5;
    private String manual6;
    @Column(name = "manual_url6")
    private String manualUrl6;
}
