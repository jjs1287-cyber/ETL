package org.etlforge.integration.client.ui.jitterpack;

import com.google.common.base.Supplier;
import java.awt.EventQueue;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.etlforge.application.Application;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectLoadingCancelledException;
import org.etlforge.integration.client.project.ProjectLoadingIssuesHandler;
import org.etlforge.integration.client.project.jitterpack.DescriptorExtractor;
import org.etlforge.integration.client.project.jitterpack.ExportParameters;
import org.etlforge.integration.client.project.jitterpack.Exporter;
import org.etlforge.integration.client.project.jitterpack.ExtractDescriptorCallback;
import org.etlforge.integration.client.project.jitterpack.ImportParameters;
import org.etlforge.integration.client.project.jitterpack.Importer;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.jitterpack.JitterPackDescriptor;
import org.etlforge.integration.client.project.jitterpack.PostExportJob;
import org.etlforge.integration.client.project.jitterpack.PostExportManager;
import org.etlforge.integration.client.project.jitterpack.UnpackCallback;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.lang.exception.ExceptionUtils;
import org.etlforge.system.callback.CallbackResult;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.util.file.SuffixFileChooserFilter;

public final class JitterPackerUi {
   public static final FileFilter FILE_FILTER = createFileFilter();
   private ProjectLoadingIssuesHandler loadingIssuesHandler;

   private static FileFilter createFileFilter() {
      SuffixFileChooserFilter var0 = new SuffixFileChooserFilter(".jpk", true);
      var0.setDescription("Project backup files");
      return var0;
   }

   public void setLoadingIssuesHandler(ProjectLoadingIssuesHandler var1) {
      this.loadingIssuesHandler = var1;
   }

   public void pack(ExportJitterPackUi var1, Exporter var2, CallbackResult var3) {
      ApplicationWorker var4 = Application.getWorker();
      var4.submitForParallel(new JitterPackerUi.PackJob(var1, var2, var3));
   }

   public void unpack(final JitterPack var1, final ImportParameters var2, final Importer var3, final File var4, final UnpackCallback var5) {
      if (!this.verifyDestinationFolder(var4)) {
         var5.unpackingCancelled("The destination folder does not exist.");
      } else {
         ApplicationWorker.SafeRunnable var6 = new ApplicationWorker.SafeRunnable() {
            @Override
            public void run() {
               JitterPackerUi.this.unpackImpl(var1, var2, var3, var4, var5);
            }
         };
         if (EventQueue.isDispatchThread()) {
            ApplicationWorker var7 = Application.getWorker();
            var7.submitForParallel(var6);
         } else {
            var6.run();
         }
      }
   }

   private boolean verifyDestinationFolder(final File var1) {
      return UiThreadSupplier.call(new Supplier<Boolean>() {
         public Boolean get() {
            if (var1.exists()) {
               return true;
            }

            Answer var1x = Ask.yesOrNo("The folder \"" + var1 + "\" does not exist.\n\nDo you want to create it?", "Destination folder does not exist");
            if (var1x.isYes()) {
               boolean var2 = var1.mkdirs();
               if (var2) {
                  return Boolean.TRUE;
               }

               Alert.error("The folder could not be created. Make sure you have sufficient permissions.", "Could not create folder");
            }

            return Boolean.FALSE;
         }
      });
   }

   private void unpackImpl(JitterPack var1, ImportParameters var2, Importer var3, File var4, UnpackCallback var5) {
      this.logUnpackRequest(var1, var4);

      try {
         DescriptorExtractor var6 = new DescriptorExtractor(var1.getFile(), var2, var3, null);
         JitterPackDescriptor var7 = var6.extract();
         DestinationChecker var8 = new DestinationChecker();
         if (!var8.isLocationOk(var2, var7, var4)) {
            this.logDestinationDenied(var4);
            var5.unpackingCancelled(null);
            return;
         }

         IntegrationProject var9 = var3.unpack(var1, var7, var4, var2, this.loadingIssuesHandler);
         var5.unpackingCompleted(var9);
      } catch (ProjectLoadingCancelledException var10) {
         this.logUnpackCancelled();
         var5.unpackingCancelled(null);
      } catch (Throwable var11) {
         this.logUnpackFailed(var11);
         var5.unpackingFailed(null, var11);
      }
   }

   private void logUnpackRequest(JitterPack var1, File var2) {
      ApplicationLog.getMainLogger().info("Asked to unpack the backup " + var1.getFile().getAbsolutePath() + " to the folder " + var2.getAbsolutePath());
   }

   private void logDestinationDenied(File var1) {
      ApplicationLog.getMainLogger().info("The user declined overwriting existing content in " + var1.getAbsolutePath());
   }

   private void logUnpackFailed(Throwable var1) {
      ApplicationLog.getMainLogger().severe("Failed to unpack the backup: " + ExceptionUtils.getStackTrace(var1));
   }

   private void logUnpackCancelled() {
      ApplicationLog.getMainLogger().info("The user cancelled unpacking the jitterpak");
   }

   public void extractDescriptor(File var1, ImportParameters var2, Importer var3, ExtractDescriptorCallback var4) {
      ApplicationWorker var5 = Application.getWorker();
      var5.submitForParallel(new DescriptorExtractor(var1, var2, var3, var4));
   }

   private static final class PackJob implements Runnable {
      private final ExportJitterPackUi exportUi;
      private final Exporter exporter;
      private final CallbackResult callback;

      public PackJob(ExportJitterPackUi var1, Exporter var2, CallbackResult var3) {
         this.exportUi = var1;
         this.exporter = var2;
         this.callback = var3;
      }

      @Override
      public void run() {
         PostExportManager var1 = null;

         try {
            ManagedProject var2 = this.exportUi.getProjectToExport();
            ExportParameters var3 = this.exportUi.getExportParameters();
            var1 = var2.prepareForExport(var3.arePasswordsExported());
            JitterPack var4 = this.exporter.pack(var2, this.exportUi.getComment(), this.exportUi.getDestination(), var3);
            this.callback.succeeded(var4);
            var1.runJobs(PostExportJob.Condition.ON_SUCCESS);
         } catch (Throwable var5) {
            this.handleException(var5, var1);
         }
      }

      private void handleException(Throwable var1, PostExportManager var2) {
         try {
            if (var2 != null) {
               var2.runJobs(PostExportJob.Condition.ON_SUCCESS);
            }
         } finally {
            this.callback.failed(var1);
         }
      }
   }
}

