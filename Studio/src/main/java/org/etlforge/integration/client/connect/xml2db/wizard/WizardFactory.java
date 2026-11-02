package org.etlforge.integration.client.connect.xml2db.wizard;

import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.client.connect.wizard.AbstractConnectWizardFactory;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;

public final class WizardFactory extends AbstractConnectWizardFactory {
   public WizardFactory() {
      super(EntityType.XmlToDatabaseConnect, "XML To Database");
   }

   @Override
   protected Wizard<Operation> createWizardImpl(EntityWizardContext var1, Operation var2) {
      return new XmlToDatabaseWizard(var1.getWindow(), var2, var1.getProjectExplorer());
   }
}
