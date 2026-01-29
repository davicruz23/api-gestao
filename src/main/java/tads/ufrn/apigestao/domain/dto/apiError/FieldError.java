package tads.ufrn.apigestao.domain.dto.apiError;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldError {

    private String field;
    private String message;
}

