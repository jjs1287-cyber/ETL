package org.etlforge.integration.client.entity.search;

import com.google.common.base.Function;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class KeyValueIntegerProperty extends AbstractSearchableProperty<Integer> {
   private final String key;
   private final int defaultValue;
   private final Function<IntegrationEntity, IntegrationData> targetFunction;

   public KeyValueIntegerProperty(String var1, String var2, int var3, Function<IntegrationEntity, IntegrationData> var4) {
      super(var1, Integer.class);
      this.key = var2;
      this.defaultValue = var3;
      this.targetFunction = var4;
   }

   public Integer getValue(IntegrationEntity var1) {
      IntegrationData var2 = this.targetFunction.apply(var1);
      synchronized (var2.getDataLock()) {
         return var2.getIntProperty(this.key, this.defaultValue);
      }
   }

   void setValue(IntegrationEntity var1, int var2) {
      IntegrationData var3 = this.targetFunction.apply(var1);
      synchronized (var3.getDataLock()) {
         var3.setIntProperty(this.key, var2);
      }
   }
}
