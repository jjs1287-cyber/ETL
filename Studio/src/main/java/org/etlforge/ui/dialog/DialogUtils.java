package org.etlforge.ui.dialog;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JDialog;
import org.etlforge.ui.undo.UndoRedoController;
import org.etlforge.ui.widget.text.undo.TextUndoRedoControllerFactory;

public final class DialogUtils {
   public static UndoRedoController enableUndoRedoForTextComponents(JDialog var0, TextUndoRedoControllerFactory var1) {
      Container var2 = var0.getContentPane();
      if (!(var2 instanceof JComponent)) {
         throw new IllegalArgumentException("The dialog's content pane must be a JComponent");
      }

      final UndoRedoController var3 = var1.newController((JComponent)var2);
      var0.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosed(WindowEvent var1) {
            var3.dispose();
         }
      });
      return var3;
   }

   private DialogUtils() {
      throw new UnsupportedOperationException();
   }
}
