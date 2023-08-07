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
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    public void setMember(Member member) {
        this.member = member;
        member.getLikes().add(this);
    }

    public static RecipeLike createRecipeLike(Member member, Recipe recipe) {
        RecipeLike recipeLike = new RecipeLike();
        recipeLike.setMember(member);
        recipeLike.recipe = recipe;
        return recipeLike;
    }
}
