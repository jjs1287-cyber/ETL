package org.etlforge.application.ui.wizard;

public interface WizardPageChain {
   WizardPage getFirstPage();

   WizardPage getLastPage();

   boolean hasNextPage(WizardPage var1);

   WizardPage getNextPage(WizardPage var1);

   boolean hasPreviousPage(WizardPage var1);

   WizardPage getPreviousPage(WizardPage var1);

   boolean canFinish(WizardPage var1);
}
