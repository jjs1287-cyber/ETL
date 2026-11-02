package org.etlforge.integration.server.implementation.webservice.interchange.checkout.impl;

public class ItemCheckoutException extends Exception {
   private boolean m_bIsPermissionProblem = false;

   public ItemCheckoutException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public ItemCheckoutException(Throwable var1) {
      super(var1);
   }

   public ItemCheckoutException(String var1, boolean var2) {
      super(var1);
      this.m_bIsPermissionProblem = var2;
   }

   public ItemCheckoutException(String var1) {
      this(var1, false);
   }

   @Override
   public String getMessage() {
      return !this.m_bIsPermissionProblem ? super.getMessage() : super.getMessage() + " This is probably a missing permission problem.";
   }
}
