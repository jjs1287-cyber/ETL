package org.etlforge.integration.client.ui.entity.search;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.IntegrationProject;

public interface SearchType {
   String getName();

   Iterable<String> getStringsToSearch(IntegrationEntity var1);

   boolean isEntityTypeSpecific();

   EntityFilter getFilter();

   void setProject(IntegrationProject var1);
}
