package com.uniapp.applicationmanager.model;

public class Rating {

  private Reviewer reviewer;
  private Applicant applicant;
  private int rating;

  public Rating(Reviewer reviewer, Applicant applicant, int rating) {
    this.reviewer = reviewer;
    this.applicant = applicant;
    this.rating = rating;
  }

  public Reviewer getReviewer() {
    return reviewer;
  }

  public void setReviewer(Reviewer reviewer) {
    this.reviewer = reviewer;
  }

  public Applicant getApplicant() {
    return applicant;
  }

  public void setApplicant(Applicant applicant) {
    this.applicant = applicant;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }
}
