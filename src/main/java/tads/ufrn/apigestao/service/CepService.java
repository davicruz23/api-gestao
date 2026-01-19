package tads.ufrn.apigestao.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tads.ufrn.apigestao.domain.dto.cep.CepResponse;

@Service
public class CepService {

    private final RestClient restClient = RestClient.create();

    public CepResponse buscarPorCep(String cep) {
        return restClient.get()
                .uri("https://viacep.com.br/ws/{cep}/json/", cep)
                .retrieve()
                .body(CepResponse.class);
    }
}
