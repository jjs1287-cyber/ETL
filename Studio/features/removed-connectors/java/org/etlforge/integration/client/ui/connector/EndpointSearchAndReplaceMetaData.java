package org.etlforge.integration.client.ui.connector;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Arrays;
import org.etlforge.integration.client.entity.search.ReplaceableKeyValueStringProperty;
import org.etlforge.integration.client.entity.search.ReplaceablePasswordProperty;
import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaData;
import org.etlforge.integration.client.entity.search.SearchableProperty;

public class EndpointSearchAndReplaceMetaData extends SearchAndReplaceMetaData {
   public EndpointSearchAndReplaceMetaData(SearchableProperty... var1) {
      super(prependDefaultProperties(Arrays.asList(var1)));
   }

   public EndpointSearchAndReplaceMetaData(ImmutableList<? extends SearchableProperty> var1) {
      super(prependDefaultProperties(var1));
   }

   private static ImmutableList<? extends SearchableProperty> prependDefaultProperties(Iterable<? extends SearchableProperty> var0) {
      ReplaceableKeyValueStringProperty var1 = new ReplaceableKeyValueStringProperty("Host", "host");
      ReplaceableKeyValueStringProperty var2 = new ReplaceableKeyValueStringProperty("User", "user");
      ReplaceablePasswordProperty var3 = new ReplaceablePasswordProperty("Password", "password");
      return new Builder().add(var1).add(var2).add(var3).addAll(var0).build();
   }
}
