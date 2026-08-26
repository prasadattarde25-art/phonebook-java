package phonebook_backend.controller;

import phonebook_backend.service.JwtService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // --------------------------------------------------
    // POST /auth/login
    // --------------------------------------------------

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String username,
            @RequestParam String password) {

        try {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    password
                            )
                    );

            String token =
                    jwtService.generateToken(
                            authentication.getName()
                    );

            return ResponseEntity.ok(
                    new LoginResponse(
                            token,
                            "bearer"
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(401)
                    .body(
                            java.util.Map.of(
                                    "detail",
                                    "Incorrect username or password"
                            )
                    );
        }
    }

    // --------------------------------------------------
    // Response
    // --------------------------------------------------

    public record LoginResponse(
            String access_token,
            String token_type
    ) {
    }
}