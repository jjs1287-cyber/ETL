package org.etlforge.integration.filetransfer.client.upload;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import org.etlforge.application.filestore.FileStoreFile;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.ServerFileUploadResult;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;

public abstract class FileUploader {
   private final FileUploadApi uploadApi;
   private final FileUploadClient client;
   private final String firstFile;
   private final Map<String, ServerFile> uploadedFiles = Maps.newHashMap();
   private final Set<File> queuedFiles = Sets.newHashSet();
   private final Map<ServerFile, List<File>> dependencies = Maps.newHashMap();
   private final SetMultimap<ServerFile, String> alreadyUploadedButServerNeedsMeAgain = HashMultimap.create();
   private volatile ServerInfo serverInfo;
   private volatile RequiredFilesProvider reqFilesProvider = new RequiredFilesProvider() {
      @Nullable
      @Override
      public List<RequiredFiles> locateRequiredFiles(List<RequiredFiles> var1, File var2) {
         return RequiredFilesPrompter.getInput(var1, var2);
      }
   };
   private volatile boolean done;
   private volatile boolean success = true;

   public FileUploader(FileUploadApi var1, FileUploadClient var2, String var3) {
      this.uploadApi = Preconditions.checkNotNull(var1, "uploadApi");
      this.client = Preconditions.checkNotNull(var2, "client");
      this.firstFile = Preconditions.checkNotNull(var3, "file");
   }

   public final FileUploadApi getUploadApi() {
      return this.uploadApi;
   }

   public final void setRequiredFilesProvider(RequiredFilesProvider var1) {
      this.reqFilesProvider = Preconditions.checkNotNull(var1);
   }

   protected abstract FileUploadJob createUploadJob(FileUploadApi var1, ServerFile var2, ImmutableList<File> var3);

   public final void start() throws InterruptedException {
      this.uploadFirstFile();
      if (!this.isDone()) {
         File var1 = new File(this.firstFile).getParentFile();

         while (!this.isDone() && this.moreFilesAreRequired()) {
            List var2 = this.getRemainingDependencies();
            if (!var2.isEmpty()) {
               List var3 = this.reqFilesProvider.locateRequiredFiles(this.getRemainingDependencies(), var1);
               if (var3 == null) {
                  this.cancelled();
                  break;
               }

               for (Object __etlItem74 : var3) {
                  RequiredFiles var5 = (RequiredFiles) __etlItem74;
                  this.uploadNewBatch(var5);
                  if (this.isDone()) {
                     break;
                  }
               }
            }

            if (!this.alreadyUploadedButServerNeedsMeAgain.isEmpty()) {
               for (ServerFile var15 : Lists.newArrayList(this.alreadyUploadedButServerNeedsMeAgain.keySet())) {
                  Set var6 = this.alreadyUploadedButServerNeedsMeAgain.get(var15);
                  if (!var6.isEmpty()) {
                     ArrayList var7 = Lists.newArrayList();

                     for (Object __etlItem88 : var6) {
                        String var9 = (String) __etlItem88;
                        FileStoreFile var10 = this.client.getFileStore().getFile(var9);
                        if (var10 == null) {
                           String var11 = "An internal error occurred. Please contact ETLForge support.\n\nThe path '"
                              + var9
                              + "' was not found in the client filestore, even though it was just uploaded to the server.";
                           Exception var12 = new Exception(var11);
                           this.caught(var12);
                        } else {
                           var7.add(var10.getPhysicalRepresentation());
                        }
                     }

                     if (this.isDone()) {
                        break;
                     }

                     this.uploadNewBatch(new RequiredFiles(var15, var7));
                  }
               }
            }
         }

         this.done = true;
         if (this.success) {
            this.notifyClientOnSuccess();
         }
      }
   }

   public final synchronized void abort() {
      this.cancelled();
   }

   private void notifyClientOnSuccess() {
      ServerFile var1 = this.uploadedFiles.get(new File(this.firstFile).getName());
      this.client.uploadComplete(this.serverInfo, var1, this.uploadedFiles.values().toArray(new ServerFile[this.uploadedFiles.size()]));
   }

   public final boolean isSuccess() {
      return this.isDone() && this.success;
   }

   final synchronized void cancelled() {
      this.done = true;
      this.success = false;
      this.client.uploadCancelled();
   }

