package tads.ufrn.apigestao.domain.dto.cep;

import lombok.Getter;

public record CepResponse(
        String cep,
        String logradouro,
        String bairro,
        String localidade
) {}

