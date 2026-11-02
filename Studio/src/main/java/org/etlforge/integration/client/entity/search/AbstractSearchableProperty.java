package org.etlforge.integration.client.entity.search;

import com.google.common.base.Preconditions;
import org.etlforge.system.ArgCheck;

public abstract class AbstractSearchableProperty<T> implements SearchableProperty<T> {
   private final String name;
   private final Class<T> valueType;

   public AbstractSearchableProperty(String var1, Class<T> var2) {
      this.name = ArgCheck.checkNotEmpty(var1);
      this.valueType = Preconditions.checkNotNull(var2);
   }

   @Override
   public final String getName() {
      return this.name;
   }

   @Override
   public final Class<T> getValueType() {
      return this.valueType;
   }
}
