package org.etlforge.application.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.concurrent.Callable;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.appdir.ApplicationTempFolder;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.window.action.OpenLocalFolderAction;
import org.etlforge.io.Folder;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class TempFolderConsole implements UiProvider {
   private final KongaLabel pathField = new KongaLabel(ApplicationTempFolder.getAbsolutePath());
   private final KongaLabel filesField = new KongaLabel("");
   private final KongaLabel foldersField = new KongaLabel("");
   private final KongaLabel sizeField = new KongaLabel("");
   private final KongaButton cleanButton = new KongaButton(new TempFolderConsole.CleanAction());
   private WaitService waitService = WaitService.NONE;

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   public void update() {
      TempFolderConsole.Worker var1 = new TempFolderConsole.Worker(new Callable<Folder.Stats>() {
         public Folder.Stats call() throws Exception {
            return ApplicationTempFolder.stats();
         }
      });
      var1.execute();
   }

   @Override
   public void decorate(UiCanvas var1) {
      TwoColumnPanel var2 = TwoColumnPanel.forFormEntry(10, 10);
      var2.addRow(WizardStyleSheet.SMALL_SECTION_STYLE.makeLabel("Temp Folder Stats"));
      var2.addRow(label("Location:"), this.pathField);
      var2.addRow(label("File(s):"), this.filesField);
      var2.addRow(label("Folder(s):"), this.foldersField);
      var2.addRow(label("Total size:"), this.sizeField);
      var2.addRow(this.layoutButtons());
      var2.decorate(var1);
   }

   private static JLabel label(String var0) {
      return TextStyles.DefaultBold.makeLabel(var0);
   }

   private JComponent layoutButtons() {
      BoxBuilder var1 = BoxBuilder.horizontal();
      if (SystemUtils.IS_OS_WINDOWS) {
         OpenLocalFolderAction var2 = new OpenLocalFolderAction("open.temp.folder", "&Open", null) {
            @Override
            protected File getFolderToOpen() {
               return new File(ApplicationTempFolder.getAbsolutePath());
            }
         };
         var1.add(var2).rigid(10);
      }

      var1.add(this.cleanButton);
      return InvisiblePanel.of(var1);
   }

   @Override
   public void requestFocus() {
      this.cleanButton.requestFocusInWindow();
   }

   private void displayNewStats(Folder.Stats var1) {
      if (var1 != null) {
         this.filesField.setText("" + var1.getNumberOfFiles());
         this.foldersField.setText("" + var1.getNumberOfFolders());
         String var2 = FileUtils.byteCountToDisplaySize(var1.getTotalSizeInBytes());
         this.sizeField.setText(var2);
         boolean var3 = !var1.isEmpty();
         this.cleanButton.setEnabled(var3);
      } else {
         this.cleanButton.setEnabled(true);
         String var4 = "calculating...";
         this.filesField.setText(var4);
         this.foldersField.setText(var4);
         this.sizeField.setText(var4);
      }
   }

   private class CleanAction extends AbstractAction {
      public CleanAction() {
         super("&Clean");
         this.putValue("ShortDescription", "Delete as many of the files and folders in the temp folder as possible");
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.setEnabled(false);
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               CleanAction.this.startClean();
            }
         });
      }

      private void startClean() {
         TempFolderConsole.Worker var1 = TempFolderConsole.this.new Worker(new Callable<Folder.Stats>() {
            public Folder.Stats call() throws Exception {
               ApplicationTempFolder.clean();
               return ApplicationTempFolder.stats();
            }
         });
         var1.execute();
      }
   }

   private final class Worker {
      private final Callable<Folder.Stats> producer;

      public Worker(Callable<Folder.Stats> var2) {
         this.producer = var2;
      }

      public void execute() {
         TempFolderConsole.this.displayNewStats(null);
         Computation var1 = this.createJob();
         var1.swingWorkerBuilder()
            .withErrorLog(ErrorLevel.ATTENTION, TempFolderConsole.class)
            .withWaitService(TempFolderConsole.this.waitService)
            .buildAndExecute();
      }

      private Computation<Folder.Stats> createJob() {
         Receiver var1 = new Receiver<Folder.Stats>() {
            public void handle(Folder.Stats var1) {
               TempFolderConsole.this.displayNewStats(var1);
            }
         };
         return Computation.create(this.producer, var1);
      }
   }
}
