package npc.bikathi.multilogin.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import npc.bikathi.multilogin.entity.ExternalEntity;
import npc.bikathi.multilogin.entity.InternalEntity;
import npc.bikathi.multilogin.payload.LoginDTO;
import npc.bikathi.multilogin.payload.SignupDTO;
import npc.bikathi.multilogin.service.ExternalEntityService;
import npc.bikathi.multilogin.type.ExternalEntityAuthenticationToken;
import npc.bikathi.multilogin.type.InternalEntityAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/external")
public class ExternalEntityAuth {
    @Autowired
    private ExternalEntityService externalEntityService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Logger logger = Logger.getLogger(ExternalEntityAuth.class.getName());

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        String jwt = "";
        ExternalEntityAuthenticationToken internalEntityAuthenticationToken = new ExternalEntityAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        ExternalEntityAuthenticationToken authenticationResponse =
                (ExternalEntityAuthenticationToken) authenticationManager.authenticate(internalEntityAuthenticationToken);

        if(authenticationResponse != null && authenticationResponse.isAuthenticated()) {
            String secret = "uIka39tkKkR9vSuWVKnQxnjkvZGlbGHmilYMki14X6ZC5nQaNsivVVXvGLfb";
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            jwt = Jwts.builder().issuer("MultiAuth").subject("JWT Token")
                .claim("username", authenticationResponse.getName())
                .claim("authorities", authenticationResponse.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .issuedAt(new java.util.Date())
                .expiration(new java.util.Date((new java.util.Date()).getTime() + 3000000))
                .signWith(secretKey)
            .compact();
        }

        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupDTO signupDTO) {
        final String hash = passwordEncoder.encode(signupDTO.getPassword());

        ExternalEntity internalEntity = new ExternalEntity(signupDTO.getUsername(), hash);
        externalEntityService.saveExternalEntity(internalEntity);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/get-entities")
    public ResponseEntity<List<ExternalEntity>> getEntities(Authentication authentication) {
        logger.info("Authenticated entity: " + authentication.getName());

        List<ExternalEntity> externalEntities = externalEntityService.getExternalEntities();
        return ResponseEntity.ok(externalEntities);
    }
}
