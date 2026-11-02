package org.etlforge.integration.client.ui.interchange;

import com.google.common.base.Supplier;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowKeyDispatcher;
import org.etlforge.integration.client.ui.interchange.content.DefaultProjectContentViewerManager;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewer;

public final class HotKeyActivator implements WindowKeyDispatcher {
   private final Supplier<? extends ProjectContentViewer> viewer;

   public static void install(ApplicationWindow var0, Supplier<? extends ProjectContentViewer> var1) {
      KeyStroke var2 = ApplicationUi.requestGlobalKeyStroke(ProjectContentViewer.ACTIVATOR_KEY, HotKeyActivator.class);
      if (var2 != null) {
         HotKeyActivator var3 = new HotKeyActivator(var1);
         var0.addGlobalKeyDispatcher(var2, var3);
      }
   }

   private HotKeyActivator(Supplier<? extends ProjectContentViewer> var1) {
      this.viewer = var1;
   }

   @Override
   public boolean dispatch(KeyEvent var1, ApplicationWindow var2) {
      this.viewer.get().selectRoot();
      return true;
   }

   @Override
   public String getId() {
      return DefaultProjectContentViewerManager.class.getName();
   }
}
