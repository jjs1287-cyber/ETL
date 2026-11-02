package org.etlforge.ui.input.undo;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.undo.UndoManager;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.KeyUtils;

public class InputPanelUndoRedoEnabler {
   private final UndoManager undoManager;
   private final InputPanel panel;
   private final KeyBindingInstaller bindings;

   public InputPanelUndoRedoEnabler(InputPanel var1) {
      this(var1, var1.getUi());
   }

   public InputPanelUndoRedoEnabler(InputPanel var1, JComponent var2) {
      this.panel = var1;
      this.undoManager = new UndoManager();
      this.bindings = this.installKeyBindings(var2);
      var1.setSendUndoEvents(true);
      var1.addUndoableEditListener(this.undoManager);
   }

   public void discardAllEdits() {
      this.undoManager.discardAllEdits();
   }

   public void uninstall() {
      this.panel.removeUndoableEditListener(this.undoManager);
      this.bindings.remove();
   }

   private KeyBindingInstaller installKeyBindings(JComponent var1) {
      KeyBindingInstaller var2 = new KeyBindingInstaller(var1, 1);
      var2.addBinding(KeyUtils.UNDO, "undo", new InputPanelUndoRedoEnabler.UndoAction());
      var2.addBinding(KeyUtils.REDO, "redo", new InputPanelUndoRedoEnabler.RedoAction());
      var2.install();
      return var2;
   }

   private class RedoAction extends AbstractAction {
      private RedoAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (InputPanelUndoRedoEnabler.this.undoManager.canRedo()) {
            InputPanelUndoRedoEnabler.this.undoManager.redo();
         }
      }
   }

   private class UndoAction extends AbstractAction {
      private UndoAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (InputPanelUndoRedoEnabler.this.undoManager.canUndo()) {
            InputPanelUndoRedoEnabler.this.undoManager.undo();
         }
      }
   }
}
