package org.etlforge.integration.debug.client.de;

import com.google.common.base.Predicate;

public enum DataElementScope implements Predicate<DataElement> {
   GLOBAL,
   LOCAL,
   SOURCE;

   public boolean apply(DataElement var1) {
      return var1.getScope() == this;
   }
}
