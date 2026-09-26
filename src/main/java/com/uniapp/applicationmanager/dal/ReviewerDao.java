package com.uniapp.applicationmanager.dal;

import com.uniapp.applicationmanager.model.Reviewer;
import com.uniapp.applicationmanager.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewerDao {

  private ReviewerDao() {}

  /** CREATE */
  public static Reviewer create(
      Connection cxn,
      final String username,
      final String firstName,
      final String lastName,
      final String email,
      final Reviewer.Program program
  ) throws SQLException {

    int userId = UserDao.create(cxn, username, firstName, lastName, email);

    final String insertReviewer = """
    INSERT INTO Reviewer (userID, program)
    VALUES (?, ?);
  """;

    try (PreparedStatement stmt = cxn.prepareStatement(insertReviewer)) {
      stmt.setInt(1, userId);
      stmt.setString(2, program.name().toLowerCase());
      stmt.executeUpdate();
    }

    User base = UserDao.getUserByUserID(cxn, userId);

    return new Reviewer(
        base.getUserId(),
        base.getUserName(),
        base.getFirstName(),
        base.getLastName(),
        base.getEmail(),
        program
    );
  }

  /** UPDATE program */
  public static Reviewer updateProgram(
      Connection cxn,
      Reviewer reviewer,
      Reviewer.Program newProgram
  ) throws SQLException {

    final String updateReviewer =
        "UPDATE Reviewer SET Program = ? WHERE UserId = ?;";

    try (PreparedStatement updateStmt = cxn.prepareStatement(updateReviewer)) {
      updateStmt.setString(1, newProgram.name().toLowerCase());
      updateStmt.setInt(2, reviewer.getUserId());
      updateStmt.executeUpdate();

      reviewer.setProgram(newProgram);
      return reviewer;
    }
  }

  /** DELETE */
  public static void delete(Connection cxn, Reviewer reviewer) throws SQLException {
    final String deleteReviewer =
        "DELETE FROM Reviewer WHERE UserId = ?;";

    try (PreparedStatement deleteStmt = cxn.prepareStatement(deleteReviewer)) {
      deleteStmt.setInt(1, reviewer.getUserId());
      deleteStmt.executeUpdate();
    }
  }

  /** GET by UserId */
  public static Reviewer getReviewerByUserId(Connection cxn, int userId) throws SQLException {
    final String selectReviewer = """
      SELECT r.UserId, r.Program,
             u.UserName, u.FirstName, u.LastName, u.Email
        FROM Reviewer r
        JOIN User u ON r.UserId = u.UserId
        WHERE r.UserId = ?;""";

    try (PreparedStatement selectStmt = cxn.prepareStatement(selectReviewer)) {
      selectStmt.setInt(1, userId);

      try (ResultSet results = selectStmt.executeQuery()) {
        if (results.next()) {
          return new Reviewer(
              results.getInt("UserId"),
              results.getString("UserName"),
              results.getString("FirstName"),
              results.getString("LastName"),
              results.getString("Email"),
              Reviewer.Program.valueOf(results.getString("Program"))
          );
        } else {
          return null;
        }
      }
    }
  }

  /** GET all reviewers */
  public static List<Reviewer> getAllReviewers(Connection cxn) throws SQLException {
    List<Reviewer> reviewers = new ArrayList<>();

    final String selectReviewers = """
      SELECT r.UserId, r.Program,
             u.UserName, u.FirstName, u.LastName, u.Email
        FROM Reviewer r
        JOIN User u ON r.UserId = u.UserId;""";

    try (PreparedStatement selectStmt = cxn.prepareStatement(selectReviewers)) {
      try (ResultSet results = selectStmt.executeQuery()) {
        while (results.next()) {
          reviewers.add(
              new Reviewer(
                  results.getInt("UserId"),
                  results.getString("UserName"),
                  results.getString("FirstName"),
                  results.getString("LastName"),
                  results.getString("Email"),
                  Reviewer.Program.valueOf(results.getString("Program"))
              )
          );
        }
      }
    }
    return reviewers;
  }
}
