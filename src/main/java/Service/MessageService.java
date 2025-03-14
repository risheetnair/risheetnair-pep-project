package Service;

import DAO.MessageDAO;

import java.util.List;

import DAO.AccountDAO;
import Model.Message;
import Model.Account;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    public Message createMessage(Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255) {
            return null;
        }

        Account account = this.accountDAO.getAccountById(message.getPosted_by());
        if (account == null) {
            return null;
        }

        return messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }
    

    public List<Message> getMessagesByAccountId(int accountId) {
        return messageDAO.getMessagesByAccountId(accountId);
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessageById(int messageId) {
        Message message = messageDAO.getMessageById(messageId);
   
        if (message != null) {
            messageDAO.deleteMessageById(messageId);
            return message;
        }
   
        return null;
    }

    public Message updateMessage(int messageId, String newText) {
        if (newText == null || newText.isEmpty() || newText.length() > 255) {
            return null;
        }
    
        Message existingMessage = messageDAO.getMessageById(messageId);
        
        if (existingMessage != null) {
            return messageDAO.updateMessage(messageId, newText);
        }
    
        return null;
    }

    
}
