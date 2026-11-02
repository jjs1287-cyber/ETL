package org.etlforge.integration.client.entity.search;

import org.etlforge.integration.data.entity.IntegrationEntity;

public final class NameProperty extends AbstractSearchableProperty<String> {
   public NameProperty() {
      super("Name", String.class);
   }

   public String getValue(IntegrationEntity var1) {
      return var1.getName();
   }
}
