package org.etlforge.integration.server.engine.connector.autodesk.rest;

import com.google.common.base.Strings;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.server.engine.connector.exception.ConnectorEndpointRuntimeException;
import org.etlforge.integration.server.engine.connector.util.HttpCallResponse;
import org.etlforge.integration.server.engine.connector.util.HttpCallUrlUtils;
import org.etlforge.util.net.ProxySettings;

public class AutoDeskEndpoint {
   final String serviceBaseUrl;
   final String userID;
   final String password;
   final boolean sessionIsCached;
   AutoDeskSession autodeskSession;
   private final ProxySettings proxySettings;

   public AutoDeskEndpoint(ProxySettings var1, String var2, String var3, String var4, String var5) {
      this.proxySettings = var1;
      this.serviceBaseUrl = var2;
      this.userID = var3;
      this.password = var4;
      this.sessionIsCached = !Strings.isNullOrEmpty(var5);
      this.autodeskSession = this.sessionIsCached ? new AutoDeskSession(var5) : null;
   }

   public ProxySettings getProxySettings() {
      return this.proxySettings;
   }

   public void login() {
      if (!this.sessionIsCached) {
         this.loginImpl();
      }
   }

   public void loginImpl() {
      String var2 = HttpCallUrlUtils.getFullUrl(this.serviceBaseUrl, "rest/auth/1/login");
      String var3 = this.getLoginPostBody();
      AutoDeskRestServiceHandler var4 = new AutoDeskRestServiceHandler("POST", var2, var3);
      AutoDeskCallResponse var5 = var4.executeLogin(this.proxySettings);
      HttpCallResponse var6 = var5.getHttpCallResponse();
      if (var6.isBadResponse()) {
         throw new ConnectorEndpointRuntimeException(var6.getStatusLine());
      }

      this.autodeskSession = AutoDeskSession.newAutoDeskSessionFromXml(var6.getResponse());
      if (this.autodeskSession.authStatusId.startsWith("20")) {
         if (Strings.isNullOrEmpty(this.autodeskSession.getSessionid())) {
            throw new ConnectorEndpointRuntimeException("fatal error: no value for sessionid after login; Please report this to ETLForge");
         }

         if (Strings.isNullOrEmpty(var6.getCookie())) {
            throw new ConnectorEndpointRuntimeException("fatal error: no value for cookie after login; Please report this to ETLForge");
         }

         this.autodeskSession = new AutoDeskSession(var6.getCookie());
      } else {
         throw new ConnectorEndpointRuntimeException("fatal error: " + this.autodeskSession.description + '(' + this.autodeskSession.authStatusId + ')');
      }
   }

   public void logout() {
      if (!this.sessionIsCached) {
         this.logoutImpl();
      }
   }

   public void logoutImpl() {
      if (this.autodeskSession != null) {
         String var2 = HttpCallUrlUtils.getFullUrl(this.serviceBaseUrl, "rest/auth/1/logout");
         AutoDeskRestServiceHandler var3 = new AutoDeskRestServiceHandler("POST", var2, "");
         AutoDeskCallResponse var4 = var3.execute(this.proxySettings, this.autodeskSession);
      }
   }

   public String getUrl(String var1, String var2) {
      return this.getUrl(var1) + var2;
   }

   public String getUrl(String var1) {
      return HttpCallUrlUtils.getFullUrl(this.serviceBaseUrl, "api/rest/v1/workspaces/" + var1 + "/items");
   }

   private String getLoginPostBody() {
      StringBuilder var1 = new StringBuilder();
      XmlElementUtils.startElementTag(var1, "userAuthInfo");
      XmlElementUtils.elementValue(var1, "userID", this.userID);
      XmlElementUtils.elementValue(var1, "password", this.password);
      XmlElementUtils.endElementTag(var1, "userAuthInfo");
      return var1.toString();
   }
}
