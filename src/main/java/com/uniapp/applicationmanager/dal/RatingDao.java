package com.uniapp.applicationmanager.dal;

import com.uniapp.applicationmanager.model.Rating;
import com.uniapp.applicationmanager.model.Reviewer;
import com.uniapp.applicationmanager.model.Applicant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingDao {

  private RatingDao() {}

  /** CREATE */
  public static Rating create(
      final Connection cxn,
      final Reviewer reviewer,
      final Applicant applicant,
      final int rating
  ) throws SQLException {

    final String insertRating = """
      INSERT INTO Rating (ReviewerId, ApplicantId, Rating)
        VALUES (?, ?, ?);""";

    try (
        PreparedStatement insertStmt = cxn.prepareStatement(insertRating)
    ) {
      insertStmt.setInt(1, reviewer.getUserId());
      insertStmt.setInt(2, applicant.getUserId());
      insertStmt.setInt(3, rating);
      insertStmt.executeUpdate();

      return new Rating(reviewer, applicant, rating);
    }
  }

  /** UPDATE rating */
  public static Rating updateRating(
      Connection cxn,
      Rating ratingObj,
      int newRating
  ) throws SQLException {

    final String updateRating =
        "UPDATE Rating SET Rating = ? WHERE ReviewerId = ? AND ApplicantId = ?;";

    try (PreparedStatement updateStmt = cxn.prepareStatement(updateRating)) {
      updateStmt.setInt(1, newRating);
      updateStmt.setInt(2, ratingObj.getReviewer().getUserId());
      updateStmt.setInt(3, ratingObj.getApplicant().getUserId());
      updateStmt.executeUpdate();

      ratingObj.setRating(newRating);
      return ratingObj;
    }
  }

  /** DELETE */
  public static void delete(Connection cxn, Rating ratingObj) throws SQLException {
    final String deleteRating =
        "DELETE FROM Rating WHERE ReviewerId = ? AND ApplicantId = ?;";

    try (PreparedStatement deleteStmt = cxn.prepareStatement(deleteRating)) {
      deleteStmt.setInt(1, ratingObj.getReviewer().getUserId());
      deleteStmt.setInt(2, ratingObj.getApplicant().getUserId());
      deleteStmt.executeUpdate();
    }
  }

  /** GET a single rating */
  public static Rating getRating(
      Connection cxn,
      int reviewerId,
      int applicantId
  ) throws SQLException {

    final String selectRating = """
      SELECT Rating
        FROM Rating
        WHERE ReviewerId = ? AND ApplicantId = ?;""";

    try (PreparedStatement selectStmt = cxn.prepareStatement(selectRating)) {
      selectStmt.setInt(1, reviewerId);
      selectStmt.setInt(2, applicantId);

      try (ResultSet results = selectStmt.executeQuery()) {
        if (results.next()) {
          Reviewer reviewer = ReviewerDao.getReviewerByUserId(cxn, reviewerId);
          Applicant applicant = ApplicantDao.getApplicantByUserId(cxn, applicantId);

          return new Rating(
              reviewer,
              applicant,
              results.getInt("Rating")
          );
        } else {
          return null;
        }
      }
    }
  }

  /** GET all ratings for an applicant */
  public static List<Rating> getRatingsForApplicant(
      Connection cxn,
      Applicant applicant
  ) throws SQLException {

    List<Rating> ratings = new ArrayList<>();

    final String selectRatings = """
      SELECT ReviewerId, Rating
        FROM Rating
        WHERE ApplicantId = ?;""";

    try (PreparedStatement selectStmt = cxn.prepareStatement(selectRatings)) {
      selectStmt.setInt(1, applicant.getUserId());

      try (ResultSet results = selectStmt.executeQuery()) {
        while (results.next()) {
          Reviewer reviewer =
              ReviewerDao.getReviewerByUserId(cxn, results.getInt("ReviewerId"));

          ratings.add(
              new Rating(
                  reviewer,
                  applicant,
                  results.getInt("Rating")
              )
          );
        }
      }
    }
    return ratings;
  }

  /** GET all ratings given by a reviewer */
  public static List<Rating> getRatingsByReviewer(
      Connection cxn,
      Reviewer reviewer
  ) throws SQLException {

    List<Rating> ratings = new ArrayList<>();

    final String selectRatings = """
      SELECT ApplicantId, Rating
        FROM Rating
        WHERE ReviewerId = ?;""";

    try (PreparedStatement selectStmt = cxn.prepareStatement(selectRatings)) {
      selectStmt.setInt(1, reviewer.getUserId());

      try (ResultSet results = selectStmt.executeQuery()) {
        while (results.next()) {
          Applicant applicant =
              ApplicantDao.getApplicantByUserId(cxn, results.getInt("ApplicantId"));

          ratings.add(
              new Rating(
                  reviewer,
                  applicant,
                  results.getInt("Rating")
              )
          );
        }
      }
    }
    return ratings;
  }
}
