package org.etlforge.integration.client.connect.text2db;

import org.etlforge.integration.client.connect.text2db.wizard.WizardFactory;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.entity.AbstractEntityConfig;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.factory.DefaultIntegrationEntityFactory;
import org.etlforge.integration.data.entity.factory.IntegrationEntityFactory;

public final class TextToDatabaseConnectConfig extends AbstractEntityConfig {
   public TextToDatabaseConnectConfig() {
      super(EntityType.TextToDatabaseConnect);
   }

   @Override
   public IntegrationEntityFactory getFactory() {
      return new DefaultIntegrationEntityFactory(EntityType.Operation);
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      return null;
   }

   @Override
   public EntityWizardFactory getEntityWizardFactory() {
      return new WizardFactory();
   }
}
