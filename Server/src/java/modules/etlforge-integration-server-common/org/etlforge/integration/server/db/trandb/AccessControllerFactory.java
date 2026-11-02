package org.etlforge.integration.server.db.trandb;

import com.google.common.base.Preconditions;
import java.sql.Connection;
import org.etlforge.integration.server.accesscontrol.AccessController;
import org.etlforge.integration.server.idmappings.GuidToIdDbMapper;

public class AccessControllerFactory {
   private final Connection conn;

   public AccessControllerFactory(Connection var1) {
      this.conn = Preconditions.checkNotNull(var1);
   }

   public AccessController newController() {
      AccessController var1 = new AccessController();
      var1.setGroupLookup(new TranDbGroupLookup(this.conn));
      var1.setUserPermissionsLookup(new TranDbUserPermissionsLookup(this.conn));
      var1.setGroupPermissionsLookup(new TranDbGroupPermissionsLookup(this.conn));
      var1.setGuidToIdMapper(new GuidToIdDbMapper(this.conn));
      return var1;
   }
}
