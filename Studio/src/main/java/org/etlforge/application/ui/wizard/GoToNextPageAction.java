package org.etlforge.application.ui.wizard;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public final class GoToNextPageAction extends AbstractAction {
   private final AbstractWizardPage page;

   public GoToNextPageAction(AbstractWizardPage var1) {
      this.page = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            GoToNextPageAction.this.next();
         }
      });
   }

   private void next() {
      if (this.page.canContinue()) {
         this.page.getWizard().nextPageRequested();
      }
   }
}
