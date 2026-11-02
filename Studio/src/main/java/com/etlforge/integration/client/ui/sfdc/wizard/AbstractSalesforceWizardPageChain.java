package com.etlforge.integration.client.ui.sfdc.wizard;

import com.google.common.base.Supplier;
import com.etlforge.integration.client.sfdc.data.ObjectSummary;
import com.etlforge.integration.client.ui.sfdc.wizard.login.LoginPage;
import java.util.Collections;
import java.util.List;
import org.etlforge.application.ui.window.editor.EditorPanel;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.ui.UiThreadSupplier;

public abstract class AbstractSalesforceWizardPageChain<T extends IntegrationEntity & SalesforceWizardEntity> implements WizardPageChain {
   private final SalesforceWizard<T> wizard;
   private final LoginPage loginPage;
   private ScheduleAndOptionsPage scheduleAndOptionsPage;
   private final LastPage<T> summaryPage;
   private AutoMapperConfig autoMapperConfig = AutoMapperConfig.NO_AUTO_MAPPING;

   public AbstractSalesforceWizardPageChain(SalesforceWizard<T> var1, boolean var2) {
      this.wizard = var1;
      this.loginPage = var1.getLoginPage();
      if (var2) {
         this.summaryPage = new LastPage<>(var1, (T)var1.getItemToConfigure(), var1.getParent());
      } else {
         this.summaryPage = null;
      }
   }

   protected SalesforceWizard<T> getWizard() {
      return this.wizard;
   }

   protected final LoginPage getLoginPage() {
      return this.loginPage;
   }

   protected final ScheduleAndOptionsPage getScheduleAndOptionsPage() {
      return this.scheduleAndOptionsPage;
   }

   protected final Supplier<ScheduleAndOptionsPage> asScheduleAndOptionsPageSupplier() {
      return new Supplier<ScheduleAndOptionsPage>() {
         public ScheduleAndOptionsPage get() {
            if (AbstractSalesforceWizardPageChain.this.scheduleAndOptionsPage == null) {
               AbstractSalesforceWizardPageChain.this.scheduleAndOptionsPage = new ScheduleAndOptionsPage(
                  AbstractSalesforceWizardPageChain.this.wizard,
                  AbstractSalesforceWizardPageChain.this.wizard.getProjectExplorer(),
                  new EditorPanel(AbstractSalesforceWizardPageChain.this.wizard.getWindow()),
                  AbstractSalesforceWizardPageChain.this.createOptionsForOptionsPage()
               );
            }

            return AbstractSalesforceWizardPageChain.this.scheduleAndOptionsPage;
         }
      };
   }

   protected List<? extends WizardOption> createOptionsForOptionsPage() {
      return Collections.emptyList();
   }

   protected boolean hasWizardOptions() {
      return false;
   }

   protected final Schedule getSchedule() {
      return this.scheduleAndOptionsPage == null ? null : this.scheduleAndOptionsPage.getSchedule();
   }

   protected final boolean hasSummaryPage() {
      return this.summaryPage != null;
   }

   protected final LastPage<T> getSummaryPage() {
      return this.summaryPage;
   }

   protected final void setAutoMapperConfig(AutoMapperConfig var1) {
      this.autoMapperConfig = var1;
      if (this.summaryPage != null) {
         AutoMapperChoice var2 = new AutoMapperChoice(var1);
         this.summaryPage.setAutoMapperChoice(var2);
      }
   }

   protected final void applyAutoMapSetting() {
      boolean var1 = UiThreadSupplier.call(
         new Supplier<Boolean>() {
            public Boolean get() {
               return AbstractSalesforceWizardPageChain.this.summaryPage != null
                  ? AbstractSalesforceWizardPageChain.this.summaryPage.isAutoMapSelected()
                  : AbstractSalesforceWizardPageChain.this.autoMapperConfig.getDefaultValue();
            }
         }
      );
      this.wizard.setAutoMap(var1);
   }

   @Override
   public WizardPage getFirstPage() {
      return this.loginPage;
   }

   @Override
   public final WizardPage getLastPage() {
      return this.summaryPage != null ? this.summaryPage : this.scheduleAndOptionsPage;
   }

   @Override
   public final boolean canFinish(WizardPage var1) {
      if (!var1.canContinue()) {
         return false;
      } else {
         AbstractWizardPage var2 = this.summaryPage == null ? this.scheduleAndOptionsPage : this.summaryPage;
         if (var1 == var2) {
            return true;
         } else {
            return !this.wizard.hasCompletedAtLeastOnce() ? false : var1 == this.scheduleAndOptionsPage || this.isLastRequiredPage(var1);
         }
      }
   }

   protected boolean isLastRequiredPage(WizardPage var1) {
      return false;
   }

   @Override
   public final boolean hasNextPage(WizardPage var1) {
      if (var1 == this.summaryPage) {
         return false;
      } else {
         return var1 == this.scheduleAndOptionsPage ? this.summaryPage != null : true;
      }
   }

   @Override
   public final boolean hasPreviousPage(WizardPage var1) {
      return var1 != this.getFirstPage();
   }

   protected final void setSection(String var1) {
      this.wizard.getDisplayer().setSection(var1);
   }

   protected final WizardPage moveToLoginPage() {
      this.setSection("Login");
      return this.loginPage;
   }

   protected final WizardPage moveToScheduleAndOptionsPage() {
      String var1 = this.hasWizardOptions() ? "Schedule & Options" : "Schedule";
      this.setSection(var1);
      return this.asScheduleAndOptionsPageSupplier().get();
   }

   protected final WizardPage moveToSummaryPage(ObjectSummary<T> var1) {
      if (this.summaryPage == null) {
         return null;
      }

      this.prepareLastPage(var1);
      this.setSection("Summary");
      return this.summaryPage;
   }

   private void prepareLastPage(ObjectSummary<T> var1) {
      this.summaryPage.setWizardName(this.wizard.getWizardItemName());
      this.summaryPage.setObjectSummary(var1);
   }
}
