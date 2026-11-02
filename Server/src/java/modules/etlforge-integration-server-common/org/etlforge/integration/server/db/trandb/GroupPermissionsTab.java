package org.etlforge.integration.server.db.trandb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.data.access.Permission;
import org.etlforge.integration.data.access.Permissions;
import org.etlforge.integration.server.accesscontrol.AccessLookupException;
import org.etlforge.integration.server.accesscontrol.Group;
import org.etlforge.integration.server.data.EntityDescriptor;

public class GroupPermissionsTab extends TranDbTable {
   private final String sql;

   public GroupPermissionsTab(String var1) {
      this(var1, null);
   }

   public GroupPermissionsTab(String var1, Connection var2) {
      super(var1, var2);
      this.sql = "SELECT allowbits, denybits FROM " + var1 + " WHERE item_id = ? AND group_id = ?";
   }

   public Permissions getPermissions(Group var1, EntityDescriptor var2) throws SQLException {
      if (!this.isOpen()) {
         this.open();
      }

      PreparedStatement var3 = null;
      ResultSet var4 = null;

      try {
         var3 = this.prepareStatement(this.sql);
         var3.setLong(1, var2.getDatabaseId());
         var3.setLong(2, var1.getDatabaseId());
         var4 = var3.executeQuery();
         if (var4.next()) {
            int var5 = var4.getInt(1);
            int var6 = var4.getInt(2);
            return new Permissions(Permission.fromBitCode(var5), Permission.fromBitCode(var6));
         }
      } catch (SQLException var11) {
         throw new AccessLookupException(var11);
      } finally {
         KongaDbUtils.close(var4);
         KongaDbUtils.close(var3);
      }

      return new Permissions();
   }
}