   final synchronized void caught(Throwable var1) {
      this.done = true;
      this.success = false;
      this.client.handleError(var1);
   }

   final synchronized void fileWasUploaded(String var1, ServerFileUploadResult var2, ServerFile var3) {
      this.serverInfo = var2.getServer();
      File var4 = new File(new File(var1).getName());
      this.client.fileWasUploaded(var1, var3, var2);
      this.uploadedFiles.put(var4.getName(), var2.getServerFile());
      this.queuedFiles.remove(var4);
      this.alreadyUploadedButServerNeedsMeAgain.remove(var3, var2.getServerFile().getFilePath());
      List var5 = this.dependencies.get(var3);
      if (var5 != null) {
         var5.remove(new File(var4.getName()));
         if (var5.isEmpty()) {
            this.dependencies.remove(var3);
         }
      }

      this.registerIncludedOrMissingDependencies(var2);
   }

   private Set<File> getAllDependentFiles() {
      HashSet var1 = Sets.newHashSet();

      for (Entry var3 : this.dependencies.entrySet()) {
         var1.addAll((Collection)var3.getValue());
      }

      return var1;
   }

   private Set<File> getDependentFiles(ServerFile var1) {
      List var2 = this.dependencies.get(var1);
      return var2 == null ? Sets.newHashSet() : Sets.newHashSet(var2);
   }

   private void registerIncludedOrMissingDependencies(ServerFileUploadResult var1) {
      Set var2 = this.getDependentFiles(var1.getServerFile());
      Set var3 = this.getAllDependentFiles();

      for (ServerFile var5 : var1.getExistingDependencies()) {
         String var6 = var5.getFilePath();
         File var7 = new File(new File(var6).getName());
         if (!this.uploadedFiles.containsKey(var7.getName()) && !this.queuedFiles.contains(var7) && !var3.contains(var7)) {
            var2.add(var7);
         }
      }

      for (String var9 : var1.getMissingFiles()) {
         File var10 = new File(new File(var9).getName());
         if (!this.uploadedFiles.containsKey(var10.getName()) && !this.queuedFiles.contains(var10)) {
            var2.add(var10);
         } else {
            this.alreadyUploadedButServerNeedsMeAgain.put(var1.getServerFile(), var9);
         }
      }

      if (var2.size() > 0) {
         this.dependencies.put(var1.getServerFile(), Lists.newArrayList(var2));
      }
   }

   private void uploadFirstFile() throws InterruptedException {
      ImmutableList var1 = this.queueFirstFile();
      this.submitJob(null, var1);
   }

   private void uploadNewBatch(RequiredFiles var1) throws InterruptedException {
      ImmutableList var2 = this.queueRequiredFiles(var1);
      this.submitJob(var1.getParentFile(), var2);
   }

   private ImmutableList<File> queueFirstFile() {
      File var1 = new File(this.firstFile);
      this.queuedFiles.add(new File(var1.getName()));
      return ImmutableList.of(var1);
   }

   private ImmutableList<File> queueRequiredFiles(RequiredFiles var1) {
      ImmutableList var2 = var1.getRequiredFiles();

      for (Object __etlItem221 : var2) {
         File var4 = (File) __etlItem221;
         this.queuedFiles.add(new File(var4.getName()));
      }

      return var2;
   }

   private void submitJob(ServerFile var1, ImmutableList<File> var2) throws InterruptedException {
      for (File var4 : var2) {
         if (!this.client.isFileClearedForUpload(var4)) {
            this.cancelled();
            return;
         }
      }

      FileUploadJob var5 = this.createUploadJob(this.uploadApi, var1, var2);
      var5.submit();
      var5.waitUntilDone();
   }

   private synchronized boolean moreFilesAreRequired() {
      return !this.dependencies.isEmpty() || !this.alreadyUploadedButServerNeedsMeAgain.isEmpty();
   }

   private synchronized List<RequiredFiles> getRemainingDependencies() {
      ArrayList var1 = Lists.newArrayList();

      for (Entry var3 : this.dependencies.entrySet()) {
         var1.add(new RequiredFiles((ServerFile)var3.getKey(), (List<File>)var3.getValue()));
      }

      return var1;
   }

   private boolean isDone() {
      return this.done || this.client.isAborted();
   }
}
