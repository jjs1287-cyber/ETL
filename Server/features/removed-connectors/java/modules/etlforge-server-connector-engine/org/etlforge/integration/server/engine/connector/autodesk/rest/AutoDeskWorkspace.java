package org.etlforge.integration.server.engine.connector.autodesk.rest;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.etlforgeconnector.AutoDeskConnectorWorkspace;
import org.etlforge.integration.server.engine.connector.exception.ConnectorWebServiceRuntimeException;
import org.etlforge.integration.server.engine.connector.util.HttpCallUrlUtils;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.xml.sax.SAXException;

public class AutoDeskWorkspace {
   public static List<AutoDeskConnectorWorkspace> getAutoDeskWorkspaces(AutoDeskEndpoint var0) {
      String var2 = HttpCallUrlUtils.getFullUrl(var0.serviceBaseUrl, "/api/rest/v1/workspaces");
      AutoDeskRestServiceHandler var3 = new AutoDeskRestServiceHandler("GET", var2);
      AutoDeskCallResponse var4 = var3.execute(var0.getProxySettings(), var0.autodeskSession);
      var4.throwRuntimeExceptionOnBadResponse();
      return parseAutoDeskWorkspaceListResponse(var4.getHttpCallResponse().getResponse());
   }

   public static List<AutoDeskConnectorWorkspace> parseAutoDeskWorkspaceListResponse(String var0) {
      KongaSaxParser var1 = new KongaSaxParser();
      AutoDeskWorkspace.AutoDeskWorkspaceHandler var2 = new AutoDeskWorkspace.AutoDeskWorkspaceHandler();
      var1.registerHandler(var2);

      try {
         var1.parseString(var0);
      } catch (SAXException var4) {
         throw new ConnectorWebServiceRuntimeException(var4);
      }

      return var2.workspaces;
   }

   @RelativePathRoot("payload/list/data")
   private static class AutoDeskWorkspaceHandler {
      private String workspaceName;
      private String workspaceId;
      public final List<AutoDeskConnectorWorkspace> workspaces = Lists.newArrayList();

      private AutoDeskWorkspaceHandler() {
         this.init();
      }

      private void init() {
         this.workspaceName = this.workspaceId = null;
      }

      @EndElementHandler("data/label")
      public void workspaceName(String var1) {
         this.workspaceName = var1;
      }

      @EndElementHandler("data/id")
      public void workspaceid(String var1) {
         this.workspaceId = var1;
      }

      @EndElementHandler("data")
      public void endSchema() {
         this.workspaces.add(new AutoDeskConnectorWorkspace(this.workspaceId, this.workspaceName));
         this.init();
      }
   }
}
