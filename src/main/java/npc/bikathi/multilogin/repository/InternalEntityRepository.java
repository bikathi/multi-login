package npc.bikathi.multilogin.repository;

import npc.bikathi.multilogin.entity.InternalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InternalEntityRepository extends JpaRepository<InternalEntity, Long> {
    InternalEntity findByUsername(String username);
}
