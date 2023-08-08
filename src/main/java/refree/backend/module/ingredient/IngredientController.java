package refree.backend.module.ingredient;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import refree.backend.infra.config.CurrentUser;
import refree.backend.infra.response.BasicResponse;
import refree.backend.infra.response.GeneralResponse;
import refree.backend.infra.response.SingleResponse;
import refree.backend.module.ingredient.Dto.IngredientDto;
import refree.backend.module.ingredient.Dto.IngredientResponseDto;
import refree.backend.module.ingredient.Dto.IngredientSearch;
import refree.backend.module.member.Member;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    //재료 저장
    @PostMapping("/create")
    public ResponseEntity<? extends BasicResponse> create(@ModelAttribute @Valid IngredientDto ingredientDto,
                                                          @RequestPart(value = "image", required = false) MultipartFile file,
                                                          @CurrentUser Member member) {
        ingredientService.create(ingredientDto, file, member.getId());
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    //재료 상세보기
    @GetMapping("/view")
    public ResponseEntity<? extends BasicResponse> view(@RequestParam("ingredientId") Long ingredientId) {
        return ResponseEntity.ok()
                .body(new GeneralResponse<>(ingredientService.view(ingredientId), "VIEW_INGREDIENT"));
    }

    //재료 수정
    @PostMapping("/edit/{ingredientId}")
    public ResponseEntity<? extends BasicResponse> update(@ModelAttribute @Valid IngredientDto ingredientDto,
                                                          @RequestPart(value = "image", required = false) MultipartFile file,
                                                          @PathVariable("ingredientId") Long ingredientId) {
        ingredientService.update(ingredientDto, file, ingredientId);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }


    //유통기한 임박 재료
    @GetMapping("/closure")
    public ResponseEntity<? extends BasicResponse> closure(@CurrentUser Member member){
        return ResponseEntity.ok()
                .body(new GeneralResponse<>(ingredientService.closure(member.getId()),"CLOSE_INGREDIENT"));
    }

    //유통기한 지난 재료
    @GetMapping("/end")
    public ResponseEntity<? extends BasicResponse> end(@CurrentUser Member member){
        return ResponseEntity.ok()
                .body(new GeneralResponse<>(ingredientService.end(member.getId()),"END_INGREDIENT"));
    }

    //재료 검색
    @GetMapping("/search")
    public ResponseEntity<? extends BasicResponse> search(@ModelAttribute IngredientSearch ingredientSearch,
                                                          @CurrentUser Member member) {
        List<IngredientResponseDto> search = ingredientService.search(ingredientSearch, member);
        return ResponseEntity.ok().body(new GeneralResponse<>(search, "INGRED_SEARCH"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<? extends BasicResponse> delete(@RequestParam("ingredientId") Long ingredientId,
                                                          @CurrentUser Member member) {
        ingredientService.delete(member, ingredientId);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }
}
