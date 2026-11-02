package org.etlforge.integration.client.ui.interchange.entity.operation;

import org.etlforge.integration.client.ui.operation.prefs.DefaultOperationTypePreference;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.factory.DefaultIntegrationEntityFactory;
import org.etlforge.integration.data.entity.factory.IntegrationEntityFactory;
import org.etlforge.integration.data.entity.factory.IntegrationEntityFactoryException;
import org.etlforge.integration.data.entity.operation.OperationPreferences;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;

final class OperationFactory implements IntegrationEntityFactory {
   private final OperationPreferences operationPrefs;

   public OperationFactory(OperationPreferences var1) {
      this.operationPrefs = var1;
   }

   public Operation createEntity(Folder var1) throws IntegrationEntityFactoryException {
      Operation var2 = this.create(var1);
      this.customize(var2);
      return var2;
   }

   private Operation create(Folder var1) throws IntegrationEntityFactoryException {
      DefaultIntegrationEntityFactory var2 = new DefaultIntegrationEntityFactory(EntityType.Operation);
      return (Operation)var2.createEntity(var1);
   }

   private void customize(Operation var1) {
      this.setDefaultPipeline(var1);
      this.operationPrefs.apply(var1);
   }

   private void setDefaultPipeline(Operation var1) {
      OperationType var2 = DefaultOperationTypePreference.get();
      var1.setOperationType(var2);
      OperationPipeline var3 = var2.createNewTemplate();
      var1.setPipeline(var3);
   }
}
