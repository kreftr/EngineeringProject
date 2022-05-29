package edu.pjatk.app.report.blockade;

import edu.pjatk.app.report.EntityTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
public class BlockadeRepository {

    private final EntityManager entityManager;

    @Autowired
    public BlockadeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void add(Blockade blockade) {
        entityManager.persist(blockade);
    }

    public void remove(Blockade blockade) {
        entityManager.remove(blockade);
    }

    public Optional<Blockade> findByEntityTypeAndEntityId(EntityTypeEnum entityType, Long entityId) {
        Optional<Blockade> blockade;
        try {
            blockade = Optional.of(
                    entityManager.createQuery("SELECT blockade FROM Blockade blockade WHERE " +
                            "blockade.entityType=:entityType AND blockade.entityId=:entityId", Blockade.class).
                            setParameter("entityType", entityType).setParameter("entityId", entityId).
                            getSingleResult()
            );
        } catch (NoResultException e)  {
            blockade = Optional.empty();
        }
        return blockade;
    }

    public Optional<List<Blockade>> findAllUsers() {
        Optional<List<Blockade>> blockades;
        try {
            blockades = Optional.of(
                    entityManager.createQuery("SELECT blockade FROM Blockade blockade WHERE blockade.entityType='USER'",
                            Blockade.class).getResultList()
            );
        } catch (NoResultException e) {
            blockades = Optional.empty();
        }
        return blockades;
    }
}
