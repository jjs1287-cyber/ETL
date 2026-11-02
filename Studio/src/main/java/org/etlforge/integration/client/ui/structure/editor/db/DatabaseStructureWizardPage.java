package org.etlforge.integration.client.ui.structure.editor.db;

import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.integration.data.structure.DatabaseStructure;

public interface DatabaseStructureWizardPage extends WizardPage {
   WizardPage getNextPage();

   void applyTo(DatabaseStructure var1);

   void setStructureToEdit(StructureToEdit var1);
}
