package tads.ufrn.apigestao.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import tads.ufrn.apigestao.controller.mapper.InspectorMapper;
import tads.ufrn.apigestao.domain.*;
import tads.ufrn.apigestao.domain.dto.inspector.InspectorDTO;
import tads.ufrn.apigestao.domain.dto.inspector.InspectorIdUserDTO;
import tads.ufrn.apigestao.domain.dto.inspector.UpsertInspectorDTO;
import tads.ufrn.apigestao.exception.ResourceNotFoundException;
import tads.ufrn.apigestao.repository.InspectorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InspectorService {

    private final InspectorRepository repository;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public List<InspectorDTO> findAllDTO() {
        return repository.findAll()
                .stream()
                .map(InspectorMapper::mapper)
                .toList();
    }

    @Transactional(readOnly = true)
    public InspectorDTO findByIdDTO(Long id) {
        Inspector inspector = findEntityById(id);
        return InspectorMapper.mapper(inspector);
    }

    @Transactional(readOnly = true)
    public InspectorIdUserDTO getInspectorByUserId(Long userId) {
        Inspector inspector = repository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Inspector não encontrado para o usuário: " + userId));

        return new InspectorIdUserDTO(inspector.getId());
    }

    @Transactional(readOnly = true)
    public Inspector findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fiscal não encontrado!"));
    }

    @Transactional
    public Inspector store(UpsertInspectorDTO inspector) {
        return repository.save(mapper.map(inspector, Inspector.class));
    }

    @Transactional
    public void deleteById(Long id){
        Inspector inspector = findEntityById(id);
        inspector.delete();
        repository.save(inspector);
    }

    public void createFromUser(User user) {
        Inspector inspector = new Inspector();
        inspector.setUser(user);
        repository.save(inspector);
    }
}
