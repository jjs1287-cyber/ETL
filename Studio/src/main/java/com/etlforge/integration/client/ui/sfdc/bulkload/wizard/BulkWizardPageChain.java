package com.etlforge.integration.client.ui.sfdc.bulkload.wizard;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.etlforge.integration.client.ui.sfdc.ObjectSelectionController;
import com.etlforge.integration.client.ui.sfdc.ObjectSelectionModel;
import com.etlforge.integration.client.ui.sfdc.wizard.AbstractSalesforceWizardPageChain;
import com.etlforge.integration.client.ui.sfdc.wizard.ObjectSelectionPage;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.salesforce.bulkload.BulkAction;
import org.etlforge.integration.salesforce.bulkload.CreateJobParams;
import org.etlforge.integration.salesforce.wsdl.ObjectMethodFilter;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.ui.UiThreadSupplier;

public final class BulkWizardPageChain extends AbstractSalesforceWizardPageChain<SalesforceBulkLoad> {
   private final ActionSelectionPage actionSelectionPage;
   private final ObjectSelectionModel objectSelectionModel = new ObjectSelectionModel();
   private final ImmutableList<DataLocationType> fileTypes;
   private ObjectSelectionPage objectSelectionPage;
   private boolean inActionSpecificPart;
   private final BulkWizardPageChain.ActionSpecificPart actionSpecificPart = new BulkWizardPageChain.ActionSpecificPart();
   private volatile BulkApiWorkflow workflow;
   private BulkAction action;

   public BulkWizardPageChain(BulkLoadWizard var1, Iterable<DataLocationType> var2, boolean var3) {
      super(var1, var3);
      this.fileTypes = ImmutableList.copyOf(var2);
      this.actionSelectionPage = new ActionSelectionPage(var1, this.objectSelectionModel);
   }

   protected BulkLoadWizard getWizard() {
      return (BulkLoadWizard)super.getWizard();
   }

   @Override
   public WizardPage getFirstPage() {
      return this.actionSelectionPage;
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      if (var1 == this.actionSelectionPage) {
         this.action = this.actionSelectionPage.getSelectedAction();
         this.getWizard().setWizardItemName("New " + this.action);
         return this.moveToLoginPage();
      }

      if (var1 == this.getLoginPage()) {
         return this.moveToObjectSelectionPage();
      }

      if (var1 == this.objectSelectionPage) {
         this.updateWizardItemName();
         return this.moveToActionSpecificPart();
      }

      if (this.inActionSpecificPart) {
         var1 = this.actionSpecificPart.getNextPage(var1);
         if (var1 != null) {
            return var1;
         }

         this.inActionSpecificPart = false;
         if (this.workflow != null) {
            this.workflow = null;
         }

         return this.moveToScheduleAndOptionsPage();
      } else if (var1 == this.getScheduleAndOptionsPage()) {
         this.createImplementingObjectsIfNecessary();
         return this.moveToSummaryPage(this.workflow);
      } else {
         return null;
      }
   }

   private void updateWizardItemName() {
      SObjectSchema var1 = this.objectSelectionModel.getSelectedObject();
      this.getWizard().setWizardItemName(this.action + " " + var1.getLabelPlural());
   }

   private WizardPage moveToObjectSelectionPage() {
      if (this.objectSelectionPage == null) {
         ObjectSelectionController var1 = this.getWizard().getObjectSelectionController(this.objectSelectionModel);
         this.objectSelectionPage = new ObjectSelectionPage(this.getWizard(), this.objectSelectionModel, var1);
      }

      this.prepareObjectSelectionPage();
      this.setSection("Object");
      return this.objectSelectionPage;
   }

   private WizardPage moveToActionSpecificPart() {
      this.inActionSpecificPart = true;
      this.actionSpecificPart.moveInto();
      return this.actionSpecificPart.moveToFirstPage();
   }

   private void prepareObjectSelectionPage() {
      ObjectMethodFilter var1 = new ObjectMethodFilter(this.action.toSfdcMethod());
      this.objectSelectionModel.setObjectFilter(var1);
   }

