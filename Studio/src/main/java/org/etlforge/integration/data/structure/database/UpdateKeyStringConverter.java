package org.etlforge.integration.data.structure.database;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;

final class UpdateKeyStringConverter {
   public static String toString(Collection<DatabaseColumn> var0) {
      StringBuilder var1 = new StringBuilder();

      for (DatabaseColumn var3 : var0) {
         var1.append(var3.getQualifiedName());
         var1.append(",");
      }

      return var1.toString();
   }

   public static DatabaseColumn[] fromString(String var0, DatabaseObject var1) {
      ArrayList var2 = Lists.newArrayList();
      String[] var3 = var0.split(",");
      if (var3.length > 0) {
         HashSet var4 = Sets.newHashSet(var3);

         for (DatabaseColumn var6 : var1.getAllColumns()) {
            if (var4.contains(var6.getQualifiedName())) {
               var2.add(var6);
            }
         }
      }

      return (DatabaseColumn[]) var2.toArray(new DatabaseColumn[var2.size()]);
   }

   private UpdateKeyStringConverter() {
   }
}
