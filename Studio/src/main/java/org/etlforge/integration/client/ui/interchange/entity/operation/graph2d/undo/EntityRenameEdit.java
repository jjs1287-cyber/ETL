package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.GraphResources;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.project.ProjectUtils;

public final class EntityRenameEdit extends AbstractUndoableEdit {
   private final IntegrationEntityId entityId;
   private final IntegrationEntityLookup entityLookup;
   private final String oldName;
   private final String newName;

   public EntityRenameEdit(IntegrationEntity var1, String var2) {
      this.entityId = var1.getID();
      this.entityLookup = ProjectUtils.getLookup(var1);
      this.oldName = var1.getName();
      this.newName = var2;
   }

   @Override
   public void redo() throws CannotRedoException {
      super.redo();
      this.setName(this.newName);
   }

   @Override
   public void undo() throws CannotUndoException {
      super.undo();
      this.setName(this.oldName);
   }

   @Override
   public String getPresentationName() {
      return GraphResources.format("EntityNameEdit", this.entityId.getEntityType());
   }

   private void setName(String var1) {
      IntegrationEntity var2 = this.entityLookup.getEntity(this.entityId);
      if (var2 != null) {
         UiCompanion.of(var2).rename(var1);
      } else {
         Alert.error("This change cannot be undone/redone.", "Not Undoable/Redoable");
      }
   }
}
