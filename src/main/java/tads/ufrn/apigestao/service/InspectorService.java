package tads.ufrn.apigestao.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import tads.ufrn.apigestao.controller.mapper.InspectorMapper;
import tads.ufrn.apigestao.domain.*;
import tads.ufrn.apigestao.domain.dto.client.UpsertClientDTO;
import tads.ufrn.apigestao.domain.dto.inspector.InspectorDTO;
import tads.ufrn.apigestao.domain.dto.inspector.InspectorHistoryPreSaleDTO;
import tads.ufrn.apigestao.domain.dto.inspector.InspectorIdUserDTO;
import tads.ufrn.apigestao.domain.dto.inspector.UpsertInspectorDTO;
import tads.ufrn.apigestao.domain.dto.seller.SellerIdUserDTO;
import tads.ufrn.apigestao.domain.dto.user.UpsertUserDTO;
import tads.ufrn.apigestao.enums.PreSaleStatus;
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

    /* =========================
       MÉTODOS PARA CONTROLLER
       ========================= */

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

    /* =========================
       MÉTODOS INTERNOS (ENTITY)
       ========================= */

    @Transactional(readOnly = true)
    public Inspector findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Inspector não encontrado"));
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
