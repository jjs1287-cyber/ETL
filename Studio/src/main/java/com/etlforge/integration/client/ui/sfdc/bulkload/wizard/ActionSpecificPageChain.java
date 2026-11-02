package com.etlforge.integration.client.ui.sfdc.bulkload.wizard;

import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.integration.data.entity.Schedule;

interface ActionSpecificPageChain {
   WizardPage moveToFirstPage();

   WizardPage moveToLastPage();

   WizardPage getLastPage();

   WizardPage getNextPage(WizardPage var1);

   WizardPage getPreviousPage(WizardPage var1);

   boolean canFinish(WizardPage var1);

   void applyToWizard(Schedule var1);
}
