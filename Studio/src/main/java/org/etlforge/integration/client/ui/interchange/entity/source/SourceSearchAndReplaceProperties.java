package org.etlforge.integration.client.ui.interchange.entity.source;

import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaData;
import org.etlforge.integration.client.ui.interchange.location.search.DataLocationTypeProperty;
import org.etlforge.integration.data.SourceTarget;

final class SourceSearchAndReplaceProperties extends SearchAndReplaceMetaData {
   public SourceSearchAndReplaceProperties() {
      super(new DataLocationTypeProperty(SourceTarget.Source));
   }
}
