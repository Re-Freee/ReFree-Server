package refree.backend.module.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import refree.backend.infra.exception.MemberException;
import refree.backend.infra.response.SingleResponse;
import refree.backend.module.ingredient.Ingredient;
import refree.backend.module.ingredient.IngredientRepository;
import refree.backend.module.member.Dto.MemberPwModifyDto;
import refree.backend.module.member.Dto.MemberPwSearchDto;
import refree.backend.module.member.Dto.MemberSignupDto;
import refree.backend.module.picture.PictureService;
import refree.backend.module.recipe.Dto.RecipeLikeDto;
import refree.backend.module.recipe.Recipe;
import refree.backend.module.recipeLike.RecipeLike;
import refree.backend.module.recipeLike.RecipeLikeRepository;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RecipeLikeRepository recipeLikeRepository;
    private final PasswordEncoder passwordEncoder;
    private final IngredientRepository ingredientRepository;
    private final PictureService pictureService;


    // 회원가입
    public SingleResponse signup(MemberSignupDto memberSignupDto, HttpServletResponse response) {

        // 이미 존재하는 이메일
        if (memberRepository.existsByEmail(memberSignupDto.getEmail())) {
            throw new MemberException("이미 존재하는 계정입니다.");
        }

        // 비밀번호 일치하지 않음
        if (!memberSignupDto.getPassword().equals(memberSignupDto.getCheckPassword())) {
            throw new MemberException("비밀번호가 일치하지 않습니다.");
        }

        Member member = memberSignupDto.toEntity();
        member.encodePassword(passwordEncoder);

        // 인증번호 발급
        String certification = UUID.randomUUID().toString();
        member.setCertification(certification);

        memberRepository.save(member);

        response.addHeader("Certification", certification);
        return new SingleResponse(201, "REGISTRATION_COMPLETE");
    }

    public SingleResponse search(MemberPwSearchDto memberPwSearchDto) {
        Member member = memberRepository.findByEmail(memberPwSearchDto.getEmail())
                .orElseThrow(() -> new MemberException("존재하지 않는 계정입니다."));

        if (!memberPwSearchDto.getCertification().equals(member.getCertification())) {
            throw new MemberException("인증번호가 일치하지 않습니다.");
        }

        member.updateFlag(1); // 비밀번호를 바꾸기 위한 flag 설정

        return new SingleResponse(200, "EMAIL_EXIST");
    }

    public SingleResponse modify(MemberPwModifyDto memberPwModifyDto) {
        Member member = memberRepository.findByEmail(memberPwModifyDto.getEmail())
                .orElseThrow(() -> new MemberException("존재하지 않는 계정입니다."));

        if (member.getIsChange() == 0) { // Exception (이미 존재하는 것이 확인된 계정에서만 호출되는 API
            throw new MemberException("잘못된 접근입니다.");
        }

        if (!memberPwModifyDto.getPassword().equals(memberPwModifyDto.getCheckPassword())) {
            throw new MemberException("비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 변경 + 계정찾기 flag 0으로 설정
        member.updatePassword(passwordEncoder, memberPwModifyDto.getPassword());
        member.updateFlag(0);

        return new SingleResponse(200, "CHANGE_PASSWORD");
    }

    @Transactional(readOnly = true)
    public List<RecipeLikeDto> recipeLike(Member member, Integer offset) {
        if (offset == null)
            offset = 0;
        else
            offset = Math.max(offset, 0);
        List<RecipeLike> recipeLikes = recipeLikeRepository.findByMemberFetchJoinRecipe(member.getId(), offset);
        List<Recipe> recipes = recipeLikes.stream().map(RecipeLike::getRecipe).collect(Collectors.toList());
        return recipes.stream().map(RecipeLikeDto::getRecipeLikeDto).collect(Collectors.toList());
    }

    public void delete(Member member) {
        // member와 연관된 모든 ingredient 조회 + fetch join image
        List<Ingredient> ingredients = ingredientRepository.findAllByMemberFetchJoinImage(member.getId());
        ingredients.forEach(pictureService::deletePicture);
        ingredientRepository.deleteAll(ingredients);
        // member와 연관된 모든 recipe_like 조회
        List<RecipeLike> recipeLikes = recipeLikeRepository.findByMemberId(member.getId());
        recipeLikeRepository.deleteAll(recipeLikes);
        // member 삭제
        memberRepository.deleteById(member.getId());
    }
}