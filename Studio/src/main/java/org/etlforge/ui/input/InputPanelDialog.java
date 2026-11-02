package org.etlforge.ui.input;

import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.undo.UndoManager;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.util.KeyUtils;

public class InputPanelDialog extends KongaDialog {
   private final InputPanel panel;
   private final UndoManager undoManager;

   public InputPanelDialog(Window var1, InputPanel var2, String var3) {
      this(var1, var2, var3, KongaDialog.OK_CANCEL);
   }

   public InputPanelDialog(Window var1, InputPanel var2, String var3, KongaDialog.ButtonMode var4) {
      this(var1, var2, var3, ModalityType.APPLICATION_MODAL, var4);
   }

   public InputPanelDialog(Window var1, InputPanel var2, String var3, ModalityType var4, KongaDialog.ButtonMode var5) {
      super(var1, var3, var4);
      this.panel = var2;
      this.undoManager = new UndoManager();
      this.layoutDialog(var5);
      this.installKeyBindings();
   }

   @Override
   protected void onDialogClose() {
      this.panel.removeUndoableEditListener(this.undoManager);
      this.panel.dispose();
      super.onDialogClose();
   }

   @Override
   protected void onDialogOpened() {
      this.panel.addUndoableEditListener(this.undoManager);
      super.onDialogOpened();
   }

   protected boolean useScroll() {
      return true;
   }

   private void layoutDialog(KongaDialog.ButtonMode var1) {
      JComponent var2 = this.useScroll() ? this.createScrollPane() : this.panel.getUi();
      this.standardLayout(var2, var1);
   }

   private JScrollPane createScrollPane() {
      JScrollPane var1 = new JScrollPane(this.panel.getUi());
      var1.getHorizontalScrollBar().setUnitIncrement(10);
      var1.getVerticalScrollBar().setUnitIncrement(10);
      return var1;
   }

   private void installKeyBindings() {
      InputMap var1 = this.getRootPane().getInputMap(2);
      ActionMap var2 = this.getRootPane().getActionMap();
      var1.put(KeyUtils.UNDO, "undo");
      var2.put("undo", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            if (InputPanelDialog.this.undoManager.canUndo()) {
               InputPanelDialog.this.undoManager.undo();
            }
         }
      });
      var1.put(KeyUtils.REDO, "redo");
      var2.put("redo", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            if (InputPanelDialog.this.undoManager.canRedo()) {
               InputPanelDialog.this.undoManager.redo();
            }
         }
      });
   }
}
