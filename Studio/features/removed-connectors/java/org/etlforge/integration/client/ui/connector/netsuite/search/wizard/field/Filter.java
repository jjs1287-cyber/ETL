package org.etlforge.integration.client.ui.connector.netsuite.search.wizard.field;

import com.google.common.collect.Ordering;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.SearchField;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;

public final class Filter {
   private final String objectName;
   private final String fieldName;
   private final SearchField fieldDef;
   private final SearchFieldValue fieldValue;

   public static Ordering<Filter> alphabeticalOrder() {
      return new Ordering<Filter>() {
         public int compare(Filter var1, Filter var2) {
            return var1.getKey().compareToIgnoreCase(var2.getKey());
         }
      };
   }

   public Filter(String var1, String var2, SearchField var3, SearchFieldValue var4) {
      this.objectName = var1;
      this.fieldName = var2;
      this.fieldDef = var3;
      this.fieldValue = var4;
   }

   public SearchField getFieldDef() {
      return this.fieldDef;
   }

   public SearchFieldValue getFieldValue() {
      return this.fieldValue;
   }

   public String getFieldName() {
      return this.fieldName;
   }

   public String getObjectName() {
      return this.objectName;
   }

   public static String getKey(String var0, String var1) {
      return var0 + var1;
   }

   public String getKey() {
      return getKey(this.objectName, this.fieldName);
   }

   @Override
   public String toString() {
      return this.objectName + ":" + this.fieldName + ": " + this.fieldValue;
   }
}
