package npc.bikathi.multilogin.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import npc.bikathi.multilogin.entity.InternalEntity;
import npc.bikathi.multilogin.payload.LoginDTO;
import npc.bikathi.multilogin.payload.SignupDTO;
import npc.bikathi.multilogin.service.InternalEntityService;
import npc.bikathi.multilogin.type.InternalEntityAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/internal")
public class InternalEntityAuth {
    @Autowired
    private InternalEntityService internalEntityService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Logger logger = Logger.getLogger(InternalEntityAuth.class.getName());

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        String jwt = "";
        InternalEntityAuthenticationToken internalEntityAuthenticationToken = new InternalEntityAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        InternalEntityAuthenticationToken authenticationResponse =
            (InternalEntityAuthenticationToken) authenticationManager.authenticate(internalEntityAuthenticationToken);

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

        InternalEntity internalEntity = new InternalEntity(signupDTO.getUsername(), hash);
        internalEntityService.saveInternalEntity(internalEntity);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/get-entities")
//    @PreAuthorize("hasRole('VIEWER')")
    public ResponseEntity<List<InternalEntity>> getEntities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Authenticated entity: " + authentication.getName());

        List<InternalEntity> internalEntities = internalEntityService.getInternalEntities();
        return ResponseEntity.ok(internalEntities);
    }
}
