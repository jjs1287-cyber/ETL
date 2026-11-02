package org.etlforge.integration.json.xml;

enum ArrayMode {
   NORMAL,
   SIMPLE,
   NOT_ARRAY;

   public boolean isArray() {
      return this != NOT_ARRAY;
   }
}
