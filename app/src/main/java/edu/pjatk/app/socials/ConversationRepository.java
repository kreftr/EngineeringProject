package edu.pjatk.app.socials;

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

    public void addMessage(String text){
        Date date = java.util.Calendar.getInstance().getTime();
        Message message = new Message(text, date);
        entityManager.persist(message);
    }

    public void deleteById(Long id){
        Conversation conversation = entityManager.find(Conversation.class, id);
        entityManager.remove(conversation);
    }

    public Optional<List<Message>> getAllMessages(){
        Optional<List<Message>> allMessages;
        try {
            allMessages = Optional.of(
                    entityManager.createQuery(
                                    "SELECT conversation.messages FROM Conversation conversation",
                            Message.class).getResultList()
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
                            "SELECT messages FROM Conversation.messages messages JOIN FETCH messages.date " +
                                    "ORDER BY messages.date DESC",
                            Message.class).setMaxResults(1).getSingleResult()
            );
        } catch (NoResultException noResultException) {
            recentMessage = Optional.empty();
        }
        return recentMessage;
    }
}
