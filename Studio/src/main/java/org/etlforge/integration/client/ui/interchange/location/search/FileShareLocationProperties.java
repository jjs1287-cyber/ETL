package org.etlforge.integration.client.ui.interchange.location.search;

import java.util.List;
import org.etlforge.integration.client.entity.search.ReplaceableKeyValueStringProperty;
import org.etlforge.integration.client.entity.search.SearchableProperty;
import org.etlforge.integration.data.SourceTarget;

final class FileShareLocationProperties {
   public static void collect(SourceTarget var0, List<SearchableProperty> var1) {
      var1.add(new ReplaceableKeyValueStringProperty("Path", "locator", TargetFunctions.TO_LOCATION));
      FileLocationProperties.collect(var0, var1);
   }

   private FileShareLocationProperties() {
   }
}
