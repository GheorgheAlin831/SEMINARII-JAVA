package org.example;

import java.sql.*;

import org.h2.tools.Server;


public class Task {

    private static final String URL =
            "jdbc:h2:mem:moviesdb;DB_CLOSE_DELAY=-1";

    private static final String USER = "sa";

    private static final String PASSWORD = "";

    public static void main(String[] args) {

        try {

            Server webServer = Server.createWebServer(
                    "-webPort",
                    "8082",
                    "-tcpAllowOthers"
            ).start();

            System.out.println(
                    "H2 Console started at: http://localhost:8082"
            );

            try (Connection connection =
                         DriverManager.getConnection(
                                 URL,
                                 USER,
                                 PASSWORD
                         )) {

                System.out.println(
                        "Connected to H2 in-memory database.\n"
                );

                createTable(connection);

                insertMovie(
                        connection,
                        "The Godfather",
                        "Francis Ford Coppola",
                        1972
                );

                insertMovie(
                        connection,
                        "The Dark Knight",
                        "Christopher Nolan",
                        2008
                );

                insertMovie(
                        connection,
                        "Fight Club",
                        "David Fincher",
                        1999
                );

                insertMovie(
                        connection,
                        "Dune",
                        "Denis Villeneuve",
                        2021
                );

                System.out.println(
                        "=== ALL MOVIES AFTER INSERT ==="
                );

                printAllMovies(connection);

                updateMovieYear(
                        connection,
                        2,
                        2010
                );

                System.out.println(
                        "\n=== ALL MOVIES AFTER UPDATE ==="
                );

                printAllMovies(connection);

                deleteMovie(connection, 1);

                System.out.println(
                        "\n=== ALL MOVIES AFTER DELETE ==="
                );

                printAllMovies(connection);

                System.out.println(
                        "\nOpen the H2 console in your browser!"
                );

                System.out.println("JDBC URL: " + URL);

                System.out.println("User: sa");

                System.out.println(
                        "\nPress ENTER to exit..."
                );

                System.in.read();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private static void createTable(Connection connection)
            throws SQLException {

        String sql = """
                CREATE TABLE movies (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    title VARCHAR(100) NOT NULL,
                    director VARCHAR(100) NOT NULL,
                    release_year INT NOT NULL
                )
                """;

        try (Statement statement =
                     connection.createStatement()) {

            statement.execute(sql);

            System.out.println(
                    "Table 'movies' created."
            );
        }
    }

    // CREATE
    private static void insertMovie(
            Connection connection,
            String title,
            String director,
            int year
    ) throws SQLException {

        String sql =
                "INSERT INTO movies (title, director, release_year) VALUES (?, ?, ?)";

        try (PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setString(1, title);

            ps.setString(2, director);

            ps.setInt(3, year);

            ps.executeUpdate();

            System.out.println(
                    "Inserted movie: " + title
            );
        }
    }

    // READ
    private static void printAllMovies(
            Connection connection
    ) throws SQLException {

        String sql =
                "SELECT id, title, director, release_year FROM movies ORDER BY id";

        try (PreparedStatement ps =
                     connection.prepareStatement(sql);

             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                System.out.println(
                        rs.getInt("id") + " | " +
                                rs.getString("title") + " | " +
                                rs.getString("director") + " | " +
                                rs.getInt("release_year")
                );
            }
        }
    }

    // UPDATE
    private static void updateMovieYear(
            Connection connection,
            int id,
            int newYear
    ) throws SQLException {

        String sql =
                "UPDATE movies SET release_year = ? WHERE id = ?";

        try (PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setInt(1, newYear);

            ps.setInt(2, id);

            ps.executeUpdate();

            System.out.println(
                    "Updated movie id " + id
            );
        }
    }

    // DELETE
    private static void deleteMovie(
            Connection connection,
            int id
    ) throws SQLException {

        String sql =
                "DELETE FROM movies WHERE id = ?";

        try (PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

            System.out.println(
                    "Deleted movie id " + id
            );
        }
    }
}