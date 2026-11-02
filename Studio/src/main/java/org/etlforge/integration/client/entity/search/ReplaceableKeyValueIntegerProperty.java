package org.etlforge.integration.client.entity.search;

import com.google.common.base.Function;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class ReplaceableKeyValueIntegerProperty implements ReplaceableProperty<Integer> {
   private final KeyValueIntegerProperty impl;

   public ReplaceableKeyValueIntegerProperty(String var1, String var2, int var3) {
      this(var1, var2, var3, TargetFunctions.IDENTITY);
   }

   public ReplaceableKeyValueIntegerProperty(String var1, String var2, int var3, Function<IntegrationEntity, IntegrationData> var4) {
      this.impl = new KeyValueIntegerProperty(var1, var2, var3, var4);
   }

   @Override
   public String getName() {
      return this.impl.getName();
   }

   @Override
   public Class<Integer> getValueType() {
      return Integer.class;
   }

   public Integer getValue(IntegrationEntity var1) {
      return this.impl.getValue(var1);
   }

   public void setValue(IntegrationEntity var1, Integer var2) {
      this.impl.setValue(var1, var2);
   }
}
