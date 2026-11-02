package org.etlforge.integration.user;

public final class GroupId extends RoleId {
   public GroupId() {
   }

   public GroupId(String var1) {
      super(var1);
   }

   @Override
   public boolean equals(Object var1) {
      return var1 instanceof GroupId && ((GroupId)var1).getGuid().equals(this.getGuid());
   }

   @Override
   public int hashCode() {
      return this.getGuid().hashCode();
   }
}
