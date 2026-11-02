package org.etlforge.integration.data.functors;

import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.location.DataLocation;

public class FileBasedLocationFilter implements EntityFilter {
   private final boolean acceptNullLocation;

   public FileBasedLocationFilter() {
      this(false);
   }

   public FileBasedLocationFilter(boolean var1) {
      this.acceptNullLocation = var1;
   }

   @Override
   public boolean apply(IntegrationEntity var1) {
      if (var1 instanceof Locatable) {
         DataLocation var2 = ((Locatable)var1).getLocation();
         return var2 == null ? this.acceptNullLocation : var2.isFileBased();
      } else {
         return false;
      }
   }

   @Override
   public String toString() {
      return "File-based Locatables";
   }
}
