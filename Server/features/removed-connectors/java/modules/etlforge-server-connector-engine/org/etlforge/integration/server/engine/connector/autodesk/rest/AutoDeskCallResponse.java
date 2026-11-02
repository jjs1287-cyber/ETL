package org.etlforge.integration.server.engine.connector.autodesk.rest;

import com.google.common.base.Strings;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.util.HttpCallResponse;
import org.etlforge.integration.server.engine.connector.util.XmlUtils;

public class AutoDeskCallResponse {
   private final boolean success;
   private final String errorMessage;
   private final HttpCallResponse response;

   AutoDeskCallResponse(HttpCallResponse var1, String var2) {
      this.response = var1;
      this.success = !var1.isBadResponse();
      this.errorMessage = var2;
   }

   AutoDeskCallResponse(boolean var1, String var2) {
      this.response = null;
      this.success = var1;
      this.errorMessage = var2;
   }

   HttpCallResponse getHttpCallResponse() {
      return this.response;
   }

   private String toXmlString(String var1, String var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append(XmlUtils.xmlUtf8Header);
      XmlElementUtils.startElementTag(var3, "etlforgeresult");
      String var4 = this.success ? "true" : "false";
      XmlElementUtils.elementValue(var3, "action", var1);
      XmlElementUtils.elementValue(var3, "success", var4);
      XmlElementUtils.elementValueOptional(var3, "message", this.errorMessage);
      if (this.response != null) {
         Integer var5 = this.response.getStatusCode();
         XmlElementUtils.elementValueOptional(var3, "statuscode", var5.toString());
         XmlElementUtils.elementValueOptional(var3, "statusline", this.response.getStatusLine());
      }

      if (!Strings.isNullOrEmpty(var2)) {
         var3.append(var2);
      }

      XmlElementUtils.endElementTag(var3, "etlforgeresult");
      return var3.toString();
   }

   void saveItem(String var1, String var2, String var3) {
      ConnectorExecutionSystem.writeFileUTF8(var1, this.toXmlString(var2, var3));
   }

   void saveDmsid(String var1, String var2, String var3, String var4) {
      StringBuilder var5 = new StringBuilder();
      XmlElementUtils.elementValueOptional(var5, "dmsID", var4);
      XmlElementUtils.elementValueOptional(var5, "workspaceID", var3);
      ConnectorExecutionSystem.writeFileUTF8(var1, this.toXmlString(var2, var5.toString()));
   }

   void throwRuntimeExceptionOnBadResponse() {
      if (this.response != null) {
         this.response.throwRuntimeExceptionOnBadResponse(this.errorMessage);
      }
   }

   void throwRuntimeExceptionOnUnauthorized() {
      if (this.response != null) {
         this.response.throwRuntimeExceptionOnUnauthorized(this.errorMessage);
      }
   }
}
