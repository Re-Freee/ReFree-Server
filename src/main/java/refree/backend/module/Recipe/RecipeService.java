package refree.backend.module.Recipe;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import refree.backend.infra.exception.BadRequestException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import refree.backend.infra.exception.NotFoundException;
import refree.backend.module.Category.CategoryRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void initRecipeData() throws IOException, ParseException {
        if (recipeRepository.count() == 0) {
            ClassPathResource classPathResource1 = new ClassPathResource("recipe_data1.json");
            ClassPathResource classPathResource2 = new ClassPathResource("recipe_data2.json");
            ClassPathResource classPathResource3 = new ClassPathResource("recipe_data3.json");
            ClassPathResource classPathResource4 = new ClassPathResource("recipe_data4.json");
            ClassPathResource classPathResource5 = new ClassPathResource("recipe_data5.json");
            ClassPathResource classPathResource6 = new ClassPathResource("recipe_data6.json");
            ClassPathResource classPathResource7 = new ClassPathResource("recipe_data7.json");
            ClassPathResource classPathResource8 = new ClassPathResource("recipe_data8.json");
            ClassPathResource classPathResource9 = new ClassPathResource("recipe_data9.json");
            ClassPathResource classPathResource10 = new ClassPathResource("recipe_data10.json");
            ClassPathResource classPathResource11 = new ClassPathResource("recipe_data11.json");

            saveRecipeData(classPathResource1);
            saveRecipeData(classPathResource2);
            saveRecipeData(classPathResource3);
            saveRecipeData(classPathResource4);
            saveRecipeData(classPathResource5);
            saveRecipeData(classPathResource6);
            saveRecipeData(classPathResource7);
            saveRecipeData(classPathResource8);
            saveRecipeData(classPathResource9);
            saveRecipeData(classPathResource10);
            saveRecipeData(classPathResource11);
        }
    }

    private void saveRecipeData(ClassPathResource classPathResource) throws IOException, ParseException {
        List<Recipe> recipeList = new ArrayList<>();
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser
                .parse(new InputStreamReader(classPathResource.getInputStream(), StandardCharsets.UTF_8));

        JSONObject cookObject = (JSONObject) jsonObject.get("COOKRCP01");
        JSONArray recipeArray = (JSONArray) cookObject.get("row");
        for (int i = 0; i < recipeArray.size(); i++) {
            JSONObject recipeObject = (JSONObject) recipeArray.get(i);
            Recipe recipe = Recipe.builder()
                    .name((String) recipeObject.get("RCP_NM"))
                    .type((String) recipeObject.get("RCP_PAT2"))
                    .calorie(Double.parseDouble((String) recipeObject.get("INFO_ENG")))
                    .imageUrl((String) recipeObject.get("ATT_FILE_NO_MAIN"))
                    .ingredient((String) recipeObject.get("RCP_PARTS_DTLS"))
                    .manuel1((String) recipeObject.get("MANUAL01"))
                    .manuelUrl1((String) recipeObject.get("MANUAL_IMG01"))
                    .manuel2((String) recipeObject.get("MANUAL02"))
                    .manuelUrl2((String) recipeObject.get("MANUAL_IMG02"))
                    .manuel3((String) recipeObject.get("MANUAL03"))
                    .manuelUrl3((String) recipeObject.get("MANUAL_IMG03"))
                    .manuel4((String) recipeObject.get("MANUAL04"))
                    .manuelUrl4((String) recipeObject.get("MANUAL_IMG04"))
                    .manuel5((String) recipeObject.get("MANUAL05"))
                    .manuelUrl5((String) recipeObject.get("MANUAL_IMG05"))
                    .manuel6((String) recipeObject.get("MANUAL06"))
                    .manuelUrl6((String) recipeObject.get("MANUAL_IMG06"))
                    .build();
            recipeList.add(recipe);
        }
        recipeRepository.saveAll(recipeList);
    }

    public List<RecipeDto> recommend(List<String> Ingredients) {
        Ingredients.forEach(i -> {
            if (!categoryRepository.existsByName(i))
                throw new NotFoundException("존재하지 않는 재료");
        });

        ArrayList<RecipeCount> recipeCounts = new ArrayList<>();
        for (long i = 0; i < 1115; i++) {
            recipeCounts.add(RecipeCount.createRecipeCount(i));
        }

        for (String ingredient : Ingredients) {
            recipeRepository.findByIngredientContains(ingredient).forEach(recipe ->
                    recipeCounts.get(recipe.getId().intValue()).plusCount()
            );
        }

        // count를 기준으로 내림차순 정렬
        recipeCounts.sort(new Comparator<RecipeCount>() {
            @Override
            public int compare(RecipeCount o1, RecipeCount o2) {
                return Integer.compare(o2.getCount(), o1.getCount());
            }
        });

        List<Long> recipeIds = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            RecipeCount recipeCount = recipeCounts.get(i);
            if (recipeCount.getCount() == 0) {
                break;
            }
            recipeIds.add(recipeCount.getRecipeId());
        }
        if (recipeIds.size() != 0) {
            return recipeIds.stream() // 우선 순위대로 내려주는 방식
                    .map(recipeId -> {
                        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(()
                                -> new BadRequestException("BAD_REQUEST"));
                        return RecipeDto.builder()
                                .id(recipe.getId())
                                .name(recipe.getName())
                                .calorie(recipe.getCalorie())
                                .ingredient(recipe.getIngredient())
                                .image(recipe.getImageUrl())
                                .build();
                    }).collect(Collectors.toList());
            /*List<Recipe> recipeResultList = recipeRepository.findByIn(recipeIds); // 우선 순위를 고려하지 않은 방식
            return recipeResultList.stream()
                    .map(recipe -> RecipeDto.builder()
                            .id(recipe.getId())
                            .name(recipe.getName())
                            .calorie(recipe.getCalorie())
                            .ingredient(recipe.getIngredient())
                            .image(recipe.getImageUrl())
                            .build()).collect(Collectors.toList());*/
        }
        return List.of();
    }
}
