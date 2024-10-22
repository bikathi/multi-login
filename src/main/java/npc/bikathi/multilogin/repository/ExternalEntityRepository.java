package npc.bikathi.multilogin.repository;

import npc.bikathi.multilogin.entity.ExternalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalEntityRepository extends JpaRepository<ExternalEntity, Long> {
    ExternalEntity findByUsername(String username);
}
