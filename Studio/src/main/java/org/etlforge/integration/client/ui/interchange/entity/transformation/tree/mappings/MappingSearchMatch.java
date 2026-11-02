package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings;

import org.etlforge.ui.util.find.SearchTarget;
import org.etlforge.util.Interval;

public interface MappingSearchMatch extends SearchTarget.Match {
   NodeMapping getMapping();

   Interval getExpressionSpan();
}
