package refree.backend.module.recipe.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ManualDto {

    private String describe;
    private String manualImage;

    public static ManualDto getManualDto(String describe, String manualImage) {
        return ManualDto.builder()
                .describe(describe)
                .manualImage(manualImage)
                .build();
    }
}
