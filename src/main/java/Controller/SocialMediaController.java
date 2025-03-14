package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private MessageService messageService;
    private AccountService accountService;

    public SocialMediaController() {
        this.messageService = new MessageService();
        this.accountService = new AccountService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::createAccount);
        app.post("/login", this::loginAccount);

        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::updateMessageById);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountId);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */

    private void createAccount(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        Account createdAccount = accountService.createAccount(account);
        
        if (createdAccount != null) {
            ctx.json(mapper.writeValueAsString(createdAccount));
        } else {
            ctx.status(400);
        }
    }

    private void loginAccount(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account loginAccount = mapper.readValue(ctx.body(), Account.class);
        
        Account existingAccount = accountService.handleLogin(loginAccount.getUsername(), loginAccount.getPassword());
    
        if (existingAccount != null) {
            ctx.json(mapper.writeValueAsString(existingAccount));
        } else {
            ctx.status(401);
        }
    }

    private void createMessage(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);

        Message createdMessage = messageService.createMessage(message);

        if (createdMessage != null) {
            ctx.json(mapper.writeValueAsString(createdMessage));
        } else {
            ctx.status(400);
        }

    }

    private void getAllMessages(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void getMessageById(Context ctx) throws JsonProcessingException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));

        Message message = messageService.getMessageById(messageId);

        if (message != null) {
            ctx.json(message);
        } else {
            ctx.status(200);
        }
    }

    private void deleteMessageById(Context ctx) throws JsonProcessingException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));

        Message deletedMessage = messageService.deleteMessageById(messageId);

        if (deletedMessage != null) {
            ctx.json(deletedMessage);
        } else {
            ctx.status(200);
        }
    }

    private void updateMessageById(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String newText = mapper.readTree(ctx.body()).get("message_text").asText();

        Message updatedMessage = messageService.updateMessage(Integer.parseInt(ctx.pathParam("message_id")), newText);
        
        if (updatedMessage != null) {
            ctx.json(updatedMessage);
        } else {
            ctx.status(400);
        }
    }

    private void getMessagesByAccountId(Context ctx) throws JsonProcessingException {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
    
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        
        ctx.json(messages);
    }


}