package com.uniapp.applicationmanager;

import com.uniapp.applicationmanager.dal.*;
import com.uniapp.applicationmanager.model.*;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Main driver class demonstrating JDBC database operations for the University Application Management System.
 * This application demonstrates:
 * - Creating and managing database connections
 * - Implementing CRUD operations through DAOs
 * - Using parameterized queries for security
 * - Managing database transactions
 */
public class Driver {

  private static final String SCHEMA_NAME = "uniapp_db";

  public static void main(String[] args) {
    try {
      resetSchema();
      insertRecords();
    } catch (SQLException e) {
      System.out.println("SQL Exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(-1);
    }
  }

  /** INSERT SAMPLE DATA */
  public static void insertRecords() throws SQLException {
    try (Connection cxn = ConnectionManager.getConnection(SCHEMA_NAME)) {

      // --- CREATE APPLICANTS ---
      Applicant mansaApplicant = ApplicantDao.create(
          cxn,
          "mansa_p",
          "Mansa Vijay",
          "Patidar",
          "mansa.patidar@example.com",
          Applicant.Program.masters,
          "My research interests include distributed systems and scalable architectures."
      );

      Applicant keshavApplicant = ApplicantDao.create(
          cxn,
          "keshav_s",
          "Keshav",
          "Sule",
          "keshav.sule@example.com",
          Applicant.Program.phd,
          "I aim to pursue research in machine learning and optimization."
      );

      // --- CREATE REVIEWER ---
      Reviewer chandraReviewer = ReviewerDao.create(
          cxn,
          "chandra_p",
          "Chandra Prakash",
          "Patidar",
          "chandra.patidar@example.com",
          Reviewer.Program.masters
      );

      // --- CREATE RATINGS ---
      RatingDao.create(cxn, chandraReviewer, mansaApplicant, 5);
      Rating keshavRating = RatingDao.create(cxn, chandraReviewer, keshavApplicant, 4);

      // --- READ BACK DATA ---
      System.out.println("\n=== USERS ===");
      for (User u : UserDao.getAllUsers(cxn)) {
        System.out.format("User: %d %s %s %s %s\n",
            u.getUserId(), u.getUserName(), u.getFirstName(), u.getLastName(), u.getEmail());
      }

      System.out.println("\n=== APPLICANTS ===");
      for (Applicant a : ApplicantDao.getAllApplicants(cxn)) {
        System.out.format("Applicant: %d %s Program:%s Essay:%s\n",
            a.getUserId(), a.getUserName(), a.getProgram(), a.getEssay());
      }

      System.out.println("\n=== REVIEWERS ===");
      for (Reviewer r : ReviewerDao.getAllReviewers(cxn)) {
        System.out.format("Reviewer: %d %s Program:%s\n",
            r.getUserId(), r.getUserName(), r.getProgram());
      }

      System.out.println("\n=== RATINGS FOR MANSA ===");
      for (Rating rating : RatingDao.getRatingsForApplicant(cxn, mansaApplicant)) {
        System.out.format("Rating: Reviewer:%s Applicant:%s Score:%d\n",
            rating.getReviewer().getUserName(),
            rating.getApplicant().getUserName(),
            rating.getRating());
      }

      System.out.println("\n=== RATINGS BY CHANDRA ===");
      for (Rating rating : RatingDao.getRatingsByReviewer(cxn, chandraReviewer)) {
        System.out.format("Rating: Reviewer:%s Applicant:%s Score:%d\n",
            rating.getReviewer().getUserName(),
            rating.getApplicant().getUserName(),
            rating.getRating());
      }

      // --- DELETE OPERATIONS ---
      System.out.println("\n=== DELETING RATING FOR KESHAV ===");
      RatingDao.delete(cxn, keshavRating);

      System.out.println("=== DELETING APPLICANT KESHAV ===");
      ApplicantDao.delete(cxn, keshavApplicant);

      System.out.println("=== DELETING REVIEWER CHANDRA ===");
      ReviewerDao.delete(cxn, chandraReviewer);
    }
  }

  /** RESET SCHEMA */
  private static void resetSchema() throws SQLException {
    try (Connection cxn = ConnectionManager.getSchemalessConnection()) {
      cxn.createStatement().executeUpdate(
          String.format("DROP SCHEMA IF EXISTS %s;", SCHEMA_NAME)
      );
      cxn.createStatement().executeUpdate(
          String.format("CREATE SCHEMA %s;", SCHEMA_NAME)
      );
    }

    try (Connection cxn = ConnectionManager.getConnection(SCHEMA_NAME)) {

      cxn.createStatement().executeUpdate("""
        CREATE TABLE User(
          userID INT PRIMARY KEY AUTO_INCREMENT,
          username VARCHAR(30) NOT NULL,
          firstName VARCHAR(50) NOT NULL,
          lastName VARCHAR(50) NOT NULL,
          email VARCHAR(50),
          CONSTRAINT uq_User_username UNIQUE (username)
        );
      """);

      cxn.createStatement().executeUpdate("""
        CREATE TABLE Applicant (
          userID INT PRIMARY KEY,
          program ENUM('masters','phd') NOT NULL,
          essay TEXT,
          CONSTRAINT fk_Applicant_userID FOREIGN KEY (userID)
            REFERENCES User(userID)
            ON DELETE CASCADE ON UPDATE CASCADE
        );
      """);

      cxn.createStatement().executeUpdate("""
        CREATE TABLE Reviewer (
          userID INT PRIMARY KEY,
          program ENUM('masters','phd') NOT NULL,
          CONSTRAINT fk_Reviewer_userID FOREIGN KEY (userID)
            REFERENCES User(userID)
            ON DELETE CASCADE ON UPDATE CASCADE
        );
      """);

      cxn.createStatement().executeUpdate("""
        CREATE TABLE Rating (
          reviewerID INT,
          applicantID INT,
          rating INT NOT NULL,
          CONSTRAINT pk_Rating PRIMARY KEY (reviewerID, applicantID),
          CONSTRAINT fk_Rating_reviewerID FOREIGN KEY (reviewerID)
            REFERENCES Reviewer(userID)
            ON DELETE CASCADE ON UPDATE CASCADE,
          CONSTRAINT fk_Rating_applicantID FOREIGN KEY (applicantID)
            REFERENCES Applicant(userID)
            ON DELETE CASCADE ON UPDATE CASCADE
        );
      """);
    }
  }
}
