package tads.ufrn.apigestao.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tads.ufrn.apigestao.domain.User;
import tads.ufrn.apigestao.domain.dto.loginDTO.LoginRequestDTO;
import tads.ufrn.apigestao.domain.dto.loginDTO.LoginResponseDTO;
import tads.ufrn.apigestao.security.TokenService;
import tads.ufrn.apigestao.service.AuthService;


@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
//@CrossOrigin("http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        System.out.println("usuario que tentou logar: "+ loginRequest.getCpf() + "   " + loginRequest.getPassword());
        User user = authService.login(loginRequest);

        if (user != null) {
            String token = tokenService.generateToken(user);
            return ResponseEntity.ok(new LoginResponseDTO(token));
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    /*@PostMapping("/register")
    //@PreAuthorize("hasRole('authRegister')")
    public ResponseEntity<UserListingtDTO> register(@RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(UserMapper.mapper(authService.register(userRequestDTO)));
    }*/

}
