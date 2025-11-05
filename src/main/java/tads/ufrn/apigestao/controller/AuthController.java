package tads.ufrn.apigestao.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tads.ufrn.apigestao.domain.User;
import tads.ufrn.apigestao.domain.dto.loginDTO.LoginRequestDTO;
import tads.ufrn.apigestao.domain.dto.loginDTO.LoginResponseDTO;
import tads.ufrn.apigestao.security.TokenService;
import tads.ufrn.apigestao.service.AuthService;

import java.util.HashMap;
import java.util.Map;


@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
//@CrossOrigin("http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        System.out.println("Usuário que tentou logar: " + loginRequest.getCpf());

        try {
            User user = authService.login(loginRequest);
            String token = tokenService.generateToken(user);

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (RuntimeException e) {
            // Retorna JSON no corpo com a mensagem
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }


    /*@PostMapping("/register")
    //@PreAuthorize("hasRole('authRegister')")
    public ResponseEntity<UserListingtDTO> register(@RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(UserMapper.mapper(authService.register(userRequestDTO)));
    }*/

}
