package npc.bikathi.multilogin.config.util;

import npc.bikathi.multilogin.entity.ExternalEntity;
import npc.bikathi.multilogin.repository.ExternalEntityRepository;
import npc.bikathi.multilogin.type.ExternalEntityUserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExternalEntityUserDetailsService implements UserDetailsService {
    private final ExternalEntityRepository externalEntityRepository;

    public ExternalEntityUserDetailsService(ExternalEntityRepository externalEntityRepository) {
        this.externalEntityRepository = externalEntityRepository;
    }

    @Override
    public ExternalEntityUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ExternalEntity externalEntity = externalEntityRepository.findByUsername(username);

        if(externalEntity != null) {
            return new ExternalEntityUserDetails(
                externalEntity.getUsername(),
                externalEntity.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
        }

        throw new UsernameNotFoundException("Username not found");
    }
}
