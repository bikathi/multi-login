package npc.bikathi.multilogin.config.util;

import npc.bikathi.multilogin.entity.InternalEntity;
import npc.bikathi.multilogin.repository.InternalEntityRepository;
import npc.bikathi.multilogin.type.InternalEntityUserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InternalEntityUserDetailsService implements UserDetailsService {
    private final InternalEntityRepository internalEntityRepository;

    public InternalEntityUserDetailsService(InternalEntityRepository internalEntityRepository) {
        this.internalEntityRepository = internalEntityRepository;
    }

    @Override
    public InternalEntityUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        InternalEntity internalEntity = internalEntityRepository.findByUsername(username);
        if (internalEntity != null) {
            return new InternalEntityUserDetails(
                internalEntity.getUsername(),
                internalEntity.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
        }

        throw new UsernameNotFoundException("Username not found");
    }
}
