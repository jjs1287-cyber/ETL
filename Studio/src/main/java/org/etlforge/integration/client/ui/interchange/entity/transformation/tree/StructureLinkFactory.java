package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.FunctionCallStructure;

final class StructureLinkFactory {
   private final Transformation tx;
   private final IntegrationEntityLookup lookup;

   public StructureLinkFactory(Transformation var1) {
      this.tx = var1;
      this.lookup = ProjectUtils.getProject(var1).getItemLookup();
   }

   public StructureLink inputLink() {
      IntegrationEntity var1 = this.lookup.getEntity(this.tx.getSourceId());
      if (var1 == null) {
         var1 = this.lookup.getEntity(this.tx.getSourceTextDocumentId());
         if (var1 == null) {
            var1 = this.getEntityFromStructure(this.tx.getInputStructure());
         }
      }

      return var1 != null ? new StructureLink(var1) : StructureLink.none();
   }

   private IntegrationEntity getEntityFromStructure(DataStructure var1) {
      if (var1 instanceof FunctionCallStructure) {
         IntegrationEntityId var2 = ((FunctionCallStructure)var1).getFunctionCallId();
         return this.lookup.getEntity(var2);
      } else {
         return null;
      }
   }

   public StructureLink outputLink() {
      IntegrationEntity var1 = this.lookup.getEntity(this.tx.getTargetId());
      if (var1 == null) {
         var1 = this.lookup.getEntity(this.tx.getTargetTextDocumentId());
         if (var1 == null) {
            var1 = this.getEntityFromStructure(this.tx.getOutputStructure());
         }
      }

      return var1 != null ? new StructureLink(var1) : StructureLink.none();
   }
}
