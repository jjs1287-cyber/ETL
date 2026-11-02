package org.etlforge.integration.client.connect.db2xml.wizard;

import org.etlforge.integration.client.connect.wizard.ConnectWizardPageChain;
import org.etlforge.integration.client.connect.wizard.Folders;
import org.etlforge.integration.client.connect.wizard.OperationBuilder;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.XmlStructure;

final class OperationBuilderImpl extends OperationBuilder<DatabaseStructure, XmlStructure> {
   public OperationBuilderImpl(IntegrationProject var1, Operation var2, Folders var3, ConnectWizardPageChain<DatabaseStructure, XmlStructure> var4) {
      super(EntityType.DatabaseToXmlConnect, var1, var2, var3, var4);
   }

   protected void doAdditionalConfiguration(Transformation var1, DatabaseStructure var2, XmlStructure var3) {
      var1.setSourceId(var2.getSourceId());
   }

   @Override
   protected void doAdditionalConfiguration(OperationPipeline var1) {
      WizardPages var2 = (WizardPages)this.getPages();
      var2.doAdditionalConfiguration(var1);
   }
}
