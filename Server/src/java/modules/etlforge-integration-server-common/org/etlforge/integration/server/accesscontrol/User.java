package org.etlforge.integration.server.accesscontrol;

public final class User {
   public final long dbId;
   public final String name;

   public User(long var1, String var3) {
      this.dbId = var1;
      this.name = var3;
   }

   public long getDatabaseId() {
      return this.dbId;
   }

   public String getName() {
      return this.name;
   }

   public boolean isAdmin() {
      return this.dbId == 0L || this.dbId == -1L;
   }

   public boolean isAnonymous() {
      return this.dbId == -2L;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof User ? this.dbId == ((User)var1).dbId : false;
      }
   }

   @Override
   public int hashCode() {
      return Long.valueOf(this.dbId).hashCode();
   }

   @Override
   public String toString() {
      return "User [name=" + this.name + " / dbId = " + this.dbId + "]";
   }
}
