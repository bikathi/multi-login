package npc.bikathi.multilogin.service;

import npc.bikathi.multilogin.entity.ExternalEntity;
import npc.bikathi.multilogin.repository.ExternalEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExternalEntityService {
    private final ExternalEntityRepository externalEntityRepository;

    public ExternalEntityService(ExternalEntityRepository externalEntityRepository) {
        this.externalEntityRepository = externalEntityRepository;
    }

    public void saveExternalEntity(ExternalEntity externalEntity) {
        externalEntityRepository.save(externalEntity);
    }

    public List<ExternalEntity> getExternalEntities() {
        return externalEntityRepository.findAll();
    }
}
