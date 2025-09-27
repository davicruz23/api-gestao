package tads.ufrn.apigestao.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import tads.ufrn.apigestao.domain.Collector;
import tads.ufrn.apigestao.domain.Inspector;
import tads.ufrn.apigestao.domain.Seller;
import tads.ufrn.apigestao.domain.User;
import tads.ufrn.apigestao.domain.dto.user.UpsertUserDTO;
import tads.ufrn.apigestao.domain.dto.user.UserDTO;
import tads.ufrn.apigestao.enums.UserType;
import tads.ufrn.apigestao.repository.CollectorRepository;
import tads.ufrn.apigestao.repository.InspectorRepository;
import tads.ufrn.apigestao.repository.SellerRepository;
import tads.ufrn.apigestao.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final ModelMapper mapper;
    public final PasswordEncoder passwordEncoder;
    private final InspectorRepository inspectorRepository;
    private final SellerRepository sellerRepository;
    private final CollectorRepository collectorRepository;

    public List<User> allUsers() {
        return repository.findAll();
    }

    public User findUserById(Long id) {
        Optional<User> user = repository.findById(id);
        return user.orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User store(UpsertUserDTO userDTO) {
        User user = mapper.map(userDTO, User.class);

        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        if (userDTO.getPosition() != null) {
            user.setPosition(UserType.fromValue(userDTO.getPosition()));
        }

        User savedUser = repository.save(user);


        switch (savedUser.getPosition()){
            case FISCAL:
                Inspector inspector = new Inspector();
                inspector.setUser(savedUser);
                inspectorRepository.save(inspector);
                break;
            case VENDEDOR:
                Seller seller = new Seller();
                seller.setUser(savedUser);
                sellerRepository.save(seller);
                break;
            case COBRADOR:
                Collector collector = new Collector();
                collector.setUser(savedUser);
                collectorRepository.save(collector);
                break;
            default:
                break;
        }

        return savedUser;
    }

    public User update(User user) {
        User searchUser = repository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        searchUser.setName(user.getPassword());

        return repository.save(searchUser);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
