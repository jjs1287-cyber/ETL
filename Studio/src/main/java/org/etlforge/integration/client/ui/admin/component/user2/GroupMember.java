package org.etlforge.integration.client.ui.admin.component.user2;

import com.google.common.base.Preconditions;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.Role;
import org.etlforge.integration.user.User;

final class GroupMember implements Role {
   private final User user;
   private final Group group;

   public GroupMember(User var1, Group var2) {
      this.user = Preconditions.checkNotNull(var1, "user");
      this.group = var2;
   }

   public User getUser() {
      return this.user;
   }

   public Group getGroup() {
      return this.group;
   }

   public void leaveGroup() throws VetoedModelChangeException {
      try {
         this.group.removeMember(this.user);
      } catch (Exception var2) {
         throw new VetoedModelChangeException(var2.getMessage());
      }
   }

   @Override
   public String getName() {
      return this.user.getName();
   }

   @Override
   public boolean equals(Object var1) {
      return var1 instanceof GroupMember && this.user.getId().equals(((GroupMember)var1).user.getId());
   }

   @Override
   public int hashCode() {
      return this.user.getId().hashCode();
   }

   @Override
   public String toString() {
      return this.user.getName();
   }
}
