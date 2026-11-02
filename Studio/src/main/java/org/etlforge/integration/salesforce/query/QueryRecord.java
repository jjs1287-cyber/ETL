package org.etlforge.integration.salesforce.query;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import org.etlforge.io.Indentation;
import org.etlforge.system.ArgCheck;

public final class QueryRecord {
   private final String name;
   private final ImmutableList<Field> fields;
   private final ImmutableList<QueryRecord> children;

   public QueryRecord(String var1, Iterable<Field> var2) {
      this(var1, var2, Collections.EMPTY_LIST);
   }

   public QueryRecord(String var1, Iterable<Field> var2, Iterable<QueryRecord> var3) {
      ArrayList var4 = new ArrayList();

      for (Field var6 : var2) {
         var4.add(var6);
      }

      for (QueryRecord var15 : var3) {
         String var7 = var15.name;

         for (Field var10 : var15.fields) {
            String var11 = var7 + "." + var10.getName();
            String var12 = var10.getValueAsString();
            Field var13 = new Field(var11, var12);
            var4.add(var13);
         }
      }

      this.name = ArgCheck.checkNotBlank(var1);
      this.fields = ImmutableList.copyOf(var4);
      this.children = ImmutableList.copyOf(var3);
   }

   public String getName() {
      return this.name;
   }

   public Iterable<Field> getFields() {
      return this.fields;
   }

   public Iterable<String> getFieldNames() {
      return Iterables.transform(this.fields, new Function<Field, String>() {
         public String apply(Field var1) {
            return var1.getName();
         }
      });
   }

   public Iterable<String> getFieldValues() {
      return Iterables.transform(this.fields, new Function<Field, String>() {
         public String apply(Field var1) {
            return var1.getValueAsString();
         }
      });
   }

   public boolean isNested() {
      return this.children.size() > 0;
   }

   public Iterable<QueryRecord> getChildren() {
      return this.children;
   }

   public void writeTo(Appendable var1) throws IOException {
      this.writeTo(var1, new Indentation());
   }

   private void writeTo(Appendable var1, Indentation var2) throws IOException {
      var2.writeTo(var1);
      var1.append(this.name);
      var1.append("\n");
      var2 = var2.increase();
      FieldList.writeTo(this.fields, var1, var2);

      for (QueryRecord var4 : this.children) {
         var4.writeTo(var1, var2.increase());
      }
   }
}
