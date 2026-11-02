package org.etlforge.integration.client.ui.interchange.entity.target;

import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaData;
import org.etlforge.integration.client.ui.interchange.location.search.DataLocationTypeProperty;
import org.etlforge.integration.data.SourceTarget;

final class TargetSearchAndReplaceProperties extends SearchAndReplaceMetaData {
   public TargetSearchAndReplaceProperties() {
      super(new DataLocationTypeProperty(SourceTarget.Target));
   }
}
