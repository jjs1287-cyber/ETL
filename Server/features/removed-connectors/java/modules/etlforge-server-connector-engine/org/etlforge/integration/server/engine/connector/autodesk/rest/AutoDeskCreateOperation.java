package org.etlforge.integration.server.engine.connector.autodesk.rest;

import com.google.common.base.Strings;
import java.io.File;
import org.etlforge.io.FileUtils;

public class AutoDeskCreateOperation {
   public static void executeCreateOperation(AutoDeskEndpoint var0, String var1, String var2, String var3) {
      String var4 = FileUtils.getPostfixFilenameXml(var2, "_kv");
      AutoDeskOperationsRequestConversion var5 = new AutoDeskOperationsRequestConversion();
      AutoDeskOperationsRequestConversion.AutoDeskOperationsRequestInfo var6 = var5.convertToKeyValuePairs(var2, var4);
      executeCreateOperationKeyValue(false, var0, var1, var4, var3);
   }

   public static void executeCreateOperationKeyValue(boolean var0, AutoDeskEndpoint var1, String var2, String var3, String var4) {
      AutoDeskCallResponse var5 = executeCreateOperationKeyValue(var1, var2, var3);
      if (var5.getHttpCallResponse().isGoodResponse()) {
         String var7 = var5.getHttpCallResponse().getResponse();
         String var8 = AutoDeskItem.getAutoDeskItemDmsID(var7);
         if (Strings.isNullOrEmpty(var8)) {
            var5.saveItem(var4, "Create", null);
         } else if (var0) {
            var5.saveDmsid(var4, "Create", var2, var8);
         } else {
            String var9 = new AutoDeskItemResponseConversion().convertFromKeyValuePairs(var7, 2);
            var5.saveItem(var4, "Create", var9);
         }
      } else {
         var5.saveItem(var4, "Create", null);
      }

      var5.throwRuntimeExceptionOnUnauthorized();
   }

   public static AutoDeskCallResponse executeCreateOperationKeyValue(AutoDeskEndpoint var0, String var1, String var2) {
      String var4 = "api/rest/v1/workspaces/" + var1 + "/items";
      String var5 = var0.getUrl(var1);
      AutoDeskRestServiceHandler var6 = new AutoDeskRestServiceHandler("POST", var5, new File(var2));
      return var6.execute(var0.getProxySettings(), var0.autodeskSession);
   }
}
