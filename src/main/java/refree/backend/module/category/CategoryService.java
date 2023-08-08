package refree.backend.module.category;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void initCategoryData() throws IOException {
        if (categoryRepository.count() == 0) {
            ClassPathResource classPathResource = new ClassPathResource("category_data.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(classPathResource.getInputStream(),
                    StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                // 한 줄씩 읽어와서 stringBuilder에 추가
                stringBuilder.append(line);
            }
            String[] split = stringBuilder.toString().split(", ");
            List<Category> categoryList = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                categoryList.add(Category.builder().name(split[i]).build());
            }
            categoryRepository.saveAll(categoryList);
        }
    }
}