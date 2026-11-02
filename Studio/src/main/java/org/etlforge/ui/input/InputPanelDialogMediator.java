package org.etlforge.ui.input;

import com.google.common.base.Preconditions;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.RootPaneContainer;
import javax.swing.undo.UndoManager;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.util.KeyUtils;

public final class InputPanelDialogMediator {
   private final InputPanel panel;
   private final Window window;
   private final UndoManager undoManager;
   private boolean disposePanelOnDialogClose;
   private boolean bindOkToValidState;

   public static InputPanelDialogMediator bind(InputPanel var0, JFrame var1) {
      return new InputPanelDialogMediator(var0, var1);
   }

   public static InputPanelDialogMediator bind(InputPanel var0, KongaDialog var1) {
      InputPanelDialogMediator var2 = new InputPanelDialogMediator(var0, var1);
      var2.setBindOkToValidState(true);
      return var2;
   }

   private InputPanelDialogMediator(InputPanel var1, Window var2) {
      this.panel = var1;
      this.window = var2;
      this.undoManager = new UndoManager();
      this.installKeyBindings((RootPaneContainer)var2);
      InputPanelDialogMediator.Listener var3 = new InputPanelDialogMediator.Listener();
      var2.addWindowListener(var3);
      var1.addInputChangeListener(var3);
      this.setDisposePanelOnDialogClose(true);
   }

   private void installKeyBindings(RootPaneContainer var1) {
      JRootPane var2 = var1.getRootPane();
      InputMap var3 = var2.getInputMap(2);
      ActionMap var4 = var2.getActionMap();
      var3.put(KeyUtils.UNDO, "undo");
      var4.put("undo", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            if (InputPanelDialogMediator.this.undoManager.canUndo()) {
               InputPanelDialogMediator.this.undoManager.undo();
            }
         }
      });
      var3.put(KeyUtils.REDO, "redo");
      var4.put("redo", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            if (InputPanelDialogMediator.this.undoManager.canRedo()) {
               InputPanelDialogMediator.this.undoManager.redo();
            }
         }
      });
   }

   public void setDisposePanelOnDialogClose(boolean var1) {
      this.disposePanelOnDialogClose = var1;
   }

   public void setBindOkToValidState(boolean var1) {
      this.bindOkToValidState = var1;
      if (var1) {
         this.updateOkButton();
      }
   }

   private void updateOkButton() {
      JButton var1 = this.getOkButton();
      if (var1 != null) {
         boolean var2 = this.panel.isInputValid();
         var1.setEnabled(var2);
      }
   }

   private JButton getOkButton() {
      Preconditions.checkState(this.window instanceof KongaDialog);
      KongaDialog var1 = (KongaDialog)this.window;
      JButton var2 = var1.getOKButton();
      if (var2 == null) {
         var2 = var1.getContinueButton();
         if (var2 == null) {
            var2 = var1.getApplyButton();
         }
      }

      return var2;
   }

   private class Listener extends WindowAdapter implements InputChangeListener {
      private Listener() {
      }

      @Override
      public void inputChanged(InputChangeEvent var1) {
         if (InputPanelDialogMediator.this.bindOkToValidState) {
            InputPanelDialogMediator.this.updateOkButton();
         }
      }

      @Override
      public void windowOpened(WindowEvent var1) {
         InputPanelDialogMediator.this.panel.addUndoableEditListener(InputPanelDialogMediator.this.undoManager);
      }

      @Override
      public void windowClosed(WindowEvent var1) {
         InputPanelDialogMediator.this.panel.removeUndoableEditListener(InputPanelDialogMediator.this.undoManager);
         if (InputPanelDialogMediator.this.disposePanelOnDialogClose) {
            InputPanelDialogMediator.this.panel.dispose();
         }
      }
   }
}
