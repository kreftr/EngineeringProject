package edu.pjatk.app.project.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamRepository {

    private final EntityManager entityManager;

    @Autowired
    public TeamRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }


    public void add(Team team){
        entityManager.persist(team);
    }

    public void update(Team team){
        entityManager.merge(team);
    }

    public void remove(Team team){
        entityManager.remove(team);
    }

    public Optional<List<Team>> getByProjectId(Long projectId){
        Optional teams;
        try{
            teams = Optional.of(
                    entityManager.createQuery("SELECT team FROM Team team WHERE team.project.id=:projectId", Team.class).
                            setParameter("projectId", projectId).getResultList()
            );
        } catch (NoResultException e){
            teams = Optional.empty();
        }
        return teams;
    }

    public Optional<Team> getById(Long id){
        Optional team;
        try {
            team = Optional.of(
                    entityManager.createQuery("SELECT team FROM Team team WHERE team.id=:id", Team.class).
                            setParameter("id", id).getSingleResult()
            );
        } catch (NoResultException e){
            team = Optional.empty();
        }
        return team;
    }

}
