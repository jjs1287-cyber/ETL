package org.etlforge.integration.user;

public final class UserId extends RoleId {
   public UserId() {
   }

   public UserId(String var1) {
      super(var1);
   }

   @Override
   public boolean equals(Object var1) {
      return var1 instanceof UserId && ((UserId)var1).getGuid().equals(this.getGuid());
   }

   @Override
   public int hashCode() {
      return this.getGuid().hashCode();
   }
}
