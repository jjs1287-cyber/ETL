package com.etlforge.integration.client.ui.sfdc.query;

import com.etlforge.integration.client.sfdc.facade.FacadeFactory;
import com.etlforge.integration.client.ui.sfdc.AbstractSfdcWizardItemConfig;
import com.etlforge.integration.client.ui.sfdc.LinkType;
import com.etlforge.integration.client.ui.sfdc.page.soql.SoqlModel;
import com.etlforge.integration.client.ui.sfdc.query.page.SalesforceQueryPage;
import com.etlforge.integration.client.ui.sfdc.query.page.SalesforceQueryPageConfig;
import com.etlforge.integration.client.ui.sfdc.query.page.v4.SalesforceQueryPageConfigV4;
import com.etlforge.integration.client.ui.sfdc.query.page.v4.SalesforceQueryPageV4;
import java.util.List;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.config.SalesforceConfig;
import org.etlforge.integration.salesforce.parser.CompoundSupportedQueryPolicy;
import org.etlforge.integration.salesforce.parser.FixedObjectPolicy;
import org.etlforge.integration.salesforce.parser.ParsedQuery;
import org.etlforge.integration.salesforce.parser.SoqlParser;
import org.etlforge.integration.salesforce.parser.SoqlParserFactory;
import org.etlforge.integration.salesforce.parser.SupportedQueryPolicy;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public final class SalesforceQueryConfig extends AbstractSfdcWizardItemConfig {
   private final SupportedQueryPolicy supportedQueryPolicy;

   public SalesforceQueryConfig(FacadeFactory var1, EntityWizardFactory var2, SupportedQueryPolicy var3, List<LinkType> var4) {
      super(EntityType.SalesforceQuery, var1, var2, var4);
      this.supportedQueryPolicy = var3;
      this.setSearchAndReplaceMetaData(new QuerySearchAndReplaceProperties());
      this.setSearchValuesCollector(new QuerySearchValuesCollector());
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      SalesforceQuery var2 = (SalesforceQuery)var1;
      if (var2.isBasedOnPartnerWsdl()) {
         SoqlModel var3 = this.createSoqlModel(var2);
         return new SalesforceQueryPage(var2, var3);
      } else {
         return new SalesforceQueryPageV4(var2);
      }
   }

   private SoqlModel createSoqlModel(SalesforceQuery var1) {
      String var2 = var1.getObject().getLocalName();
      String var3 = var1.getQueryString();
      SoqlParser var4 = SoqlParserFactory.newParser();
      ParsedQuery var5 = var4.parse(var3);
      SoqlModel var6 = new SoqlModel(var2, var3, var5);
      CompoundSupportedQueryPolicy var7 = new CompoundSupportedQueryPolicy(this.supportedQueryPolicy, new FixedObjectPolicy(var2));
      var6.setSupportedQueryPolicy(var7);
      return var6;
   }

   @Override
   protected PageConfig createPageConfig(ApplicationWindow var1, IntegrationEntity var2, ProjectPersistor var3) {
      SalesforceQuery var4 = (SalesforceQuery)var2;
      if (var4.isBasedOnPartnerWsdl()) {
         SalesforceQueryPageConfig var6 = new SalesforceQueryPageConfig(var1, var3, this.getFacadeFactory());
         this.installDefaultCapabilitiesInPageConfig(var6);
         return var6;
      } else {
         SalesforceConfig var5 = SalesforceConfig.getInstance();
         return new SalesforceQueryPageConfigV4(
            var1, var3, var5.getMethodConfig(SfdcMethod.QUERY), this.getOperationRunnerFactory(), this.getOperationTesterFactory()
         );
      }
   }

   @Override
   public IntegrationEntityToolTip getToolTip(IntegrationEntity var1) {
      return new QueryToolTip((SalesforceQuery)var1);
   }
}
