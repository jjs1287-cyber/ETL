package org.etlforge.application.ui.job;

public final class JobMessage {
   private final String message;

   public JobMessage(String var1) {
      this.message = var1 == null ? "" : var1;
   }

   public String getMessage() {
      return this.message;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof JobMessage ? this.message.equals(((JobMessage)var1).message) : false;
      }
   }

   @Override
   public int hashCode() {
      return this.message.hashCode();
   }
}
