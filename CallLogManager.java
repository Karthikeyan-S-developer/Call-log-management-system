import java.sql.*;
import java.util.Scanner;

public class CallLogManager {
    private static final String URL = "jdbc:mysql://localhost:3306/CallLogDB";
    private static final String USER = "root";
    private static final String PASSWORD = "SKy@12345";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connected to MySQL database!");

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("1. Add Call Log");
                System.out.println("2. View All Call Logs");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int option = scanner.nextInt();

                if (option == 1) {
                    addCallLog(conn, scanner);
                } else if (option == 2) {
                    viewAllCallLogs(conn);
                } else if (option == 3) {
                    System.out.println("Exited");
                    break;
                } else {
                    System.out.println("Invalid option. Try again.");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }

    private static void addCallLog(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter caller name: ");
        String callerName = scanner.next();
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.next();
        System.out.print("Enter call date (YYYY-MM-DD HH:MM:SS): ");
        String callDate = scanner.next() + " " + scanner.next();
        System.out.print("Enter duration in seconds: ");
        int duration = scanner.nextInt();
        System.out.print("Enter call type (Incoming/Outgoing/Missed): ");
        String callType = scanner.next();

        String sql = "INSERT INTO call_logs (caller_name, phone_number, call_date, duration, call_type) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, callerName);
            pstmt.setString(2, phoneNumber);
            pstmt.setString(3, callDate);
            pstmt.setInt(4, duration);
            pstmt.setString(5, callType);
            pstmt.executeUpdate();
            System.out.println("Call log added successfully!");
        }
    }

    private static void viewAllCallLogs(Connection conn) throws SQLException {
        String sql = "SELECT * FROM call_logs";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("ID: %d, Caller: %s, Number: %s, Date: %s, Duration: %d sec, Type: %s%n",
                        rs.getInt("call_id"), rs.getString("caller_name"), rs.getString("phone_number"),
                        rs.getTimestamp("call_date"), rs.getInt("duration"), rs.getString("call_type"));
            }
        }
    }
}
