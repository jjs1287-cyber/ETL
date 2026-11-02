package org.etlforge.integration.client.ui.core.window;

import javax.swing.KeyStroke;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.ui.util.KeyUtils;

final class CloseEditorShortcuts {
   public static void install(ApplicationWindow var0) {
      Class<CloseEditorShortcuts> var1 = CloseEditorShortcuts.class;
      closeActive(var0, var1);
      closeAll(var0, var1);
      closeOther(var0, var1);
   }

   private static void closeActive(ApplicationWindow var0, Class<?> var1) {
      KeyStroke var2 = ApplicationUi.requestGlobalKeyStroke(KeyUtils.getCtrlDown(115), var1);
      if (var2 != null) {
         var0.addGlobalKeyDispatcher(var2, new CurrentEditorCloser());
      }
   }

   private static void closeAll(ApplicationWindow var0, Class<?> var1) {
      KeyStroke var2 = ApplicationUi.requestGlobalKeyStroke(KeyUtils.getCtrlShiftDown(115), var1);
      if (var2 != null) {
         var0.addGlobalKeyDispatcher(var2, new CloseAllEditorsExecutor());
      }
   }

   private static void closeOther(ApplicationWindow var0, Class<?> var1) {
      KeyStroke var2 = ApplicationUi.requestGlobalKeyStroke(KeyStroke.getKeyStroke("ctrl alt pressed F4"), var1);
      if (var2 != null) {
         var0.addGlobalKeyDispatcher(var2, new CloseOtherEditorsExecutor());
      }
   }

   private CloseEditorShortcuts() {
   }
}
