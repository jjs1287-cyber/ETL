package org.etlforge.integration.client.connect.db2db.wizard;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.connect.wizard.AbstractConnectWizard;
import org.etlforge.integration.client.connect.wizard.ConnectWizardPageChain;
import org.etlforge.integration.client.connect.wizard.Folders;
import org.etlforge.integration.client.connect.wizard.OperationBuilder;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.structure.DatabaseStructure;

public final class DatabaseToDatabaseWizard extends AbstractConnectWizard<DatabaseStructure, DatabaseStructure> {
   public DatabaseToDatabaseWizard(ApplicationWindow var1, Operation var2, EntityExplorerSupport var3) {
      super("Database To Database", var1, var2, var3);
   }

   protected WizardPages createPages(Folders var1, EntityExplorerSupport var2) {
      return new WizardPages(this, var1, var2);
   }

   @Override
   protected OperationBuilder<DatabaseStructure, DatabaseStructure> createOperationBuilder(
      IntegrationProject var1, Operation var2, Folders var3, ConnectWizardPageChain<DatabaseStructure, DatabaseStructure> var4
   ) {
      return new OperationBuilderImpl(var1, var2, var3, (WizardPages)var4);
   }
}
