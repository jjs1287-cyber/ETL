package org.etlforge.integration.client.connect.wizard;

import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.integration.data.structure.DataStructure;

public abstract class ConnectWizardPageChain<S extends DataStructure, T extends DataStructure> implements WizardPageChain {
   private final Wizard<?> wizard;
   private final WizardPageChain sourcePages;
   private final WizardPageChain targetPages;
   private final SummaryPage summaryPage;
   private WizardPageChain currentChain;

   public ConnectWizardPageChain(AbstractConnectWizard<?, ?> var1, Folders var2, WizardPageChain var3, WizardPageChain var4) {
      this.wizard = var1;
      this.sourcePages = var3;
      this.targetPages = var4;
      this.summaryPage = new SummaryPage(var1, var2);
      this.currentChain = var3;
   }

   protected final WizardPageChain getSourcePages() {
      return this.sourcePages;
   }

   protected final WizardPageChain getTargetPages() {
      return this.targetPages;
   }

   public final String getWizardName() {
      return this.summaryPage.getWizardName();
   }

   public abstract S getSourceStructure();

   public abstract T getTargetStructure();

   public final boolean isAutoMapSelected() {
      return this.summaryPage.isAutoMapSelected();
   }

   @Override
   public final WizardPage getFirstPage() {
      return this.sourcePages.getFirstPage();
   }

   @Override
   public final WizardPage getLastPage() {
      return this.summaryPage;
   }

   @Override
   public final boolean hasNextPage(WizardPage var1) {
      return var1 != this.summaryPage;
   }

   @Override
   public final WizardPage getNextPage(WizardPage var1) {
      if (var1 == this.summaryPage) {
         return null;
      }

      WizardPage var2 = this.currentChain.getNextPage(var1);
      if (var2 == null) {
         if (this.currentChain == this.sourcePages) {
            this.prepareTargetChain();
            this.setSection("Target");
            this.currentChain = this.targetPages;
            var2 = this.targetPages.getFirstPage();
         } else {
            this.setSection("Summary");
            this.currentChain = null;
            var2 = this.summaryPage;
         }
      }

      return var2;
   }

   protected void prepareTargetChain() {
   }

   private void setSection(String var1) {
      this.wizard.getDisplayer().setSection(var1);
   }

   @Override
   public final boolean hasPreviousPage(WizardPage var1) {
      return var1 != this.summaryPage && this.currentChain != this.targetPages ? this.sourcePages.hasPreviousPage(var1) : true;
   }

   @Override
   public final WizardPage getPreviousPage(WizardPage var1) {
      WizardPage var2 = null;
      if (var1 == this.summaryPage) {
         this.setSection("Target");
         this.currentChain = this.targetPages;
         var2 = this.targetPages.getLastPage();
      } else {
         var2 = this.currentChain.getPreviousPage(var1);
         if (var2 == null && this.currentChain == this.targetPages) {
            this.setSection("Source");
            this.currentChain = this.sourcePages;
            var2 = this.sourcePages.getLastPage();
         }
      }

      return var2;
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return var1.canContinue() && (var1 == this.summaryPage || this.isInTargetChain() && this.targetPages.canFinish(var1));
   }

   private boolean isInTargetChain() {
      return this.currentChain == this.targetPages;
   }
}
