package npc.bikathi.multilogin.config.provider;

import npc.bikathi.multilogin.config.util.ExternalEntityUserDetailsService;
import npc.bikathi.multilogin.type.ExternalEntityAuthenticationToken;
import npc.bikathi.multilogin.type.ExternalEntityUserDetails;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class ExternalEntityAuthenticationProvider implements AuthenticationProvider {
    private final ExternalEntityUserDetailsService externalEntityUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public ExternalEntityAuthenticationProvider(ExternalEntityUserDetailsService externalEntityUserDetailsService, PasswordEncoder passwordEncoder) {
        this.externalEntityUserDetailsService = externalEntityUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        ExternalEntityUserDetails externalEntityUserDetails = externalEntityUserDetailsService.loadUserByUsername(username);
        if (externalEntityUserDetails != null && passwordEncoder.matches(password, externalEntityUserDetails.getPassword())) {
            return new ExternalEntityAuthenticationToken(username, password, List.copyOf(externalEntityUserDetails.getAuthorities()));
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (ExternalEntityAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
