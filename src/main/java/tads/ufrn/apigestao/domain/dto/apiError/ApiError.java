package tads.ufrn.apigestao.domain.dto.apiError;
import lombok.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    private Integer status;
    private String error;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private List<FieldError> fieldErrors = new ArrayList<>();;

    public ApiError(
            Integer status,
            String error,
            String message,
            String path
    ) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}
