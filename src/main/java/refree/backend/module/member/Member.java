package refree.backend.module.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import refree.backend.module.RecipeLike.RecipeLike;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "check_password")
    private String checkPassword;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", length = 8, nullable = false)
    private String nickname;

    @Column(name = "is_change", nullable = false)
    private int isChange;

    @OneToMany(mappedBy = "member")
    private Set<RecipeLike> likes = new HashSet<>();

    public void deleteRecipeFromLikes(RecipeLike recipeLike) {
        likes.remove(recipeLike);
    }

    @Column(name = "certification", nullable = false)
    private String certification;

    public int getIsChange(){
        return isChange;
    }

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }


    public void updatePassword(PasswordEncoder passwordEncoder, String newPassword){
        this.password = passwordEncoder.encode(newPassword);
    }

    public void updateFlag(int isChange){
        this.isChange = isChange;
    }

    public void setCertification(String certification){
        this.certification = certification;
    }
}
