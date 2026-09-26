package com.uniapp.applicationmanager.model;

public class Applicant extends User {

  private Program program;
  private String essay;

  public enum Program {
    masters, phd
  }

  public Applicant(int userId, String userName, String firstName, String lastName, String email,
      Program program, String essay) {
    super(userId, userName, firstName, lastName, email);
    this.program = program;
    this.essay = essay;
  }

  public Program getProgram() {
    return program;
  }

  public void setProgram(Program program) {
    this.program = program;
  }

  public String getEssay() {
    return essay;
  }

  public void setEssay(String essay) {
    this.essay = essay;
  }
}
