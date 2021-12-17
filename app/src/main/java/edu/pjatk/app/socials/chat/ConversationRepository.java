package edu.pjatk.app.socials.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public class ConversationRepository {
    private final EntityManager entityManager;

    @Autowired
    public ConversationRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public Optional<Conversation> getConversationById(Long id) {
        Optional<Conversation> conversation;
        try {
            conversation = Optional.of(
                    entityManager.createQuery(
                            "SELECT conversation from Conversation conversation where conversation.id=:conversationId",
                            Conversation.class).setParameter("conversationId", id).getSingleResult()
            );
        } catch (NoResultException noResultException) {
            conversation = Optional.empty();
        }
        return conversation;
    }

    public Optional<Conversation> getConversationByUserId(Long first_user_id, Long second_user_id) {
        Optional<Conversation> conversation;
        try {
            conversation = Optional.of(
                    entityManager.createQuery(
                            "SELECT conversation from Conversation conversation where " +
                                    "conversation.first_user.id=:first_user_id and conversation.second_user.id=:second_user_id",
                            Conversation.class).setParameter("first_user_id", first_user_id).
                            setParameter("second_user_id", second_user_id).getSingleResult()
            );
        } catch (NoResultException noResultException) {
            conversation = Optional.empty();
        }
        return conversation;
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

    public Optional<List<Message>> getAllMessages(Long id) {
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

    public Optional<Message> getRecentMessage(Long id) {
        Optional<Message> recentMessage;
        try {
            recentMessage = Optional.of(
                    entityManager.createQuery(
                            "SELECT message from Message message where message.conversation.id=:conversationId " +
                                    "order by message.message_date DESC",
                            Message.class).setParameter("conversationId", id).setMaxResults(1).getSingleResult()
            );
        } catch (NoResultException noResultException) {
            recentMessage = Optional.empty();
        }
        return recentMessage;
    }

    public Optional<List<Conversation>> getAllUserConversations(Long userId) {
        Optional<List<Conversation>> allConversations;
        try {
            allConversations = Optional.of(
                    entityManager.createQuery(
                            "SELECT conversation from Conversation conversation where " +
                                    "conversation.first_user.id=:userId or conversation.second_user.id=:userId",
                            Conversation.class).setParameter("userId", userId).getResultList()
            );
        } catch (NoResultException noResultException) {
            allConversations = Optional.empty();
        }
        return allConversations;
    }

    @Transactional
    public void createConversation(Conversation conversation){
        entityManager.persist(conversation);
    }

}
