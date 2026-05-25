package org.example;

import java.sql.*;

import org.h2.tools.Server;

public class JDBCDemo {

    private static final String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try {
            Server webServer = Server.createWebServer("-webPort", "8082", "-tcpAllowOthers").start();
            System.out.println("H2 Console started at: http://localhost:8082");

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                System.out.println("Connected to H2 in-memory database.\n");

                createTable(connection);

                insertStudent(connection, "Alice Johnson", "alice@example.com");
                insertStudent(connection, "Bob Smith", "bob@example.com");
                insertStudent(connection, "Carol Davis", "carol@example.com");

                System.out.println("=== ALL STUDENTS AFTER INSERT ===");
                printAllStudents(connection);

                updateStudentEmail(connection, 2, "bob.smith@school.com");
                System.out.println("\n=== ALL STUDENTS AFTER UPDATE ===");
                printAllStudents(connection);

                deleteStudent(connection, 1);
                System.out.println("\n=== ALL STUDENTS AFTER DELETE ===");
                printAllStudents(connection);

                System.out.println("\nOpen the H2 console in your browser!");
                System.out.println("JDBC URL: " + URL);
                System.out.println("User: sa (no password)");
                System.out.println("\nPress ENTER to exit...");
                System.in.read();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        String sql = """
            CREATE TABLE students (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                email VARCHAR(100) NOT NULL
            )
            """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Table 'students' created.");
        }
    }

    private static void insertStudent(Connection connection, String name, String email) throws SQLException {
        String sql = "INSERT INTO students (name, email) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);

            ps.executeUpdate();
            System.out.println("Inserted: " + name);
        }
    }

    private static void printAllStudents(Connection connection) throws SQLException {
        String sql = "SELECT id, name, email FROM students ORDER BY id";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                                rs.getString("name") + " | " +
                                rs.getString("email")
                );
            }
        }
    }

    private static void updateStudentEmail(Connection connection, int id, String newEmail) throws SQLException {
        String sql = "UPDATE students SET email = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newEmail);
            ps.setInt(2, id);

            ps.executeUpdate();
            System.out.println("Updated student id " + id);
        }
    }

    private static void deleteStudent(Connection connection, int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            ps.executeUpdate();
            System.out.println("Deleted student id " + id);
        }
    }
}