package tads.ufrn.apigestao.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import tads.ufrn.apigestao.domain.Client;
import tads.ufrn.apigestao.domain.Inspector;
import tads.ufrn.apigestao.domain.Product;
import tads.ufrn.apigestao.domain.User;
import tads.ufrn.apigestao.domain.dto.client.UpsertClientDTO;
import tads.ufrn.apigestao.domain.dto.inspector.UpsertInspectorDTO;
import tads.ufrn.apigestao.domain.dto.user.UpsertUserDTO;
import tads.ufrn.apigestao.enums.UserType;
import tads.ufrn.apigestao.repository.InspectorRepository;
import tads.ufrn.apigestao.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InspectorService {

    private final InspectorRepository repository;
    private final ModelMapper mapper;

    public List<Inspector> findAll() {
        return repository.findAll();
    }

    public Inspector findById(Long id) {
        Optional<Inspector> inspector = repository.findById(id);
        return inspector.orElseThrow(() -> new NotFoundException("User not found"));
    }

    public Inspector store(UpsertInspectorDTO inspector) {
        return repository.save(mapper.map(inspector, Inspector.class));
    }

    /*public User update(User user) {
        User searchUser = repository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        searchUser.setName(user.getPassword());

        return repository.save(searchUser);
    }*/

    public void deleteById(Long id){
        Inspector inspector = repository.findById(id)
                .orElseThrow(()-> new NotFoundException("Product not found"));
        inspector.delete();
        repository.save(inspector);

    }
}
