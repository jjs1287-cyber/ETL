package org.etlforge.application.ui.wizard;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.ui.selection.AbstractSelectionAction;

public class GoToNextPageSelectionAction extends AbstractSelectionAction {
   private final AbstractWizardPage page;

   public GoToNextPageSelectionAction(AbstractWizardPage var1) {
      this.page = Preconditions.checkNotNull(var1, "page");
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            GoToNextPageSelectionAction.this.next();
         }
      });
   }

   private void next() {
      if (this.page.canContinue()) {
         this.page.getWizard().nextPageRequested();
      }
   }
}
