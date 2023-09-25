package refree.backend.module.recipe.Dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RecommendRequest {

    @NotNull(message = "소비기한 만료 여부를 선택해주세요")
    private Boolean isOutdatedOk;
}
