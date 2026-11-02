package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.client.structure.cache.CacheWriter;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources.TreeStrings;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;
import org.etlforge.lang.exception.ExceptionHandler;

public final class LoadStructuresJob extends UiJob implements ExceptionHandler<Throwable> {
   private final LoadStructureInvoker invoker;
   private final MappingStructureFactory structureFactory;
   private final InputAndOutputMappingTreeStructureCache cache;
   private final boolean checkCacheFirst;
   private MappingTreeStructure inputJtr;
   private MappingTreeStructure outputJtr;

   public LoadStructuresJob(LoadStructureInvoker var1, MappingStructureFactory var2, InputAndOutputMappingTreeStructureCache var3, boolean var4) {
      super(TreeStrings.format("LoadStructures.Name", var1.getTransformation().getName()));
      this.invoker = var1;
      this.structureFactory = var2;
      this.cache = var3;
      this.checkCacheFirst = var4;
      this.setUncaughtExceptionHandler(this);
      var1.jobHasBeenInitialized();
   }

   @Override
   protected void runImpl() throws InterruptedException {
      if (!this.isCancelled()) {
         if (this.checkCacheFirst) {
            this.checkCache();
         }

         if (!this.isCancelled()) {
            try {
               if (this.getInputJtr() == null && this.invoker.isAskingForInputStructure()) {
                  this.retrieveInputStructure();
               }

               if (this.isCancelled()) {
                  return;
               }

               if (this.getOutputJtr() == null && this.invoker.isAskingForOutputStructure()) {
                  this.retrieveOutputStructure();
               }

               if (this.isCancelled()) {
                  return;
               }

               this.setMessage(UiJobMessages.UPDATE_UI);
               this.invoker.displayLoadedStructures(this.getInputJtr(), this.getOutputJtr());
            } catch (MappingTreeStructureException var2) {
               this.handleError(var2);
            }

            this.invoker.jobHasCompleted();
         }
      }
   }

   private void checkCache() {
      Transformation var1 = this.invoker.getTransformation();

      try {
         MappingTreeStructure var2 = this.cache.getInputStructure(var1.getID()).orNull();
         MappingTreeStructure var3 = this.cache.getOutputStructure(var1.getID()).orNull();
         if (var2 != null && !var2.outOfDate()) {
            this.setInputJtr(var2);
         }

         if (var3 != null && !var3.outOfDate()) {
            this.setOutputJtr(var3);
         }
      } catch (DataCacheException var4) {
         this.cache.delete(var1.getID());
      }
   }

   private void retrieveInputStructure() throws MappingTreeStructureException, InterruptedException {
      this.setMessage(TreeStrings.get("LoadStructures.Input"));
      Transformation var1 = this.invoker.getTransformation();
      MappingTreeStructure var2 = this.structureFactory.getInputStructure(var1);
      if (var2 == null) {
         this.invoker.cancelled();
         this.cancel();
      } else {
         this.setInputJtr(var2);
         this.writeToCache(var1, var2, CacheWriter.newInputWriter());
      }

      this.invoker.jobHasCompleted();
   }

   private void writeToCache(Transformation var1, MappingTreeStructure var2, CacheWriter var3) {
      var3.setErrorHandler(this.getCacheWriterErrorHandler());
      var3.addToCache(this.cache, var1.getID(), var2);
   }

   private ExceptionHandler<? super Exception> getCacheWriterErrorHandler() {
      return new LoggingExceptionHandler(
         LoadStructuresJob.class, "Failed to update the mapping structure cache for the transformation with ID = " + this.invoker.getTransformation().getID()
      );
   }

   private void handleError(MappingTreeStructureException var1) {
      if (!this.isCancelled()) {
         this.setStatus(JobStatus.ERROR);
         String var2 = TreeStrings.format("LoadStructures.Error.Message", this.invoker.getTransformation().getName());
         this.setMessage(var2);
         ErrorLog.attention(LoadStructuresJob.class, var2, var1);
         this.invoker.jobHasCompleted();
      }
   }

   private void retrieveOutputStructure() throws MappingTreeStructureException, InterruptedException {
      this.setMessage(TreeStrings.get("LoadStructures.Output"));
      Transformation var1 = this.invoker.getTransformation();
      MappingTreeStructure var2 = this.structureFactory.getOutputStructure(var1);
      if (var2 == null) {
         this.invoker.cancelled();
         this.cancel();
      } else {
         this.setOutputJtr(var2);
         this.writeToCache(var1, var2, CacheWriter.newOutputWriter());
      }

      this.invoker.jobHasCompleted();
   }

   @Override
   protected void onJobCancelled() {
      this.invoker.cancelled();
      this.invoker.jobHasCompleted();
   }

   @Override
   public void caught(Throwable var1) {
      if (!this.isCancelled()) {
         this.setMessage(UiJobMessages.ERROR);
         ErrorLog.attention(LoadStructuresJob.class, UiJobMessages.ERROR, var1);
         this.invoker.jobHasCompleted();
      }
   }

   private synchronized void setInputJtr(MappingTreeStructure var1) {
      this.inputJtr = var1;
   }

   private synchronized void setOutputJtr(MappingTreeStructure var1) {
      this.outputJtr = var1;
   }

   private synchronized MappingTreeStructure getInputJtr() {
      return this.inputJtr;
   }

   private synchronized MappingTreeStructure getOutputJtr() {
      return this.outputJtr;
   }
}
