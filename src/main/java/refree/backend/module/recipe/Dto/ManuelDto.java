package refree.backend.module.recipe.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ManuelDto {

    private String describe;
    private String manuelImage;

    public static ManuelDto getManuelDto(String describe, String manuelImage) {
        return ManuelDto.builder()
                .describe(describe)
                .manuelImage(manuelImage)
                .build();
    }
}
