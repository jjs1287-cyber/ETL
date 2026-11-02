package org.etlforge.integration.client.ui.structure.editor;

import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;

public interface StructureEditorFactory {
   StructureEditor getEditor(Transformation var1, DataStructure var2, SourceTarget var3, EntityExplorerSupport var4);
}
