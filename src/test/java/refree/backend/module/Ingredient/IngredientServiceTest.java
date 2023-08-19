package refree.backend.module.Ingredient;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import refree.backend.module.ingredient.Ingredient;
import refree.backend.module.ingredient.IngredientRepository;
import refree.backend.module.ingredient.IngredientService;
import refree.backend.module.member.Member;
import refree.backend.module.member.MemberRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import refree.backend.module.picture.Picture;
import refree.backend.module.picture.PictureService;
import software.amazon.awssdk.services.chime.model.MemberError;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @InjectMocks
    private IngredientService ingredientService;

    @Mock
    private PictureService pictureService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private IngredientRepository ingredientRepository;

    private IngredientDto dto;
    private MultipartFile file;
    private Long memberId;
    private Long ingredientId;
    private IngredientSearch search;
    private Ingredient ingredient;
    private Ingredient ingredient2;
    private Ingredient ingredient3;
    private Ingredient ingredient4;
    private Ingredient ingredient5;

    private Member member;
    private Member member2;

    @BeforeEach
    void setUp() {
        memberId = 1L;
        ingredientId=2L;
        member=Member.builder()
                .email("1111@naver.com")
                .password("1111")
                .id(3L)
                .build();
        member2=Member.builder()
                .email("1112@naver.com")
                .password("1111")
                .id(4L)
                .build();

        dto = new IngredientDto();
        dto.setName("김치");
        dto.setPeriod("2023.08.11");
        dto.setCategory("김치");
        dto.setQuantity(2);
        dto.setContent("유기농");
        dto.setOptions(0);
        file = mock(MultipartFile.class);
        search = mock(IngredientSearch.class);
        LocalDate localDate=LocalDate.now(); //오늘
        LocalDate oneDayPass=localDate.plusDays(1); //내일
        LocalDate oneDayLeft=localDate.minusDays(1); //어제
        LocalDate fourDayPass=localDate.plusDays(4);//4일 후
        LocalDate threeDayPass=localDate.plusDays(3); //3일 후

        ingredient=Ingredient.createIngredient(member,mock(Category.class),localDate,
                dto,mock(Picture.class));
        ingredient2=Ingredient.createIngredient(member,mock(Category.class),oneDayPass,
                dto,mock(Picture.class));
        ingredient3=Ingredient.createIngredient(member,mock(Category.class),oneDayLeft,
                dto,mock(Picture.class));
        ingredient4=Ingredient.createIngredient(member,mock(Category.class),fourDayPass,
                dto,mock(Picture.class));
        ingredient5=Ingredient.createIngredient(member,mock(Category.class),threeDayPass,
                dto,mock(Picture.class));

    }

    @Test
    void create() {
        when(pictureService.saveImage(any(MultipartFile.class))).thenReturn("image-name");
        when(pictureService.getPicture(anyString())).thenReturn(new Picture());
        when(memberRepository.findById(anyLong())).thenReturn(java.util.Optional.of(new Member()));
        when(categoryRepository.findByName(anyString())).thenReturn(java.util.Optional.of(new Category()));

        ingredientService.create(dto, file, memberId);

        verify(pictureService, times(1)).saveImage(any(MultipartFile.class));
        verify(pictureService, times(1)).getPicture(anyString());
        verify(memberRepository, times(1)).findById(anyLong());
        verify(categoryRepository, times(1)).findByName(anyString());
        verify(ingredientRepository, times(1)).save(any(Ingredient.class));
    }
    @Test
    void createNoMem() {
        when(pictureService.saveImage(any(MultipartFile.class))).thenReturn("image-name");
        when(pictureService.getPicture(anyString())).thenReturn(new Picture());
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

//        assertThrows(NotFoundException.class, () -> ingredientService.create(dto, file, memberId));
        NotFoundException e = assertThrows(NotFoundException.class, () -> ingredientService.create(dto, file, memberId));
        assertEquals("존재하지 않는 계정입니다.", e.getMessage());
    }
    @Test
    void createNoCategory() {
        when(pictureService.saveImage(any(MultipartFile.class))).thenReturn("image-name");
        when(pictureService.getPicture(anyString())).thenReturn(new Picture());
        when(memberRepository.findById(anyLong())).thenReturn(java.util.Optional.of(new Member()));
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> ingredientService.create(dto, file, memberId));
        assertEquals("NOT_VALID_CATEGORY", e.getMessage());
    }

    @Test
    void view() {
        when(ingredientRepository.findByIdFetchJoinImageAndCategory(1L)).thenReturn(ingredient);

        List<IngredientViewDto> expected = List.of(IngredientViewDto.getIngredientViewDto(ingredient));
        List<IngredientViewDto> actual=ingredientService.view(1L);

        assertEquals(expected,actual);
    }
    @Test
    void viewNoIngredient(){
        when(ingredientRepository.findByIdFetchJoinImageAndCategory(anyLong())).thenReturn(null);

        NotFoundException e = assertThrows(NotFoundException.class, () -> ingredientService.view(ingredientId));
        assertEquals("존재하지 않는 재료", e.getMessage());
    }


    @Test
    void getLocalDateFromString() {
        String check="2023.08.10";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        LocalDate localDate=ingredientService.getLocalDateFromString(check);
        LocalDate date=LocalDate.parse(check,dateTimeFormatter);

        Assertions.assertEquals(localDate,date);
    }

    @Test
    void getLocalDateParse(){
        String check="2023-08-10";

        ParsingException e = assertThrows(ParsingException.class, () -> ingredientService.getLocalDateFromString(check));
        assertEquals("PARSING_ERROR", e.getMessage());
    }
    @Test
    void EliminationParse(){
        String check="2023.08";

        ParsingException e = assertThrows(ParsingException.class, () -> ingredientService.getLocalDateFromString(check));
        assertEquals("PARSING_ERROR", e.getMessage());
    }


    @Test
    void update() {
        when(ingredientRepository.findByIdFetchJoinImage(anyLong())).thenReturn(new Ingredient());
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(new Category()));

        ingredientService.update(dto,file,ingredientId);

        verify(ingredientRepository, times(1)).findByIdFetchJoinImage(anyLong());
        verify(pictureService, times(1)).updateImageCheck(any(MultipartFile.class),any(Ingredient.class));
        verify(pictureService, times(1)).saveImage(any(MultipartFile.class));
        verify(categoryRepository, times(1)).findByName(anyString());
    }
    @Test
    void updateNoIngredient() {
        when(ingredientRepository.findByIdFetchJoinImage(anyLong())).thenReturn(null);

        NotFoundException e = assertThrows(NotFoundException.class, () -> ingredientService.update(dto, file, ingredientId));
        assertEquals("존재하지 않는 재료", e.getMessage());
    }
    @Test
    void updateNoCategory() {
        when(ingredientRepository.findByIdFetchJoinImage(anyLong())).thenReturn(new Ingredient());
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> ingredientService.update(dto, file, ingredientId));
        assertEquals("NOT_VALID_CATEGORY", e.getMessage());
    }

    @Test
    void search() {
        List<Ingredient> check=new ArrayList<>();
        when(ingredientRepository.search(search,member.getId())).thenReturn(check);

        ingredientService.search(search,member);

        verify(ingredientRepository,times(1)).search(search,member.getId());
    }
    @Test
    void closure() {
        List<Ingredient> check=new ArrayList<>();
        check.add(ingredient);check.add(ingredient2);
        check.add(ingredient3);check.add(ingredient4); check.add(ingredient5);
        when(ingredientRepository.findAllByMemberFetchJoinImage(member.getId())).thenReturn(check);

        List<IngredientShortResponse> expected=new ArrayList<>();
        expected.add(IngredientShortResponse.getIngredientShortResponse(ingredient));//오늘
        expected.add(IngredientShortResponse.getIngredientShortResponse(ingredient2));//내일
        expected.add(IngredientShortResponse.getIngredientShortResponse(ingredient5));//3일 후
        List<IngredientShortResponse> actual=ingredientService.imminent(member.getId());

        assertEquals(expected,actual);
    }

    @Test
    void end() {
        List<Ingredient> check=new ArrayList<>();
        check.add(ingredient);check.add(ingredient2);
        check.add(ingredient3);check.add(ingredient4); check.add(ingredient5);
        when(ingredientRepository.findAllByMemberFetchJoinImage(member.getId())).thenReturn(check);

        List<IngredientShortResponse> expected=new ArrayList<>();
        expected.add(IngredientShortResponse.getIngredientShortResponse(ingredient3)); //어제
        List<IngredientShortResponse> actual=ingredientService.end(member.getId());

        assertEquals(expected,actual);
    }

    @Test
    void delete(){
        when(ingredientRepository.findByIdFetchJoinImage(ingredient.getId())).thenReturn(ingredient);

        ingredientService.delete(member,ingredient.getId());

        verify(ingredientRepository,times(1)).findByIdFetchJoinImage(ingredient.getId());
        verify(pictureService,times(1)).deletePicture(ingredient);
        verify(ingredientRepository,times(1)).delete(ingredient);
    }
    @Test
    void deleteNoIngredient(){
        when(ingredientRepository.findByIdFetchJoinImage(ingredient.getId())).thenReturn(null);

        NotFoundException e = assertThrows(NotFoundException.class, () -> ingredientService.delete(member,ingredient.getId()));
        assertEquals("존재하지 않는 재료", e.getMessage());
    }
    @Test
    void deleteNoMember(){
        when(ingredientRepository.findByIdFetchJoinImage(ingredient.getId())).thenReturn(ingredient);

        MemberException e=assertThrows(MemberException.class, () -> ingredientService.delete(member2,ingredient.getId()));
        assertEquals("UNAUTHORIZED_ACCESS",e.getMessage());
    }


}