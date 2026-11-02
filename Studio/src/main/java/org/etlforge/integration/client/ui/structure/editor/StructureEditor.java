package org.etlforge.integration.client.ui.structure.editor;

import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.data.entity.Transformation;

public interface StructureEditor {
   EditedStructure edit(Wizard var1);

   Object getInitialState(Transformation var1);

   void restoreInitialState(Object var1);
}
