package edu.pjatk.app.project.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

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


}
