package org.etlforge.integration.client.ui.entity.testhistory;

import java.awt.event.ActionEvent;
import javax.swing.Icon;
import org.etlforge.integration.data.test.EntityTestResult;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.selection.IndexedSelection;

public abstract class SingleTestResultAction extends AbstractSelectionAction {
   protected SingleTestResultAction() {
   }

   public SingleTestResultAction(String var1, Icon var2) {
      super(var1, var2);
   }

   protected abstract void actOn(EntityTestResult var1);

   @Override
   public final void actionPerformed(ActionEvent var1) {
      EntityTestResult var2 = this.getSelectedTestResult();
      if (var2 != null) {
         this.actOn(var2);
      }
   }

   @Override
   protected final void handleSelectionChange() {
      this.setEnabled(this.getSelectedTestResult() != null);
   }

   private EntityTestResult getSelectedTestResult() {
      IndexedSelection var1 = (IndexedSelection)this.getSelection();
      if (var1.size() != 1) {
         return null;
      }

      Object var2 = var1.head();
      return var2 instanceof EntityTestResult ? (EntityTestResult)var2 : null;
   }
}
