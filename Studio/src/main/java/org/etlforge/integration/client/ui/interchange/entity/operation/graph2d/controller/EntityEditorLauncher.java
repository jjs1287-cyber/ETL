package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SystemTransformation;
import org.etlforge.integration.data.entity.operation.pipeline.EntityActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;

public final class EntityEditorLauncher {
   public static void openEditor(PipelineActivityNode var0) {
      PipelineActivity var1 = var0.getDataObject();
      if (var1 instanceof EntityActivity) {
         EntityActivity var2 = (EntityActivity)var1;
         IntegrationEntity var3 = (IntegrationEntity)var2.getContent();
         if (var3 instanceof BuiltInIntegrationEntity) {
            handleSystemEntity(var0, var2, var3);
         } else {
            UiCompanion.of(var3).openPage();
         }
      }
   }

   private static void handleSystemEntity(PipelineActivityNode var0, EntityActivity var1, IntegrationEntity var2) {
      if (var2 != SystemTransformation.SALESFORCE_QUERY_REQUEST || !openQueryObject(var0)) {
         Alert.error(Strings.format("UneditableItem.Name", var1.getTypeName()), Strings.get("Uneditable.Title"));
      }
   }

   private static boolean openQueryObject(PipelineActivityNode var0) {
      Operation var1 = var0.getParent().getOperation();
      IntegrationEntityId var2 = var1.getSalesforceWizardGuid();
      if (var2 != null) {
         IntegrationEntity var3 = lookupWizard(var1, var2);
         if (var3 != null) {
            UiCompanion.of(var3).openPage();
            return true;
         }
      }

      return false;
   }

   private static IntegrationEntity lookupWizard(Operation var0, IntegrationEntityId var1) {
      IntegrationProject var2 = ProjectUtils.getProject(var0);
      IntegrationEntityLookup var3 = var2.getItemLookup();
      return var3.getEntity(var1);
   }
}
