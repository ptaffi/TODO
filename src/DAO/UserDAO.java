package DAO;

import model.User;
import util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;

public class UserDAO {

    public static User login(String identifier, String password) throws SQLException {
        String sql = """
            SELECT id, username, password, email
            FROM `user`
            WHERE (username = ? OR email = ?) AND password = ?
            LIMIT 1
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, identifier);
            ps.setString(2, identifier);
            ps.setString(3, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                );
            }
        }
    }

    public static boolean existsUsernameOrEmail(String username, String email) throws SQLException {
        String sql = """
            SELECT 1
            FROM `user`
            WHERE username = ? OR email = ?
            LIMIT 1
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static boolean register(String username, String password, String email) throws SQLException {
        String sql = "INSERT INTO `user` (username, password, email) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, email); // nhớ set email như m đã fix
            return ps.executeUpdate() == 1;
        }
    }

    public static User getById(int id) throws SQLException {
        String sql = """
            SELECT id, username, password, email
            FROM `user`
            WHERE id = ?
            LIMIT 1
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                );
            }
        }
    }

    public static User getByEmail(String email) throws SQLException {
        String sql = """
            SELECT id, username, password, email
            FROM `user`
            WHERE email = ?
            LIMIT 1
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                );
            }
        }
    }
    
    //  forgot pass

    public static String createResetCodeByEmail(String email, int minutesValid) throws SQLException {
        User u = getByEmail(email);
        if (u == null) return null;

        String otp = generateOtp6();
        String otpHash = sha256Hex(otp);
        LocalDateTime expires = LocalDateTime.now().plusMinutes(minutesValid);

        String invalidateOld = "UPDATE password_reset SET used = 1 WHERE user_id = ? AND used = 0";
        String insert = "INSERT INTO password_reset(user_id, code_hash, expires_at, used) VALUES(?, ?, ?, 0)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(invalidateOld);
                PreparedStatement ps2 = conn.prepareStatement(insert)) {

                ps1.setInt(1, u.getId());
                ps1.executeUpdate();

                ps2.setInt(1, u.getId());
                ps2.setString(2, otpHash);
                ps2.setTimestamp(3, Timestamp.valueOf(expires));
                ps2.executeUpdate();

                conn.commit();
                return otp; // trả OTP gốc để GUI gửi mail
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public static boolean verifyResetCode(String email, String otp) throws SQLException {
        User u = getByEmail(email);
        if (u == null) return false;

        String sql = """
            SELECT 1
            FROM password_reset
            WHERE user_id = ?
            AND code_hash = ?
            AND used = 0
            AND expires_at >= NOW()
            LIMIT 1
        """;

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, u.getId());
            ps.setString(2, sha256Hex(otp));

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static boolean resetPasswordByEmailAndCode(String email, String otp, String newPassword) throws SQLException {
        User u = getByEmail(email);
        if (u == null) return false;

        String check = """
            SELECT id
            FROM password_reset
            WHERE user_id = ?
            AND code_hash = ?
            AND used = 0
            AND expires_at >= NOW()
            LIMIT 1
        """;

        String updatePass = "UPDATE `user` SET password = ? WHERE id = ?";
        String markUsed = "UPDATE password_reset SET used = 1 WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psCheck = conn.prepareStatement(check)) {

                psCheck.setInt(1, u.getId());
                psCheck.setString(2, sha256Hex(otp));

                Integer tokenId = null;
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) tokenId = rs.getInt("id");
                }
                if (tokenId == null) {
                    conn.rollback();
                    return false;
                }

                try (PreparedStatement psUp = conn.prepareStatement(updatePass);
                    PreparedStatement psUsed = conn.prepareStatement(markUsed)) {

                    psUp.setString(1, newPassword);
                    psUp.setInt(2, u.getId());
                    psUp.executeUpdate();

                    psUsed.setInt(1, tokenId);
                    psUsed.executeUpdate();

                    conn.commit();
                    return true;
                }
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

  //forgot pass

    public static boolean changePassword(int userId, String oldPass, String newPass) throws SQLException {
        String check = "SELECT 1 FROM `user` WHERE id = ? AND password = ? LIMIT 1";
        String update = "UPDATE `user` SET password = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps1 = conn.prepareStatement(check);
             PreparedStatement ps2 = conn.prepareStatement(update)) {

            ps1.setInt(1, userId);
            ps1.setString(2, oldPass);

            try (ResultSet rs = ps1.executeQuery()) {
                if (!rs.next()) return false;
            }

            ps2.setString(1, newPass);
            ps2.setInt(2, userId);
            return ps2.executeUpdate() == 1;
        }
    }

    private static String generateOtp6() {
        int x = (int)(Math.random() * 900000) + 100000;
        return String.valueOf(x);
    }
    private static String sha256Hex(String input) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
