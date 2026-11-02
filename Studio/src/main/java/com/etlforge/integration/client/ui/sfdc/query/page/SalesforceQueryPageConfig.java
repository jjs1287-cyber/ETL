package com.etlforge.integration.client.ui.sfdc.query.page;

import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.etlforge.integration.client.sfdc.facade.FacadeFactory;
import com.etlforge.integration.client.sfdc.facade.SalesforceFacade;
import com.etlforge.integration.client.ui.sfdc.SoqlEditorFactory;
import com.etlforge.integration.client.ui.sfdc.page.AbstractSalesforceItemPageConfig;
import com.etlforge.integration.client.ui.sfdc.page.SoapHeaderEditor;
import com.etlforge.integration.client.ui.sfdc.page.soql.EditSoqlController;
import com.etlforge.integration.client.ui.sfdc.page.soql.SoqlEditor;
import com.etlforge.integration.client.ui.sfdc.soql.test.QueryTester;
import com.etlforge.integration.client.ui.sfdc.soql.test.QueryTesterFactory;
import java.util.Arrays;
import java.util.List;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequence;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.salesforce.query.QueryRunner;
import org.etlforge.integration.salesforce.soap.header.SessionHeader;
import org.etlforge.integration.salesforce.soap.ui.SoapHeaderUiFactory;

public final class SalesforceQueryPageConfig extends AbstractSalesforceItemPageConfig<SalesforceQueryPage> {
   private final FacadeFactory sfdcFacadeFactory;

   public SalesforceQueryPageConfig(ApplicationWindow var1, ProjectPersistor var2, FacadeFactory var3) {
      super(var1, var2);
      this.sfdcFacadeFactory = var3;
   }

   protected void configureSoapHeaderUiFactory(SalesforceQueryPage var1, SoapHeaderUiFactory var2) {
      SalesforceQuery var3 = var1.getObject();
      if (!var3.isUsingBuiltInRequest()) {
         var2.addFilter(Predicates.instanceOf(SessionHeader.class));
      }
   }

   protected Optional<? extends SaveConsequence> createRequestMappingsConsequence(SalesforceQueryPage var1, SoapHeaderEditor var2, EntityPersistor var3) {
      return Optional.of(new RequestMappingsConsequence(var1, var2, var3));
   }

   protected List<? extends SaveConsequence> createAdditionalSaveConsequences(SalesforceQueryPage var1, EntityPersistor var2) {
      SoqlChangeConsequence var3 = new SoqlChangeConsequence(var1.getObject(), var1.getSoqlModel());
      var3.setWaitService(var1.getWaitService());
      return Arrays.asList(new QueryAllConsequence(var1, var2), var3);
   }

   protected void doAdditionalConfig(SalesforceQueryPage var1) {
      if (this.isSoqlEditorSupported(var1)) {
         this.installSoqlEditor(var1);
      }
   }

   private boolean isSoqlEditorSupported(SalesforceQueryPage var1) {
      SalesforceQuery var2 = var1.getObject();
      return var2.isUsingBuiltInRequest();
   }

   private void installSoqlEditor(SalesforceQueryPage var1) {
      SoqlEditor var2 = this.createSoqlEditor(var1);
      EditSoqlController var3 = new EditSoqlController(var1.getSoqlModel(), var2, this.sfdcFacadeFactory.newFacade(), var1.getConnectorSupplier());
      var1.setSoqlEditor(var2, var3);
   }

   private SoqlEditor createSoqlEditor(SalesforceQueryPage var1) {
      SalesforceFacade var2 = this.sfdcFacadeFactory.newFacade();
      SoqlEditor var3 = SoqlEditorFactory.newEditor(var2);
      QueryRunner var4 = new QueryRunner(var2.getConnectionFactory(), var1.getOrgIdSupplier());
      QueryTesterFactory var5 = new QueryTesterFactory(ApplicationUi.getAppWinFrame());
      QueryTester var6 = var5.newTester(var4, var1.getSoqlModel().getSupportedQueryPolicy());
      var3.setQueryTester(var6);
      return var3;
   }
}
