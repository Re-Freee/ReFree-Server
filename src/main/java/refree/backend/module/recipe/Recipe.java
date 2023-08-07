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
    private String manuel1;
    @Column(name = "manuel_url1")
    private String manuelUrl1;
    private String manuel2;
    @Column(name = "manuel_url2")
    private String manuelUrl2;
    private String manuel3;
    @Column(name = "manuel_url3")
    private String manuelUrl3;
    private String manuel4;
    @Column(name = "manuel_url4")
    private String manuelUrl4;
    private String manuel5;
    @Column(name = "manuel_url5")
    private String manuelUrl5;
    private String manuel6;
    @Column(name = "manuel_url6")
    private String manuelUrl6;
}
