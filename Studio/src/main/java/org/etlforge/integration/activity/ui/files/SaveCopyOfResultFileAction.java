package org.etlforge.integration.activity.ui.files;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.widget.ApplicationFileChooser;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.file.PreferencesFileStore;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.ui.util.file.SuffixFileChooserFilter;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.util.preference.PreferencesKey;

public final class SaveCopyOfResultFileAction extends AbstractAction {
   private File file;
   private final RecentFileStore lastFile;
   private ApplicationFileChooser fileBrowser;
   private WaitService waitService = WaitService.NONE;
   private Optional<Component> invoker = Optional.absent();

   public SaveCopyOfResultFileAction() {
      super("Copy File To...");
      this.lastFile = new PreferencesFileStore(new PreferencesKey(this.getClass(), "LastFile"), 1);
      this.putValue("ShortDescription", "Save a copy of the file to another location");
      this.setEnabled(false);
   }

   public void setFile(File var1) {
      this.file = Preconditions.checkNotNull(var1);
      this.setEnabled(true);
   }

   public void setInvoker(Optional<Component> var1) {
      this.invoker = Preconditions.checkNotNull(var1);
   }

   public void setWaitService(WaitService var1) {
      this.waitService = Preconditions.checkNotNull(var1);
   }

   @Override
   public void actionPerformed(final ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            SaveCopyOfResultFileAction.this.browseAndCopy(var1);
         }
      });
   }

   private void browseAndCopy(ActionEvent var1) {
      Optional var2 = this.getInvoker(var1);
      File var3 = this.selectDestination(var2);
      if (var3 != null) {
         KongaSwingWorker.runWhileWaiting(new SaveCopyOfResultFileAction.Copier(this.file, var3), this.waitService);
      }
   }

   private Optional<Component> getInvoker(ActionEvent var1) {
      return this.invoker.isPresent() ? this.invoker : UiUtils.getComponentSource(var1);
   }

   private File selectDestination(Optional<Component> var1) {
      if (this.fileBrowser == null) {
         this.fileBrowser = this.createFileBrowser(var1);
      }

      return this.fileBrowser.getFileToSave(null, new SuffixFileChooserFilter(".csv", ".txt"), this.lastFile, "Save to file");
   }

   private ApplicationFileChooser createFileBrowser(Optional<Component> var1) {
      Window var2 = ApplicationUi.getWindowAncestor(var1);
      return new ApplicationFileChooser(var2);
   }

   private static class Copier implements Runnable {
      private final File from;
      private final File to;

      public Copier(File var1, File var2) {
         this.from = var1;
         this.to = var2;
      }

      @Override
      public void run() {
         try {
            Files.copy(this.from, this.to);
         } catch (IOException var2) {
            NonBlockingAlert.error(null, "The file could not be copied. Reported error: " + var2.getMessage(), "Error");
         }
      }
   }
}
