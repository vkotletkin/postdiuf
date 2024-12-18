package ru.kotletkin.accounts;

public abstract class Account {

  private final String userName;
  private final String firstName;
  private final String lastName;

  private final long id;

  public Account(String userName, String firstName, String lastName, long id) {
    this.userName = userName;
    this.firstName = firstName;
    this.lastName = lastName;
    this.id = id;
  }

  public String getUserName() {
    return userName;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public long getId() {
    return id;
  }
}
