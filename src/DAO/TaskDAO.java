package DAO;

import model.Task;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    public static List<Task> getByListId(int listId) throws SQLException {
        String sql = """
            SELECT id, title, note, completed, due_date, list_id
            FROM tasks
            WHERE list_id = ?
            ORDER BY id DESC
        """;

        List<Task> tasks = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, listId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date due = rs.getDate("due_date"); // có thể null
                    tasks.add(new Task(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("note"),
                            rs.getBoolean("completed"),
                            due,
                            rs.getInt("list_id")
                    ));
                }
            }
        }
        return tasks;
    }
    // task mới
    public static int create(String title, String note, boolean completed, Date dueDate, int listId) throws SQLException {
        String sql = "INSERT INTO tasks (title, note, completed, due_date, list_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, title);
            ps.setString(2, note);
            ps.setBoolean(3, completed);

            if (dueDate == null) ps.setNull(4, Types.DATE);
            else ps.setDate(4, dueDate);

            ps.setInt(5, listId);

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) throw new SQLException("Không lấy được ID task mới.");
                return keys.getInt(1); //trả id task mới
            }
        }
    }
    //sửa 
    public static boolean update(int taskId, String title, String note, boolean completed, Date dueDate) throws SQLException {
        String sql = """
            UPDATE tasks
            SET title = ?, note = ?, completed = ?, due_date = ?
            WHERE id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, note);
            ps.setBoolean(3, completed);

            if (dueDate == null) ps.setNull(4, Types.DATE);
            else ps.setDate(4, dueDate);

            ps.setInt(5, taskId);

            return ps.executeUpdate() == 1;
        }
    }
    //check comp
    public static boolean setCompleted(int taskId, boolean completed) throws SQLException {
        String sql = "UPDATE tasks SET completed = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, completed);
            ps.setInt(2, taskId);
            return ps.executeUpdate() == 1;
        }
    }
    //xóa task 
    public static boolean delete(int taskId) throws SQLException {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, taskId);
            return ps.executeUpdate() == 1;
        }
    }
    //đếm task trog list
    public static int countByListId(int listId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM tasks WHERE list_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, listId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
    //xóa list-> xóa task 
    public static int deleteByListId(int listId) throws SQLException {
        String sql = "DELETE FROM tasks WHERE list_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, listId);
            return ps.executeUpdate(); 
        }
    }
}
