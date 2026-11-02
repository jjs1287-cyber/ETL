package org.etlforge.application.ui;

import com.google.common.base.Optional;
import java.awt.Component;
import java.awt.Window;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.Application;
import org.etlforge.application.config.ApplicationConfigurationException;
import org.etlforge.application.config.FatalConfigurationError;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.UiJobManager;
import org.etlforge.application.ui.job.UiJobManagerFactory;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.ui.util.focus.KongaFocusManager;

public abstract class ApplicationUi {
   private static final KongaFocusManager focusManager = new KongaFocusManager();
   private static UiJobManager uiJobManager;
   private static final GlobalKeyStrokeStore globalKeyStrokeStore = new GlobalKeyStrokeStore();
   private static volatile boolean macMenusInstalled;

   public static ApplicationWindow getAppWin() {
      String var0 = "ui.AppWin";
      return Application.containsBean(var0) ? Application.getBean(var0, ApplicationWindow.class) : null;
   }

   public static JFrame getAppWinFrame() {
      ApplicationWindow var0 = getAppWin();
      return var0 != null ? var0.getFrame() : null;
   }

   public static synchronized UiJobManager getJobManager() {
      if (uiJobManager == null) {
         uiJobManager = createUiJobManager();
      }

      return uiJobManager;
   }

   private static UiJobManager createUiJobManager() {
      try {
         return UiJobManagerFactory.createManager();
      } catch (ApplicationConfigurationException var2) {
         String var1 = "Failed to create the UI job manager. This is most probably a configuration error. The application will exit.";
         FatalConfigurationError.throwError(9, ApplicationUi.class, var2, var1);
         return null;
      }
   }

   public static KongaFocusManager getFocusManager() {
      return focusManager;
   }

   public static KeyStroke requestGlobalKeyStroke(KeyStroke var0, Object var1) {
      return globalKeyStrokeStore.request(var0, var1);
   }

   public static void releaseGlobalKeyStroke(KeyStroke var0, Object var1) {
      globalKeyStrokeStore.release(var0, var1);
   }

   private static void initializeSunAwtExceptionHandler() {
      System.setProperty("sun.awt.exception.handler", UncaughtExceptionLogger.class.getName());
   }

   public static Window getWindowAncestor(Optional<? extends Component> var0) {
      return var0.isPresent() ? SwingUtilities.getWindowAncestor((Component)var0.get()) : getAppWinFrame();
   }

   public static void macMenusHaveBeenInstalled() {
      if (!SystemUtils.IS_OS_MAC) {
         throw new UnsupportedOperationException("This method can only be called when running on Mac.");
      }

      macMenusInstalled = true;
   }

   public static boolean hasMacMenus() {
      return macMenusInstalled;
   }

   static {
      initializeSunAwtExceptionHandler();
      ErrorLog.addListener(new ErrorLogMonitor());
      focusManager.register();
   }
}
