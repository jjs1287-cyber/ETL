package org.etlforge.integration.salesforce.query;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import org.etlforge.io.Indentation;

public final class FieldList implements Iterable<Field> {
   private final LinkedHashMap<String, Field> fields = Maps.newLinkedHashMap();
   private boolean skippedOneIdField;

   public void add(Field var1) {
      String var2 = var1.getName();
      if (!"type".equals(var2) && !"queryLocator".equals(var2) && !"size".equals(var2) && !"done".equals(var2)) {
         if ("Id".equals(var2) && !this.skippedOneIdField) {
            this.skippedOneIdField = true;
         } else {
            this.fields.put(var2, var1);
         }
      }
   }

   public void remove(Iterable<String> var1) {
      for (String var3 : var1) {
         this.fields.remove(var3);
      }
   }

   @Override
   public Iterator<Field> iterator() {
      return this.fields.values().iterator();
   }

   static void writeTo(Iterable<Field> var0, Appendable var1, Indentation var2) throws IOException {
      for (Field var4 : var0) {
         var2.writeTo(var1);
         var1.append(var4.getName());
         var1.append(": ");
         var1.append(var4.getValueAsString());
         var1.append("\n");
      }
   }
}
