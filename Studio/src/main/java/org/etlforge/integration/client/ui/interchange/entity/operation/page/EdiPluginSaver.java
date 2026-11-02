package org.etlforge.integration.client.ui.interchange.entity.operation.page;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePlugin;
import org.etlforge.integration.client.plugin.pipeline.MutableAppliedPipelinePluginStore;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.TransformationOperationType;
import org.etlforge.integration.data.entity.operation.WebServiceOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.transformation.TransformationPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.ws.WebServicePipelineBrowser;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.structure.edi.EdiPlugin;
import org.etlforge.integration.plugin.PluginIdentifier;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;

class EdiPluginSaver {
   private final ManagedProject project;
   private boolean needsSaving;

   public EdiPluginSaver(ManagedProject var1) {
      this.project = var1;
   }

   public void applyPluginIfNeeded(OperationPipeline var1) {
      this.needsSaving = false;
      OperationType var2 = var1.getType();
      if (var2 == TransformationOperationType.TYPE) {
         this.checkTransformation(var1);
      } else if (var2 == WebServiceOperationType.TYPE) {
         this.checkWebServiceCall(var1);
      }
   }

   private void checkTransformation(OperationPipeline var1) {
      TransformationPipelineBrowser var2 = new TransformationPipelineBrowser(var1);
      Transformation var3 = var2.getTransformation();
      if (var3 != null) {
         if (var3.isXmlSourceEdi()) {
            Source var4 = var2.getSource();
            this.applyPluginToSourceOrTarget(var4);
         }

         if (var3.isXmlTargetEdi()) {
            Target var5 = var2.getTarget();
            this.applyPluginToSourceOrTarget(var5);
         }
      }
   }

   private void applyPluginToSourceOrTarget(IntegrationEntity var1) {
      if (var1 != null) {
         MutableAppliedPipelinePluginStore var2 = this.project.getPipelinePlugins().getAppliedPlugins();
         AppliedPipelinePlugin[] var3 = var2.getAllAssociations(var1);
         if (!this.isEdiPluginAlreadyThere(var3)) {
            List var4 = this.createNewPluginList(var1, var3);
            var2.setAssociations(var1, var4);
            this.needsSaving = true;
         }
      }
   }

   private boolean isEdiPluginAlreadyThere(AppliedPipelinePlugin[] var1) {
      PluginIdentifier var2 = EdiPlugin.getPluginId();

      for (AppliedPipelinePlugin var6 : var1) {
         if (var6.getPluginId().equals(var2)) {
            return true;
         }
      }

      return false;
   }

   private List<AppliedPipelinePlugin> createNewPluginList(IntegrationEntity var1, AppliedPipelinePlugin[] var2) {
      ArrayList var3 = Lists.newArrayList(var2);
      AppliedPipelinePlugin var4 = this.createEdiPlugin(var1);
      var3.add(var4);
      return var3;
   }

   private AppliedPipelinePlugin createEdiPlugin(IntegrationEntity var1) {
      PipelinePosition var2 = EdiPlugin.getPipelinePosition(var1.getEntityType());
      return new AppliedPipelinePlugin(var1.getID(), var2);
   }

   private void checkWebServiceCall(OperationPipeline var1) {
      WebServicePipelineBrowser var2 = new WebServicePipelineBrowser(var1);
      Transformation var3 = var2.getRequest();
      if (var3 != null && var3.isXmlSourceEdi()) {
         Source var4 = var2.getSource();
         this.applyPluginToSourceOrTarget(var4);
      }

      Transformation var6 = var2.getResponse();
      if (var6 != null && var6.isXmlTargetEdi()) {
         Target var5 = var2.getTarget();
         this.applyPluginToSourceOrTarget(var5);
      }
   }

   public void saveChanges() {
      if (this.needsSaving) {
         try {
            this.project.savePluginInfo();
         } catch (InterchangeSavingException var2) {
            ErrorLog.log(EdiPluginSaver.class, null, var2);
         }
      }
   }
}
