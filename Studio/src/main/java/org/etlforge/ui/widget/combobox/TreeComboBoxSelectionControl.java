package org.etlforge.ui.widget.combobox;

import java.awt.EventQueue;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public final class TreeComboBoxSelectionControl {
   public static void install(JComboBox var0) {
      TreeComboBoxSelectionControl.Listener var1 = new TreeComboBoxSelectionControl.Listener(var0);
      var0.addPopupMenuListener(var1);
   }

   private TreeComboBoxSelectionControl() {
   }

   private static class Listener implements PopupMenuListener {
      private final JComboBox comboBox;

      public Listener(JComboBox var1) {
         this.comboBox = var1;
      }

      @Override
      public void popupMenuWillBecomeVisible(PopupMenuEvent var1) {
         this.setMode(AbstractTreeComboBoxModel.UnselectableItemMode.IGNORE);
      }

      @Override
      public void popupMenuWillBecomeInvisible(PopupMenuEvent var1) {
         this.setMode(AbstractTreeComboBoxModel.UnselectableItemMode.SELECT_NEXT);
      }

      private void setMode(final AbstractTreeComboBoxModel.UnselectableItemMode var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ComboBoxModel var1x = Listener.this.comboBox.getModel();
               if (var1x instanceof AbstractTreeComboBoxModel) {
                  ((AbstractTreeComboBoxModel)var1x).setUnselectableItemMode(var1);
               }
            }
         });
      }

      @Override
      public void popupMenuCanceled(PopupMenuEvent var1) {
      }
   }
}
