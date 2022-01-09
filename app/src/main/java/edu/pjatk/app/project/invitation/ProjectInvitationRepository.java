package edu.pjatk.app.project.invitation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
public class ProjectInvitationRepository {

    private final EntityManager entityManager;

    @Autowired
    public ProjectInvitationRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }


    public void add(ProjectInvitation projectInvitation) {
        entityManager.persist(projectInvitation);
    }

    public void remove(ProjectInvitation projectInvitation){
        entityManager.remove(projectInvitation);
    }

    public Optional<ProjectInvitation> getById(Long id){
        Optional invitation;
        try {
            invitation = Optional.of(
                    entityManager.createQuery("SELECT invitation FROM ProjectInvitation invitation WHERE invitation.id = :id").
                            setParameter("id", id).getResultList()
            );
        }
        catch (NoResultException e){
            invitation = Optional.empty();
        }
        return invitation;
    }

    public Optional<List<ProjectInvitation>> getByUserId(Long id){
        Optional invitations;
        try {
            invitations = Optional.of(
                    entityManager.createQuery("SELECT invitation FROM ProjectInvitation invitation WHERE invitation.receiver.id = :id").
                            setParameter("id", id).getResultList()
            );
        }
        catch (NoResultException e){
            invitations = Optional.empty();
        }
        return invitations;
    }

}
