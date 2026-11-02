package com.etlforge.integration.client.ui.sfdc.bulkload.wizard;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.etlforge.integration.client.ui.sfdc.ObjectSelectionModel;
import com.etlforge.integration.client.ui.sfdc.bulkload.mapping.HeaderMappingsController;
import com.etlforge.integration.client.ui.sfdc.bulkload.mapping.HeaderMappingsModel;
import com.etlforge.integration.client.ui.sfdc.bulkload.mapping.HeaderMappingsUi;
import com.etlforge.integration.client.ui.sfdc.wizard.LocationPageModel;
import com.etlforge.integration.client.ui.sfdc.wizard.SourceLocationPage;
import com.etlforge.integration.client.ui.sfdc.wizard.file.SampleFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.etlforge.application.ui.window.editor.EditorPanel;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.salesforce.bulkload.BulkAction;
import org.etlforge.integration.salesforce.bulkload.FileSplitterParams;
import org.etlforge.integration.salesforce.bulkload.HeaderMappings;
import org.etlforge.integration.salesforce.bulkload.TargetFields;
import org.etlforge.integration.salesforce.wsdl.RelatedObject;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.util.csv.CSVParams;

final class BulkLoadPageChain implements ActionSpecificPageChain {
   private final BulkLoadWizard wizard;
   private final ObjectSelectionModel objectSelectionModel;
   private final ImmutableList<DataLocationType> fileTypes;
   private BulkAction action;
   private SourceLocationPage sourceLocationPage;
   private SampleFilePage sampleFilePage;
   private HeaderMappingPage headerMappingPage;
   private List<String> sourceHeader;
   private HeaderMappingsModel headerMappingModel;

   public BulkLoadPageChain(BulkLoadWizard var1, ObjectSelectionModel var2, Iterable<DataLocationType> var3) {
      this.wizard = var1;
      this.objectSelectionModel = var2;
      this.fileTypes = ImmutableList.copyOf(var3);
   }

   public void setAction(BulkAction var1) {
      this.action = var1;
   }

   @Override
   public WizardPage moveToFirstPage() {
      return this.moveToSourceFilePage();
   }

   private WizardPage moveToSourceFilePage() {
      this.setSection("Source");
      if (this.sourceLocationPage == null) {
         this.sourceLocationPage = new SourceLocationPage(this.wizard, this.fileTypes, new EditorPanel(this.wizard.getWindow()));
         this.sourceLocationPage.setFileExtensions(Arrays.asList(".csv", ".txt"));
         LocationPageModel var1 = this.sourceLocationPage.getModel();
         var1.setCsvParams(CSVParams.DEFAULT);
      }

      return this.sourceLocationPage;
   }

   private void setSection(String var1) {
      this.wizard.getDisplayer().setSection(var1);
   }

   @Override
   public WizardPage getLastPage() {
      return this.headerMappingPage;
   }

   @Override
   public WizardPage moveToLastPage() {
      this.setSection("Mappings");
      return this.headerMappingPage;
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      if (var1 == this.sourceLocationPage) {
         Optional var3 = this.sourceLocationPage.getSampleFile();
         return var3.isPresent() ? this.moveToHeaderMappingPage((SampleFile)var3.get()) : this.moveToSampleFilePage();
      } else if (var1 == this.sampleFilePage) {
         SampleFile var2 = this.sampleFilePage.getSampleFile();
         return this.moveToHeaderMappingPage(var2);
      } else {
         return null;
      }
   }

   private WizardPage moveToHeaderMappingPage(SampleFile var1) {
      if (this.headerMappingPage == null) {
         this.headerMappingModel = new HeaderMappingsModel(this.action);
         HeaderMappingsController var2 = new HeaderMappingsController(this.headerMappingModel);
         HeaderMappingsUi var3 = new HeaderMappingsUi(this.headerMappingModel, var2);
         this.headerMappingPage = new HeaderMappingPage(this.wizard, var3);
      }

      this.sourceHeader = var1.getHeader();
      TargetFields var4 = this.getTargetFieldsForHeaderMappingsPage();
      this.headerMappingModel.updateAndRetainMappings(this.action, this.sourceHeader, var4);
      this.headerMappingPage.setSampleFile(var1);
      this.setSection("Mappings");
      return this.headerMappingPage;
   }

   private TargetFields getTargetFieldsForHeaderMappingsPage() {
      if (this.action.requiresIdOnly()) {
         return TargetFields.forDelete();
      }

      SObjectSchema var1 = this.objectSelectionModel.getSelectedObject();
      List var2 = this.getRelatedObjects();
      return new TargetFields(var1.getFields(), var2);
   }

   private List<RelatedObject> getRelatedObjects() {
      return this.action.supportsForeignKeys() ? this.objectSelectionModel.getRelatedObjects() : Collections.emptyList();
   }

   private WizardPage moveToSampleFilePage() {
      if (this.sampleFilePage == null) {
         this.sampleFilePage = new SampleFilePage(this.wizard);
      }

      return this.sampleFilePage;
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      if (var1 == this.headerMappingPage) {
         this.setSection("Source");
         return this.sourceLocationPage.getSampleFile() != null ? this.sourceLocationPage : this.sampleFilePage;
      } else {
         return var1 == this.sampleFilePage ? this.sourceLocationPage : null;
      }
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return var1 == this.headerMappingPage;
   }

   @Override
   public void applyToWizard(Schedule var1) {
      Source var2 = this.sourceLocationPage.getLocation();
      LoadOptions var3 = this.getOptions(var1);
      HeaderMappings var4 = this.headerMappingPage.getHeaderMappings();
      FileSplitterParams var5 = var3.applyTo(FileSplitterParams.defaultParams(var4));
      this.wizard.configureLoad(var2, var5, var3);
   }

   private LoadOptions getOptions(Schedule var1) {
      boolean var2 = this.sourceLocationPage.isUTF8();
      return LoadOptions.defaultOptions(var1, var2);
   }
}
