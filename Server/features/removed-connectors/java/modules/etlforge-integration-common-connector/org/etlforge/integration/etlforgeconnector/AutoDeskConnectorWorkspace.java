package org.etlforge.integration.etlforgeconnector;

import java.io.Serializable;

public class AutoDeskConnectorWorkspace implements Serializable {
   private final String workspaceName;
   private String workspaceId;
   private static String IDPREFIX = "id:";
   private static char LEFTBRACKET = '(';
   private static char RIGHTBRACKET = ')';
   private static char UNICHAR = '_';

   public AutoDeskConnectorWorkspace(String var1, String var2) {
      this.workspaceId = var1 == null ? "" : var1;
      this.workspaceName = var2 == null ? "" : var2.replace(LEFTBRACKET, UNICHAR).replace(RIGHTBRACKET, UNICHAR);
   }

   public String getWorkspaceID() {
      return this.workspaceId;
   }

   void setWorkspaceID(String var1) {
      this.workspaceId = var1 == null ? "" : var1;
   }

   public String getWorkspaceName() {
      return this.workspaceName;
   }

   public String toWorkspaceKey() {
      return this.workspaceName + LEFTBRACKET + IDPREFIX + this.workspaceId + RIGHTBRACKET;
   }

   public static AutoDeskConnectorWorkspace fromWorkspaceKey(String var0) {
      String var1 = "";
      String var2 = var0;
      int var3 = var0.lastIndexOf(LEFTBRACKET);
      int var4 = var0.lastIndexOf(RIGHTBRACKET);
      if (var3 >= 0 && var4 > 0 && var3 < var4) {
         var2 = var0.substring(0, var3);
         var1 = var0.substring(var3 + 1, var4);
         if (var1.startsWith(IDPREFIX)) {
            var1 = var1.substring(IDPREFIX.length());
         } else {
            var1 = "";
         }
      }

      return new AutoDeskConnectorWorkspace(var1, var2);
   }
}
