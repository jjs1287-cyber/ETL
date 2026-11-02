package org.etlforge.integration.client.connect.xml2db.wizard;

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

final class OperationBuilderImpl extends OperationBuilder<XmlStructure, DatabaseStructure> {
   public OperationBuilderImpl(IntegrationProject var1, Operation var2, Folders var3, ConnectWizardPageChain<XmlStructure, DatabaseStructure> var4) {
      super(EntityType.XmlToDatabaseConnect, var1, var2, var3, var4);
   }

   protected void doAdditionalConfiguration(Transformation var1, XmlStructure var2, DatabaseStructure var3) {
      var1.setTargetId(var3.getTargetId());
      WizardPages var4 = (WizardPages)this.getPages();
      var4.doAdditionalConfiguration(var1);
   }

   @Override
   protected void doAdditionalConfiguration(OperationPipeline var1) {
      WizardPages var2 = (WizardPages)this.getPages();
      var2.doAdditionalConfiguration(var1);
   }
}
