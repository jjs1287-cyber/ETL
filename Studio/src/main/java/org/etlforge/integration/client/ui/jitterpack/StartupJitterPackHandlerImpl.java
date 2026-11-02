package org.etlforge.integration.client.ui.jitterpack;

import java.io.File;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.jitterpack.ImporterFactory;
import org.etlforge.integration.client.project.jitterpack.StartupJitterPackHandler;
import org.etlforge.integration.client.project.jitterpack.UnpackCallback;
import org.etlforge.system.thread.Condition;

public final class StartupJitterPackHandlerImpl implements StartupJitterPackHandler {
   private final ApplicationWindow appWin;
   private final ProjectManager projectManager;
   private final ImporterFactory importerFactory;
   private Condition condition = Condition.ALWAYS_TRUE;

   public StartupJitterPackHandlerImpl(ApplicationWindow var1, ProjectManager var2, ImporterFactory var3) {
      this.appWin = var1;
      this.projectManager = var2;
      this.importerFactory = var3;
   }

   public void setCondition(Condition var1) {
      this.condition = var1;
   }

   @Override
   public void handle(File var1, UnpackCallback var2) {
      try {
         this.condition.waitFor();
         PushedJitterPackHandler var3 = new PushedJitterPackHandler(this.appWin, this.projectManager, var2);
         var3.handlePotentialJitterpack(var1, this.importerFactory);
      } catch (InterruptedException var4) {
         Thread.currentThread().interrupt();
         var2.unpackingFailed("The thread was interrupted.", var4);
      }
   }
}

