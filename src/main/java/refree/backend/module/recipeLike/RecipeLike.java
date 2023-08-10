package refree.backend.module.recipeLike;

import lombok.Getter;
import refree.backend.module.recipe.Recipe;
import refree.backend.module.member.Member;

import javax.persistence.*;

@Entity
@Getter
public class RecipeLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    public static RecipeLike createRecipeLike(Member member, Recipe recipe) {
        RecipeLike recipeLike = new RecipeLike();
        recipeLike.member = member;
        recipeLike.recipe = recipe;
        return recipeLike;
    }
}
