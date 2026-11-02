package org.etlforge.integration.client.ui.entity.testhistory;

import java.awt.EventQueue;
import java.util.List;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.integration.data.test.EntityTestResult;
import org.etlforge.integration.data.test.TestResultHistory;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;

final class DeleteSelectedHistoryEntriesAction<T extends EntityTestResult> extends AbstractSingleTypeSelectionAction<T> {
   private TestResultHistory<T> history;

   public DeleteSelectedHistoryEntriesAction(Class<T> var1) {
      super(var1);
      ActionDecorators.DELETE.decorate(this);
      this.putValue("ShortDescription", "Deletes the selected results from the test history");
      this.setEnabled(false);
   }

   protected boolean isEnabledFor(T var1) {
      return this.history != null;
   }

   @Override
   protected void run(final List<T> var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            DeleteSelectedHistoryEntriesAction.this.removeSelection(var1);
         }
      });
   }

   private void updateEnabledState() {
      this.setEnabled(this.history != null && !this.isSelectionEmpty());
   }

   public void setHistory(TestResultHistory<T> var1) {
      this.history = var1;
      this.updateEnabledState();
   }

   private void removeSelection(List<T> var1) {
      for (EntityTestResult var3 : var1) {
         this.history.remove((T)var3);
      }
   }
}
