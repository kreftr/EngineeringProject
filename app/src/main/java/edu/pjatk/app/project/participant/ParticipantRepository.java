package edu.pjatk.app.project.participant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
public class ParticipantRepository {

    private final EntityManager entityManager;

    @Autowired
    public ParticipantRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }


    public void update(Participant participant){
        entityManager.merge(participant);
    }

    public void remove(Participant participant){
        entityManager.remove(participant);
    }

    public Optional<Participant> getById(Long id){
        Optional participant;
        try {
            participant = Optional.of(
                    entityManager.createQuery("SELECT participant FROM Participant participant WHERE participant.id=:id").
                            setParameter("id", id).getSingleResult()
            );
        } catch (NoResultException e){
            participant = Optional.empty();
        }
        return participant;
    }

    public Optional<Participant> getByUserAndProjectIds(Long userId, Long projectId){
        Optional participant;
        try {
            participant = Optional.of(
                    entityManager.createQuery("SELECT participant FROM Participant participant WHERE " +
                                    "participant.user.id = :userId AND participant.project.id = :projectId", Participant.class).
                            setParameter("userId", userId).setParameter("projectId", projectId).getSingleResult()
            );
        } catch (NoResultException e){
            participant = Optional.empty();
        }
        return participant;
    }

    public Optional<List<Participant>> getAllWhereUserIsMemberByUserId(Long userId){
        Optional participants;
        try {
            participants = Optional.of(
                    entityManager.createQuery("SELECT participant FROM Participant participant WHERE" +
                            " participant.user.id=:id AND participant.participantRole<>'OWNER' AND participant.pending=FALSE", Participant.class).
                            setParameter("id", userId).getResultList()
            );
        } catch (NoResultException e){
            participants = Optional.empty();
        }
        return participants;
    }

    public Optional<List<Participant>> getAllPending(Long userId){
        Optional participants;
        try {
            participants = Optional.of(
                    entityManager.createQuery("SELECT participant FROM Participant participant WHERE" +
                                    " participant.project.creator.id=:id AND participant.pending=TRUE", Participant.class).
                            setParameter("id", userId).getResultList()
            );
        } catch (NoResultException e){
            participants = Optional.empty();
        }
        return participants;
    }

}
