package org.etlforge.integration.data.location;

public interface DataLocationStore<T extends DataLocation> {
   T[] locations();

   void refresh();
}
