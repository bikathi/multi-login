package npc.bikathi.multilogin.config.provider;

import npc.bikathi.multilogin.config.util.InternalEntityUserDetailsService;
import npc.bikathi.multilogin.type.InternalEntityAuthenticationToken;
import npc.bikathi.multilogin.type.InternalEntityUserDetails;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class InternalEntityAuthenticationProvider implements AuthenticationProvider {
    private final InternalEntityUserDetailsService internalEntityUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public InternalEntityAuthenticationProvider(InternalEntityUserDetailsService internalEntityUserDetailsService, PasswordEncoder passwordEncoder) {
        this.internalEntityUserDetailsService = internalEntityUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        InternalEntityUserDetails internalEntityUserDetails = internalEntityUserDetailsService.loadUserByUsername(username);
        if (internalEntityUserDetails != null && passwordEncoder.matches(password, internalEntityUserDetails.getPassword())) {
            return new InternalEntityAuthenticationToken(username, password, List.copyOf(internalEntityUserDetails.getAuthorities()));
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (InternalEntityAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
