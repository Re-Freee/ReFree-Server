package refree.backend.module.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import refree.backend.infra.response.BasicResponse;
import refree.backend.infra.response.SingleResponse;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/home")
    public ResponseEntity<? extends BasicResponse> home()  {
        return ResponseEntity.ok().body(new SingleResponse("home"));
    }

    @GetMapping("/test")
    public ResponseEntity<? extends BasicResponse> test()  {
        return ResponseEntity.ok().body(new SingleResponse("test"));
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<? extends BasicResponse> signup(@RequestBody @Valid MemberSignupDto memberSignupDto)  {
        return ResponseEntity.ok().body(memberService.signup(memberSignupDto));
    }

    @PostMapping("/login/search")
    public ResponseEntity<? extends BasicResponse> search(@RequestBody @Valid MemberPwSearchDto memberPwSearchDto) {
        return ResponseEntity.ok().body(memberService.search(memberPwSearchDto));
    }

    @PostMapping("/login/search/modify")
    public ResponseEntity<? extends BasicResponse> modify(@RequestBody @Valid MemberPwModifyDto memberPwModifyDto)  {
        return ResponseEntity.ok().body(memberService.modify(memberPwModifyDto));
    }
}
