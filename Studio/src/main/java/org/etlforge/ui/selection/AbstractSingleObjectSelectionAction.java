package org.etlforge.ui.selection;

import java.awt.event.ActionEvent;
import javax.swing.Icon;

public abstract class AbstractSingleObjectSelectionAction<T> extends AbstractSelectionAction {
   private final Class<T> objectClass;
   private T selected;

   public AbstractSingleObjectSelectionAction(Class<T> var1) {
      this.objectClass = var1;
   }

   public AbstractSingleObjectSelectionAction(Class<T> var1, String var2, Icon var3) {
      super(var2, var3);
      this.objectClass = var1;
   }

   public AbstractSingleObjectSelectionAction(Class<T> var1, String var2) {
      super(var2);
      this.objectClass = var1;
   }

   @Override
   public final void actionPerformed(ActionEvent var1) {
      if (this.selected != null) {
         this.run(this.selected, var1);
      } else {
         this.setEnabled(false);
      }
   }

   protected abstract void run(T var1, ActionEvent var2);

   protected final T getSelectedObject() {
      return this.selected;
   }

   @Override
   protected final void handleSelectionChange() {
      this.selected = SelectionUtils.getSingleSelection(this.getSelection(), this.objectClass);
      if (this.selected != null) {
         this.setEnabled(this.isEnabledFor(this.selected));
      } else {
         this.setEnabled(false);
         this.onInvalidSelection();
      }
   }

   protected boolean isEnabledFor(T var1) {
      return true;
   }

   protected void onInvalidSelection() {
   }
}
