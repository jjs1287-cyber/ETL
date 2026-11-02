package org.etlforge.integration.client.ui.entity;

import com.google.common.base.Optional;
import org.etlforge.integration.client.ui.entity.factory.Request;
import org.etlforge.integration.client.ui.entity.factory.Result;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.data.entity.EntityPath;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;

public abstract class ProjectItemTransform {
   private final UiEntityFactory worker;
   private final EntityType destinationType;

   protected ProjectItemTransform(UiEntityFactory var1, EntityType var2) {
      this.worker = var1;
      this.destinationType = var2;
   }

   public boolean isSupported(IntegrationEntity var1, Optional<Folder> var2) {
      return true;
   }

   public final void doTransform(IntegrationEntity var1) {
      Folder var2 = this.findParent(var1);
      this.doTransform(var1, var2);
   }

   private Folder findParent(IntegrationEntity var1) {
      IntegrationProject var2 = ProjectUtils.getProject(var1);
      if (var2 == null) {
         throw new UnsupportedOperationException("The original item must be a part of a project.");
      }

      EntityPath var3 = new EntityPath(var1, false);
      RootFolder var4 = var2.getRootFolder(this.destinationType);
      Folder var5 = var4.matchPathByName(var3);
      if (var5 == null) {
         var5 = var4;
      }

      return var5;
   }

   public final void doTransform(IntegrationEntity var1, Folder var2) {
      if (var2 == null) {
         var2 = this.findParent(var1);
      }

      boolean var3 = this.isTemplateClean(var1);
      if (var3) {
         Request var4 = Request.builder(this.destinationType).withParent(var2).openEditor(true).build();
         this.worker.createEntity(var4, new ProjectItemTransform.Customizer(var1));
      }
   }

   private boolean isTemplateClean(IntegrationEntity var1) {
      UiCompanion var2 = UiCompanion.of(var1);
      return var2.saveIfDirty();
   }

   public final EntityType getDestinationType() {
      return this.destinationType;
   }

   protected abstract void configureDestination(IntegrationEntity var1, IntegrationEntity var2);

   protected String getSuggestedNameOfNewItem(IntegrationEntity var1) {
      return var1.getName();
   }

   private class Customizer implements UiEntityFactoryCallback {
      private final IntegrationEntity originalItem;

      public Customizer(IntegrationEntity var2) {
         this.originalItem = var2;
      }

      @Override
      public final Result preProjectInsertion(IntegrationEntity var1, Folder var2) {
         ProjectItemTransform.this.configureDestination(this.originalItem, var1);
         return Result.continueProcess(var2);
      }

      @Override
      public final void postProjectInsertion(IntegrationEntity var1) {
         String var2 = ProjectItemTransform.this.getSuggestedNameOfNewItem(this.originalItem);
         String var3 = EntityUtils.getAvailableName(var1, var2);
         var1.setName(var3);
      }
   }
}
