package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;

public interface LoadStructureInvoker {
   void displayLoadedStructures(MappingTreeStructure var1, MappingTreeStructure var2);

   void cancelled();

   Transformation getTransformation();

   boolean isAskingForInputStructure();

   boolean isAskingForOutputStructure();

   void jobHasBeenInitialized();

   void jobHasCompleted();
}
