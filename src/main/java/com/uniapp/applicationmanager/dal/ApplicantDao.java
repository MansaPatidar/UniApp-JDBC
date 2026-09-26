package com.uniapp.applicationmanager.dal;

import com.uniapp.applicationmanager.model.Applicant;
import com.uniapp.applicationmanager.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicantDao {

  private ApplicantDao() {}

  /** CREATE */
  public static Applicant create(
      Connection cxn,
      final String username,
      final String firstName,
      final String lastName,
      final String email,
      final Applicant.Program program,
      final String essay
  ) throws SQLException {

    // Create User row
    int userId = UserDao.create(cxn, username, firstName, lastName, email);

    // Insert into Applicant
    final String insertApplicant = """
    INSERT INTO Applicant (userID, program, essay)
    VALUES (?, ?, ?);
  """;

    try (PreparedStatement stmt = cxn.prepareStatement(insertApplicant)) {
      stmt.setInt(1, userId);
      stmt.setString(2, program.name().toLowerCase());
      stmt.setString(3, essay);
      stmt.executeUpdate();
    }

    // Build full User object
    User base = UserDao.getUserByUserID(cxn, userId);

    return new Applicant(
        base.getUserId(),
        base.getUserName(),
        base.getFirstName(),
        base.getLastName(),
        base.getEmail(),
        program,
        essay
    );
  }

  /** UPDATE essay */
  public static Applicant updateEssay(
      Connection cxn,
      Applicant applicant,
      String newEssay
  ) throws SQLException {

    final String updateApplicant =
        "UPDATE Applicant SET Essay = ? WHERE UserId = ?;";

    try (PreparedStatement updateStmt = cxn.prepareStatement(updateApplicant)) {
      updateStmt.setString(1, newEssay);
      updateStmt.setInt(2, applicant.getUserId());
      updateStmt.executeUpdate();

      applicant.setEssay(newEssay);
      return applicant;
    }
  }

  /** DELETE */
  public static void delete(Connection cxn, Applicant applicant) throws SQLException {
    final String deleteApplicant =
        "DELETE FROM Applicant WHERE UserId = ?;";

    try (PreparedStatement deleteStmt = cxn.prepareStatement(deleteApplicant)) {
      deleteStmt.setInt(1, applicant.getUserId());
      deleteStmt.executeUpdate();
    }
  }

  /** GET by UserId */
  public static Applicant getApplicantByUserId(Connection cxn, int userId) throws SQLException {
    final String selectApplicant = """
      SELECT a.UserId, a.Program, a.Essay,
             u.UserName, u.FirstName, u.LastName, u.Email
        FROM Applicant a
        JOIN User u ON a.UserId = u.UserId
        WHERE a.UserId = ?;""";

    try (PreparedStatement selectStmt = cxn.prepareStatement(selectApplicant)) {
      selectStmt.setInt(1, userId);

      try (ResultSet results = selectStmt.executeQuery()) {
        if (results.next()) {
          return new Applicant(
              results.getInt("UserId"),
              results.getString("UserName"),
              results.getString("FirstName"),
              results.getString("LastName"),
              results.getString("Email"),
              Applicant.Program.valueOf(results.getString("Program")),
              results.getString("Essay")
          );
        } else {
          return null;
        }
      }
    }
  }

  /** GET all applicants */
  public static List<Applicant> getAllApplicants(Connection cxn) throws SQLException {
    List<Applicant> applicants = new ArrayList<>();

    final String selectApplicants = """
      SELECT a.UserId, a.Program, a.Essay,
             u.UserName, u.FirstName, u.LastName, u.Email
        FROM Applicant a
        JOIN User u ON a.UserId = u.UserId;""";

    try (PreparedStatement selectStmt = cxn.prepareStatement(selectApplicants)) {
      try (ResultSet results = selectStmt.executeQuery()) {
        while (results.next()) {
          applicants.add(
              new Applicant(
                  results.getInt("UserId"),
                  results.getString("UserName"),
                  results.getString("FirstName"),
                  results.getString("LastName"),
                  results.getString("Email"),
                  Applicant.Program.valueOf(results.getString("Program")),
                  results.getString("Essay")
              )
          );
        }
      }
    }
    return applicants;
  }
}
