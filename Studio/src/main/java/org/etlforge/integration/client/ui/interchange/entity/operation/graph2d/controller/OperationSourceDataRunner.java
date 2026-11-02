package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import com.google.common.base.Optional;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.widget.ApplicationFileChooserFactory;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.activity.ui.monitor.OperationMonitor;
import org.etlforge.integration.activity.ui.monitor.OperationMonitorApis;
import org.etlforge.integration.activity.ui.monitor.OperationMonitorSite;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.operation.OperationMonitorDialog;
import org.etlforge.integration.client.ui.operation.runner.OperationRunner;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineBrowser;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.util.file.DefaultFileChooser;
import org.etlforge.ui.util.file.FileChooser;
import org.etlforge.ui.util.file.PreferencesFileStore;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.util.preference.PreferencesKey;

public final class OperationSourceDataRunner {
   private final OperationRunnerFactory runnerFactory;
   private final OperationWrapperNode opNode;
   private final RecentFileStore fileStore;
   private FileChooser fileChooser;
   private final Optional<OperationMonitorApis> monitorApis = Optional.absent();

   public OperationSourceDataRunner(OperationRunnerFactory var1, OperationWrapperNode var2) {
      this.runnerFactory = var1;
      this.opNode = var2;
      this.fileStore = new PreferencesFileStore(new PreferencesKey(RunOperationWithSourceDataAction.class, "File"), 1);
   }

   public void run() {
      if (this.isApplicableOperation()) {
         this.askForFileAndSubmit();
      }
   }

   public boolean isApplicableOperation() {
      Source var1 = this.getSource();
      if (var1 == null) {
         Alert.error("The operation does not have a source.", "No Source");
         return false;
      } else {
         DataLocation var2 = var1.getLocation();
         if (!var2.isFileBased()) {
            Alert.error("The operation source is not file based.", "Not File Based");
            return false;
         } else {
            return true;
         }
      }
   }

   private Source getSource() {
      OperationPipelineBrowser var1 = new OperationPipelineBrowser(this.opNode.getPipeline());
      return var1.getSource();
   }

   private void askForFileAndSubmit() {
      File var1 = this.selectFile();
      if (var1 != null) {
         this.runOperation(var1);
      }
   }

   private File selectFile() {
      FileChooser var1 = this.getFileChooser();
      return var1.getFileToOpen(ApplicationUi.getAppWinFrame(), null, this.fileStore);
   }

   private FileChooser getFileChooser() {
      if (this.fileChooser == null) {
         this.fileChooser = new DefaultFileChooser(ApplicationFileChooserFactory.getFactory());
      }

      return this.fileChooser;
   }

   public void runOperation(File var1) {
      try {
         this.fileStore.update(var1);
         OperationRunner var2 = this.runnerFactory.newRunner();
         Receiver var3 = this.createCallback();
         var2.runWithSourceData(this.opNode.getDataObject().getActivity(), new FileReader(var1), var3);
      } catch (FileNotFoundException var4) {
         ErrorLog.attention(this.getClass(), "Failed to read the selected file.", var4);
      }
   }

   private Receiver<Long> createCallback() {
      if (this.monitorApis.isPresent()) {
         OperationMonitor var1 = new OperationMonitor(this.monitorApis.get());
         return new OperationSourceDataRunner.MonitorLauncher(var1);
      } else {
         return OperationRunner.NO_CALLBACK;
      }
   }

   private class MonitorLauncher implements Receiver<Long> {
      private final OperationMonitor monitor;

      public MonitorLauncher(OperationMonitor var2) {
         this.monitor = var2;
      }

      public void handle(final Long var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               MonitorLauncher.this.openMonitor(var1);
            }
         });
      }

      private void openMonitor(long var1) {
         OperationMonitorSite var3 = this.getMonitorSite();
         if (var3 != null) {
            var3.openActivityMonitor(var1);
         }
      }

      private OperationMonitorSite getMonitorSite() {
         Operation var1 = OperationSourceDataRunner.this.opNode.getDataObject().getActivity();
         UiCompanion var2 = UiCompanion.of(var1);
         Editor var3 = var2.findOpenPage();
         if (var3 instanceof OperationMonitorSite) {
            var3.getWindow().getEditorService().openEditor(var3);
            return (OperationMonitorSite)var3;
         } else {
            return new OperationMonitorDialog(var1, this.monitor, ApplicationUi.getAppWinFrame());
         }
      }
   }
}
