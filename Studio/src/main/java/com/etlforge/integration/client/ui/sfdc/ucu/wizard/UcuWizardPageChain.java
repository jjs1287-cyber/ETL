package com.etlforge.integration.client.ui.sfdc.ucu.wizard;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.etlforge.integration.client.sfdc.data.SoapWizardWorkflow;
import com.etlforge.integration.client.ui.sfdc.ObjectSelectionController;
import com.etlforge.integration.client.ui.sfdc.ObjectSelectionModel;
import com.etlforge.integration.client.ui.sfdc.wizard.AbstractSalesforceWizardPageChain;
import com.etlforge.integration.client.ui.sfdc.wizard.ModifierResponseOption;
import com.etlforge.integration.client.ui.sfdc.wizard.ObjectSelectionPage;
import com.etlforge.integration.client.ui.sfdc.wizard.RequestSource;
import com.etlforge.integration.client.ui.sfdc.wizard.RequestSourceConfig;
import com.etlforge.integration.client.ui.sfdc.wizard.RequestSourceType;
import com.etlforge.integration.client.ui.sfdc.wizard.SourceLocationPage;
import com.etlforge.integration.client.ui.sfdc.wizard.SourceStructureTypePage;
import com.etlforge.integration.client.ui.sfdc.wizard.WizardOption;
import java.util.List;
import java.util.Map;
import org.etlforge.application.ui.window.editor.EditorPanel;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.sfdc.AbstractSalesforceUCU;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.ui.UiThreadSupplier;

final class UcuWizardPageChain<T extends AbstractSalesforceUCU> extends AbstractSalesforceWizardPageChain<T> {
   private final RequestSourceConfig sourceConfig;
   private final ObjectSelectionModel objectSelectionModel;
   private ObjectSelectionPage objectSelectionPage;
   private SourceLocationPage sourceLocationPage;
   private DataLocationType sourceLocationType;
   private SourceStructureTypePage sourceStructureTypePage;
   private RequestSourceType sourceStructureType;
   private WizardPageChain sourceChain;
   private final Map<UcuWizardPageChain.ExistingSourceChainsKey, WizardPageChain> sourceChains = Maps.newHashMap();
   private ModifierResponseOption modifierResponseOption;
   private SoapWizardWorkflow<T> objects;

   public UcuWizardPageChain(UcuSalesforceWizard<T> var1, ObjectSelectionModel var2, RequestSourceConfig var3, boolean var4) {
      super(var1, var4);
      this.objectSelectionModel = var2;
      this.sourceConfig = var3;
      UcuAutoMapperConfig var5 = new UcuAutoMapperConfig(var1.getCapitalizedMethodName());
      this.setAutoMapperConfig(var5);
   }

   protected UcuSalesforceWizard<T> getWizard() {
      return (UcuSalesforceWizard<T>)super.getWizard();
   }

   @Override
   protected List<? extends WizardOption> createOptionsForOptionsPage() {
      this.modifierResponseOption = new ModifierResponseOption();
      return Lists.newArrayList(this.modifierResponseOption);
   }

   @Override
   protected boolean hasWizardOptions() {
      return true;
   }

   @Override
   protected boolean isLastRequiredPage(WizardPage var1) {
      return this.sourceChain != null && this.sourceChain.canFinish(var1);
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      if (var1 == this.getLoginPage()) {
         return this.moveToObjectSelectionPage();
      }

      if (var1 == this.objectSelectionPage) {
         return this.moveToSourceLocationPage();
      }

      if (var1 == this.sourceLocationPage) {
         WizardPage var3 = this.moveToPageAfterSourceLocationPage();
         if (var3 == null) {
            var3 = this.moveToScheduleAndOptionsPage();
         }

         return var3;
      } else {
         if (var1 == this.sourceStructureTypePage) {
            this.sourceStructureType = this.sourceStructureTypePage.getSelectedValue();
            return this.moveIntoSourceChain();
         }

         if (var1 == this.getScheduleAndOptionsPage()) {
            this.objects = this.createObjectSummary();
            return this.moveToSummaryPage(this.objects);
         }

         if (var1 == this.getSummaryPage()) {
            return null;
         }

         WizardPage var2 = this.sourceChain.getNextPage(var1);
         return var2 != null ? var2 : this.moveToScheduleAndOptionsPage();
      }
   }

   private WizardPage moveToObjectSelectionPage() {
      if (this.objectSelectionPage == null) {
         ObjectSelectionController var1 = this.getWizard().getObjectSelectionController(this.objectSelectionModel);
         this.objectSelectionPage = new ObjectSelectionPage(this.getWizard(), this.objectSelectionModel, var1);
      }

      this.setSection("Object");
      return this.objectSelectionPage;
   }

   private WizardPage moveToSourceLocationPage() {
      if (this.sourceLocationPage == null) {
         this.sourceLocationPage = new SourceLocationPage(this.getWizard(), this.sourceConfig.getLocationTypes(), new EditorPanel(this.getWizard().getWindow()));
         this.sourceLocationPage.setFileExtensions(this.sourceConfig.getFileExtensions());
      }

      SObjectSchema var1 = this.objectSelectionModel.getSelectedObject();
      this.getWizard().setSalesforceObject(var1);
      this.setSection("Source");
      return this.sourceLocationPage;
   }

