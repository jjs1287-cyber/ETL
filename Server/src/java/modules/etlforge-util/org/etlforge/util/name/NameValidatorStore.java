package org.etlforge.util.name;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;

public final class NameValidatorStore {
   private static final NameValidatorStore instance = new NameValidatorStore();
   private final Map<Class<? extends Nameable>, NameValidator> validators = Maps.newHashMap();

   private NameValidatorStore() {
   }

   public void put(Class<? extends Nameable> var1, NameValidator var2) {
      this.validators.put(var1, var2);
   }

   public NameValidator get(Class<? extends Nameable> var1) {
      return this.validators.get(var1);
   }

   public NameValidator get(Nameable var1) {
      Preconditions.checkNotNull(var1, "o");
      Class var2 = var1.getClass();
      NameValidator var3 = this.validators.get(var2);
      if (var3 == null) {
         for (Class var5 : this.validators.keySet()) {
            if (var5.isAssignableFrom(var2)) {
               var3 = this.validators.get(var5);
               break;
            }
         }
      }

      return var3;
   }

   public static NameValidatorStore getInstance() {
      return instance;
   }
}
