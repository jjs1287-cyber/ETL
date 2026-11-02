package org.etlforge.integration.server.db.trandb;

import com.google.common.base.Preconditions;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.etlforge.integration.server.accesscontrol.AccessLookupException;
import org.etlforge.integration.server.accesscontrol.Group;
import org.etlforge.integration.server.accesscontrol.GroupLookup;
import org.etlforge.integration.server.accesscontrol.User;

public class TranDbGroupLookup implements GroupLookup {
   private final Connection conn;

   public TranDbGroupLookup(Connection var1) {
      this.conn = Preconditions.checkNotNull(var1);
   }

   @Override
   public List<Group> getGroups(User var1) throws AccessLookupException {
      try {
         return new UserGroupTab(this.conn).getGroupsForUser(var1);
      } catch (SQLException var3) {
         throw new AccessLookupException(var3);
      }
   }
}
