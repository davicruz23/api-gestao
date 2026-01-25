package tads.ufrn.apigestao.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tads.ufrn.apigestao.domain.dto.cep.CepResponse;
import tads.ufrn.apigestao.domain.dto.cep.ViaCepResponse;

@Service
public class CepService {

    private final RestClient restClient = RestClient.create();

    public CepResponse buscarPorCep(String cep) {

        ViaCepResponse viaCep = restClient.get()
                .uri("https://viacep.com.br/ws/{cep}/json/", cep)
                .retrieve()
                .body(ViaCepResponse.class);

        if (viaCep == null || viaCep.getLogradouro() == null) {
            throw new RuntimeException("CEP não encontrado");
        }

        String streetComBairro = viaCep.getLogradouro();

        if (viaCep.getBairro() != null && !viaCep.getBairro().isBlank()) {
            streetComBairro += " - " + viaCep.getBairro();
        }

        return new CepResponse(
                viaCep.getCep(),
                streetComBairro,
                viaCep.getLocalidade()
        );
    }
}
