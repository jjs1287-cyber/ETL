package org.etlforge.ui.input;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.undo.UndoManager;
import org.etlforge.ui.Empty;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.overlay.OverlayDecorator;

public class InputPanelOverlay extends OverlayDecorator {
   private final InputPanel panel;
   private final UndoManager undoManager;

   public InputPanelOverlay(InputPanel var1, String var2) {
      this(var1, var2, false);
   }

   public InputPanelOverlay(InputPanel var1, String var2, boolean var3) {
      super(getUi(var1, var3), var2);
      this.panel = var1;
      this.undoManager = new UndoManager();
      var1.addUndoableEditListener(this.undoManager);
      this.installKeyBindings();
   }

   private static JComponent getUi(InputPanel var0, boolean var1) {
      var0.setBorder(Empty.border(10));
      if (var1) {
         KongaScrollPane var2 = new KongaScrollPane(var0.getUi());
         UiUtils.setOpaqueRecursivelySkipDefaults(var0.getUi(), false);
         var2.makeInconspicuous();
         return var2;
      } else {
         return var0.getUi();
      }
   }

   public final void dispose() {
      this.panel.removeUndoableEditListener(this.undoManager);
   }

   private void installKeyBindings() {
      this.installKeyBinding(KeyUtils.UNDO, "undo", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            if (InputPanelOverlay.this.undoManager.canUndo()) {
               InputPanelOverlay.this.undoManager.undo();
            }
         }
      });
      this.installKeyBinding(KeyUtils.REDO, "redo", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            if (InputPanelOverlay.this.undoManager.canRedo()) {
               InputPanelOverlay.this.undoManager.redo();
            }
         }
      });
   }
}
