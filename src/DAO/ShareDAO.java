package DAO;

import util.DatabaseConnection;
import java.security.SecureRandom;
import java.sql.*;
import java.time.LocalDateTime;

public class ShareDAO {

    private static final String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom rand = new SecureRandom();

    private static String randCode(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return sb.toString();
    }
    // gen share code 
    public static String genShareCode(int listId, int ownerId, int daysToLive) throws SQLException {
        String sql = "INSERT INTO shares (share_code, list_id, owner_id, expires_at) VALUES (?, ?, ?, ?)";

        LocalDateTime expires = LocalDateTime.now().plusDays(daysToLive);

        for (int attempt = 0; attempt < 10; attempt++) {
            String code = randCode(12);

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, code);
                ps.setInt(2, listId);
                ps.setInt(3, ownerId);
                ps.setTimestamp(4, Timestamp.valueOf(expires));
                ps.executeUpdate();
                return code;
            } catch (SQLIntegrityConstraintViolationException dup) {
                // trùng unique -> gen lại
            }
        }
        throw new SQLException("Không tạo được share code.");
    }

    public static int acceptShareAndCopy(String code, int receiverId) throws SQLException {
        String getShareSql = """
            SELECT s.list_id, l.name AS list_name
            FROM shares s
            JOIN todo_list l ON l.id = s.list_id
            WHERE s.share_code = ? AND s.expires_at > NOW()
            LIMIT 1
        """;

        String insertListSql = "INSERT INTO todo_list (name, user_id) VALUES (?, ?)";
        String selectTasksSql = "SELECT title, note, completed, due_date FROM tasks WHERE list_id = ?";
        String insertTaskSql = "INSERT INTO tasks (title, note, completed, due_date, list_id) VALUES (?, ?, ?, ?, ?)";

        String deleteCode = "DELETE FROM shares WHERE share_code = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try {
                int srcListId;
                String srcListName;
                //check code + in4 list src 
                try (PreparedStatement ps = conn.prepareStatement(getShareSql)) {
                    ps.setString(1, code);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new SQLException("Code sai hoặc đã hết hạn.");
                        srcListId = rs.getInt("list_id");
                        srcListName = rs.getString("list_name");
                    }
                }
               //list mới
                int newListId;
                try (PreparedStatement ps = conn.prepareStatement(insertListSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, srcListName + " (Shared)");
                    ps.setInt(2, receiverId);
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) throw new SQLException("Không lấy được ID list mới.");
                        newListId = keys.getInt(1);
                    }
                }

                //copy tasks
                try (PreparedStatement psSelect = conn.prepareStatement(selectTasksSql)) {
                    psSelect.setInt(1, srcListId);

                    try (ResultSet rs = psSelect.executeQuery();
                         PreparedStatement psInsert = conn.prepareStatement(insertTaskSql)) {

                        while (rs.next()) {
                            psInsert.setString(1, rs.getString("title"));
                            psInsert.setString(2, rs.getString("note"));
                            psInsert.setBoolean(3, rs.getBoolean("completed"));

                            Date due = rs.getDate("due_date");
                            if (due == null) psInsert.setNull(4, Types.DATE);
                            else psInsert.setDate(4, due);

                            psInsert.setInt(5, newListId);
                            psInsert.addBatch();
                        }
                        psInsert.executeBatch();
                    }
                }
                //xoá code
                try (PreparedStatement ps = conn.prepareStatement(deleteCode)) {
                    ps.setString(1, code);
                    ps.executeUpdate();
                }
                conn.commit();
                return newListId;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
