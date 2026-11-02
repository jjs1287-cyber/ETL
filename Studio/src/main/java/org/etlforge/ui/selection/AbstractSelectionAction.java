package org.etlforge.ui.selection;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import org.etlforge.ui.action.Action2;

public abstract class AbstractSelectionAction extends AbstractAction implements SelectionAction, Action2 {
   private Selection selection = Selection.EMPTY;
   private SelectionSource selectionSource;
   private SelectionListener selectionListener;

   public AbstractSelectionAction() {
      this.setEnabled(false);
   }

   public AbstractSelectionAction(String var1) {
      super(var1);
      this.setEnabled(false);
   }

   public AbstractSelectionAction(String var1, Icon var2) {
      super(var1, var2);
      this.setEnabled(false);
   }

   @Override
   public Selection getSelection() {
      return this.selection;
   }

   @Override
   public void setSelection(Selection var1) {
      if (this.selection != var1) {
         this.selection = var1 != null ? var1 : Selection.EMPTY;
         this.handleSelectionChange();
      }
   }

   @Override
   public void setSelectionSource(SelectionSource var1) {
      if (var1 != this.selectionSource) {
         if (this.selectionSource != null) {
            this.uninstallSelectionSource();
         }

         if (var1 != null) {
            this.installSelectionSource(var1);
         }

         this.handleSelectionChange();
      }
   }

   protected final SelectionSource getSelectionSource() {
      return this.selectionSource;
   }

   private void uninstallSelectionSource() {
      this.selectionSource.removeSelectionListener(this.selectionListener);
      this.selectionSource = null;
      this.selection = Selection.EMPTY;
   }

   private void installSelectionSource(SelectionSource var1) {
      if (this.selectionListener == null) {
         this.selectionListener = new AbstractSelectionAction.SelectionListenerImpl();
      }

      var1.addSelectionListener(this.selectionListener);
      this.selection = var1.getSelection();
      this.selectionSource = var1;
   }

   protected void handleSelectionChange() {
      this.setEnabled(!this.isSelectionEmpty());
   }

   protected final boolean isSelectionEmpty() {
      return this.selection.isEmpty();
   }

   private class SelectionListenerImpl implements SelectionListener {
      private SelectionListenerImpl() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         AbstractSelectionAction.this.selection = var1.getSelection();
         AbstractSelectionAction.this.handleSelectionChange();
      }
   }
}
