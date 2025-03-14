package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                int id = rs.getInt(1);
                return new Message(id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), 
                                                rs.getInt("posted_by"), 
                                                rs.getString("message_text"), 
                                                rs.getLong("time_posted_epoch"));
                messages.add(message);
            }

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    public Message getMessageById(int messageId) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, messageId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
                                            rs.getInt("posted_by"),
                                            rs.getString("message_text"),
                                            rs.getLong("time_posted_epoch"));
                return message;
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Message> getMessagesByAccountId(int accountId) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
    
            ps.setInt(1, accountId);
    
            ResultSet rs = ps.executeQuery();
    
            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    
        return messages;
    }

    public Message deleteMessageById(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
        Message deletedMessage = null;
    
        try {
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement selectPs = connection.prepareStatement(selectSql);
            selectPs.setInt(1, messageId);
            ResultSet rs = selectPs.executeQuery();
    
            if (rs.next()) {
                deletedMessage = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                String deleteSql = "DELETE FROM message WHERE message_id = ?";
                PreparedStatement deletePs = connection.prepareStatement(deleteSql);
                deletePs.setInt(1, messageId);
                deletePs.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    
        return deletedMessage;
    }

    public Message updateMessage(int messageId, String newText) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            if (newText == null || newText.isBlank() || newText.length() > 255) {
                return null;
            }

            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, newText);
            ps.setInt(2, messageId);

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                String selectSql = "SELECT * FROM message WHERE message_id = ?";
                PreparedStatement psSelect = connection.prepareStatement(selectSql);
                psSelect.setInt(1, messageId);

                ResultSet rs = psSelect.executeQuery();

                if (rs.next()) {
                    return new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
