package edu.pjatk.app.timestamp;

import edu.pjatk.app.socials.chat.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public class TimestampRepository {
    private final EntityManager entityManager;

    @Autowired
    public TimestampRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public Optional<Timestamp> getTimestampById(Long id) {
        Optional<Timestamp> timestamp;
        try {
            timestamp = Optional.of(
                    entityManager.createQuery(
                            "SELECT timestamp from Timestamp timestamp where timestamp.id=:timestampId",
                            Timestamp.class).setParameter("timestampId", id).getSingleResult()
            );
        } catch (NoResultException noResultException) {
            timestamp = Optional.empty();
        }
        return timestamp;
    }

    public Optional<List<Timestamp>> getUserTimestampsForProject(Long userId, Long projectId) {
        Optional<List<Timestamp>> allTimestamps;
        try {
            allTimestamps = Optional.of(
                    entityManager.createQuery(
                            "SELECT timestamp FROM Timestamp timestamp WHERE timestamp.participant.user.id=:userId AND " +
                                    "timestamp.participant.project.id=:projectId",
                            Timestamp.class).setParameter("userId", userId).setParameter("projectId", projectId)
                            .getResultList()
            );
        } catch (NoResultException noResultException) {
            allTimestamps = Optional.empty();
        }
        return allTimestamps;
    }

    @Transactional
    public void addTimestamp(Timestamp timestamp){
        entityManager.persist(timestamp);
    }

    @Transactional
    public void deleteTimestamp(Timestamp timestamp){
        entityManager.remove(timestamp);
    }


}
