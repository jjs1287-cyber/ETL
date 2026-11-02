package org.etlforge.ui.property;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.MutableSelectionSource;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.util.ListProperty;

public abstract class MoveListPropertyItemAction extends AbstractSelectionAction {
   protected final ListProperty<?> property;
   private int selectedIndex;

   public static MoveListPropertyItemAction up(ListProperty<?> var0) {
      return new MoveListPropertyItemAction.Up(var0);
   }

   public static MoveListPropertyItemAction down(ListProperty<?> var0) {
      return new MoveListPropertyItemAction.Down(var0);
   }

   protected MoveListPropertyItemAction(ListProperty<?> var1, Icon var2, String var3) {
      super(null, var2);
      this.property = var1;
      this.putValue("ShortDescription", var3);
   }

   @Override
   protected void handleSelectionChange() {
      this.selectedIndex = -1;
      Selection var1 = this.getSelection();
      if (var1 instanceof IndexedSelection) {
         IndexedSelection var2 = (IndexedSelection)var1;
         if (var2.size() == 1) {
            this.selectedIndex = var2.headIndex();
         }
      }

      boolean var3 = this.selectedIndex >= 0 && this.isEnabledForIndex(this.selectedIndex);
      this.setEnabled(var3);
   }

   protected abstract boolean isEnabledForIndex(int var1);

   @Override
   public void actionPerformed(ActionEvent var1) {
      final int var2 = this.selectedIndex;
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            MoveListPropertyItemAction.this.swap(var2);
         }
      });
   }

   private void swap(int var1) {
      int var2 = this.getEndIndex(var1);
      this.property.swap(var1, var2);
      SelectionSource var3 = this.getSelectionSource();
      if (var3 instanceof MutableSelectionSource) {
         IndexedSelection var4 = SelectionFactory.newIndexedSelection(this.property.itemAt(var2), var2);
         ((MutableSelectionSource)var3).setSelection(var4);
      }
   }

   protected abstract int getEndIndex(int var1);

   private static class Down extends MoveListPropertyItemAction {
      public Down(ListProperty<?> var1) {
         super(var1, UiResourceBundle.getIcon("DownArrow"), "Move the selected item one step down");
      }

      @Override
      protected boolean isEnabledForIndex(int var1) {
         return var1 < this.property.size() - 1;
      }

      @Override
      protected int getEndIndex(int var1) {
         return var1 + 1;
      }
   }

   private static class Up extends MoveListPropertyItemAction {
      public Up(ListProperty<?> var1) {
         super(var1, UiResourceBundle.getIcon("UpArrow"), "Move the selected item one step up");
      }

      @Override
      protected boolean isEnabledForIndex(int var1) {
         return var1 > 0;
      }

      @Override
      protected int getEndIndex(int var1) {
         return var1 - 1;
      }
   }
}
