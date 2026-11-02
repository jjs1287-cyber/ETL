package org.etlforge.integration.client.ui.interchange.location.search;

import java.util.List;
import org.etlforge.integration.client.entity.search.ReplaceableKeyValueStringProperty;
import org.etlforge.integration.client.entity.search.SearchableProperty;
import org.etlforge.integration.data.SourceTarget;

final class LocalFileLocationProperties {
   public static void collect(SourceTarget var0, List<SearchableProperty> var1) {
      var1.add(stringProperty("Folder", "locator"));
      FileLocationProperties.collect(var0, var1, false);
   }

   private static SearchableProperty<String> stringProperty(String var0, String var1) {
      return new ReplaceableKeyValueStringProperty(var0, var1, TargetFunctions.TO_LOCATION);
   }

   private LocalFileLocationProperties() {
   }
}
