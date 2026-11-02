package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.impl;

import java.rmi.RemoteException;
import java.util.zip.DataFormatException;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Stub;
import org.etlforge.integration.client.mapping.serverapi.GetMappingStructureCallback;
import org.etlforge.integration.client.mapping.serverapi.transformation.TreeMapperInfoProvider;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ETLForgeServerEncoding;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.CppWebServiceServerCall;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.data.entity.id.WebServiceCallId;
import org.etlforge.integration.data.entity.webservicecall.WebServiceOperationIdentifier;
import org.etlforge.integration.loadsource.serverapi.LoadSourceDataCallback;
import org.etlforge.integration.loadsource.serverapi.LoadSourceDataInput;
import org.etlforge.integration.loadsource.serverapi.LoadSourceDataProvider;
import org.etlforge.integration.loadsource.serverapi.SourceDataEncodingException;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.PipelinePositionStruct;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.TreeMapperInfoProviderServiceLocator;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WebServiceMethodData;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsLoadSourceDataInput;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsLoadSourceResult;
import org.etlforge.integration.structure.mapping.MappingTreeStructureFactory;

public final class TreeMapperInfoProviderWsImpl extends CppWebServiceServerCall implements TreeMapperInfoProvider, LoadSourceDataProvider {
   private static final String WS_NAME = "konga_treemapper_infoprovider";
   private TreeMapperInfoProviderServiceLocator locator;
   private String user;
   private String password;
   private ServerGuid serverId;
   private String serverName;

   public TreeMapperInfoProviderWsImpl(CppWebServiceConfiguration var1) {
      super(var1);
   }

   @Override
   public void getJtrForStructure(String var1, String var2, boolean var3, GetMappingStructureCallback var4) {
      if (this.init(var4)) {
         try {
            String var5 = this.getJtrForStructureImpl(var1, var2, var3);
            String var6 = ETLForgeServerEncoding.base64DecodeAndInflateString(var5);
            Object var10 = null;
            var4.jtrGenerated(MappingTreeStructureFactory.fromString(var6));
         } catch (DataFormatException var7) {
            var4.caught(var7);
         } catch (RemoteException var8) {
            var4.caught(this.convert(var8));
         } catch (ServiceException var9) {
            var4.caught(new IntegrationServerException("Failed to call webservice: " + var9.getMessage(), var9));
         }
      }
   }

   private String getJtrForStructureImpl(String var1, String var2, boolean var3) throws RemoteException, ServiceException {
      org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.TreeMapperInfoProvider var4 = this.locator
         .getkonga_treemapper_infoprovider();
      this.getConfiguration().setTimeOut((Stub)var4);
      return var4.getJtrForStructureFile(this.user, this.password, var1, var2, var3);
   }

   @Override
   public void getJtrForCombinedRequestAndResponse(String var1, String var2, String var3, String var4, boolean var5, GetMappingStructureCallback var6) {
      if (this.init(var6)) {
         try {
            String var7 = this.getJtrForCombinedRequestAndResponseImpl(var1, var2, var3, var4, var5);
            String var8 = ETLForgeServerEncoding.base64DecodeAndInflateString(var7);
            Object var12 = null;
            var6.jtrGenerated(MappingTreeStructureFactory.fromString(var8));
         } catch (DataFormatException var9) {
            var6.caught(var9);
         } catch (RemoteException var10) {
            var6.caught(this.convert(var10));
         } catch (ServiceException var11) {
            var6.caught(new IntegrationServerException("Failed to call webservice: " + var11.getMessage(), var11));
         }
      }
   }

   private String getJtrForCombinedRequestAndResponseImpl(String var1, String var2, String var3, String var4, boolean var5) throws RemoteException, ServiceException {
      org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.TreeMapperInfoProvider var6 = this.locator
         .getkonga_treemapper_infoprovider();
      this.getConfiguration().setTimeOut((Stub)var6);
      return var6.getJtrForCombinedRequestAndResponse(this.user, this.password, var1, var2, var3, var4, var5);
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
      this.serverId = this.getConfiguration().getServerId();
      this.serverName = this.getConfiguration().getServerName();
      return true;
   }

