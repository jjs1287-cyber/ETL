package org.etlforge.integration.client.connect.db2db.wizard;

import org.etlforge.integration.client.connect.wizard.ConnectWizardPageChain;
import org.etlforge.integration.client.connect.wizard.Folders;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.structure.editor.db.DatabaseStructureWizardPageChain;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DatabaseStructure;

final class WizardPages extends ConnectWizardPageChain<DatabaseStructure, DatabaseStructure> {
   public WizardPages(DatabaseToDatabaseWizard var1, Folders var2, EntityExplorerSupport var3) {
      super(
         var1,
         var2,
         new DatabaseStructureWizardPageChain(var1, var3, SourceTarget.Source),
         new DatabaseStructureWizardPageChain(var1, var3, SourceTarget.Target)
      );
   }

   public DatabaseStructure getSourceStructure() {
      DatabaseStructureWizardPageChain var1 = (DatabaseStructureWizardPageChain)this.getSourcePages();
      return var1.getDefinedStructure();
   }

   public DatabaseStructure getTargetStructure() {
      DatabaseStructureWizardPageChain var1 = (DatabaseStructureWizardPageChain)this.getTargetPages();
      return var1.getDefinedStructure();
   }

   public void applyInsertMode(Transformation var1) {
      DatabaseStructureWizardPageChain var2 = (DatabaseStructureWizardPageChain)this.getTargetPages();
      var2.applyInsertMode(var1);
   }
}
