package org.etlforge.integration.server.accesscontrol;

public class Group {
   public final long dbId;
   public final String name;

   public Group(long var1, String var3) {
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
      return this.dbId == 0L;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof Group ? this.dbId == ((Group)var1).dbId : false;
      }
   }

   @Override
   public int hashCode() {
      return Long.valueOf(this.dbId).hashCode();
   }

   @Override
   public String toString() {
      return "Group [name=" + this.name + " / dbId = " + this.dbId + "]";
   }
}
