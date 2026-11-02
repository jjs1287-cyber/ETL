package org.etlforge.integration.server.engine.connector.autodesk.rest;

import com.google.common.base.Strings;
import java.io.File;
import java.io.IOException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorWebServiceRuntimeException;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.xml.sax.SAXException;

public class AutoDeskDeleteOperation {
   private String dmsID = null;
   private boolean isUndelete = false;

   public static void executeDeleteOperation(AutoDeskEndpoint var0, String var1, String var2, String var3) {
      AutoDeskDeleteOperation var4 = getAutoDeskDeleteOperation(var2);
      executeDeleteOperation(var0, var1, var4, var3);
   }

   public static void executeDeleteOperation(AutoDeskEndpoint var0, String var1, AutoDeskDeleteOperation var2, String var3) {
      AutoDeskCallResponse var4 = executeDeleteOperation(var0, var1, var2);
      String var5 = var2.isUndelete ? "Undelete" : "Delete";
      var4.saveDmsid(var3, var5, var1, var2.dmsID);
      var4.throwRuntimeExceptionOnUnauthorized();
   }

   public static AutoDeskCallResponse executeDeleteOperation(AutoDeskEndpoint var0, String var1, AutoDeskDeleteOperation var2) {
      if (Strings.isNullOrEmpty(var2.dmsID)) {
         return new AutoDeskCallResponse(false, "no dmsid");
      }

      String var3 = var2.isUndelete ? "PUT" : "DELETE";
      String var4 = var2.isUndelete ? "undeletions/" : "";
      String var5 = "/" + var4 + var2.dmsID;
      String var6 = var0.getUrl(var1, var5);
      AutoDeskRestServiceHandler var7 = new AutoDeskRestServiceHandler(var3, var6);
      return var7.execute(var0.getProxySettings(), var0.autodeskSession);
   }

   static AutoDeskDeleteOperation getAutoDeskDeleteOperation(String var0) {
      KongaSaxParser var1 = new KongaSaxParser();
      AutoDeskDeleteOperation.DeleteItemRequestHandler var2 = new AutoDeskDeleteOperation.DeleteItemRequestHandler();
      var1.registerHandler(var2);

      try {
         var1.parseFile(new File(var0));
         return var2.deleteOperation;
      } catch (SAXException var4) {
         throw new ConnectorWebServiceRuntimeException(var4);
      } catch (IOException var5) {
         throw new ConnectorWebServiceRuntimeException(var5);
      }
   }

   @RelativePathRoot("deleteitem")
   private static class DeleteItemRequestHandler {
      private final AutoDeskDeleteOperation deleteOperation = new AutoDeskDeleteOperation();

      private DeleteItemRequestHandler() {
      }

      @EndElementHandler("dmsID")
      public void dmsID(String var1) {
         this.deleteOperation.dmsID = var1.trim();
      }

      @EndElementHandler("undelete")
      public void undelete(Boolean var1) {
         this.deleteOperation.isUndelete = var1;
      }
   }
}
