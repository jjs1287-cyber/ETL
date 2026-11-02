package org.etlforge.integration.user;

public interface GroupMemberListener {
   void memberAdded(Group var1, User var2);

   void memberRemoved(Group var1, User var2);
}
