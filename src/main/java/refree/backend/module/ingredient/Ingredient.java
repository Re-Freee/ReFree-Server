package refree.backend.module.ingredient;

import lombok.Getter;
import refree.backend.module.category.Category;
import refree.backend.module.ingredient.Dto.IngredientDto;
import refree.backend.module.picture.Picture;
import refree.backend.module.member.Member;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    private Long id;
    private String name;
    private LocalDate period;
    private int quantity;
    @Column(length = 100)
    private String content;
    private int options; // 0:실온 | 1:냉장 | 2:냉동 | 3:기타

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "picture_id")
    private Picture picture;

    public static Ingredient createIngredient(Member member, Category category, LocalDate period,
                                              IngredientDto ingredientDto, Picture picture) {
        Ingredient ingredient = new Ingredient();
        ingredient.name = ingredientDto.getName();
        ingredient.period = period;
        ingredient.quantity = ingredientDto.getQuantity();
        ingredient.content = ingredientDto.getContent();
        ingredient.options = ingredientDto.getOptions();
        ingredient.member = member;
        ingredient.category = category;
        ingredient.picture = picture;
        return ingredient;
    }

    public void update(LocalDate period, Category category, IngredientDto ingredientDto, Picture picture) {
        this.name = ingredientDto.getName();
        this.period = period;
        this.quantity = ingredientDto.getQuantity();
        this.content = ingredientDto.getContent();
        this.options = ingredientDto.getOptions();
        this.category = category;
        this.picture = picture;
    }

    public void deletePicture() {
        this.picture = null;
    }
}