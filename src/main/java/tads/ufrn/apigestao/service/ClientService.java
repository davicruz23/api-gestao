package tads.ufrn.apigestao.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tads.ufrn.apigestao.domain.Address;
import tads.ufrn.apigestao.domain.Client;
import tads.ufrn.apigestao.domain.dto.client.ClientRecentDTO;
import tads.ufrn.apigestao.domain.dto.client.UpsertClientDTO;
import tads.ufrn.apigestao.exception.ResourceNotFoundException;
import tads.ufrn.apigestao.repository.ClientRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;
    private final ModelMapper mapper;

    public List<Client> findAll(){
        return repository.findAll();
    }

    public Client findById(Long id) {
        Optional<Client> client = repository.findById(id);
        return client.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado!"));
    }

    @Transactional
    public Client store(UpsertClientDTO dto) {
        Address address = new Address(
                null,
                dto.getAddress().getState(),
                dto.getAddress().getCity(),
                dto.getAddress().getStreet(),
                dto.getAddress().getNumber(),
                dto.getAddress().getZipCode(),
                dto.getAddress().getComplement()
        );

        Client client = new Client(
                null,
                dto.getName(),
                dto.getCpf(),
                dto.getPhone(),
                address
        );

        return repository.save(client);
    }

    public void deleteById(Long id){
        Client client = repository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Usuário não encontrado!"));
        repository.save(client);

    }

    public List<ClientRecentDTO> findLastClients() {
        return repository.findRecentClients(PageRequest.of(0, 6));
    }

}
