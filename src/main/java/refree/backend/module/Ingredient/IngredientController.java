package refree.backend.module.Ingredient;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import refree.backend.infra.response.BasicResponse;
import refree.backend.infra.response.GeneralResponse;

import java.io.IOException;

@RestController
@RequestMapping("ingredient")
public class IngredientController {
    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService){
        this.ingredientService=ingredientService;
    }

    //재료 저장
    @PostMapping("/create")
    public void create(@RequestBody Ingredient ingredient) throws IOException {
        ingredientService.create(ingredient);
    }
    //재료 상세보기(new GeneralResponse<>(recipeDtos, "RECOMMEND_RECIPE_RESULT")
    @GetMapping("/view")
    public ResponseEntity<? extends BasicResponse> view(@RequestParam int ingredient_id){
        return ResponseEntity.ok().body(new GeneralResponse<>(ingredientService.view(ingredient_id),"VIEW_INGREDIENT"));
    }
    //재료 수량 조절
    @PutMapping("/delete")
    public ResponseEntity<? extends BasicResponse> delete(@RequestParam int ingred_id,@RequestParam int cnt,@RequestParam String memo){
        return ResponseEntity.ok().body(new GeneralResponse<>(ingredientService.delete(ingred_id,cnt,memo),"DELETE_SUCCESS"));
    }
    //재료 소비기한 임박
    @GetMapping("/closure")
    public ResponseEntity<? extends BasicResponse> closure(@RequestParam int mem_id){
        return ResponseEntity.ok().body(new GeneralResponse<>(ingredientService.closure(mem_id),"CLOSE_INGREDIENT"));
    }
    //재료 소비기한 만료
    @GetMapping("/end")
    public ResponseEntity<? extends BasicResponse> end(@RequestParam int mem_id){
        return ResponseEntity.ok().body(new GeneralResponse<>(ingredientService.end(mem_id),"END_INGREDIENT"));
    }
    //재료 검색
    @GetMapping("/search")
    public ResponseEntity<? extends BasicResponse> search(@RequestParam String searchKey, @RequestParam int mem_id){
        return ResponseEntity.ok().body(new GeneralResponse<>(ingredientService.search(searchKey,mem_id),"INGRED_SEARCH"));
    }
}
