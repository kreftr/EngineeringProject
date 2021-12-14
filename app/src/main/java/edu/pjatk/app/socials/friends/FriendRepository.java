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

    // TODO przywroc
    public Optional<Friend> findById(Long id) {
//        Optional<Friend> friend;
//        try {
//            friend = Optional.of(
//                    entityManager.createQuery(
//                            "SELECT friend from Friend friend where friend.id=:friendId",
//                            Friend.class).setParameter("friendId", id).getSingleResult()
//            );
//        } catch (NoResultException noResultException) {
//            friend = Optional.empty();
//        }
//        return friend;

        return Optional.of(entityManager.find(Friend.class, id));
    }

    @Transactional
    public void addFriend(Friend friend) { entityManager.persist(friend); }

    @Transactional
    public void deleteFriendById(Long id) {
        Friend friend = entityManager.find(Friend.class, id);
        entityManager.remove(friend);
    }

    @Transactional
    public void acceptFriend(Friend friend) {
        friend.setPending(false);
        entityManager.persist(friend);
    }

    Optional<List<Friend>> getAllFriends(Long id) {
        Optional<List<Friend>> allFriends;
        try {
            allFriends = Optional.of(
                    entityManager.createQuery(
                            "SELECT friend from Friend friend where " +
                                    "friend.firstUser.id=:friendId or friend.secondUser.id=:friendId and friend.pending=false",
                            Friend.class).setParameter("friendId", id).getResultList()
            );
        } catch (NoResultException noResultException) {
            allFriends = Optional.empty();
        }
        return allFriends;
    }

    Optional<List<Friend>> getAllPending(Long id) {
        Optional<List<Friend>> allPending;
        try {
            allPending = Optional.of(
                    entityManager.createQuery(
                            "SELECT friend from Friend friend where " +
                                    "friend.firstUser.id=:friendId or friend.secondUser.id=:friendId and friend.pending=true",
                            Friend.class).setParameter("friendId", id).getResultList()
            );
        } catch (NoResultException noResultException) {
            allPending = Optional.empty();
        }
        return allPending;
    }
}
