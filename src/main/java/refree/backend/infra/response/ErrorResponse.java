package refree.backend.infra.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse extends BasicResponse {

    private int code;
    private String message;

    public ErrorResponse(int errorCode, String message) {
        this.code = errorCode;
        this.message = message;
    }
}