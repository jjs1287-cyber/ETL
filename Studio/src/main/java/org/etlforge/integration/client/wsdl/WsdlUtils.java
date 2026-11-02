package org.etlforge.integration.client.wsdl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.etlforge.application.Application;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.wsdl.serverapi.WsdlProvider;
import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;
import org.etlforge.integration.filetransfer.serverapi.UploadServerFileCallback;

public final class WsdlUtils {
   public static WebServiceInfo parseRemoteWsdl(String var0) throws WsdlParserException, MalformedURLException {
      WebServiceInfoBuilder var1 = new WebServiceInfoBuilder();
      return var1.build(new URL(var0));
   }

   public static WebServiceInfo parseLocalWsdl(File var0) throws WsdlParserException {
      WebServiceInfoBuilder var1 = new WebServiceInfoBuilder();
      return var1.build(var0);
   }

   public static WebServiceInfo parseWsdlAsBytes(byte[] var0) throws WsdlParserException {
      WebServiceInfoBuilder var1 = new WebServiceInfoBuilder();
      return var1.build(var0);
   }

   public static void uploadRemoteWsdl(final String var0, final UploadServerFileCallback var1) {
      Runnable var2 = new Runnable() {
         @Override
         public void run() {
            WsdlUtils.uploadRemoteWsdlSynchronously(var0, var1);
         }
      };
      ApplicationWorker var3 = Application.getWorker();
      var3.submitForParallel(var2);
   }

   public static void uploadRemoteWsdlSynchronously(String var0, UploadServerFileCallback var1) {
      try {
         IntegrationServer var2 = IntegrationServer.getInstance();
         WsdlProvider var3 = var2.getServerCall(WsdlProvider.class);
         var3.uploadRemoteWsdl(var0, var1);
      } catch (Exception var4) {
         var1.caught(var4);
      }
   }

   private WsdlUtils() {
   }
}
