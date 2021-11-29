package edu.pjatk.app.socials;

import edu.pjatk.app.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class ConversationRepository {
    private final EntityManager entityManager;

    @Autowired
    public ConversationRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Transactional
    public void addMessage(Message message){
        entityManager.persist(message);
    }

    @Transactional
    public void deleteById(Long id){
        Message message = entityManager.find(Message.class, id);
        entityManager.remove(message);
    }

    public Optional<Conversation> findById(Long id) {
        Optional<Conversation> conversation;
        try{
            conversation = Optional.of(
                    entityManager.createQuery(
                            "SELECT conversation from Conversation conversation",
                            Conversation.class).getSingleResult()
            );
        } catch (NoResultException noResultException) {
            conversation = Optional.empty();
        }
        return conversation;
    }

    public Optional<List<Message>> getAllMessages(Long id){
        Optional<List<Message>> allMessages;
        try {
            allMessages = Optional.of(
                    entityManager.createQuery(
                            "SELECT message from Message message where message.conversation.id=:conversationId",
                            Message.class).setParameter("conversationId", id).getResultList()
            );
        } catch (NoResultException noResultException) {
            allMessages = Optional.empty();
        }
        return allMessages;
    }

    public Optional<Message> getRecentMessage(){
        Optional<Message> recentMessage;
        try {
            recentMessage = Optional.of(
                    entityManager.createQuery(
                            "SELECT message.content from Message message order by message.message_date DESC",
                            Message.class).setMaxResults(1).getSingleResult()
            );
        } catch (NoResultException noResultException) {
            recentMessage = Optional.empty();
        }
        return recentMessage;
    }
}
