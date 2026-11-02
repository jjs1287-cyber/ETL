package com.etlforge.integration.client.sfdc.data;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.etlforge.integration.salesforce.wsdl.SalesforceWsdlInfo;
import com.etlforge.integration.salesforce.wsdl.SalesforceWsdlInfoCache;
import java.io.InputStream;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.integration.salesforce.config.SalesforceConfig;

public final class ProjectSalesforceWsdlParser {
   private WebServiceCall wsCall;
   private final SalesforceConfig config;
   private Function<String, InputStream> localWsdlFileStore;
   private SalesforceWsdlInfoCache wsdlCache = SalesforceWsdlInfoCache.NONE;

   public ProjectSalesforceWsdlParser(SalesforceConfig var1) {
      Preconditions.checkNotNull(var1);
      this.config = var1;
   }

   public void setLocalWsdlFileStore(Function<String, InputStream> var1) {
      this.localWsdlFileStore = var1;
   }

   public void setWsdlCache(SalesforceWsdlInfoCache var1) {
      Preconditions.checkNotNull(var1);
      this.wsdlCache = var1;
   }

   public SalesforceWsdlInfo parseWsdl(SalesforceWizardEntity var1) {
      WizardItemBrowser var2 = new WizardItemBrowser(var1);
      WebServiceCall var3 = var2.getWebServiceCall();
      return var3 == null ? SalesforceWsdlInfo.NOT_SALESFORCE : this.parseWsdl(var3);
   }

   public SalesforceWsdlInfo parseWsdl(WebServiceCall var1) {
      this.wsCall = var1;
      WsdlFile var2 = var1.getWsdlFile();
      WsdlFileParser var3 = this.createWsdlParser();
      return var3.parse(var2);
   }

   public SalesforceWsdlInfo parseWsdl(String var1) {
      WsdlFileParser var2 = this.createWsdlParser();
      return var2.parse(new WsdlFile(var1));
   }

   private WsdlFileParser createWsdlParser() {
      Function var1 = this.getLocalWsdlFileStore();
      WsdlFileParser var2 = new WsdlFileParser(this.config, var1);
      var2.setCache(this.wsdlCache);
      return var2;
   }

   private Function<String, InputStream> getLocalWsdlFileStore() {
      if (this.localWsdlFileStore != null) {
         return this.localWsdlFileStore;
      }

      IntegrationProject var1 = ProjectUtils.getProject(this.wsCall);
      ManagedProject var2 = var1.getExtensionObject(ManagedProject.class);
      return new LocalWsdlFileReader(var2);
   }
}
