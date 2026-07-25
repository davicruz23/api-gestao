package tads.ufrn.apigestao.domain.dto.address;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsertAddressDTO {

    private Long id;

    @NotBlank(message = "O estado é obrigatório")
    private String state;

    @NotBlank(message = "A cidade é obrigatória")
    private String city;

    @NotBlank(message = "A rua é obrigatória")
    private String street;

    @NotBlank(message = "O número é obrigatório")
    private String number;

    @NotBlank(message = "O CEP é obrigatório")
    private String zipCode;
    private String complement;
}
