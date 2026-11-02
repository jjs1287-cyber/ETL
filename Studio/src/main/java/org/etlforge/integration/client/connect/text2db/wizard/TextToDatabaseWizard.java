package org.etlforge.integration.client.connect.text2db.wizard;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.connect.wizard.AbstractConnectWizard;
import org.etlforge.integration.client.connect.wizard.ConnectWizardPageChain;
import org.etlforge.integration.client.connect.wizard.Folders;
import org.etlforge.integration.client.connect.wizard.OperationBuilder;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.TextStructure;

public final class TextToDatabaseWizard extends AbstractConnectWizard<TextStructure, DatabaseStructure> {
   public TextToDatabaseWizard(ApplicationWindow var1, Operation var2, EntityExplorerSupport var3) {
      super("Text To Database", var1, var2, var3);
   }

   protected WizardPages createPages(Folders var1, EntityExplorerSupport var2) {
      return new WizardPages(this, var1, var2);
   }

   @Override
   protected OperationBuilder<TextStructure, DatabaseStructure> createOperationBuilder(
      IntegrationProject var1, Operation var2, Folders var3, ConnectWizardPageChain<TextStructure, DatabaseStructure> var4
   ) {
      return new OperationBuilderImpl(var1, var2, var3, var4);
   }
}
