package org.etlforge.integration.client.ui.entity.search;

import java.util.Collections;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;

public final class GuidSearchType extends AbstractSearchType {
   public GuidSearchType() {
      super("GUIDs");
   }

   @Override
   public Iterable<String> getStringsToSearch(IntegrationEntity var1) {
      return Collections.singleton(var1.getID().toString());
   }

   @Override
   public void setProject(IntegrationProject var1) {
   }
}
