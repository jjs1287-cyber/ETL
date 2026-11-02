package org.etlforge.integration.client.plugin.ui.pipeline.view;

import com.google.common.collect.Maps;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import javax.swing.JLabel;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePlugin;
import org.etlforge.integration.client.plugin.pipeline.xml.XmlToManifestTranslator;
import org.etlforge.integration.client.plugin.serverapi.PipelinePluginInfoProvider;
import org.etlforge.integration.client.plugin.serverapi.PipelinePluginManifestDownloadCallback;
import org.etlforge.integration.client.plugin.serverapi.PluginAsXml;
import org.etlforge.integration.client.plugin.ui.resources.PluginUiResources;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.plugin.PluginSpecificationException;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.util.version.Version;

final class CheckAvailabilityJob extends UiJob {
   private final PipelinePluginInfoProvider infoProvider;
   private final PipelinePluginManifestDownloadCallback callback;
   private final CheckAvailabilityJob.Result result;

   public CheckAvailabilityJob(PipelinePluginInfoProvider var1, Set<AppliedPipelinePlugin> var2) {
      super(PluginUiResources.getString("RequiredPluginsView.Check.Job.Name"));
      this.infoProvider = var1;
      this.callback = new CheckAvailabilityJob.Callback();
      this.result = new CheckAvailabilityJob.Result(var2);
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage(PluginUiResources.getString("RequiredPluginsView.Check.Job.Start"));
      this.infoProvider.downloadPluginManifests(this.callback);
   }

   private final class Callback implements PipelinePluginManifestDownloadCallback {
      private Callback() {
      }

      @Override
      public void pluginInfoDownloaded(ServerInfo var1, PluginAsXml[] var2) {
         CheckAvailabilityJob.this.setMessage(PluginUiResources.getString("RequiredPluginsView.Check.Job.Compare"));

         for (PluginAsXml var6 : var2) {
            for (PipelinePluginManifest var8 : this.parseManifests(var6)) {
               CheckAvailabilityJob.this.result.available(var8);
               if (CheckAvailabilityJob.this.result.isOk()) {
                  break;
               }
            }
         }

         this.reportResult();
      }

      @Override
      public void cancelled() {
      }

      @Override
      public void caught(Throwable var1) {
         ErrorLog.attention(CheckAvailabilityJob.class, "An error occurred when downloading plugin information from the server.", var1);
      }

      private Iterable<PipelinePluginManifest> parseManifests(PluginAsXml var1) {
         XmlToManifestTranslator var2 = new XmlToManifestTranslator();
         ArrayList var3 = new ArrayList();

         try {
            KongaListUtils.add(var3, var2.translate(var1));
         } catch (PluginSpecificationException var5) {
            ErrorLog.log(this.getClass(), "An error occurred while parsing a plugin manifest.", var5);
         }

         return var3;
      }

      private void reportResult() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               CheckAvailabilityJob.this.result.display();
            }
         });
      }
   }

   private static final class Result {
      private final Map<String, CheckAvailabilityJob.Result.Entry> entries = Maps.newHashMap();

      public Result(Set<AppliedPipelinePlugin> var1) {
         for (AppliedPipelinePlugin var3 : var1) {
            this.entries.put(var3.getPluginId().getName(), new CheckAvailabilityJob.Result.Entry(var3));
         }
      }

      public void available(PipelinePluginManifest var1) {
         String var2 = var1.getName();
         if (this.entries.containsKey(var2)) {
            CheckAvailabilityJob.Result.Entry var3 = this.entries.get(var2);
            Version var4 = var1.getVersion();
            if (var4.isSameAsOrLaterThan(var3.requiredVersion)) {
               this.entries.remove(var2);
               return;
            }

            if (var3.availableVersion == null || var4.isLaterThan(var3.availableVersion)) {
               var3.availableVersion = var4;
            }
         }
      }

      public boolean isOk() {
         return this.entries.isEmpty();
      }

      public void display() {
         if (this.isOk()) {
            Alert.info(PluginUiResources.getString("RequiredPluginsView.Check.Ok"), PluginUiResources.getString("RequiredPluginsView.Check.Ok.Title"));
         } else {
            String var1 = PluginUiResources.getString("RequiredPluginsView.Problem.Title");
            String var2 = PluginUiResources.getString("RequiredPluginsView.Problem.Caption");
            Alert.Type var3 = this.isFatal() ? Alert.Type.ERROR : Alert.Type.WARNING;
            BorderLayoutBuilder var4 = new BorderLayoutBuilder(10, 0);
            JLabel var5 = new JLabel(var3.getIcon());
            var5.setVerticalAlignment(1);
            var4.west(var5);
            var4.center(new AvailabilityResultTable(var2, this.getTableModel()));
            UiProviderDialogDisplayer.display(ApplicationUi.getAppWinFrame(), var4, var1, KongaDialog.OK_ONLY, CheckAvailabilityJob.class);
         }
      }

      private AvailabilityResultTableModel getTableModel() {
         assert !this.isOk();
         AvailabilityResultTableModel var1 = new AvailabilityResultTableModel();

         for (CheckAvailabilityJob.Result.Entry var3 : this.entries.values()) {
            var1.addRow(var3.asProblem());
         }

         return var1;
      }

      private boolean isFatal() {
         for (CheckAvailabilityJob.Result.Entry var2 : this.entries.values()) {
            if (var2.availableVersion == null) {
               return true;
            }
         }

         return false;
      }

      private static final class Entry {
         private final String display;
         public final Version requiredVersion;
         public Version availableVersion;

         public Entry(AppliedPipelinePlugin var1) {
            this.requiredVersion = var1.getPluginId().getVersion();
            this.display = var1.getDisplayName();
         }

         public AvailabilityProblem asProblem() {
            return this.availableVersion == null
               ? AvailabilityProblem.missing(this.display)
               : AvailabilityProblem.versionTooLow(this.display, this.requiredVersion, this.availableVersion);
         }
      }
   }
}
