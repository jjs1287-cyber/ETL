package com.etlforge.integration.client.ui.sfdc.wizard.soql;

import com.google.common.base.Optional;
import com.etlforge.integration.client.ui.sfdc.soql.test.QueryTester;
import com.etlforge.integration.client.ui.sfdc.wizard.soql.relational.RelationalFieldSelector;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.salesforce.parser.CaseCorrector;
import org.etlforge.integration.salesforce.parser.CompoundSupportedQueryPolicy;
import org.etlforge.integration.salesforce.parser.FixedObjectPolicy;
import org.etlforge.integration.salesforce.parser.ParsedQuery;
import org.etlforge.integration.salesforce.parser.SoqlErrorReporter;
import org.etlforge.integration.salesforce.parser.SupportedQueryPolicy;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.util.KeyUtils;

public final class QueryBuilderPage extends AbstractWizardPage {
   private final QueryBuilderModel model;
   private final SupportedQueryPolicy supportedQueryPolicy;
   private final QueryBuilder ui;
   private volatile ParsedQuery parsedQuery;
   private Optional<SObjectSchemaProvider> schemaProvider = Optional.absent();

   public QueryBuilderPage(Wizard var1, QueryBuilderModel var2, SupportedQueryPolicy var3) {
      super(var1, "Salesforce Query");
      this.model = var2;
      this.supportedQueryPolicy = var3;
      this.ui = new QueryBuilder(var2);
      this.bindCanContinue();
   }

   private void bindCanContinue() {
      QueryBuilderPage.BlankQueryStringBinding var1 = new QueryBuilderPage.BlankQueryStringBinding(this.model);
      var1.syncUi();
   }

   public void disableQueryAll(String var1) {
      this.ui.disableQueryAll(var1);
   }

   public void setQueryTester(QueryTester var1) {
      var1.setWaitService(this.getWaitService());
      this.ui.setQueryTester(var1);
   }

   public void setRelationalFieldSelector(RelationalFieldSelector var1) {
      this.ui.setRelationalFieldSelector(var1);
   }

   public void setSchemaProviderForQueryCorrection(SObjectSchemaProvider var1) {
      this.schemaProvider = Optional.of(var1);
   }

   public ParsedQuery getParsedQuery() {
      return this.parsedQuery;
   }

   @Override
   public boolean prepareForForwardNavigation() {
      InputtedQueryStringProcessor var1 = this.createSoqlProcessor();
      String var2 = this.model.getQueryString();
      this.parsedQuery = var1.process(var2);
      return this.parsedQuery.isValid();
   }

   private InputtedQueryStringProcessor createSoqlProcessor() {
      CompoundSupportedQueryPolicy var1 = new CompoundSupportedQueryPolicy(this.supportedQueryPolicy, new FixedObjectPolicy(this.model.getSObject().getName()));
      InputtedQueryStringProcessor var2 = new InputtedQueryStringProcessor(var1);
      var2.setErrorReporter(new SoqlErrorReporter() {
         @Override
         public void reportError(String var1, String var2x) {
            NonBlockingAlert.error(QueryBuilderPage.this.getMainPanel(), var2x, var1);
         }
      });
      if (this.schemaProvider.isPresent()) {
         CaseCorrector var3 = new CaseCorrector(this.model.getSObject().getName(), this.schemaProvider.get());
         var2.correctForCase(var3);
      }

      return var2;
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout("Build Your Salesforce Query");
      var1.add(this.ui, true, true);
      JPanel var2 = var1.asCenterBorderLayout();
      ActionKeyBinding.install(new QueryBuilderPage.QuickSoqlAction(), var2, 1);
      return var2;
   }

   private class BlankQueryStringBinding extends AbstractBinding<String> {
      public BlankQueryStringBinding(QueryBuilderModel var2) {
         super(var2, "soql");
      }

      protected void updateUiWithValue(String var1) {
         QueryBuilderPage.this.setCanContinue(StringUtils.isNotBlank(var1));
      }
   }

   private class QuickSoqlAction extends AbstractAction {
      public QuickSoqlAction() {
         this.putValue("AcceleratorKey", KeyUtils.getCtrlDown(48));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         QueryBuilderPage.this.model.setQueryString("SELECT Id, Name FROM Account");
      }
   }
}
