package org.etlforge.integration.client.ui.connector;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.concurrent.Callable;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.connector.schema.DownloadedSchemasProcessor;
import org.etlforge.integration.client.connector.serverapi.Schemas;
import org.etlforge.integration.client.ui.interchange.entity.MappingStructureRefresher;
import org.etlforge.integration.data.connector.ConnectorCall;
import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.FunctionCallRequestStructure;
import org.etlforge.integration.data.structure.FunctionCallResponseStructure;
import org.etlforge.integration.data.structure.FunctionCallStructureFilter;
import org.etlforge.integration.data.structure.FunctionCallTransformationFilter;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;

public class RefreshFunctionJob implements Callable<Void> {
   private final ConnectorCall function;
   private final Callable<Schemas> schemaGenerator;
   private final DownloadedSchemasProcessor schemaProcessor;
   private final Runnable pageRefresher;
   private final MappingStructureRefresher txRefresher;
   private final FunctionCallStructureFilter structureFilter;

   public RefreshFunctionJob(RefreshFunctionJob var1) {
      this(var1.function, var1.schemaGenerator, var1.schemaProcessor, var1.pageRefresher, var1.txRefresher);
   }

   public RefreshFunctionJob(ConnectorCall var1, Callable<Schemas> var2, DownloadedSchemasProcessor var3, Runnable var4, MappingStructureRefresher var5) {
      this.function = var1;
      this.schemaGenerator = var2;
      this.schemaProcessor = var3;
      this.pageRefresher = var4;
      this.txRefresher = var5;
      this.structureFilter = new FunctionCallStructureFilter(var1);
   }

   public Void call() throws Exception {
      Schemas var1 = this.schemaGenerator.call();
      if (var1 != null && !this.isCanceled()) {
         boolean var2 = this.isRootChanged(var1);
         DownloadedSchemasProcessor.Result var3 = this.schemaProcessor.process(var1);
         if (this.isCanceled()) {
            return null;
         }

         if (var3.wasFileStoreUpdated()) {
            this.refreshStructures(var2);
         }

         return null;
      } else {
         return null;
      }
   }

   protected boolean save() throws InterchangeSavingException {
      NetSuiteFunction var1 = (NetSuiteFunction)this.function;
      return var1.save();
   }

   private boolean isCanceled() {
      return Thread.currentThread().isInterrupted();
   }

   private void refreshStructures(boolean var1) throws InterchangeSavingException {
      this.pageRefresher.run();
      if (!this.isCanceled()) {
         this.refreshTransformations(var1);
      }
   }

   private void refreshTransformations(boolean var1) throws InterchangeSavingException {
      List var2 = this.collectAffectedTransformations();
      if (var1) {
         this.updateAffectedTransformationStructures(var2);
      }

      for (Object __etlItem87 : var2) {
         Transformation var4 = (Transformation) __etlItem87;
         try {
            this.txRefresher.refreshInputAndOuputStructures(var4);
         } catch (MappingTreeStructureException var6) {
            ErrorLog.log(this.getClass(), null, var6);
         }

         if (this.isCanceled()) {
            return;
         }
      }
   }

   private List<Transformation> collectAffectedTransformations() {
      IntegrationProject var1 = ProjectUtils.getProject((IntegrationEntity)this.function);
      return var1 == null
         ? ImmutableList.of()
         : var1.getAllEntitiesOfClass(Transformation.class).filter(new FunctionCallTransformationFilter(this.function)).toImmutableList();
   }

   private boolean isRootChanged(Schemas var1) {
      return this.function.getInputStructure() != null && !isEqual(var1.getRequestRootName(), this.function.getInputStructure().getRootName())
         ? true
         : !isEqual(var1.getResponseRootName(), this.function.getOutputStructure().getRootName());
   }

   private static boolean isEqual(String var0, String var1) {
      return Strings.isNullOrEmpty(var0) ? Strings.isNullOrEmpty(var1) : var0.equals(var1);
   }

   private void updateAffectedTransformationStructures(List<Transformation> var1) throws InterchangeSavingException {
      ((IntegrationEntity)this.function).save();

      for (Transformation var3 : var1) {
         synchronized (var3.getDataLock()) {
            DataStructure var5 = var3.getInputStructure();
            boolean var6 = true;
            this.updateTransformationStructureIfNeeded(var3, var6, var5);
            DataStructure var7 = var3.getOutputStructure();
            var6 = false;
            this.updateTransformationStructureIfNeeded(var3, var6, var7);
         }

         var3.save();
      }
   }

   private void updateTransformationStructureIfNeeded(Transformation var1, boolean var2, DataStructure var3) {
      if (this.structureFilter.apply(var3)) {
         DataStructure var4 = this.getNewFunctionStructure(var3);
         if (var2) {
            var1.setInputStructure(var4);
         } else {
            var1.setOutputStructure(var4);
         }
      }
   }

   private DataStructure getNewFunctionStructure(DataStructure var1) {
      if (var1 instanceof FunctionCallRequestStructure) {
         return this.function.getInputStructure();
      } else {
         return var1 instanceof FunctionCallResponseStructure ? this.function.getOutputStructure() : null;
      }
   }
}
