package org.etlforge.integration.client.ui.script.builder;

import java.awt.Component;
import java.awt.Point;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.widget.LabelDecorator;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.window.PopupWindow;

public final class OperatorWindow {
   private final Component owner;
   private final OperatorButtonPanel buttons;
   private PopupWindow window;
   private Receiver<String> callback;

   public OperatorWindow(Component var1) {
      this.owner = var1;
      this.buttons = new OperatorButtonPanel(new OperatorWindow.InternalCallback());
      this.callback = NoReceiver.create();
   }

   public void setCallback(Receiver<String> var1) {
      this.callback = var1;
   }

   public void show(int var1, int var2) {
      this.window = this.createWindow(this.owner, var1, var2);
      this.window.show();
      this.buttons.requestFocus();
   }

   private PopupWindow createWindow(Component var1, int var2, int var3) {
      PopupWindow var4 = PopupWindow.newWindow(var1, InvisiblePanel.of(this.buttons), new Point(var2, var3));
      var4.setTitle(this.createTitle(), true);
      var4.setGiveFocusToOwnerWhenClosed(false);
      return var4;
   }

   private LabelDecorator createTitle() {
      return StyleSheet.createPopupWindowTitle("Operators:");
   }

   private class InternalCallback implements Receiver<String> {
      private InternalCallback() {
      }

      public void handle(String var1) {
         OperatorWindow.this.window.dispose();
         OperatorWindow.this.callback.handle(var1);
      }
   }
}
