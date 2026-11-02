package org.etlforge.integration.server.engine.connector.autodesk.rest;

import com.google.common.base.Strings;
import java.io.File;
import org.etlforge.io.FileUtils;

public class AutoDeskUpdateOperation {
   public static void executeUpdateOperation(AutoDeskEndpoint var0, String var1, String var2, String var3) {
      String var4 = FileUtils.getPostfixFilenameXml(var2, "_kv");
      AutoDeskOperationsRequestConversion var5 = new AutoDeskOperationsRequestConversion();
      AutoDeskOperationsRequestConversion.AutoDeskOperationsRequestInfo var6 = var5.convertToKeyValuePairs(var2, var4);
      executeUpdateOperationKeyValue(false, var0, var1, var6.dmsID, var4, var3);
   }

   public static void executeUpdateOperationKeyValue(boolean var0, AutoDeskEndpoint var1, String var2, String var3, String var4, String var5) {
      String var6 = null;
      if (var0) {
         AutoDeskGetFunctions var7 = new AutoDeskGetFunctions(var1, var2);
         var6 = var7.getItemVersionId(var3);
      }

      String var9 = Strings.isNullOrEmpty(var6) ? var4 : FileUtils.getPostfixFilenameXml(var4, "vid");
      if (!Strings.isNullOrEmpty(var6)) {
         AutoDeskUpdateRequestVersionIDReplacer.replaceVersionID(var6, var4, var9);
      }

      AutoDeskCallResponse var8 = executeUpdateOperationKeyValue(var1, var2, var3, var9);
      var8.saveDmsid(var5, "Update", var2, var3);
      var8.throwRuntimeExceptionOnUnauthorized();
   }

   public static AutoDeskCallResponse executeUpdateOperationKeyValue(AutoDeskEndpoint var0, String var1, String var2, String var3) {
      if (Strings.isNullOrEmpty(var2)) {
         return new AutoDeskCallResponse(false, "no dmsid");
      }

      String var5 = var0.getUrl(var1, "/" + var2);
      AutoDeskRestServiceHandler var6 = new AutoDeskRestServiceHandler("PUT", var5, new File(var3));
      return var6.execute(var0.getProxySettings(), var0.autodeskSession);
   }
}
