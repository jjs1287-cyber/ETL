package org.etlforge.integration.client.ui;

import java.awt.EventQueue;
import org.etlforge.application.ui.view.ViewManager;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.jitterpack.UnpackCallback;
import org.etlforge.integration.client.ui.core.main.OpeningScreen;
import org.etlforge.integration.data.project.IntegrationProject;

final class StartupJitterpackUnpackCallback implements UnpackCallback {
   private final ApplicationWindow appWin;
   private final OpeningScreen openingScreen;

   public StartupJitterpackUnpackCallback(ApplicationWindow var1, OpeningScreen var2) {
      this.appWin = var1;
      this.openingScreen = var2;
   }

   @Override
   public void unpackingCancelled(String var1) {
      this.onFailure();
   }

   @Override
   public void unpackingCompleted(IntegrationProject var1) {
   }

   @Override
   public void unpackingFailed(String var1, Throwable var2) {
      this.onFailure();
   }

   private void onFailure() {
      if (this.isUiEmpty()) {
         this.showOpeningScreen();
      }
   }

   private boolean isUiEmpty() {
      ViewManager var1 = this.appWin.getViewManager();
      return !var1.hasVisibleViews();
   }

   private void showOpeningScreen() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            StartupJitterpackUnpackCallback.this.openingScreen.publish(StartupJitterpackUnpackCallback.this.appWin.getFrame());
         }
      });
   }
}
