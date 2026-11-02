package org.etlforge.application.update;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.common.io.OutputSupplier;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import org.etlforge.application.Application;
import org.etlforge.application.ExitListener;
import org.etlforge.system.callback.Receiver;
import org.etlforge.system.thread.Cancelable;
import org.etlforge.system.thread.Timeout;

public final class UpdateService {
   private final Callable<VersionInfo> versionInfoSupplier;
   private final DownloadStreamFactory downloadStreamFactory;
   private ExecutorService executor;
   private volatile ExitWhileDownloadingHandler exitWhileDownloadingHandler = ExitWhileDownloadingHandler.CANCEL_DOWNLOAD;
   private final Set<UpdateService.Download> currentDownloads = Sets.newHashSet();
   private final Optional<UpdateService.ExitRequestHandler> exitRequestHandler;
   private final Set<BranchId> checkedBranchIds = Collections.synchronizedSet(new HashSet<>());
   private final Object lock = new Object();

   public UpdateService(Callable<VersionInfo> var1, DownloadStreamFactory var2) {
      this(var1, var2, true);
   }

   @VisibleForTesting
   UpdateService(Callable<VersionInfo> var1, DownloadStreamFactory var2, boolean var3) {
      this.versionInfoSupplier = Preconditions.checkNotNull(var1);
      this.downloadStreamFactory = Preconditions.checkNotNull(var2);
      if (var3) {
         this.exitRequestHandler = Optional.of(new UpdateService.ExitRequestHandler());
         Application.addExitListener(this.exitRequestHandler.get());
      } else {
         this.exitRequestHandler = Optional.absent();
      }
   }

   public void setExitWhileDownloadingHandler(ExitWhileDownloadingHandler var1) {
      this.exitWhileDownloadingHandler = Preconditions.checkNotNull(var1);
   }

   public void shutdownAndAwaitTermination(Timeout var1) {
      if (this.exitRequestHandler.isPresent()) {
         Application.removeExitListener(this.exitRequestHandler.get());
      }

      synchronized (this.lock) {
         if (this.executor != null) {
            try {
               this.executor.shutdownNow();
               var1.awaitTermination(this.executor);
            } catch (InterruptedException var9) {
               Thread.currentThread().interrupt();
            } finally {
               this.executor = null;
            }
         }
      }
   }

   public boolean hasCheckedForUpdates(BranchId var1) {
      return this.checkedBranchIds.contains(var1);
   }

   public CheckForUpdatesRequest checkForUpdates(BranchId var1, CheckForUpdatesCallback var2, Timeout var3) {
      UpdateService.InfoConsumer var4 = new UpdateService.InfoConsumer(var1, var2);
      synchronized (this.lock) {
         this.startExecutorIfNecessary();
         Future var6 = this.executor.submit(this.versionInfoSupplier);
         final UpdateService.JobMonitor var7 = new UpdateService.JobMonitor(var6, var4, var3, var2);
         final Future var8 = this.executor.submit(var7);
         this.checkedBranchIds.add(var1);
         return new CheckForUpdatesRequest() {
            @Override
            public void waitUntilDone() throws InterruptedException {
               try {
                  var8.get();
               } catch (ExecutionException var2x) {
                  var2x.printStackTrace();
               }
            }

            @Override
            public void cancel() {
               var7.cancel();
            }
         };
      }
   }

   private void startExecutorIfNecessary() {
      if (this.executor == null) {
         this.executor = Executors.newCachedThreadPool();
      }
   }

   public Cancelable downloadNewVersion(LatestVersion var1, File var2, DownloadNewVersionCallback var3) {
      if (!var1.hasDownloadUrl()) {
         var3.downloadFailed(new Exception("The download URL is missing. Please contact Support."));
         return new Cancelable() {
            @Override
            public boolean cancel() {
               return false;
            }
         };
      }

      UpdateService.Download var4 = new UpdateService.Download(var3);
      String var5 = var1.getDownloadUrl();
      InputSupplier var6 = this.downloadStreamFactory.newStreamSupplier(var5);
      OutputSupplier var7 = Files.newOutputStreamSupplier(var2);
      long var8 = var1.hasFileSize() ? var1.getFileSizeInBytes() : -1L;
      final DownloadJob var10 = new DownloadJob(var6, var7, var8, var4);
      synchronized (this.lock) {
         this.startExecutorIfNecessary();
         final Future var12 = this.executor.submit(var10);
         Cancelable var13 = new Cancelable() {
            @Override
            public boolean cancel() {
               var10.cancel();
               var12.cancel(true);
               return true;
            }
         };
         var4.setJob(var13);
         this.currentDownloads.add(var4);
         return var4;
      }
   }

