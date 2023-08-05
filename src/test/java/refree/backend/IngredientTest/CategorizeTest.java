package refree.backend.IngredientTest;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import refree.backend.module.Ingredient.Ingredient;
import refree.backend.module.Ingredient.IngredientService;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class CategorizeTest {
    @Autowired
    IngredientService ingredientService;

    @Test
    public void Category() throws Exception{
        Ingredient ingredient=new Ingredient();
        Ingredient ingredient2=new Ingredient();
        ingredient.setName("땅콩밥");
        ingredient2.setName("현미밥");

        String category=ingredientService.ingredientToCategory(ingredient.getName());
        String category2=ingredientService.ingredientToCategory(ingredient2.getName());

        assertEquals(category,category2);
    }


}
