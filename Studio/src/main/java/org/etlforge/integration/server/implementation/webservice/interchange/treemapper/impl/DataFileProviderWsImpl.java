package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.impl;

import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Stub;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.CppWebServiceServerCall;
import org.etlforge.integration.debug.client.file.DataFile;
import org.etlforge.integration.debug.client.file.DataFileIdentifier;
import org.etlforge.integration.debug.serverapi.DataFileProvider;
import org.etlforge.integration.debug.serverapi.DownloadDataFileCallback;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.TreeMapperInfoProvider;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.TreeMapperInfoProviderServiceLocator;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsDataFile;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsDataFileIdentifier;
import org.etlforge.util.id.KongaGuid;

public class DataFileProviderWsImpl extends CppWebServiceServerCall implements DataFileProvider {
   private static final String WS_NAME = "konga_treemapper_infoprovider";
   private TreeMapperInfoProviderServiceLocator locator;
   private String user;
   private String password;

   public DataFileProviderWsImpl(CppWebServiceConfiguration var1) {
      super(var1);
   }

   @Override
   public void downloadDataFile(KongaGuid var1, DataFileIdentifier var2, DownloadDataFileCallback var3) {
      if (this.init(var3)) {
         if (Thread.currentThread().isInterrupted()) {
            var3.cancelled();
         } else {
            try {
               TreeMapperInfoProvider var4 = this.locator.getkonga_treemapper_infoprovider();
               this.getConfiguration().setTimeOut((Stub)var4);
               WsDataFileIdentifier var5 = this.toWsIdentifier(var2);
               WsDataFile var6 = var4.downloadDataFile(
                  this.user, this.password, var1.toString(), toString(var2.transformationId), toString(var2.operationId), var5
               );
               if (Thread.currentThread().isInterrupted()) {
                  var3.cancelled();
                  return;
               }

               DataFile var7 = new DataFile(var6.getLocalPath(), var6.getCompressedData());
               var3.fileWasDownloaded(var2, var7);
            } catch (RemoteException var8) {
               var3.caught(this.convert(var8));
            } catch (ServiceException var9) {
               var3.caught(new IntegrationServerException("Failed to call webservice: " + var9.getMessage(), var9));
            }
         }
      }
   }

   private boolean init(ServerCallback var1) {
      this.locator = new TreeMapperInfoProviderServiceLocator();
      StringBuilder var2 = new StringBuilder();
      if (!this.getConfiguration().configureWebservice(this.locator, "konga_treemapper_infoprovider", var2, var1)) {
         return false;
      }

      this.locator.setkonga_treemapper_infoproviderEndpointAddress(var2.toString());
      this.user = this.getConfiguration().getUserName();
      this.password = this.getConfiguration().getPasswordHash();
      return true;
   }

   private WsDataFileIdentifier toWsIdentifier(DataFileIdentifier var1) {
      WsDataFileIdentifier var2 = new WsDataFileIdentifier();
      var2.setType(var1.type.ordinal());
      var2.setOriginalFileNumber(var1.instanceNumber);
      var2.setChunkNumber(var1.chunkNumber);
      return var2;
   }

   private static String toString(Object var0) {
      return var0 != null ? var0.toString() : null;
   }
}