   private WizardPage moveToPageAfterSourceLocationPage() {
      this.sourceLocationType = this.sourceLocationPage.getLocationType();
      this.getWizard().setSampleFile(this.sourceLocationPage.getSampleFile());
      ImmutableSet var1 = this.sourceConfig.getStructureTypes(this.sourceLocationType);
      if (var1.size() == 1) {
         this.sourceStructureType = (RequestSourceType)Iterables.get(var1, 0);
         return this.moveIntoSourceChain();
      } else {
         this.sourceStructureTypePage = new SourceStructureTypePage(this.getWizard(), var1);
         this.sourceStructureTypePage.setDeducedType(this.sourceLocationPage.getDeducedFileType());
         return this.sourceStructureTypePage;
      }
   }

   private WizardPage moveIntoSourceChain() {
      this.sourceChain = this.getSourceChain(this.sourceStructureType, this.sourceLocationPage.getLocation());
      return this.sourceChain.getFirstPage();
   }

   private WizardPageChain getSourceChain(RequestSourceType var1, Source var2) {
      UcuWizardPageChain.ExistingSourceChainsKey var3 = new UcuWizardPageChain.ExistingSourceChainsKey(var1, var2);
      WizardPageChain var4 = this.sourceChains.get(var3);
      if (var4 == null) {
         var4 = var1.createPages(this.getWizard(), var2);
         this.sourceChains.put(var3, var4);
      }

      return var4;
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      if (var1 == this.getLoginPage()) {
         return null;
      }

      if (var1 == this.objectSelectionPage) {
         return this.moveToLoginPage();
      }

      if (var1 == this.sourceLocationPage) {
         this.setSection("Object");
         return this.objectSelectionPage;
      }

      if (var1 == this.sourceStructureTypePage) {
         return this.sourceLocationPage;
      }

      if (var1 == this.getScheduleAndOptionsPage()) {
         this.setSection("Source");
         WizardPage var2 = this.sourceChain.getLastPage();
         if (var2 == null) {
            var2 = this.sourceLocationPage;
         }

         return var2;
      } else {
         return var1 == this.getSummaryPage() ? this.moveToScheduleAndOptionsPage() : this.moveBackInSourceChain(var1);
      }
   }

   private WizardPage moveBackInSourceChain(WizardPage var1) {
      return var1 == this.sourceChain.getFirstPage() ? this.getPageBeforeSourceChain() : this.sourceChain.getPreviousPage(var1);
   }

   private WizardPage getPageBeforeSourceChain() {
      return this.sourceStructureTypePage != null ? this.sourceStructureTypePage : this.sourceLocationPage;
   }

   public void apply() {
      this.configureWizardItem();
      this.applyAutoMapSetting();
   }

   private void configureWizardItem() {
      if (this.objects == null) {
         this.objects = this.createObjectSummary();
      }
   }

   private SoapWizardWorkflow<T> createObjectSummary() {
      return UiThreadSupplier.call(new Supplier<SoapWizardWorkflow<T>>() {
         public SoapWizardWorkflow<T> get() {
            Source var1 = UcuWizardPageChain.this.sourceLocationPage.getLocation();
            RequestSource var2 = UcuWizardPageChain.this.sourceStructureType.createStructure(UcuWizardPageChain.this.sourceChain);
            Options var3 = UcuWizardPageChain.this.getOptions();
            return UcuWizardPageChain.this.getWizard().buildWorkflow(var1, var2, var3);
         }
      });
   }

   private Options getOptions() {
      SfdcMethod var1 = this.getWizard().getSfdcMethod();
      Schedule var2 = this.getSchedule();
      boolean var3 = this.modifierResponseOption == null || !this.modifierResponseOption.isCustomResponseSelected();
      boolean var4 = this.sourceLocationPage.isUTF8();
      return Options.defaultOptions(var1, var2, var3, var4);
   }

   private static final class ExistingSourceChainsKey {
      private final RequestSourceType type;
      private final Source source;

      public ExistingSourceChainsKey(RequestSourceType var1, Source var2) {
         this.type = var1;
         this.source = var2;
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (var1 instanceof UcuWizardPageChain.ExistingSourceChainsKey) {
            UcuWizardPageChain.ExistingSourceChainsKey var2 = (UcuWizardPageChain.ExistingSourceChainsKey)var1;
            if (this.type == var2.type) {
               if (this.type == RequestSourceType.DATABASE) {
                  return this.source == var2.source;
               }

               return true;
            }
         }

         return false;
      }

      @Override
      public int hashCode() {
         int var1 = 17;
         var1 = 37 * var1 + this.type.hashCode();
         if (this.type == RequestSourceType.DATABASE) {
            var1 = 37 * var1 + this.source.hashCode();
         }

         return var1;
      }
   }
}
