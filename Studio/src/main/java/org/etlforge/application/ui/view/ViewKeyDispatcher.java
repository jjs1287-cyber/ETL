package org.etlforge.application.ui.view;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.awt.AWTKeyStroke;
import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.SwingUtilities;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.service.EditorSelectorList;
import org.etlforge.ui.event.KeyStrokeProcessorDispatcher;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.ListSelectorWindow;

public class ViewKeyDispatcher implements KeyEventDispatcher {
   private static AWTKeyStroke ctrlTab = KeyUtils.getCtrlDown(9);
   private static AWTKeyStroke ctrlShiftTab = KeyUtils.getCtrlShiftDown(9);
   private final ApplicationWindow appWin;
   private final List<KeyEventDispatcher> preProcessors;

   public ViewKeyDispatcher(ApplicationWindow var1) {
      Preconditions.checkNotNull(var1, "appWin");
      this.appWin = var1;
      this.preProcessors = Lists.newArrayList();
      this.preProcessors.add(new KeyStrokeProcessorDispatcher());
   }

   @Override
   public boolean dispatchKeyEvent(KeyEvent var1) {
      boolean var2 = this.checkPreProcessors(var1);
      if (!var2) {
         var2 = this.checkPageNavigation(var1);
      }

      return var2;
   }

   private boolean checkPreProcessors(KeyEvent var1) {
      for (KeyEventDispatcher var3 : this.preProcessors) {
         if (var3.dispatchKeyEvent(var1)) {
            return true;
         }
      }

      return false;
   }

   private boolean checkPageNavigation(KeyEvent var1) {
      if (SwingUtilities.getWindowAncestor(var1.getComponent()) != this.appWin.getFrame()) {
         return false;
      }

      AWTKeyStroke var2 = AWTKeyStroke.getAWTKeyStrokeForEvent(var1);
      int var3 = this.appWin.getEditorService().getNumberOfEditors();
      if (var2.equals(ctrlTab)) {
         if (var3 >= 2) {
            this.selectEditor(ListSelectorWindow.Direction.NEXT);
         }

         return true;
      } else if (var2.equals(ctrlShiftTab)) {
         if (var3 >= 2) {
            this.selectEditor(ListSelectorWindow.Direction.PREVIOUS);
         }

         return true;
      } else {
         return false;
      }
   }

   private void selectEditor(ListSelectorWindow.Direction var1) {
      EditorSelectorList var2 = new EditorSelectorList(this.appWin.getEditorService());
      var2.display(var1);
   }
}
