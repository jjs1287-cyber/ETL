package org.etlforge.integration.client.entity.search;

import com.google.common.base.Function;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.integration.data.entity.IntegrationEntity;

public class ReplaceableKeyValueStringProperty implements ReplaceableProperty<String> {
   private final KeyValueStringProperty impl;

   public ReplaceableKeyValueStringProperty(String var1, String var2) {
      this(var1, var2, TargetFunctions.IDENTITY);
   }

   public ReplaceableKeyValueStringProperty(String var1, String var2, Function<IntegrationEntity, IntegrationData> var3) {
      this.impl = new KeyValueStringProperty(var1, var2, var3);
   }

   @Override
   public String getName() {
      return this.impl.getName();
   }

   @Override
   public Class<String> getValueType() {
      return this.impl.getValueType();
   }

   public String getValue(IntegrationEntity var1) {
      return this.impl.getValue(var1);
   }

   public void setValue(IntegrationEntity var1, String var2) {
      this.impl.setValue(var1, var2);
   }
}
