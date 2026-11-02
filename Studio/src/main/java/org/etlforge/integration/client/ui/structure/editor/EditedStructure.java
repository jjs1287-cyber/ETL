package org.etlforge.integration.client.ui.structure.editor;

import org.etlforge.integration.data.entity.Transformation;

public interface EditedStructure {
   void apply(Transformation var1) throws EditStructureException;
}
