package org.etlforge.integration.debug.client.de;

public interface DataElementFactory {
   DataElementFactory TRIVIAL = new DataElementFactory() {
      @Override
      public DataElement create(String var1, DataElementScope var2) {
         return new DataElement(var1, var2, null);
      }
   };

   DataElement create(String var1, DataElementScope var2);
}
