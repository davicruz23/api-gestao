package tads.ufrn.apigestao.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tads.ufrn.apigestao.domain.User;
import tads.ufrn.apigestao.domain.dto.loginDTO.LoginRequestDTO;
import tads.ufrn.apigestao.domain.dto.loginDTO.LoginResponseDTO;
import tads.ufrn.apigestao.repository.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User login(LoginRequestDTO loginDTO) {
        Optional<User> userOptional = userRepository.findByCpf(loginDTO.getCpf());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                return user;
            } else {
                throw new RuntimeException("Invalid credentials");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }


//    public User register(UserRequestDTO userRequestDTO) {
//        if (userRepository.findByCpf(userRequestDTO.getCpf()).isPresent()) {
//            throw new RuntimeException("CPF already registered");
//        }
//
//        // Cria um novo usuário a partir do DTO
//        User user = new User();
//        user.setName(userRequestDTO.getName());
//        user.setRegistration(userRequestDTO.getRegistration());
//        user.setCpf(userRequestDTO.getCpf());
//        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
//        user.setRoleId(userRequestDTO.getRoleId());
//
//        // Busca as entrevistas pelos IDs fornecidos e associa ao usuário
//        if (userRequestDTO.getInterviewIds() != null && !userRequestDTO.getInterviewIds().isEmpty()) {
//            List<Interview> interviews = interviewService.findByIds(userRequestDTO.getInterviewIds());
//            user.setInterviews(new HashSet<>(interviews));
//        }
//
//        // Salva o usuário
//        return userRepository.save(user);
//    }

}

