package org.etlforge.application.ui.wizard;

import java.awt.Component;
import org.etlforge.ui.wait.WaitService;

public interface WizardDisplayer {
   void showPage(WizardPage var1);

   WizardPage getCurrentPage();

   void updateTitle();

   void updateNavigationControls();

   void handleContextMenuRequest(Component var1, int var2, int var3);

   WaitService getWaitService();

   void setSections(String[] var1);

   void setSection(String var1);

   String getSection();
}
