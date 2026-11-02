package org.etlforge.integration.client.ui.jitterpack;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import javax.swing.AbstractAction;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.project.jitterpack.ImportParameters;
import org.etlforge.integration.client.project.jitterpack.Importer;
import org.etlforge.integration.client.project.jitterpack.ImporterFactory;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.jitterpack.UnpackCallback;
import org.etlforge.integration.client.ui.jitterpack.resources.JitterpackResources;
import org.etlforge.integration.client.ui.project.loading.LoadingIssuesPresenter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;

public final class DoImportAction extends AbstractAction {
   private final ImportJitterPackUi importUi;
   private final ImporterFactory importerFactory;
   private final UnpackCallback unpackCallback;
   private WaitService waitService = WaitService.NONE;
   private WaitLock waitLock;

   public DoImportAction(ImportJitterPackUi var1, ImporterFactory var2, UnpackCallback var3) {
      super(Strings.get("Import"), JitterpackResources.IMPORT_16);
      this.importUi = var1;
      this.importerFactory = var2;
      this.unpackCallback = new DoImportAction.UnpackCallbackWrapper(var3);
      this.putValue("ShortDescription", Strings.getJitterPackString("Import.ToolTip"));
      this.putValue("KongaMediumIconKey", JitterpackResources.IMPORT_24);
      this.putValue("SwingLargeIconKey", JitterpackResources.IMPORT_32);
      this.setEnabled(false);
   }

   public void setWaitService(WaitService var1) {
      this.waitService = Preconditions.checkNotNull(var1);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.waitLock = this.waitService.startWait();
      this.setEnabled(false);
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            DoImportAction.this.startImport();
         }
      });
   }

   private void startImport() {
      ImportParameters var1 = this.importUi.getImportParameters();
      if (!this.validateParameters(var1)) {
         this.done();
      } else {
         JitterPack var2 = this.importUi.getJitterPackToImport();
         if (this.isSelectedFileClearlyNotAJitterPack(var2)) {
            this.done();
         } else {
            if (this.closeCurrentProject()) {
               JitterPackerUi var3 = new JitterPackerUi();
               var3.setLoadingIssuesHandler(new LoadingIssuesPresenter());
               Importer var4 = this.importerFactory.newImporter(var1);
               var3.unpack(this.importUi.getJitterPackToImport(), var1, var4, this.importUi.getDestinationFolder(), this.unpackCallback);
            } else {
               this.done();
            }
         }
      }
   }

   private void done() {
      this.waitLock.release();
      ActionUtils.setEnabledSafely(this, true);
   }

   private boolean validateParameters(ImportParameters var1) {
      ImportParametersValidator var2 = new ImportParametersValidator(var1);
      return var2.validate();
   }

   private boolean isSelectedFileClearlyNotAJitterPack(JitterPack var1) {
      if (var1.getFile().isDirectory()) {
         String var2 = "The path \"" + var1.getFile().getAbsolutePath() + "\" is a folder. You need to select a file.";
         this.unpackCallback.unpackingFailed(var2, new FileNotFoundException(var2));
         return true;
      } else if (!var1.getFile().exists()) {
         this.unpackCallback.unpackingFailed("The selected file does not exist.", new FileNotFoundException(var1.getFile().getAbsolutePath()));
         return true;
      } else {
         return false;
      }
   }

   private boolean closeCurrentProject() {
      try {
         this.importUi.getProjectManager().closeCurrentProject();
         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   private final class UnpackCallbackWrapper implements UnpackCallback {
      private final UnpackCallback wrapped;

      public UnpackCallbackWrapper(UnpackCallback var2) {
         this.wrapped = var2;
      }

      @Override
      public void unpackingCancelled(String var1) {
         try {
            this.wrapped.unpackingCancelled(var1);
         } finally {
            DoImportAction.this.done();
         }
      }

      @Override
      public void unpackingCompleted(IntegrationProject var1) {
         try {
            this.wrapped.unpackingCompleted(var1);
         } finally {
            DoImportAction.this.done();
         }
      }

      @Override
      public void unpackingFailed(String var1, Throwable var2) {
         try {
            this.logError(var1, var2);
            this.wrapped.unpackingFailed(var1, var2);
         } finally {
            DoImportAction.this.done();
         }
      }

      private void logError(String var1, Throwable var2) {
         if (var1 == null || var1.length() == 0) {
            var1 = Strings.getJitterPackString("FailedToImport") + " " + Strings.getJitterPackString("FailedToImport.Suggestion");
         }

         ErrorLog.attention(DoImportAction.class, var1, var2);
      }
   }
}

