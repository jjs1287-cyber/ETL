package org.etlforge.integration.client.connect.db2db.wizard;

import org.etlforge.integration.client.connect.wizard.Folders;
import org.etlforge.integration.client.connect.wizard.OperationBuilder;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.structure.DatabaseStructure;

final class OperationBuilderImpl extends OperationBuilder<DatabaseStructure, DatabaseStructure> {
   public OperationBuilderImpl(IntegrationProject var1, Operation var2, Folders var3, WizardPages var4) {
      super(EntityType.DatabaseToDatabaseConnect, var1, var2, var3, var4);
   }

   protected void doAdditionalConfiguration(Transformation var1, DatabaseStructure var2, DatabaseStructure var3) {
      var1.setSourceId(var2.getSourceId());
      var1.setTargetId(var3.getTargetId());
      WizardPages var4 = (WizardPages)this.getPages();
      var4.applyInsertMode(var1);
   }
}
