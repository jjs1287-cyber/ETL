package com.etlforge.integration.client.ui.sfdc.delete.wizard;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.etlforge.integration.client.sfdc.data.SoapWizardWorkflow;
import com.etlforge.integration.client.ui.sfdc.wizard.AbstractSalesforceWizardPageChain;
import com.etlforge.integration.client.ui.sfdc.wizard.ModifierResponseOption;
import com.etlforge.integration.client.ui.sfdc.wizard.RequestSource;
import com.etlforge.integration.client.ui.sfdc.wizard.RequestSourceConfig;
import com.etlforge.integration.client.ui.sfdc.wizard.RequestSourceType;
import com.etlforge.integration.client.ui.sfdc.wizard.SourceLocationPage;
import com.etlforge.integration.client.ui.sfdc.wizard.SourceStructureTypePage;
import com.etlforge.integration.client.ui.sfdc.wizard.WizardOption;
import com.etlforge.integration.data.entity.SalesforceDelete;
import java.util.List;
import java.util.Map;
import org.etlforge.application.ui.window.editor.EditorPanel;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.ui.UiThreadSupplier;

final class DeleteWizardPageChain extends AbstractSalesforceWizardPageChain<SalesforceDelete> {
   private final RequestSourceConfig sourceConfig;
   private SourceLocationPage sourceLocationPage;
   private DataLocationType sourceLocationType;
   private SourceStructureTypePage sourceStructureTypePage;
   private RequestSourceType sourceStructureType;
   private WizardPageChain sourceChain;
   private final Map<DeleteWizardPageChain.ExistingSourceChainsKey, WizardPageChain> sourceChains = Maps.newHashMap();
   private ModifierResponseOption modifierResponseOption;
   private SoapWizardWorkflow<SalesforceDelete> objects;

   public DeleteWizardPageChain(DeleteWizard var1, RequestSourceConfig var2, boolean var3) {
      super(var1, var3);
      this.sourceConfig = var2;
   }

   protected DeleteWizard getWizard() {
      return (DeleteWizard)super.getWizard();
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
   public WizardPage getNextPage(WizardPage var1) {
      if (var1 == this.getLoginPage()) {
         return this.moveToSourceLocationPage();
      }

      if (var1 == this.sourceLocationPage) {
         this.sourceLocationType = this.sourceLocationPage.getLocationType();
         this.getWizard().setSampleFile(this.sourceLocationPage.getSampleFile());
         ImmutableSet var4 = this.sourceConfig.getStructureTypes(this.sourceLocationType);
         if (var4.size() == 1) {
            this.sourceStructureType = (RequestSourceType)Iterables.get(var4, 0);
            WizardPage var3 = this.moveIntoSourceChain();
            if (var3 == null) {
               var3 = this.moveToScheduleAndOptionsPage();
            }

            return var3;
         } else {
            this.sourceStructureTypePage = new SourceStructureTypePage(this.getWizard(), var4);
            this.sourceStructureTypePage.setDeducedType(this.sourceLocationPage.getDeducedFileType());
            return this.sourceStructureTypePage;
         }
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

   private WizardPage moveToSourceLocationPage() {
      this.setSection("Source");
      if (this.sourceLocationPage == null) {
         this.sourceLocationPage = new SourceLocationPage(this.getWizard(), this.sourceConfig.getLocationTypes(), new EditorPanel(this.getWizard().getWindow()));
         this.sourceLocationPage.setFileExtensions(this.sourceConfig.getFileExtensions());
      }

      return this.sourceLocationPage;
   }

   private WizardPage moveIntoSourceChain() {
      this.sourceChain = this.getSourceChain(this.sourceStructureType, this.sourceLocationPage.getLocation());
      return this.sourceChain.getFirstPage();
   }

   private WizardPageChain getSourceChain(RequestSourceType var1, Source var2) {
      DeleteWizardPageChain.ExistingSourceChainsKey var3 = new DeleteWizardPageChain.ExistingSourceChainsKey(var1, var2);
      WizardPageChain var4 = this.sourceChains.get(var3);
      if (var4 == null) {
         var4 = var1.createPages(this.getWizard(), var2);
         this.sourceChains.put(var3, var4);
      }

      return var4;
   }

   private SoapWizardWorkflow<SalesforceDelete> createObjectSummary() {
      return UiThreadSupplier.call(new Supplier<SoapWizardWorkflow<SalesforceDelete>>() {
         public SoapWizardWorkflow<SalesforceDelete> get() {
            Source var1 = DeleteWizardPageChain.this.sourceLocationPage.getLocation();
            RequestSource var2 = DeleteWizardPageChain.this.sourceStructureType.createStructure(DeleteWizardPageChain.this.sourceChain);
            Options var3 = DeleteWizardPageChain.this.getOptions();
            return DeleteWizardPageChain.this.getWizard().buildWorkflow(var1, var2, var3);
         }
      });
   }

   private Options getOptions() {
      Schedule var1 = this.getSchedule();
      boolean var2 = this.modifierResponseOption == null || !this.modifierResponseOption.isCustomResponseSelected();
      boolean var3 = this.sourceLocationPage.isUTF8();
      return Options.defaultOptions(var1, var2, var3);
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      if (var1 == this.getLoginPage()) {
         return null;
      }

      if (var1 == this.sourceLocationPage) {
         return this.moveToLoginPage();
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

   @Override
   protected boolean isLastRequiredPage(WizardPage var1) {
      return this.sourceChain != null && this.sourceChain.canFinish(var1);
   }

   public void apply(SalesforceDelete var1) {
      this.configureWizardObject(var1);
      this.applyAutoMapSetting();
   }

   private void configureWizardObject(SalesforceDelete var1) {
      if (this.objects == null) {
         this.objects = this.createObjectSummary();
      }
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

         if (var1 instanceof DeleteWizardPageChain.ExistingSourceChainsKey) {
            DeleteWizardPageChain.ExistingSourceChainsKey var2 = (DeleteWizardPageChain.ExistingSourceChainsKey)var1;
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
