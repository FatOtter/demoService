package com.example.demo.securityService;

import java.util.Date;

public class Token {

  private boolean valid;
  private final String securityCode;
  private final long generated;
  private final User user;
  private static long validPeriod = 1000 * 60 * 60 * 2;

  public Token(String securityCode, User user) {
    this.securityCode = securityCode;
    Date date = new Date();
    this.generated = date.getTime();
    this.user = user;
    this.valid = true;
  }

  public static void setValidPeriod(long newValidPeriod) {
    validPeriod = newValidPeriod;
  }

  public static void resetValidPeriod() {
    validPeriod = 1000 * 60 * 60 * 2;
  }

  public String getSecurityCode() {
    return securityCode;
  }

  public User getUser() {
    return user;
  }

  public boolean expired() {
    Date date = new Date();
    if (date.getTime() - generated > validPeriod) {
      this.valid = false;
      return true;
    }
    return false;
  }

  public void invalidate() {
    this.valid = false;
  }

  public boolean isValid() {
    this.expired();
    return this.valid;
  }
}
