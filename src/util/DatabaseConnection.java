// package util;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.SQLException;

// public class DatabaseConnection {
//     private static final String URL = "jdbc:mysql://mysql-32cabc3a-dbtodo.l.aivencloud.com:16701/defaultdb\r\n" + //
//                 "?useSSL=true\r\n" + //
//                 "&requireSSL=true\r\n" + //
//                 "&verifyServerCertificate=true\r\n" + //
//                 "" ;
//     private static final String USERNAME = "avnadmin";
//     private static final String PASSWORD = "";

//     private static Connection connection = null;

//     public static Connection getConnection() {
//         try {
//             if (connection == null || connection.isClosed()) {
//                 Class.forName("com.mysql.cj.jdbc.Driver");
//                 connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
//                 System.out.println("successfully");
//             }
//         } catch (ClassNotFoundException e) {
//             System.out.println("Connected unsuccesfully");
//             e.printStackTrace();
//         } catch (SQLException e) {
//             System.out.println("Fail to connect: " + e.getMessage());
//             e.printStackTrace();
//         }
//         return connection;
//     }

//     public static void main(String[] args) {
//         getConnection();  // Test chạy file này riêng
//     }
// }
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://mysql-32cabc3a-dbtodo.l.aivencloud.com:16701/dbtodo"
            + "?useSSL=true"
            + "&requireSSL=true"
            + "&verifyServerCertificate=false";

    private static final String USERNAME = "avnadmin";
    private static final String PASSWORD = "AVNS_5DozOk42Ru62zDG8aiA";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL Driver loaded");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find JDBC Driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("Connected!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}