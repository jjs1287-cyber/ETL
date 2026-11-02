package org.etlforge.integration.client.ui.interchange;

import java.awt.event.KeyEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowKeyDispatcher;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewerManager;
import org.etlforge.ui.util.KeyUtils;

final class FocusContentViewerFilterFieldBinding implements WindowKeyDispatcher, GlobalShortcut {
   private final ProjectContentViewerManager viewer;

   public FocusContentViewerFilterFieldBinding(ProjectContentViewerManager var1) {
      this.viewer = var1;
   }

   @Override
   public void install(ApplicationWindow var1) {
      var1.addGlobalKeyDispatcher(KeyUtils.getCtrlShiftDown(70), this);
   }

   @Override
   public boolean dispatch(KeyEvent var1, ApplicationWindow var2) {
      this.viewer.giveFocusToFilterField();
      return true;
   }

   @Override
   public String getId() {
      return this.getClass().getName();
   }
}
