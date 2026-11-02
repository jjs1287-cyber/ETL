package org.etlforge.integration.server.engine.connector.autodesk.rest;

import com.google.common.base.Strings;
import org.etlforge.integration.server.engine.connector.exception.ConnectorEndpointRuntimeException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorWebServiceRuntimeException;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.xml.sax.SAXException;

public class AutoDeskSession {
   private String sessionid;
   String customerToken;
   String userid;
   String description;
   String authStatusId;
   private final String cookie;

   String toCookie() {
      return this.getCookie();
   }

   public AutoDeskSession() {
      this.cookie = null;
   }

   public AutoDeskSession(String var1) {
      this.cookie = var1;
   }

   public static AutoDeskSession newAutoDeskSessionFromXml(String var0) {
      if (Strings.isNullOrEmpty(var0)) {
         throw new ConnectorEndpointRuntimeException("fatal error: no value for sessionid");
      }

      KongaSaxParser var1 = new KongaSaxParser();
      AutoDeskSession.AutoDeskSessionHandler var2 = new AutoDeskSession.AutoDeskSessionHandler();
      var1.registerHandler(var2);

      try {
         var1.parseString(var0);
      } catch (SAXException var4) {
         throw new ConnectorWebServiceRuntimeException(var4);
      }

      return var2.autodeskSession;
   }

   public String getCookie() {
      return this.cookie;
   }

   public String getSessionid() {
      return this.sessionid;
   }

   @RelativePathRoot("userAuthInfo")
   private static class AutoDeskSessionHandler {
      public final AutoDeskSession autodeskSession = new AutoDeskSession();

      private AutoDeskSessionHandler() {
      }

      @EndElementHandler("userid")
      public void userid(String var1) {
         this.autodeskSession.userid = var1;
      }

      @EndElementHandler("sessionid")
      public void sessionid(String var1) {
         this.autodeskSession.sessionid = var1;
      }

      @EndElementHandler("customerToken")
      public void customerToken(String var1) {
         this.autodeskSession.customerToken = var1;
      }

      @EndElementHandler("authStatus/description")
      public void description(String var1) {
         this.autodeskSession.description = var1;
      }

      @EndElementHandler("authStatus/id")
      public void authStatusId(String var1) {
         this.autodeskSession.authStatusId = var1;
      }
   }
}
