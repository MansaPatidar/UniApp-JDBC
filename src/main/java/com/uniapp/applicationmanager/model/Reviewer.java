package com.uniapp.applicationmanager.model;

public class Reviewer extends User {

  private Program program;

  public enum Program {
    masters, phd
  }

  public Reviewer(int userId, String userName, String firstName, String lastName, String email,
      Program program) {
    super(userId, userName, firstName, lastName, email);
    this.program = program;
  }

  public Program getProgram() {
    return program;
  }

  public void setProgram(Program program) {
    this.program = program;
  }
}
