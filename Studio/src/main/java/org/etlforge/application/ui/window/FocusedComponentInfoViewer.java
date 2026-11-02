package org.etlforge.application.ui.window;

import java.awt.Component;
import java.awt.Container;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.ui.Size;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.text.KongaTextArea;

public final class FocusedComponentInfoViewer implements WindowKeyDispatcher {
   private static final KeyStroke KEY_STROKE = KeyUtils.getCtrlShiftDown(44);

   private FocusedComponentInfoViewer() {
   }

   public static boolean install(ApplicationWindow var0) {
      KeyStroke var1 = ApplicationUi.requestGlobalKeyStroke(KEY_STROKE, FocusedComponentInfoViewer.class);
      if (var1 != null) {
         var0.addGlobalKeyDispatcher(var1, new FocusedComponentInfoViewer());
         return true;
      } else {
         ApplicationLog.getMainLogger().warning("Failed to install the FocusedComponentInfoViewer key trigger");
         return false;
      }
   }

   @Override
   public boolean dispatch(KeyEvent var1, ApplicationWindow var2) {
      KeyboardFocusManager var3 = KeyboardFocusManager.getCurrentKeyboardFocusManager();
      Component var4 = var3.getFocusOwner();
      Component var5 = var3.getPermanentFocusOwner();
      String var6 = "Focus owner: " + var4 + "\n\nPermanent focus owner: " + var5;
      if (var4 instanceof JComponent) {
         StringBuilder var7 = new StringBuilder("\n\n");
         this.dumpKeyBindings((JComponent)var4, var7);
         var6 = var6 + var7.toString();
      }

      Alert.message(null, KongaTextArea.viewer(var6, Size.rows(16), Size.columns(80)), "Focused Components", Alert.Type.INFORMATION);
      return false;
   }

   private void dumpKeyBindings(JComponent var1, StringBuilder var2) {
      var2.append("Key Bindings for ").append(var1.getClass()).append(":").append(SystemUtils.LINE_SEPARATOR);
      this.dumpKeyBindings(var1, var2, var1.getInputMap(0), "WHEN_FOCUSED");
      this.dumpKeyBindings(var1, var2, var1.getInputMap(1), "WHEN_ANCESTOR_OF_FOCUSED_COMPONENT");
      this.dumpKeyBindings(var1, var2, var1.getInputMap(2), "WHEN_IN_FOCUSED_WINDOW");
      Container var3 = var1.getParent();
      if (var3 instanceof JComponent) {
         var2.append(SystemUtils.LINE_SEPARATOR);
         this.dumpKeyBindings((JComponent)var3, var2);
      }
   }

   private void dumpKeyBindings(JComponent var1, StringBuilder var2, InputMap var3, String var4) {
      var2.append(var4).append(":").append(SystemUtils.LINE_SEPARATOR);
      KeyStroke[] var5 = var3.keys();
      if (var5 != null) {
         for (KeyStroke var9 : var5) {
            Object var10 = var3.get(var9);
            Action var11 = var1.getActionMap().get(var10);
            if (var11 != null) {
               var2.append("  ").append(var9).append(" -> ").append(var10).append(" -> ").append(var11.getClass().getName()).append(SystemUtils.LINE_SEPARATOR);
            }
         }
      }
   }

   @Override
   public String getId() {
      return this.getClass().getName();
   }
}
