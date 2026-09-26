package com.uniapp.applicationmanager.dal;

import com.uniapp.applicationmanager.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

  private UserDao() {}

  /** CREATE */
  public static int create(
      Connection cxn,
      final String username,
      final String firstName,
      final String lastName,
      final String email
  ) throws SQLException {

    final String insertUser = """
    INSERT INTO User (username, firstName, lastName, email)
    VALUES (?, ?, ?, ?);""";

    try (PreparedStatement stmt = cxn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {

      stmt.setString(1, username);
      stmt.setString(2, firstName);
      stmt.setString(3, lastName);
      stmt.setString(4, email);

      stmt.executeUpdate();

      try (ResultSet keys = stmt.getGeneratedKeys()) {
        if (keys.next()) {
          return keys.getInt(1);
        } else {
          throw new SQLException("UserDao.create: No generated key returned.");
        }
      }
    }
  }

  /** UPDATE email */
  public static User updateEmail(
      Connection cxn,
      User user,
      String newEmail
  ) throws SQLException {

    final String updateUser =
        "UPDATE User SET Email = ? WHERE UserId = ?;";

    try (PreparedStatement updateStmt = cxn.prepareStatement(updateUser)) {
      updateStmt.setString(1, newEmail);
      updateStmt.setInt(2, user.getUserId());
      updateStmt.executeUpdate();

      user.setEmail(newEmail);
      return user;
    }
  }

  /** DELETE */
  public static void delete(Connection cxn, User user) throws SQLException {
    final String deleteUser = "DELETE FROM User WHERE UserId = ?;";

    try (PreparedStatement deleteStmt = cxn.prepareStatement(deleteUser)) {
      deleteStmt.setInt(1, user.getUserId());
      deleteStmt.executeUpdate();
    }
  }

  /** GET by ID */
  public static User getUserById(Connection cxn, int userId) throws SQLException {
    final String selectUser = """
      SELECT UserId, UserName, FirstName, LastName, Email
        FROM User
        WHERE UserId = ?;""";

    try (PreparedStatement selectStmt = cxn.prepareStatement(selectUser)) {
      selectStmt.setInt(1, userId);

      try (ResultSet results = selectStmt.executeQuery()) {
        if (results.next()) {
          return new User(
              results.getInt("UserId"),
              results.getString("UserName"),
              results.getString("FirstName"),
              results.getString("LastName"),
              results.getString("Email")
          );
        } else {
          return null;
        }
      }
    }
  }

  /** GET by username */
  public static User getUserByUserName(Connection cxn, String userName) throws SQLException {
    final String selectUser = """
      SELECT UserId, UserName, FirstName, LastName, Email
        FROM User
        WHERE UserName = ?;""";

    try (PreparedStatement selectStmt = cxn.prepareStatement(selectUser)) {
      selectStmt.setString(1, userName);

      try (ResultSet results = selectStmt.executeQuery()) {
        if (results.next()) {
          return new User(
              results.getInt("UserId"),
              results.getString("UserName"),
              results.getString("FirstName"),
              results.getString("LastName"),
              results.getString("Email")
          );
        } else {
          return null;
        }
      }
    }
  }

  /** GET all users */
  public static List<User> getAllUsers(Connection cxn) throws SQLException {
    List<User> user = new ArrayList<>();

    final String selectUsers = """
      SELECT UserId, UserName, FirstName, LastName, Email
        FROM User;""";

    try (PreparedStatement selectStmt = cxn.prepareStatement(selectUsers)) {
      try (ResultSet results = selectStmt.executeQuery()) {
        while (results.next()) {
          user.add(
              new User(
                  results.getInt("UserId"),
                  results.getString("UserName"),
                  results.getString("FirstName"),
                  results.getString("LastName"),
                  results.getString("Email")
              )
          );
        }
      }
    }
    return user;
  }

  public static User getUserByUserID(Connection cxn, int userID) throws SQLException {
    final String query = """
    SELECT userID, username, firstName, lastName, email
    FROM User
    WHERE userID = ?;
  """;

    try (PreparedStatement stmt = cxn.prepareStatement(query)) {
      stmt.setInt(1, userID);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return new User(
              rs.getInt("userID"),
              rs.getString("username"),
              rs.getString("firstName"),
              rs.getString("lastName"),
              rs.getString("email")
          );
        }
        return null;
      }
    }
  }
}
