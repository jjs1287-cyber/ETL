package org.etlforge.integration.client.ui.interchange.entity.transformation;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.ui.interchange.entity.transformation.resources.TransformationResources;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.TextDocumentId;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.FunctionCallStructure;

public final class TransformationTreeTitleGenerator {
   private final TransformationTreeTitleGenerator.Generator sourceGenerator;
   private final TransformationTreeTitleGenerator.Generator targetGenerator;

   public TransformationTreeTitleGenerator(Transformation var1) {
      Preconditions.checkNotNull(var1, "tf");
      this.sourceGenerator = getSourceGenerator(var1);
      this.targetGenerator = getTargetGenerator(var1);
   }

   public String getSourceTreeTitle() {
      return this.sourceGenerator.getTitle();
   }

   public String getTargetTreeTitle() {
      return this.targetGenerator.getTitle();
   }

   private static TransformationTreeTitleGenerator.Generator getSourceGenerator(final Transformation var0) {
      return new TransformationTreeTitleGenerator.Generator(var0) {
         @Override
         public IntegrationEntityId getDatabaseId() {
            return var0.getSourceId();
         }

         @Override
         public DataStructure getDataStructure() {
            return var0.getInputStructure();
         }

         @Override
         protected TextDocumentId getTextDocumentId() {
            return var0.getSourceTextDocumentId();
         }
      };
   }

   private static TransformationTreeTitleGenerator.Generator getTargetGenerator(final Transformation var0) {
      return new TransformationTreeTitleGenerator.Generator(var0) {
         @Override
         public IntegrationEntityId getDatabaseId() {
            return var0.getTargetId();
         }

         @Override
         public DataStructure getDataStructure() {
            return var0.getOutputStructure();
         }

         @Override
         protected TextDocumentId getTextDocumentId() {
            return var0.getTargetTextDocumentId();
         }
      };
   }

   private abstract static class Generator {
      protected final Transformation transformation;

      protected Generator(Transformation var1) {
         this.transformation = var1;
      }

      public String getTitle() {
         TextDocumentId var1 = this.getTextDocumentId();
         if (var1 != null) {
            return this.getEntityName(var1);
         }

         IntegrationEntityId var2 = this.getDatabaseId();
         if (var2 != null) {
            return this.getEntityName(var2);
         }

         IntegrationEntityId var3 = this.getFunctionCallId();
         if (var3 != null) {
            return this.getFunctionCallName(var3);
         }

         DataStructure var4 = this.getDataStructure();
         return var4 != null ? var4.getName() : TransformationResources.getString("UknownStructure");
      }

      private IntegrationProject getProject() {
         return ProjectUtils.getProject(this.transformation);
      }

      protected abstract IntegrationEntityId getDatabaseId();

      private IntegrationEntityId getFunctionCallId() {
         DataStructure var1 = this.getDataStructure();
         return var1 instanceof FunctionCallStructure ? ((FunctionCallStructure)var1).getFunctionCallId() : null;
      }

      private String getFunctionCallName(IntegrationEntityId var1) {
         String var2 = var1.getEntityType().getDisplayName();
         String var3 = this.getEntityName(var1);
         return var3.isEmpty() ? var2 : var2 + ": " + var3;
      }

      protected abstract DataStructure getDataStructure();

      protected abstract TextDocumentId getTextDocumentId();

      private String getEntityName(IntegrationEntityId var1) {
         IntegrationProject var2 = this.getProject();
         if (var2 == null) {
            return "";
         }

         IntegrationEntity var3 = var2.getItemLookup().getEntity(var1);
         return var3 == null ? "" : var3.getName();
      }
   }
}
