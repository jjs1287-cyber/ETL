package org.etlforge.integration.client.connect.wizard;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.TransformationOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.transformation.TransformationAutoCompleter;
import org.etlforge.integration.data.entity.operation.pipeline.transformation.TransformationPipelineBrowser;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.structure.DataStructure;

public class OperationBuilder<S extends DataStructure, T extends DataStructure> {
   private final EntityType connectType;
   private final IntegrationProject project;
   private final Operation operation;
   private final ConnectWizardPageChain<S, T> pages;
   private final String wizardName;
   private Transformation transformation;
   private final Folders folders;

   public OperationBuilder(EntityType var1, IntegrationProject var2, Operation var3, Folders var4, ConnectWizardPageChain<S, T> var5) {
      this.connectType = var1;
      this.project = var2;
      this.operation = var3;
      this.folders = var4;
      this.pages = var5;
      this.wizardName = var5.getWizardName();
   }

   protected final ConnectWizardPageChain<S, T> getPages() {
      return this.pages;
   }

   public final Transformation getTransformation() {
      return this.transformation;
   }

   public final void build() {
      this.transformation = this.createTransformation();
      this.configureOperation();
      this.insertOperation();
   }

   private Transformation createTransformation() {
      Folder var1 = this.getTransformationParent();
      String var2 = EntityUtils.getAvailableChildName(var1, this.wizardName);
      Transformation var3 = new Transformation(var2);
      DataStructure var4 = this.pages.getSourceStructure();
      DataStructure var5 = this.pages.getTargetStructure();
      var3.setInputStructure(var4);
      var3.setOutputStructure(var5);
      this.doAdditionalConfiguration(var3, (S)var4, (T)var5);
      this.project.insertEntity(var3, var1);
      return var3;
   }

   protected void doAdditionalConfiguration(Transformation var1, S var2, T var3) {
   }

   private Folder getTransformationParent() {
      return this.folders.getTransformationParent(true);
   }

   private void configureOperation() {
      OperationPipeline var1 = this.createPipeline();
      this.operation.setOperationType(var1.getType());
      this.operation.setPipeline(var1);
      this.operation.setConnectType(this.connectType);
   }

   private OperationPipeline createPipeline() {
      OperationPipeline var1 = TransformationOperationType.TYPE.createNewTemplate();
      TransformationPipelineBrowser var2 = new TransformationPipelineBrowser(var1);
      var2.setTransformation(this.transformation);
      TransformationAutoCompleter var3 = new TransformationAutoCompleter(this.project.getItemLookup());
      var3.complete(var1, var2.getTransformationActivity());
      this.doAdditionalConfiguration(var1);
      return var1;
   }

   protected void doAdditionalConfiguration(OperationPipeline var1) {
   }

   private void insertOperation() {
      Folder var1 = this.folders.getOperationParent(true);
      String var2 = EntityUtils.getAvailableChildName(var1, this.wizardName);
      this.operation.setName(var2);
      this.project.insertEntity(this.operation, var1);
   }
}
