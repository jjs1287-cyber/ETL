package org.etlforge.integration.client.connect.db2xml.wizard;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.connect.wizard.AbstractConnectWizard;
import org.etlforge.integration.client.connect.wizard.ConnectWizardPageChain;
import org.etlforge.integration.client.connect.wizard.Folders;
import org.etlforge.integration.client.connect.wizard.OperationBuilder;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.XmlStructure;

final class DatabaseToXmlWizard extends AbstractConnectWizard<DatabaseStructure, XmlStructure> {
   public DatabaseToXmlWizard(ApplicationWindow var1, Operation var2, EntityExplorerSupport var3) {
      super("Database To XML", var1, var2, var3);
   }

   protected WizardPages createPages(Folders var1, EntityExplorerSupport var2) {
      return new WizardPages(this, var1, var2);
   }

   @Override
   protected OperationBuilder<DatabaseStructure, XmlStructure> createOperationBuilder(
      IntegrationProject var1, Operation var2, Folders var3, ConnectWizardPageChain<DatabaseStructure, XmlStructure> var4
   ) {
      return new OperationBuilderImpl(var1, var2, var3, var4);
   }
}
