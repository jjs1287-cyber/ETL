package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.impl;

import java.io.IOException;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Stub;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ETLForgeServerEncoding;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.common.server.ClientServerCommunicationUtils;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.TestTransformationResult;
import org.etlforge.integration.debug.client.file.SourceDataIdentifier;
import org.etlforge.integration.debug.serverapi.ServerTestCallback;
import org.etlforge.integration.debug.serverapi.TestTransformationProvider;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.DebuggingParameters;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.TreeMapperInfoProvider;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsTestTransformationResult;
import org.etlforge.util.id.KongaGuid;

public final class TestTransformationProviderWsImpl extends AbstractServerTestWsCall implements TestTransformationProvider {
   public TestTransformationProviderWsImpl(CppWebServiceConfiguration var1, DebugService var2) {
      super(var1, var2);
   }

   @Override
   public void resumeTest(DebugSession var1, ServerTestCallback var2) {
      this.testTransformation(var1, null, null, var2);
   }

   @Override
   public void testTransformation(DebugSession var1, SourceDataIdentifier var2, String var3, ServerTestCallback var4) {
      if (this.init(var1, var4)) {
         try {
            if (!this.isServerSourceDataTokenValid(var2, var4)) {
               return;
            }

            String var5 = var3 != null ? ETLForgeServerEncoding.deflateAndBase64EncodeString(var3) : null;
            TreeMapperInfoProvider var6 = this.getLocator().getkonga_treemapper_infoprovider();
            this.getConfiguration().setTimeOut((Stub)var6);
            DebuggingParameters var7 = WsDebuggingParametersBuilder.build(var1.getParams());
            WsTestTransformationResult var8 = var6.testTransformation(
               this.getUser(), this.getPassword(), var1.getDebuggeeId().toString(), var2 != null ? var2.getGuid().toString() : null, var5, var7
            );
            if (Thread.currentThread().isInterrupted()) {
               var4.cancelled();
               return;
            }

            this.decodeResultString(var8);
            KongaGuid var9 = var2 != null ? var2.getGuid() : var1.getSourceDataGuid();
            TestTransformationResult var10 = TestTransformationResultBuilder.build(
               var8, var1, var9, this.getServerId(), this.getServerName(), var1.getDebuggeeId(), getTargetNodePath(var1)
            );
            this.checkDebuggingResult(var1, var10, var4);
         } catch (RemoteException var11) {
            var4.caught(this.convert(var11));
         } catch (IOException var12) {
            var4.caught(var12);
         } catch (ServiceException var13) {
            var4.caught(new IntegrationServerException("Failed to call webservice: " + var13.getMessage(), var13));
         }
      }
   }

   private boolean isServerSourceDataTokenValid(SourceDataIdentifier var1, ServerCallback var2) {
      assert this.getServerId() != null : "isServerSourceDataTokenValid() should be called only if a successful connection was made to a ETLForge server.";
      if (var1 == null) {
         return true;
      } else if (!this.getServerId().equals(var1.getServerId())) {
         var2.caught(
            new IntegrationServerException(
               "The source data currently loaded into this Transformation comes from a different server than the one you are currently logged into. Please log in to the server \""
                  + var1.getServerName()
                  + "\" and try again!"
            )
         );
         return false;
      } else {
         return true;
      }
   }

   private void decodeResultString(WsTestTransformationResult var1) {
      String var2 = var1.getIntermediateResult();
      String var3 = ClientServerCommunicationUtils.decodeString(var2);
      var1.setIntermediateResult(var3);
   }
}
