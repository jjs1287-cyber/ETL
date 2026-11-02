package com.etlforge.integration.client.ui.sfdc.bulkload.wizard;

import com.google.common.collect.ImmutableList;
import com.etlforge.integration.client.ui.sfdc.ObjectSelectionModel;
import com.etlforge.integration.client.ui.sfdc.QueryBuilderModelFactory;
import com.etlforge.integration.client.ui.sfdc.soql.test.QueryTester;
import com.etlforge.integration.client.ui.sfdc.wizard.TargetLocationPage;
import com.etlforge.integration.client.ui.sfdc.wizard.soql.QueryBuilderModel;
import com.etlforge.integration.client.ui.sfdc.wizard.soql.QueryBuilderPage;
import java.util.Arrays;
import org.etlforge.application.ui.window.editor.EditorPanel;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.salesforce.parser.SupportedQueryPolicy;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

final class BulkQueryPageChain implements ActionSpecificPageChain {
   private final BulkLoadWizard wizard;
   private final ObjectSelectionModel objectSelectionModel;
   private final ImmutableList<DataLocationType> fileTypes;
   private QueryBuilderModel queryBuilderModel;
   private QueryBuilderPage queryBuilderPage;
   private TargetLocationPage targetPage;

   public BulkQueryPageChain(BulkLoadWizard var1, ObjectSelectionModel var2, Iterable<DataLocationType> var3) {
      this.wizard = var1;
      this.objectSelectionModel = var2;
      this.fileTypes = ImmutableList.copyOf(var3);
   }

   @Override
   public void applyToWizard(Schedule var1) {
      Target var2 = this.targetPage.getLocation();
      String var3 = this.queryBuilderModel.getQueryString();
      boolean var4 = this.targetPage.isUTF8();
      QueryOptions var5 = new QueryOptions(var1, var4);
      this.wizard.configureQuery(var3, var2, var5);
   }

   @Override
   public WizardPage moveToFirstPage() {
      SObjectSchema var1 = this.objectSelectionModel.getSelectedObject();
      if (this.queryBuilderPage == null) {
         this.queryBuilderModel = QueryBuilderModelFactory.newModel(var1);
         this.queryBuilderPage = this.createQueryBuilderPage();
      } else {
         this.queryBuilderModel.setSObject(var1);
      }

      this.setSection("Query");
      return this.queryBuilderPage;
   }

   private QueryBuilderPage createQueryBuilderPage() {
      SupportedQueryPolicy var1 = this.wizard.getSupportedQueryPolicy();
      QueryBuilderPage var2 = new QueryBuilderPage(this.wizard, this.queryBuilderModel, var1);
      QueryTester var3 = this.wizard.createQueryTester();
      var2.setQueryTester(var3);
      var2.disableQueryAll("This option is not available when using the Bulk API");
      return var2;
   }

   private void setSection(String var1) {
      this.wizard.getDisplayer().setSection(var1);
   }

   @Override
   public WizardPage getLastPage() {
      return this.targetPage;
   }

   @Override
   public WizardPage moveToLastPage() {
      return this.moveToTargetPage();
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      return var1 == this.queryBuilderPage ? this.moveToTargetPage() : null;
   }

   private WizardPage moveToTargetPage() {
      if (this.targetPage == null) {
         this.targetPage = new TargetLocationPage(this.wizard, this.fileTypes, new EditorPanel(this.wizard.getWindow()));
         this.targetPage.setFileExtensions(Arrays.asList(".csv", ".txt"));
      }

      this.setSection("Target");
      return this.targetPage;
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      if (var1 == this.targetPage) {
         this.setSection("Query");
         return this.queryBuilderPage;
      } else {
         return null;
      }
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return var1 == this.targetPage;
   }
}
