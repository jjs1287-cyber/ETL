package org.etlforge.integration.client.ui.interchange.entity.webservice.edit.wsdl;

import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.filestore.FileStoreFile;
import org.etlforge.integration.client.wsdl.WebServiceInfoBuilder;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;

final class NewWsdlDataApplier {
   private final EntityPersistor persistor;
   private final WebServiceInfo oldWsInfo;

   public NewWsdlDataApplier(EntityPersistor var1, FileStore var2, WsdlFile var3) {
      this.persistor = var1;
      this.oldWsInfo = this.loadOldWsInfo(var2, var3);
   }

   private WebServiceInfo loadOldWsInfo(FileStore var1, WsdlFile var2) {
      try {
         FileStoreFile var3 = var1.getFile(var2.getLocator());
         if (var3 != null) {
            return new WebServiceInfoBuilder().build(var3.getPhysicalRepresentation());
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return null;
   }

   public void apply(WebServiceCall var1, WsdlFile var2, WebServiceOperationInfo var3) {
      NewWsdlDataApplier.ReplacePolicy var4 = this.getReplacePolicy(var1);

      try {
         var4.doReplace(var1, var2, var3);
         this.persistor.save(var1);
      } catch (InterchangeSavingException var6) {
         var6.printStackTrace();
      }
   }

   private NewWsdlDataApplier.ReplacePolicy getReplacePolicy(WebServiceCall var1) {
      boolean var2 = true;
      boolean var3 = true;
      if (this.oldWsInfo != null) {
         var2 = var1.getUrl().equals(this.oldWsInfo.getWebServiceURL());
         WebServiceOperationInfo var4 = OpInfoFinder.findMatchingOpInfo(var1, this.oldWsInfo);
         if (var4 != null) {
            var3 = var1.getProperty("soapaction", "").equals(var4.getProperty("SoapAction"));
         }
      }

      return new NewWsdlDataApplier.ReplacePolicy(var2, var3);
   }

   private static final class ReplacePolicy {
      private final boolean replaceUrl;
      private final boolean replaceSoapAction;

      public ReplacePolicy(boolean var1, boolean var2) {
         this.replaceUrl = var1;
         this.replaceSoapAction = var2;
      }

      public void doReplace(WebServiceCall var1, WsdlFile var2, WebServiceOperationInfo var3) {
         var1.setWsdlFile(var2);
         if (this.replaceUrl) {
            var1.setUrl(var3.getProperty("WebService_Url"));
         }

         String var4 = var1.getProperty("soapaction", "");
         var1.setOperationInfo(var3);
         if (!this.replaceSoapAction) {
            var1.setStringProperty("soapaction", var4);
         }
      }
   }
}
