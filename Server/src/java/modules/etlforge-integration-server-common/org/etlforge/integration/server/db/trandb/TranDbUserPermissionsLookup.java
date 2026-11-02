package org.etlforge.integration.server.db.trandb;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import org.etlforge.integration.data.access.Permissions;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.server.accesscontrol.AccessLookupException;
import org.etlforge.integration.server.accesscontrol.User;
import org.etlforge.integration.server.accesscontrol.UserPermissionsLookup;
import org.etlforge.integration.server.data.EntityDescriptor;

public class TranDbUserPermissionsLookup implements UserPermissionsLookup {
   private static final Map<EntityType, String> tableNames = Maps.newEnumMap(EntityType.class);
   private final Connection conn;

   public TranDbUserPermissionsLookup(Connection var1) {
      this.conn = Preconditions.checkNotNull(var1);
   }

   @Override
   public Permissions getPermissions(User var1, EntityDescriptor var2) throws AccessLookupException {
      try {
         String var3 = getTableName(var2.getEntityType());
         UserPermissionsTab var4 = new UserPermissionsTab(var3, this.conn);
         return var4.getPermissions(var1, var2);
      } catch (SQLException var5) {
         throw new AccessLookupException(var5);
      }
   }

   private static String getTableName(EntityType var0) {
      String var1 = tableNames.get(var0);
      if (var1 == null) {
         throw new IllegalArgumentException("Unexpected entity type: " + var0);
      } else {
         return var1;
      }
   }

   static {
      for (EntityType var3 : EntityType.values()) {
         String var4;
         switch (var3) {
            case WebServiceCall:
               var4 = "userpermissionsforwebservicestab";
               break;
            case Project:
               var4 = "userpermissionsforinterchangeprojectstab";
               break;
            case EmailMessage:
               var4 = "userpermissionsformessagestab";
               break;
            default:
               var4 = "userpermissionsfor" + var3.getPlural().toLowerCase() + "tab";
         }

         tableNames.put(var3, var4);
      }
   }
}