   private class Download implements DownloadNewVersionCallback, Cancelable {
      private final DownloadNewVersionCallback externalCallback;
      private volatile Cancelable job;

      public Download(DownloadNewVersionCallback var2) {
         this.externalCallback = var2;
      }

      public void setJob(Cancelable var1) {
         this.job = var1;
      }

      @Override
      public boolean cancel() {
         return this.job == null || this.job.cancel();
      }

      @Override
      public void progressUpdate(int var1, String var2) {
         this.externalCallback.progressUpdate(var1, var2);
      }

      @Override
      public void downloadComplete() {
         this.done();
         this.externalCallback.downloadComplete();
      }

      private void done() {
         synchronized (UpdateService.this.lock) {
            UpdateService.this.currentDownloads.remove(this);
         }
      }

      @Override
      public void downloadCanceled() {
         this.done();
         this.externalCallback.downloadCanceled();
      }

      @Override
      public void downloadFailed(Exception var1) {
         this.done();
         this.externalCallback.downloadFailed(var1);
      }
   }

   private class ExitRequestHandler implements ExitListener {
      private ExitRequestHandler() {
      }

      @Override
      public boolean isExitAllowed() {
         synchronized (UpdateService.this.lock) {
            if (UpdateService.this.currentDownloads.isEmpty()) {
               return true;
            }
         }

         return UpdateService.this.exitWhileDownloadingHandler.isExitAllowed();
      }

      @Override
      public void performExitWork() {
         synchronized (UpdateService.this.lock) {
            for (UpdateService.Download var3 : UpdateService.this.currentDownloads) {
               var3.cancel();
            }

            UpdateService.this.shutdownAndAwaitTermination(Timeout.seconds(3L));
         }
      }
   }

   private static class InfoConsumer implements Receiver<VersionInfo> {
      private final BranchId branchId;
      private final CheckForUpdatesCallback callback;

      public InfoConsumer(BranchId var1, CheckForUpdatesCallback var2) {
         this.branchId = var1;
         this.callback = var2;
      }

      public void handle(VersionInfo var1) {
         LatestVersions var2 = var1.getLatestVersions(this.branchId);
         if (var2.equals(LatestVersions.UNKNOWN)) {
            this.callback.resultNotAvailable(UpdateServiceException.unknownLatestVersion());
         } else {
            LatestVersion var3 = var2.getRecommended();
            if (var3.isSameAsOrEarlierThan(this.branchId.currentVersion)) {
               this.callback.isUpToDate();
            } else {
               assert var3.isLaterThan(this.branchId.currentVersion);
               this.callback.laterVersionFound(var3);
            }
         }
      }
   }

   private class JobMonitor implements Runnable {
      private final Future<VersionInfo> infoProducer;
      private final Receiver<VersionInfo> infoConsumer;
      private final Timeout timeout;
      private final CheckForUpdatesCallback callback;
      private volatile boolean canceled;

      public JobMonitor(Future<VersionInfo> var2, Receiver<VersionInfo> var3, Timeout var4, CheckForUpdatesCallback var5) {
         this.infoProducer = var2;
         this.infoConsumer = var3;
         this.timeout = var4;
         this.callback = var5;
      }

      public void cancel() {
         this.canceled = true;
         this.callback.requestCanceled();
         this.infoProducer.cancel(true);
      }

      @Override
      public void run() {
         try {
            VersionInfo var1 = this.timeout.getComputedValue(this.infoProducer);
            if (!this.canceled) {
               this.infoConsumer.handle(var1);
            }
         } catch (TimeoutException var3) {
            this.callback.resultNotAvailable(UpdateServiceException.timeout());
            this.infoProducer.cancel(true);
         } catch (ExecutionException var4) {
            Throwable var2 = var4.getCause();
            this.callback.resultNotAvailable(new UpdateServiceException(ErrorCode.OTHER, var2));
         } catch (InterruptedException var5) {
            var5.printStackTrace();
         }
      }
   }
}