   private String getJtrForWSImpl(
      WebServiceCallId var1, String var2, String var3, WebServiceOperationIdentifier var4, String var5, String var6, boolean var7, boolean var8
   ) throws RemoteException, ServiceException {
      org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.TreeMapperInfoProvider var9 = this.locator
         .getkonga_treemapper_infoprovider();
      this.getConfiguration().setTimeOut((Stub)var9);
      WebServiceMethodData var10 = new WebServiceMethodData();
      var10.setBinding(var3);
      var10.setIsTarget(var7);
      var10.setIsWSRequest(var8);
      var10.setOpInput(var4.getInputName());
      var10.setOpName(var4.getOperationName());
      var10.setOpOutput(var4.getOutputName());
      var10.setOpStyle(var5);
      var10.setSoapBodyUse(var6);
      var10.setWsdlGuid(var2);
      var10.setWsGuid(var1.toString());
      return var9.getJtrForWS(this.user, this.password, var10);
   }

   @Override
   public void getJtrForWS(
      WebServiceCallId var1,
      String var2,
      String var3,
      WebServiceOperationIdentifier var4,
      String var5,
      String var6,
      boolean var7,
      boolean var8,
      GetMappingStructureCallback var9
   ) {
      if (this.init(var9)) {
         try {
            String var10 = this.getJtrForWSImpl(var1, var2, var3, var4, var5, var6, var7, var8);
            String var11 = ETLForgeServerEncoding.base64DecodeAndInflateString(var10);
            Object var15 = null;
            var9.jtrGenerated(MappingTreeStructureFactory.fromString(var11));
         } catch (RemoteException var12) {
            var9.caught(this.convert(var12));
         } catch (DataFormatException var13) {
            var9.caught(var13);
         } catch (ServiceException var14) {
            var9.caught(new IntegrationServerException("Failed to call webservice: " + var14.getMessage(), var14));
         }
      }
   }

   @Override
   public void loadSourceDataTree(LoadSourceDataInput var1, TransformationId var2, LoadSourceDataCallback var3) {
      if (this.init(var3)) {
         PipelinePosition[] var4 = var1.getPlugins();
         PipelinePositionStruct[] var5 = null;
         if (var4 != null && var4.length > 0) {
            var5 = new PipelinePositionStruct[var4.length];
            int var6 = 0;

            for (PipelinePosition var10 : var4) {
               PipelinePositionStruct var11 = new PipelinePositionStruct();
               var11.setEntityType(var10.getEntityType().getId());
               var11.setExecutionOrder(var10.getExecutionOrder());
               var11.setPluginGuid(var10.getPluginId().getName());
               var11.setRelativePosition(var10.getRelativePosition().toString());
               var5[var6++] = var11;
            }
         }

         try {
            org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.TreeMapperInfoProvider var15 = this.locator
               .getkonga_treemapper_infoprovider();
            this.getConfiguration().setTimeOut((Stub)var15);
            WsLoadSourceDataInput var16 = new WsLoadSourceDataInput();
            var16.setCompressedBase64EncodedData(var1.getCompressedBase64EncodedData());
            var16.setPipelinePlugins(var5);
            var16.setTransformationGuid(var2.toString());
            WsLoadSourceResult var17 = var15.loadSourceData(this.user, this.password, var16);
            if (Thread.currentThread().isInterrupted()) {
               var3.cancelled();
               return;
            }

            LoadSourceDataResultImpl var18 = new LoadSourceDataResultImpl(var17, this.serverId, this.serverName, var2);
            var3.sourceDataTreeGenerated(var18);
         } catch (SourceDataEncodingException var12) {
            var3.caught(var12);
         } catch (RemoteException var13) {
            var3.caught(this.convert(var13));
         } catch (ServiceException var14) {
            var3.caught(new IntegrationServerException("Failed to call webservice: " + var14.getMessage(), var14));
         }
      }
   }
}
