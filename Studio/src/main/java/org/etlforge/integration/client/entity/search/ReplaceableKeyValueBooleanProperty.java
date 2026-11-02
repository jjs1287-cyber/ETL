package org.etlforge.integration.client.entity.search;

import com.google.common.base.Function;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class ReplaceableKeyValueBooleanProperty implements ReplaceableProperty<Boolean> {
   private final KeyValueBooleanProperty impl;

   public ReplaceableKeyValueBooleanProperty(String var1, String var2, boolean var3) {
      this(var1, var2, var3, TargetFunctions.IDENTITY);
   }

   public ReplaceableKeyValueBooleanProperty(String var1, String var2, boolean var3, Function<IntegrationEntity, IntegrationData> var4) {
      this.impl = new KeyValueBooleanProperty(var1, var2, var3, var4);
   }

   @Override
   public Class<Boolean> getValueType() {
      return Boolean.class;
   }

   @Override
   public String getName() {
      return this.impl.getName();
   }

   public Boolean getValue(IntegrationEntity var1) {
      return this.impl.getValue(var1);
   }

   public void setValue(IntegrationEntity var1, Boolean var2) {
      this.impl.setValue(var1, var2);
   }
}
