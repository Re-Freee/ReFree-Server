package refree.backend.module.member;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private int id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    public static Member crateMember(String email, String password) {
        Member member = new Member();
        member.email = email;
        member.password = password;
        return member;
    }
}
