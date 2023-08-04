package refree.backend.module.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private int id;

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

    public int getIsChange(){
        return isChange;
    }

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }

    public void encodeCheckPassword(PasswordEncoder passwordEncoder){
        this.checkPassword = passwordEncoder.encode(checkPassword);
    }

    public void updatePassword(String newPassword){
        this.password = newPassword;
    }

    public void updateCheckPassword(String newCheckPassword){
        this.checkPassword = newCheckPassword;
    }

    public void updateFlag(int isChange){
        this.isChange = isChange;
    }
}
