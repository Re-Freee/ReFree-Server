package refree.backend.module.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import refree.backend.infra.config.CurrentUser;
import refree.backend.infra.response.BasicResponse;
import refree.backend.infra.response.GeneralResponse;
import refree.backend.infra.response.SingleResponse;
import refree.backend.module.member.Dto.MemberPwModifyDto;
import refree.backend.module.member.Dto.MemberPwSearchDto;
import refree.backend.module.member.Dto.MemberSignupDto;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<? extends BasicResponse> signup(@RequestBody @Valid MemberSignupDto memberSignupDto, HttpServletResponse response) {
        return ResponseEntity.ok().body(memberService.signup(memberSignupDto, response));
    }

    @PostMapping("/login/search")
    public ResponseEntity<? extends BasicResponse> search(@RequestBody @Valid MemberPwSearchDto memberPwSearchDto) {
        return ResponseEntity.ok().body(memberService.search(memberPwSearchDto));
    }

    @PostMapping("/login/search/modify")
    public ResponseEntity<? extends BasicResponse> modify(@RequestBody @Valid MemberPwModifyDto memberPwModifyDto) {
        return ResponseEntity.ok().body(memberService.modify(memberPwModifyDto));
    }

    @GetMapping("/member/like")
    public ResponseEntity<? extends BasicResponse> likedRecipe(@CurrentUser Member member) {
        return ResponseEntity.ok()
                .body(new GeneralResponse<>(memberService.recipeLike(member), "LIKED_RECIPE_RESULT"));
    }

    @DeleteMapping("/member/delete")
    public ResponseEntity<? extends BasicResponse> delete(@CurrentUser Member member) {
        memberService.delete(member);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }
}
