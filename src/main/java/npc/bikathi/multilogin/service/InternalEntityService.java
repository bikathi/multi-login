package npc.bikathi.multilogin.service;

import npc.bikathi.multilogin.entity.InternalEntity;
import npc.bikathi.multilogin.repository.InternalEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternalEntityService {
    private final InternalEntityRepository internalEntityRepository;

    public InternalEntityService(InternalEntityRepository internalEntityRepository) {
        this.internalEntityRepository = internalEntityRepository;
    }

    public void saveInternalEntity(InternalEntity internalEntity) {
        internalEntityRepository.save(internalEntity);
    }

    public List<InternalEntity> getInternalEntities() {
        return internalEntityRepository.findAll();
    }
}
