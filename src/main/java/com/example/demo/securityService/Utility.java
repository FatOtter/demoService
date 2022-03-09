package com.example.demo.securityService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.UUID;

public class Utility {

  public static String encode(String input) {
    try {
      final MessageDigest digest = MessageDigest.getInstance("SHA-256");
      final byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(hash);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static boolean validate(String encoded, String plainText) {
    return encoded.equals(Utility.encode(plainText));
  }

  public static String randomString() {
    String uuid = UUID.randomUUID().toString();
    return uuid.replace("-", "");
  }

}
