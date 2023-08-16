package refree.backend.module.member.Dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
public class MemberPwModifyDto {
    @NotBlank(message = "이메일을 입력해 주세요.")
    @Size(max = 40, message = "이메일은 40자 내로 입력해 주세요.")
    @Email(message = "이메일 형식으로 입력해 주세요.")
    private String email;

    @NotBlank(message = "새 비밀번호를 입력해 주세요.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "새 비밀번호를 입력해 주세요.")
    private String checkPassword;
}
