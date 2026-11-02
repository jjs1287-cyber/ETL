package org.etlforge.integration.project.upgrade;

import java.util.concurrent.CountDownLatch;
import org.etlforge.integration.client.project.ProjectFile;
import org.etlforge.integration.client.project.ProjectVersionHandler;
import org.etlforge.integration.client.project.UpgradeStrategy;

public final class DefaultPreLoader {
   private final String projectName;
   private final ProjectFile projectFile;
   private final CountDownLatch waitLatch;
   private volatile boolean greenLighted;
   private final UpgradeStrategy strategy;

   public DefaultPreLoader(String var1, ProjectFile var2, UpgradeStrategy var3) {
      this.projectName = var1;
      this.projectFile = var2;
      this.strategy = var3;
      this.waitLatch = new CountDownLatch(1);
   }

   public void run() {
      UpgradeStrategy.Instruction var1 = this.strategy.getInstruction(this.projectName);
      switch (var1) {
         case CANCEL:
            this.veto();
            break;
         case UPGRADE:
            this.giveGreenLight();
            break;
         case BACKUP_AND_UPGRADE:
            boolean var2 = this.strategy.doBackup(this.projectName, this.projectFile.getParentFolder(), this.projectFile.getVersionSavedWith());
            if (var2) {
               this.giveGreenLight();
            } else {
               this.veto();
            }
      }
   }

   public ProjectVersionHandler.PreLoadingResult getResult() {
      try {
         this.waitLatch.await();
         if (this.greenLighted) {
            return ProjectVersionHandler.PreLoadingResult.ALLOWED;
         }
      } catch (InterruptedException var2) {
         Thread.currentThread().interrupt();
      }

      return ProjectVersionHandler.PreLoadingResult.DENIED;
   }

   private void giveGreenLight() {
      this.greenLighted = true;
      this.waitLatch.countDown();
   }

   private void veto() {
      this.greenLighted = false;
      this.waitLatch.countDown();
   }
}
