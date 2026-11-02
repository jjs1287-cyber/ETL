package org.etlforge.integration.client.connect.db2text.wizard;

import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.client.connect.wizard.AbstractConnectWizardFactory;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;

public final class WizardFactory extends AbstractConnectWizardFactory {
   public WizardFactory() {
      super(EntityType.DatabaseToTextConnect, "Database To Text");
   }

   @Override
   protected Wizard<Operation> createWizardImpl(EntityWizardContext var1, Operation var2) {
      return new DatabaseToTextWizard(var1.getWindow(), var2, var1.getProjectExplorer());
   }
}
