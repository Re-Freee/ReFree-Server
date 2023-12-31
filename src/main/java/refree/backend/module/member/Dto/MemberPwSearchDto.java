package refree.backend.module.member.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import refree.backend.module.member.Member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MemberPwSearchDto {

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Size(max = 40, message = "이메일은 40자 내로 입력해 주세요.")
    @Email(message = "이메일 형식으로 입력해 주세요.")
    private String email;

    @NotBlank(message = "인증번호를 입력해 주세요.")
    private String certification;
}
