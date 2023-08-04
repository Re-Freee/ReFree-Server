package refree.backend.module.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MemberSignupDto{

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Size(max = 40, message = "이메일은 40자 내로 입력해 주세요.")
    @Email(message = "이메일 형식으로 입력해 주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Size(min = 8, message = "비밀번호는 8자 이상 입력하셔야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호를 다시 입력해 주세요.")
    private String checkPassword;

    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Size(min = 2, max = 8, message = "닉네임은 2~8자 이내로 입력하셔야 합니다.")
    private String nickname;

    public Member toEntity(){

        return Member.builder()
                .email(email).password(password)
                .nickname(nickname)
                .isChange(0)
                .build();
    }

}
