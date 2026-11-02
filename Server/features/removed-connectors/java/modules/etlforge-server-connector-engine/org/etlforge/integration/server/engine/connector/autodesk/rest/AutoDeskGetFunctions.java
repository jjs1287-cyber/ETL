package org.etlforge.integration.server.engine.connector.autodesk.rest;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

public class AutoDeskGetFunctions {
   private final AutoDeskEndpoint autodeskEndpoint;
   private final String workspaceId;
   AutoDeskCallResponse response;
   boolean shouldNotDoUpsert;

   public AutoDeskGetFunctions(AutoDeskEndpoint var1, String var2) {
      this.autodeskEndpoint = var1;
      this.workspaceId = var2;
      this.response = null;
      this.shouldNotDoUpsert = false;
   }

   public String getItemVersionId(String var1) {
      if (Strings.isNullOrEmpty(var1)) {
         return "";
      } else {
         this.executeGetOperationDmsid(var1);
         if (this.response == null) {
            return "";
         } else {
            this.response.throwRuntimeExceptionOnUnauthorized();
            if (this.response.getHttpCallResponse().isGoodResponse()) {
               String var2 = this.response.getHttpCallResponse().getResponse();
               return AutoDeskItem.getAutoDeskItemVersionId(var2);
            } else {
               return "";
            }
         }
      }
   }

   public String getUpsertDmsid(String var1) {
      this.shouldNotDoUpsert = false;
      if (var1 != null) {
         var1 = var1.trim();
      }

      if (Strings.isNullOrEmpty(var1)) {
         return null;
      } else {
         this.executeGetOperationAlias(var1);
         if (this.response == null) {
            return null;
         } else if (this.response.getHttpCallResponse().getStatusCode() == 404) {
            return null;
         } else {
            this.shouldNotDoUpsert = this.response.getHttpCallResponse().isBadResponse();
            if (this.shouldNotDoUpsert) {
               return "";
            } else {
               String var2 = this.response.getHttpCallResponse().getResponse();
               List var3 = AutoDeskGetOperation.parseAliasUrlList(false, var2);
               List var4 = getDmsids(var3);
               Object var6 = null;
               this.shouldNotDoUpsert = var4.size() > 1;
               if (this.shouldNotDoUpsert) {
                  String var5 = "duplicate items " + var4.toString() + " for workspaceId=" + this.workspaceId + ";alias=" + var1;
                  this.response = new AutoDeskCallResponse(false, var5);
                  return "";
               } else {
                  return var4.size() == 0 ? null : (String)var4.get(0);
               }
            }
         }
      }
   }

   private static List<String> getDmsids(List<String> var0) {
      ArrayList var1 = Lists.newArrayList();

      for (String var3 : var0) {
         String[] var4 = var3.split("/");
         var1.add(var4[var4.length - 1]);
      }

      return var1;
   }

   private void executeGetOperationAlias(String var1) {
      if (!Strings.isNullOrEmpty(this.workspaceId)) {
         String var2 = this.autodeskEndpoint.getUrl(this.workspaceId, "/alias/" + var1);
         this.response = executeGetOperationUrl(this.autodeskEndpoint, var2);
      }
   }

   private void executeGetOperationDmsid(String var1) {
      if (!Strings.isNullOrEmpty(this.workspaceId)) {
         String var2 = this.autodeskEndpoint.getUrl(this.workspaceId, "/" + var1);
         this.response = executeGetOperationUrl(this.autodeskEndpoint, var2);
      }
   }

   private static AutoDeskCallResponse executeGetOperationUrl(AutoDeskEndpoint var0, String var1) {
      AutoDeskRestServiceHandler var3 = new AutoDeskRestServiceHandler("GET", var1);
      return var3.execute(var0.getProxySettings(), var0.autodeskSession);
   }
}
