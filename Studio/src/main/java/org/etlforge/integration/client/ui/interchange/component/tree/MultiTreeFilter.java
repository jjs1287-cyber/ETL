package org.etlforge.integration.client.ui.interchange.component.tree;

import java.util.Set;
import org.etlforge.integration.client.ui.interchange.content.ContentViewerCategory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.ui.widget.tree.KongaTreeFilter;

public interface MultiTreeFilter extends EntityFilter {
   boolean hasMatchingType(ContentViewerCategory var1);

   boolean containsWizards();

   boolean isEmpty();

   Set<EntityType> getAllTypes();

   KongaTreeFilter getTopLevelFilter(ContentViewerCategory var1);
}
