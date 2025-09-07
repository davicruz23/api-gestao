package tads.ufrn.apigestao.security;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tads.ufrn.apigestao.repository.UserRepository;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByCpf(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with cpf: " + username)); // Aqui, `User` deve implementar `UserDetails`
    }
}

