package org.etlforge.integration.jitterpack.impl;

public class InvalidPasswordException extends RuntimeException {
   public InvalidPasswordException() {
      super("The specified password is not correct.");
   }
}
