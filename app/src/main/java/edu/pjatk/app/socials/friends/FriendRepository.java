package edu.pjatk.app.socials.friends;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public class FriendRepository {

    private final EntityManager entityManager;

    @Autowired
    public FriendRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }


    @Transactional
    public void addFriend(Friend friend) { entityManager.persist(friend); }

    public void removeFriend(Friend friend){
        entityManager.remove(friend);
    }

    @Transactional
    public void acceptFriend(Friend friend) {
        entityManager.merge(friend);
    }

    public Optional<Friend> getFriendByUserId(Long first_user_id, Long second_user_id){
        Optional friend;
        try{
            friend = Optional.of(
                    entityManager.createQuery(
                            "SELECT friend from Friend friend where friend.firstUser.id=:f_id and friend.secondUser.id=:s_id or friend.firstUser.id=:s_id and friend.secondUser.id=:f_id",
                            Friend.class).setParameter("f_id", first_user_id).setParameter("s_id", second_user_id).getSingleResult()
            );
        } catch (NoResultException e){
            friend = Optional.empty();
        }
        return friend;
    }

    public Optional<List<Friend>> getAllFriendsByUserId(Long id){
        Optional friends;
        try {
            friends = Optional.of(
                    entityManager.createQuery(
                            "SELECT friend FROM Friend friend WHERE friend.pending = FALSE AND (friend.firstUser.id=:id OR friend.secondUser.id=:id)",
                            Friend.class).setParameter("id", id).getResultList()
            );
        } catch (NoResultException e){
            friends = Optional.empty();
        }

        return friends;
    }

    public Optional<List<Friend>> getAllPending(Long id){
        Optional pendingFriends;
        try {
            pendingFriends = Optional.of(
                    entityManager.createQuery(
                            "SELECT friend from Friend friend where friend.secondUser.id=:id and friend.pending=true",
                            Friend.class).setParameter("id", id).getResultList()
            );
        } catch (NoResultException noResultException) {
            pendingFriends = Optional.empty();
        }

        return pendingFriends;
    }

    @Transactional
    public void deleteFriend(Friend friend) {
        entityManager.remove(friend);
    }

    //Get all relations friends + pending requests
    public Optional<List<Friend>> getAll(Long id){
        Optional<List<Friend>> all;
        try {
            all = Optional.of(
                    entityManager.createQuery(
                            "SELECT friend from Friend friend where " +
                                    "friend.firstUser.id=:id or friend.secondUser.id=:id",
                            Friend.class).setParameter("id", id).getResultList()
            );
        } catch (NoResultException noResultException) {
            all = Optional.empty();
        }
        return all;
    }

}
