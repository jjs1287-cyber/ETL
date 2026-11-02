package org.etlforge.integration.server.db;

import com.google.common.base.Preconditions;
import java.sql.Connection;
import java.sql.SQLException;
import org.etlforge.integration.data.access.Permission;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.server.accesscontrol.AccessController;
import org.etlforge.integration.server.accesscontrol.ServerAccessException;
import org.etlforge.integration.server.accesscontrol.User;
import org.etlforge.integration.server.crypto.Crypto;
import org.etlforge.integration.server.db.trandb.SourcesTab;
import org.etlforge.integration.server.db.trandb.SourcesTabRow;
import org.etlforge.integration.server.db.trandb.TargetsTab;
import org.etlforge.integration.server.db.trandb.TargetsTabRow;
import org.etlforge.integration.server.idmappings.GuidToIdDbMapper;
import org.etlforge.integration.server.idmappings.GuidToIdMappingException;

public class LocationPasswordLookup {
   private final Connection tranDbConnection;
   private User etlforgeUser;
   private AccessController accessController;

   public LocationPasswordLookup(Connection var1) {
      this.tranDbConnection = var1;
   }

   public void enableAccessControl(User var1, AccessController var2) {
      Preconditions.checkNotNull(var1, "user");
      Preconditions.checkNotNull(var2, "controller");
      this.etlforgeUser = var1;
      this.accessController = var2;
   }

   public String lookupPassword(String var1, String var2) throws ServerDbException, ServerAccessException {
      if (var1 != null) {
         SourceId var8 = null;

         try {
            var8 = new SourceId(var1);
         } catch (IllegalArgumentException var5) {
            throw new ServerDbException("Invalid source ID: " + var1, var5);
         }

         return this.lookupPasswordFromDeployedSource(var8);
      } else if (var2 != null) {
         TargetId var3 = null;

         try {
            var3 = new TargetId(var2);
         } catch (IllegalArgumentException var6) {
            throw new ServerDbException("Invalid target ID: " + var2, var6);
         }

         return this.lookupPasswordFromDeployedTarget(var3);
      } else {
         throw new ServerDbException("No password has been specified, and no source or target ID provided.");
      }
   }

   public String lookupPassword(SourceId var1, TargetId var2) throws ServerDbException, ServerAccessException {
      if (var1 != null) {
         return this.lookupPasswordFromDeployedSource(var1);
      } else if (var2 != null) {
         return this.lookupPasswordFromDeployedTarget(var2);
      } else {
         throw new ServerDbException("No password has been specified, and no source or target ID provided.");
      }
   }

   private String lookupPasswordFromDeployedSource(SourceId var1) throws ServerDbException, ServerAccessException {
      try {
         GuidToIdDbMapper var2 = new GuidToIdDbMapper(this.tranDbConnection);
         Long var3 = var2.getId(var1);
         if (var3 != null) {
            SourcesTab var4 = new SourcesTab(this.tranDbConnection);
            SourcesTabRow var5 = var4.query(var3.intValue());
            String var6 = var5.get_password();
            if (var6 != null) {
               this.verifyAccess(var1);
               return Crypto.decryptString(var6);
            }
         }
      } catch (SQLException var7) {
         handleLookupException(var7);
      } catch (GuidToIdMappingException var8) {
         handleLookupException(var8);
      }

      return null;
   }

   private String lookupPasswordFromDeployedTarget(TargetId var1) throws ServerDbException, ServerAccessException {
      try {
         GuidToIdDbMapper var2 = new GuidToIdDbMapper(this.tranDbConnection);
         Long var3 = var2.getId(var1);
         if (var3 != null) {
            TargetsTab var4 = new TargetsTab(this.tranDbConnection);
            TargetsTabRow var5 = var4.query(var3.intValue());
            String var6 = var5.get_password();
            if (var6 != null) {
               this.verifyAccess(var1);
               return Crypto.decryptString(var6);
            }
         }
      } catch (SQLException var7) {
         handleLookupException(var7);
      } catch (GuidToIdMappingException var8) {
         handleLookupException(var8);
      }

      return null;
   }

   private void verifyAccess(IntegrationEntityId var1) throws ServerAccessException {
      if (this.etlforgeUser != null && this.accessController != null) {
         if (!this.accessController.hasPermission(this.etlforgeUser, var1, Permission.READ)) {
            throw new ServerAccessException(
               "The ETLForge user " + this.etlforgeUser.getName() + " does not have sufficient permissions to access this " + var1.getEntityType() + "."
            );
         }
      }
   }

   private static void handleLookupException(Exception var0) throws ServerDbException {
      throw new ServerDbException("Failed to lookup the requested password from the backend database", var0);
   }
}
