package org.etlforge.integration.data.location;

import java.util.EnumSet;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;

public class LocatableFilter implements EntityFilter {
   private final EnumSet<DataLocationType> types;

   public LocatableFilter(DataLocationType var1) {
      this.types = EnumSet.of(var1);
   }

   public LocatableFilter(EnumSet<DataLocationType> var1) {
      this.types = var1.clone();
   }

   @Override
   public boolean apply(IntegrationEntity var1) {
      return var1 instanceof Locatable ? this.evaluate((Locatable)var1) : false;
   }

   private boolean evaluate(Locatable var1) {
      DataLocation var2 = var1.getLocation();
      return var2 != null && this.types.contains(var2.getDataLocationType());
   }

   @Override
   public int hashCode() {
      return this.types.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof LocatableFilter && this.types.equals(((LocatableFilter)var1).types);
   }

   @Override
   public String toString() {
      return "DataLocations of type(s): " + this.types;
   }
}
