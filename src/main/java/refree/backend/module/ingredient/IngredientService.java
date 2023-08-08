package refree.backend.module.ingredient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import refree.backend.infra.exception.MemberException;
import refree.backend.infra.exception.NotFoundException;
import refree.backend.infra.exception.ParsingException;
import refree.backend.module.category.Category;
import refree.backend.module.category.CategoryRepository;
import refree.backend.module.ingredient.Dto.IngredientDto;
import refree.backend.module.ingredient.Dto.IngredientShortResponse;
import refree.backend.module.ingredient.Dto.IngredientViewDto;
import refree.backend.module.ingredient.Dto.IngredientSearch;
import refree.backend.module.picture.Picture;
import refree.backend.module.picture.PictureService;
import refree.backend.module.member.Member;
import refree.backend.module.member.MemberRepository;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final PictureService pictureService;

    public void create(IngredientDto ingredientDto, MultipartFile file, Long memberId) {
        Picture picture = null;
        if (file != null) {
            String storageImageName = pictureService.saveImage(file);
            picture = pictureService.getPicture(storageImageName);
        }

        LocalDate localDateFromString = getLocalDateFromString(ingredientDto.getPeriod());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 계정입니다."));
        Category category = categoryRepository.findByName(ingredientDto.getCategory())
                .orElseThrow(() -> new NotFoundException("NOT_VALID_CATEGORY"));
        Ingredient ingredient = Ingredient.createIngredient(member, category, localDateFromString, ingredientDto, picture);
        ingredientRepository.save(ingredient);
    }

    @Transactional(readOnly = true)
    public List<IngredientViewDto> view(Long ingredientId) {
        Ingredient ingredient = ingredientRepository.findByIdFetchJoinImage(ingredientId);
        if (ingredient == null)
            throw new NotFoundException("존재하지 않는 재료");
        return List.of(IngredientViewDto.getIngredientViewDto(ingredient));
    }

    public void update(IngredientDto ingredientDto, MultipartFile file, Long ingredientId) {
        Ingredient ingredient = ingredientRepository.findByIdFetchJoinImage(ingredientId);
        if (ingredient == null)
            throw new NotFoundException("존재하지 않는 재료");

        Picture savePicture = null;
        if (file != null) { // 저장하려는 이미지 있음
            pictureService.updateImageCheck(file, ingredient);
            String storageImageName = pictureService.saveImage(file);
            savePicture = pictureService.getPicture(storageImageName);
        } else { // 저장하려는 이미지 : null
            pictureService.updateImageCheck(null, ingredient);
        }

        LocalDate localDateFromString = getLocalDateFromString(ingredientDto.getPeriod());
        Category category = categoryRepository.findByName(ingredientDto.getCategory())
                .orElseThrow(() -> new NotFoundException("NOT_VALID_CATEGORY"));
        ingredient.update(localDateFromString, category, ingredientDto, savePicture);
    }


    public List<IngredientShortResponse> imminent(Long memId) { //유통기한이 3일 남은 경우
        List<Ingredient> ingredients = ingredientRepository.findAllByMemberFetchJoinImage(memId);

        List<IngredientShortResponse> confirm = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            LocalDate expire = ingredient.getPeriod(); // 재료 소비기한날짜
            LocalDate currentDate = LocalDate.now(); // 현재 날짜

            Period period = Period.between(expire, currentDate);
            int daysPassed = period.getDays();

            if (expire.isAfter(currentDate) && daysPassed <= 3) { // 날짜 아직 지나지 않았고 3일 이하
                confirm.add(IngredientShortResponse.getIngredientShortResponse(ingredient));
            }
        }
        return confirm;
    }

    public List<IngredientShortResponse> end(Long memId) { // 유통기한이 만료된 경우
        List<Ingredient> ingredients = ingredientRepository.findAllByMemberFetchJoinImage(memId);

        List<IngredientShortResponse> confirm = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            LocalDate expire = ingredient.getPeriod();
            LocalDate currentDate = LocalDate.now();

            if (currentDate.isAfter(expire)) // 현재시간이 만료일보다 미래
                confirm.add(IngredientShortResponse.getIngredientShortResponse(ingredient));
        }
        return confirm;
    }

    @Transactional(readOnly = true)
    public List<IngredientShortResponse> search(IngredientSearch ingredientSearch, Member member) {
        List<Ingredient> ingredients = ingredientRepository.search(ingredientSearch, member.getId());
        return ingredients.stream()
                .map(IngredientShortResponse::getIngredientShortResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LocalDate getLocalDateFromString(String period) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        try {
            return LocalDate.parse(period, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            throw new ParsingException("PARSING_ERROR");
        }
    }

    public void delete(Member member, Long ingredientId) {
        Ingredient ingredient = ingredientRepository.findByIdFetchJoinImage(ingredientId);
        if (ingredient == null)
            throw new NotFoundException("존재하지 않는 재료");
        if (!ingredient.getMember().getId().equals(member.getId()))
            throw new MemberException("UNAUTHORIZED_ACCESS");
        // 연관된 이미지 삭제 + 재료 삭제
        pictureService.deletePicture(ingredient);
        ingredientRepository.delete(ingredient);
    }
}
