package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.structure.mapping.InvalidMapping;

public interface InvalidMappingsListener {
   void invalidMappingsChanged(TreeMapper var1, InvalidMapping[] var2);
}
