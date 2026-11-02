package org.etlforge.integration.data.structure.database;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.driver.DatabaseDriverQuoteHandler;
import org.etlforge.integration.database.driver.DatabaseDriverQuoteHandlerFactory;
import org.etlforge.integration.database.info.DatabaseObject;

public final class ObjectAliasGenerator {
   private final BeginEndQuote quotes;
   private final DatabaseDriverQuoteHandler quoteHandler;
   private final ListMultimap<String, String> tableAliases;
   private final Set<String> usedNames;

   public ObjectAliasGenerator(BeginEndQuote var1, DatabaseObject[] var2) {
      this(var1, Arrays.asList(var2));
   }

   public ObjectAliasGenerator(BeginEndQuote var1, Iterable<DatabaseObject> var2) {
      this.quotes = var1;
      this.quoteHandler = DatabaseDriverQuoteHandlerFactory.newInstance(var1);
      this.tableAliases = ArrayListMultimap.create();
      this.usedNames = Sets.newHashSet();
      this.storeInitialNames(var2);
   }

   public void reset() {
      this.tableAliases.clear();
      this.usedNames.clear();
   }

   public String getAlias(DatabaseObject var1) {
      boolean var2 = this.isWrappedInBeginEndQuote(var1.getName());
      String var3 = this.quoteHandler.unwrapName(var1.getName());
      String var4 = this.generateAlias(var3, var2);
      this.tableAliases.put(var1.getName(), var4);
      return var4;
   }

   public String[] getAllAliases(DatabaseObject var1) {
      List var2 = this.tableAliases.get(var1.getName());
      return (String[]) var2.toArray(new String[var2.size()]);
   }

   public void setTableUsage(Map<DatabaseObject, Integer> var1) {
      this.reset();

      for (Entry var3 : var1.entrySet()) {
         int var4 = (Integer)var3.getValue();
         if (var4 > 1) {
            for (int var5 = 1; var5 < var4; var5++) {
               this.getAlias((DatabaseObject)var3.getKey());
            }
         }
      }
   }

   private String generateAlias(String var1, boolean var2) {
      int var3 = 2;

      while (true) {
         String var4 = AliasNameGenerator.getAliasName(var1, var3, this.quotes);
         if (!this.usedNames.contains(var4)) {
            this.usedNames.add(var4);
            if (var2) {
               var4 = this.quoteHandler.wrapName(var4);
            }

            return var4;
         }

         var3++;
      }
   }

   private void storeInitialNames(Iterable<DatabaseObject> var1) {
      for (DatabaseObject var3 : var1) {
         this.usedNames.add(this.getNameFromObject(var3));
      }
   }

   private String getNameFromObject(DatabaseObject var1) {
      String var2 = var1.getName();
      return this.quoteHandler.unwrapName(var2);
   }

   private boolean isWrappedInBeginEndQuote(String var1) {
      return this.quoteHandler.isNameWrapped(var1);
   }
}
