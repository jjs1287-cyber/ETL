package org.etlforge.integration.user;

public class ProtectedMemberException extends Exception {
   public ProtectedMemberException(Group var1, User var2) {
      super("The user " + var2.getName() + " cannot be removed from the group " + var1.getName());
   }
}
