package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings;

import java.util.List;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;

public interface SuggestionsModelListener {
   void nodesFound(List<Node> var1);

   void cleared();
}
