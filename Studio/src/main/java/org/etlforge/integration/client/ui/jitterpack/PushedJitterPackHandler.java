package org.etlforge.integration.client.ui.jitterpack;

import com.google.common.base.Supplier;
import java.awt.EventQueue;
import java.io.File;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.jitterpack.FileToJitterpackConverter;
import org.etlforge.integration.client.project.jitterpack.ImportParameters;
import org.etlforge.integration.client.project.jitterpack.Importer;
import org.etlforge.integration.client.project.jitterpack.ImporterFactory;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.jitterpack.UnpackCallback;
import org.etlforge.integration.client.ui.project.loading.LoadingIssuesPresenter;
import org.etlforge.integration.client.ui.project.resources.ProjectUiResources;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.wait.WaitLock;

public final class PushedJitterPackHandler {
   private final ApplicationWindow appWin;
   private final ProjectManager projectManager;
   private JitterPackerUi packerUi;
   private final File destinationFolder;
   private final UnpackCallback externalCallback;
   private WaitLock waitLock;

   public PushedJitterPackHandler(ApplicationWindow var1, ProjectManager var2) {
      this(var1, var2, null);
   }

   public PushedJitterPackHandler(ApplicationWindow var1, ProjectManager var2, UnpackCallback var3) {
      this.appWin = var1;
      this.projectManager = var2;
      this.destinationFolder = var2.getProjectDirectory().getLocation();
      this.externalCallback = new PushedJitterPackHandler.SafeCallback(var3);
   }

   public void handlePotentialJitterpack(final File var1, final ImporterFactory var2) {
      this.startWait();
      if (EventQueue.isDispatchThread()) {
         ApplicationWorker var3 = Application.getWorker();
         var3.submitForParallel(ApplicationWorker.GUARD.protect(new Runnable() {
            @Override
            public void run() {
               PushedJitterPackHandler.this.handleImpl(var1, var2);
            }
         }));
      } else {
         this.handleImpl(var1, var2);
      }
   }

   private void handleImpl(File var1, ImporterFactory var2) {
      try {
         ApplicationLog.getMainLogger().info("Handling pushed Jitterpak file: " + var1.getAbsolutePath());
         this.projectManager.closeCurrentProject();
         FileToJitterpackConverter var3 = new FileToJitterpackConverter();
         JitterPack var4 = var3.convert(var1, var2.newImporter(ImportParameters.DEFAULT));
         if (var4.getDescriptor().isEncrypted()) {
            this.unpackEncryptedJitterPack(var4, var2);
         } else {
            ImportParameters var5 = ImportParameters.DEFAULT;
            Importer var6 = var2.newImporter(var5);
            this.getPackerUi().unpack(var4, var5, var6, this.destinationFolder, new PushedJitterPackHandler.UnpackCallbackImpl());
         }
      } catch (Throwable var7) {
         this.handleException(var7);
         this.stopWait();
      }
   }

   private void unpackEncryptedJitterPack(JitterPack var1, ImporterFactory var2) {
      assert var1.getDescriptor().isEncrypted();
      char[] var3 = this.promptForPassword(var1);
      if (var3 != null) {
         ImportParameters var4 = new ImportParameters(var3, false);
         Importer var5 = var2.newImporter(var4);
         this.getPackerUi().unpack(var1, var4, var5, this.destinationFolder, new PushedJitterPackHandler.UnpackCallbackImpl());
      } else {
         this.stopWait();
         this.externalCallback.unpackingCancelled("No password was given for encrypted Jitterpak");
      }
   }

   private char[] promptForPassword(final JitterPack var1) {
      try {
         UiThreadSupplier var2 = UiThreadSupplier.wrap(new Supplier<char[]>() {
            public char[] get() {
               return JitterPackPasswordPrompter.getPassword(var1, PushedJitterPackHandler.this.appWin.getFrame());
            }
         });
         return (char[])var2.get();
      } catch (Exception var3) {
         this.externalCallback.unpackingFailed("An unexpected error occured", var3.getCause());
         return null;
      }
   }

   private void startWait() {
      WaitLock var1 = this.waitLock;
      this.waitLock = this.appWin.startWait();
      if (var1 != null) {
         var1.release();
      }
   }

   private void stopWait() {
      if (this.waitLock != null) {
         this.waitLock.release();
         this.waitLock = null;
      }
   }

   private JitterPackerUi getPackerUi() {
      if (this.packerUi == null) {
         this.packerUi = new JitterPackerUi();
         this.packerUi.setLoadingIssuesHandler(new LoadingIssuesPresenter());
      }

      return this.packerUi;
   }

   private void handleException(Throwable var1) {
      String var2 = ProjectUiResources.formatMessage("PushedJitterPackHandler.Error", "Backup");
      this.handleException(var2, var1);
      this.externalCallback.unpackingFailed(var2, var1);
   }

   private void handleException(String var1, Throwable var2) {
      if (var1 == null) {
         var1 = var2.getMessage();
      }

      ErrorLog.attention(PushedJitterPackHandler.class, var1, var2);
   }

   private static class SafeCallback implements UnpackCallback {
      private final UnpackCallback wrappee;

      public SafeCallback(UnpackCallback var1) {
         this.wrappee = var1;
      }

      @Override
      public void unpackingCancelled(String var1) {
         if (this.wrappee != null) {
            this.wrappee.unpackingCancelled(var1);
         }
      }

      @Override
      public void unpackingCompleted(IntegrationProject var1) {
         if (this.wrappee != null) {
            this.wrappee.unpackingCompleted(var1);
         }
      }

      @Override
      public void unpackingFailed(String var1, Throwable var2) {
         if (this.wrappee != null) {
            this.wrappee.unpackingFailed(var1, var2);
         }
      }
   }

   private class UnpackCallbackImpl implements UnpackCallback {
      private UnpackCallbackImpl() {
      }

      @Override
      public void unpackingCancelled(String var1) {
         PushedJitterPackHandler.this.stopWait();
         PushedJitterPackHandler.this.externalCallback.unpackingCancelled(var1);
      }

      @Override
      public void unpackingCompleted(IntegrationProject var1) {
         try {
            PushedJitterPackHandler.this.projectManager.setCurrentProject(var1);
            PushedJitterPackHandler.this.externalCallback.unpackingCompleted(var1);
         } finally {
            PushedJitterPackHandler.this.stopWait();
         }
      }

      @Override
      public void unpackingFailed(String var1, Throwable var2) {
         PushedJitterPackHandler.this.stopWait();
         PushedJitterPackHandler.this.handleException(var1, var2);
         PushedJitterPackHandler.this.externalCallback.unpackingFailed(var1, var2);
      }
   }
}

