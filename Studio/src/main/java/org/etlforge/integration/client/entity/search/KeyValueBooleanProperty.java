package org.etlforge.integration.client.entity.search;

import com.google.common.base.Function;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class KeyValueBooleanProperty extends AbstractSearchableProperty<Boolean> {
   private final String key;
   private final boolean defaultValue;
   private final Function<IntegrationEntity, IntegrationData> targetFunction;

   public KeyValueBooleanProperty(String var1, String var2, boolean var3) {
      this(var1, var2, var3, TargetFunctions.IDENTITY);
   }

   public KeyValueBooleanProperty(String var1, String var2, boolean var3, Function<IntegrationEntity, IntegrationData> var4) {
      super(var1, Boolean.class);
      this.key = var2;
      this.defaultValue = var3;
      this.targetFunction = var4;
   }

   public Boolean getValue(IntegrationEntity var1) {
      IntegrationData var2 = this.targetFunction.apply(var1);
      synchronized (var2.getDataLock()) {
         return var2.getBooleanProperty(this.key, this.defaultValue);
      }
   }

   void setValue(IntegrationEntity var1, boolean var2) {
      IntegrationData var3 = this.targetFunction.apply(var1);
      synchronized (var3.getDataLock()) {
         var3.setBooleanProperty(this.key, var2);
      }
   }
}
