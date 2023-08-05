package refree.backend.module.Ingredient;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import refree.backend.infra.config.CurrentUser;
import refree.backend.infra.response.BasicResponse;
import refree.backend.infra.response.GeneralResponse;
import refree.backend.infra.response.SingleResponse;
import refree.backend.module.Ingredient.Dto.IngredientDto;
import refree.backend.module.Ingredient.Dto.IngredientResponseDto;
import refree.backend.module.Ingredient.Dto.IngredientSearch;
import refree.backend.module.member.Member;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    //재료 저장
    @PostMapping("/create") //TODO: Multipart
    public ResponseEntity<? extends BasicResponse> create(@RequestBody @Valid IngredientDto ingredientDto,
                                                          @CurrentUser Member member) {
        ingredientService.create(ingredientDto, member.getId());
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    //재료 상세보기
    @GetMapping("/view")
    public ResponseEntity<? extends BasicResponse> view(@RequestParam("ingredientId") Long ingredientId) {
        return ResponseEntity.ok()
                .body(new GeneralResponse<>(ingredientService.view(ingredientId),"VIEW_INGREDIENT"));
    }

    //재료 수량 조절
    @PutMapping("/edit/{ingredientId}") //TODO: Multipart
    public ResponseEntity<? extends BasicResponse> update(@RequestBody @Valid IngredientDto ingredientDto,
                                                          @PathVariable("ingredientId") Long ingredientId) {
        ingredientService.update(ingredientDto, ingredientId);
        return ResponseEntity.ok().body(new SingleResponse("SUCCESS"));
    }

    //재료 소비기한 임박
    /*@GetMapping("/closure")
    public ResponseEntity<? extends BasicResponse> closure(@RequestParam int mem_id){
        return ResponseEntity.ok().body(new GeneralResponse<>(ingredientService.closure(mem_id),"CLOSE_INGREDIENT"));
    }*/

    //재료 소비기한 만료
    /*@GetMapping("/end")
    public ResponseEntity<? extends BasicResponse> end(@RequestParam int mem_id){
        return ResponseEntity.ok().body(new GeneralResponse<>(ingredientService.end(mem_id),"END_INGREDIENT"));
    }
*/
    //재료 검색
    @GetMapping("/search")
    public ResponseEntity<? extends BasicResponse> search(@ModelAttribute IngredientSearch ingredientSearch,
                                                          @CurrentUser Member member){
        List<IngredientResponseDto> search = ingredientService.search(ingredientSearch, member);
        return ResponseEntity.ok().body(new GeneralResponse<>(search, "INGRED_SEARCH"));
    }
}