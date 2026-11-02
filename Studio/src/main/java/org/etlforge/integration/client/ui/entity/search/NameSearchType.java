package org.etlforge.integration.client.ui.entity.search;

import java.util.Collections;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;

public final class NameSearchType extends AbstractSearchType {
   public NameSearchType() {
      super("Names");
   }

   @Override
   public Iterable<String> getStringsToSearch(IntegrationEntity var1) {
      return Collections.singleton(var1.getName());
   }

   @Override
   public void setProject(IntegrationProject var1) {
   }
}
