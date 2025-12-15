package DAO;

import model.TodoList;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TodoListDAO {

    public static List<TodoList> getByUserId(int userId) throws SQLException {
        String sql = """
            SELECT id, name, user_id
            FROM todo_list
            WHERE user_id = ?
            ORDER BY id DESC
        """;

        List<TodoList> lists = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lists.add(new TodoList(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("user_id")
                    ));
                }
            }
        }
        return lists;
    }
    //check list
    public static TodoList getById(int listId) throws SQLException {
        String sql = """
            SELECT id, name, user_id
            FROM todo_list
            WHERE id = ?
            LIMIT 1
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, listId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new TodoList(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("user_id")
                );
            }
        }
    }
    // tạo list 
    public static int create(String name, int userId) throws SQLException {
        String sql = "INSERT INTO todo_list (name, user_id) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setInt(2, userId);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) throw new SQLException("Không lấy được ID list mới.");
                return keys.getInt(1);
            }
        }
    }

    public static boolean rename(int listId, String newName) throws SQLException {
        String sql = "UPDATE todo_list SET name = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newName);
            ps.setInt(2, listId);
            return ps.executeUpdate() == 1;
        }
    }

    public static boolean delete(int listId) throws SQLException {
        String sql = "DELETE FROM todo_list WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, listId);
            return ps.executeUpdate() == 1;
        }
    }
}