   public void apply() {
      this.createImplementingObjectsIfNecessary();
   }

   private void createImplementingObjectsIfNecessary() {
      UiThreadSupplier.call(new Supplier<Void>() {
         public Void get() {
            if (BulkWizardPageChain.this.workflow == null) {
               BulkWizardPageChain.this.collectWizardInput();
               BulkWizardPageChain.this.workflow = BulkWizardPageChain.this.getWizard().buildWorkflow();
            }

            return null;
         }
      });
   }

   private void collectWizardInput() {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "collectWizardInput");
      Schedule var1 = this.getSchedule();
      this.actionSpecificPart.applyToWizard(var1);
      this.getWizard().setCreateJobParams(this.getCreateJobParams());
   }

   private CreateJobParams getCreateJobParams() {
      SObjectSchema var1 = this.objectSelectionModel.getSelectedObject();
      if (var1 == null) {
         throw new IllegalStateException("No SObject selected");
      } else {
         String var2 = this.objectSelectionModel.getExternalIdFieldName();
         if (this.action.requiresExternalId() && StringUtils.isEmpty(var2)) {
            throw new IllegalStateException("No external ID field selected");
         } else {
            return CreateJobParams.forObject(var1, this.action, var2);
         }
      }
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      if (var1 == this.getSummaryPage()) {
         return this.moveToScheduleAndOptionsPage();
      }

      if (var1 == this.getScheduleAndOptionsPage()) {
         this.inActionSpecificPart = true;
         return this.actionSpecificPart.moveToLastPage();
      }

      if (this.inActionSpecificPart) {
         WizardPage var2 = this.actionSpecificPart.getPreviousPage(var1);
         if (var2 != null) {
            return var2;
         }

         this.inActionSpecificPart = false;
         this.setSection("Object");
         return this.objectSelectionPage;
      } else if (var1 == this.objectSelectionPage) {
         return this.moveToLoginPage();
      } else if (var1 == this.getLoginPage()) {
         this.setSection("Action");
         return this.actionSelectionPage;
      } else {
         return null;
      }
   }

   @Override
   protected boolean isLastRequiredPage(WizardPage var1) {
      return this.inActionSpecificPart ? this.actionSpecificPart.canFinish(var1) : false;
   }

   private class ActionSpecificPart implements ActionSpecificPageChain {
      private BulkLoadPageChain loadChain;
      private BulkQueryPageChain queryChain;
      private ActionSpecificPageChain currentChain;

      private ActionSpecificPart() {
      }

      public void moveInto() {
         if (BulkWizardPageChain.this.action == BulkAction.QUERY) {
            if (this.queryChain == null) {
               this.queryChain = new BulkQueryPageChain(
                  BulkWizardPageChain.this.getWizard(), BulkWizardPageChain.this.objectSelectionModel, BulkWizardPageChain.this.fileTypes
               );
            }

            this.currentChain = this.queryChain;
         } else {
            if (this.loadChain == null) {
               this.loadChain = new BulkLoadPageChain(
                  BulkWizardPageChain.this.getWizard(), BulkWizardPageChain.this.objectSelectionModel, BulkWizardPageChain.this.fileTypes
               );
            }

            this.loadChain.setAction(BulkWizardPageChain.this.action);
            this.currentChain = this.loadChain;
         }
      }

      @Override
      public WizardPage moveToFirstPage() {
         return this.currentChain.moveToFirstPage();
      }

      @Override
      public WizardPage getLastPage() {
         return this.currentChain.getLastPage();
      }

      @Override
      public WizardPage moveToLastPage() {
         return this.currentChain.moveToLastPage();
      }

      @Override
      public WizardPage getNextPage(WizardPage var1) {
         return this.currentChain.getNextPage(var1);
      }

      @Override
      public WizardPage getPreviousPage(WizardPage var1) {
         return this.currentChain.getPreviousPage(var1);
      }

      @Override
      public boolean canFinish(WizardPage var1) {
         return this.currentChain.canFinish(var1);
      }

      @Override
      public void applyToWizard(Schedule var1) {
         this.currentChain.applyToWizard(var1);
      }
   }
}
