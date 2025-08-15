package db;

import model.LogResult;
import util.DbUtil;

import java.sql.*;
import java.util.List;

public class DatabaseManager {

    public DatabaseManager() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS log_analysis (
                id INT AUTO_INCREMENT PRIMARY KEY,
                filename VARCHAR(255),
                word_count INT,
                keyword_count INT,
                processed_at DATETIME
            )
        """;
        try (Connection conn = DbUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Batch insert với Transaction
    public void saveBatch(List<LogResult> results) {
        String sql = "INSERT INTO log_analysis(filename, word_count, keyword_count, processed_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);

            for (LogResult r : results) {
                ps.setString(1, r.getFileName());
                ps.setInt(2, r.getWordCount());
                ps.setInt(3, r.getKeywordCount());
                ps.setTimestamp(4, Timestamp.valueOf(r.getProcessedAt()));
                ps.addBatch();
            }

            ps.executeBatch();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // CRUD: Read
    public void showAll() {
        String sql = "SELECT * FROM log_analysis";
        try (Connection conn = DbUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("%d | %s | %d | %d | %s%n",
                        rs.getInt("id"),
                        rs.getString("filename"),
                        rs.getInt("word_count"),
                        rs.getInt("keyword_count"),
                        rs.getTimestamp("processed_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
