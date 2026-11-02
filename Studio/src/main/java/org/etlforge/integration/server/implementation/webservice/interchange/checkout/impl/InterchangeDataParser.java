package org.etlforge.integration.server.implementation.webservice.interchange.checkout.impl;

import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.server.implementation.webservice.interchange.checkout.client.InterchangeDataRow;
import org.etlforge.integration.server.implementation.webservice.interchange.checkout.client.InterchangeDataTable;

class InterchangeDataParser {
   private String[] m_keys = null;

   InterchangeDataParser(InterchangeDataTable var1) {
      this.init(var1);
   }

   private void init(InterchangeDataTable var1) {
      if (var1 != null) {
         this.m_keys = var1.getColumnNames();

         for (String var5 : this.m_keys) {
            var5 = var5.toLowerCase();
         }
      } else {
         this.m_keys = null;
      }
   }

   StringKeyValuePairs parse(InterchangeDataRow var1) {
      if (this.m_keys == null) {
         throw new IllegalStateException("This InterchangeDataParser has not been initialized with valid data.");
      }

      if (var1.getFields().length != this.m_keys.length) {
         throw new IllegalArgumentException("Illegal size of data row. Expected " + this.m_keys.length + " but got " + var1.getFields().length + ".");
      }

      StringKeyValuePairs var2 = new StringKeyValuePairs();
      int var3 = 0;

      for (String var7 : var1.getFields()) {
         var2.put(this.m_keys[var3], var7);
         var3++;
      }

      return var2;
   }
}
