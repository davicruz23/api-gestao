package tads.ufrn.apigestao.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import tads.ufrn.apigestao.domain.User;
import tads.ufrn.apigestao.domain.dto.user.UpsertUserDTO;
import tads.ufrn.apigestao.domain.dto.user.UserDTO;
import tads.ufrn.apigestao.enums.UserType;
import tads.ufrn.apigestao.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final ModelMapper mapper;
    public final PasswordEncoder passwordEncoder;

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

        return repository.save(user);
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
