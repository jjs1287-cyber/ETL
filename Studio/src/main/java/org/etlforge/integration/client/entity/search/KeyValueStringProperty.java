package org.etlforge.integration.client.entity.search;

import com.google.common.base.Function;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class KeyValueStringProperty extends AbstractSearchableProperty<String> {
   private final String key;
   private final Function<IntegrationEntity, IntegrationData> targetFunction;

   public KeyValueStringProperty(String var1, String var2) {
      this(var1, var2, TargetFunctions.IDENTITY);
   }

   public KeyValueStringProperty(String var1, String var2, Function<IntegrationEntity, IntegrationData> var3) {
      super(var1, String.class);
      this.key = var2;
      this.targetFunction = var3;
   }

   public String getValue(IntegrationEntity var1) {
      IntegrationData var2 = this.targetFunction.apply(var1);
      synchronized (var2.getDataLock()) {
         return var2.getProperty(this.key);
      }
   }

   void setValue(IntegrationEntity var1, String var2) {
      IntegrationData var3 = this.targetFunction.apply(var1);
      synchronized (var3.getDataLock()) {
         var3.setStringProperty(this.key, var2);
      }
   }
}
