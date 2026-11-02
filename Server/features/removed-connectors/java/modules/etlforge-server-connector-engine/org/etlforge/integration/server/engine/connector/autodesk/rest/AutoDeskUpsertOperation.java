package org.etlforge.integration.server.engine.connector.autodesk.rest;

import com.google.common.base.Strings;
import org.etlforge.io.FileUtils;

public class AutoDeskUpsertOperation {
   final AutoDeskEndpoint autodeskEndpoint;
   final String workspaceId;

   public AutoDeskUpsertOperation(AutoDeskEndpoint var1, String var2) {
      this.autodeskEndpoint = var1;
      this.workspaceId = var2;
   }

   public static void executeUpsertOperation(AutoDeskEndpoint var0, String var1, String var2, String var3) {
      AutoDeskUpsertOperation var4 = new AutoDeskUpsertOperation(var0, var1);
      var4.executeUpsertOperation(var2, var3);
   }

   void executeUpsertOperation(String var1, String var2) {
      String var3 = FileUtils.getPostfixFilenameXml(var1, "_kv");
      AutoDeskOperationsRequestConversion var4 = new AutoDeskOperationsRequestConversion();
      AutoDeskOperationsRequestConversion.AutoDeskOperationsRequestInfo var5 = var4.convertToKeyValuePairs(var1, var3);
      boolean var6 = Strings.isNullOrEmpty(var5.versionID);
      this.executeUpsertOperation(var6, var5.alias, var3, var2);
   }

   void executeUpsertOperation(boolean var1, String var2, String var3, String var4) {
      AutoDeskGetFunctions var5 = new AutoDeskGetFunctions(this.autodeskEndpoint, this.workspaceId);
      String var6 = var5.getUpsertDmsid(var2);
      AutoDeskCallResponse var7 = var5.response;
      if (var5.shouldNotDoUpsert) {
         assert var7 != null;
         var7.saveDmsid(var4, "Upsert", this.workspaceId, var6);
         var7.throwRuntimeExceptionOnUnauthorized();
      } else {
         boolean var8 = Strings.isNullOrEmpty(var6);
         if (var8) {
            AutoDeskCreateOperation.executeCreateOperationKeyValue(true, this.autodeskEndpoint, this.workspaceId, var3, var4);
         } else {
            AutoDeskUpdateOperation.executeUpdateOperationKeyValue(var1, this.autodeskEndpoint, this.workspaceId, var6, var3, var4);
         }
      }
   }
}
