package org.etlforge.integration.client.database.ui.sqlbuilder;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.EnumSet;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.DatabaseObjectType;
import org.etlforge.integration.database.info.retriever.TableSearchFilter;
import org.etlforge.string.StringMatcherPredicate;
import org.etlforge.string.StringMatchers;
import org.etlforge.string.WildcardStringMatcher;

public final class DbObjectsViewStringFilter implements DbObjectsViewFilter {
   private final Predicate<String> tableMatcher;
   private final Predicate<String> columnMatcher;
   private final TableSearchFilter infoProviderFilter;

   public DbObjectsViewStringFilter(String var1) {
      DbObjectsViewStringFilter.Splitter var2 = new DbObjectsViewStringFilter.Splitter(var1);
      this.tableMatcher = var2.getTableMatcher();
      this.columnMatcher = var2.getColumnMatcher();
      this.infoProviderFilter = var2.getInfoProviderFilter();
   }

   @Override
   public boolean acceptsTable(DatabaseObject var1) {
      String var2 = var1.getName();
      return this.tableMatcher.apply(var2);
   }

   @Override
   public boolean acceptsColumn(DatabaseColumn var1) {
      String var2 = var1.getName();
      return this.columnMatcher.apply(var2);
   }

   @Override
   public TableSearchFilter asInfoProviderFilter() {
      return this.infoProviderFilter;
   }

   private static class Splitter {
      private final StringBuilder tablePart = new StringBuilder();
      private final StringBuilder columnPart = new StringBuilder();
      private StringBuilder currentPart = this.tablePart;
      private boolean tablePartIsDone;

      public Splitter(String var1) {
         this.tokenize(var1);
      }

      private void tokenize(String var1) {
         StringTokenizer var2 = new StringTokenizer(var1, "[] ", true);
         boolean var3 = false;

         while (var2.hasMoreTokens()) {
            String var4 = var2.nextToken();
            char var5 = var4.charAt(0);
            switch (var5) {
               case ' ':
                  this.addToPart(var4);
                  if (!var3 && this.currentPart == this.tablePart) {
                     this.currentPart = this.columnPart;
                     this.tablePartIsDone = true;
                  }
                  break;
               case '[':
                  var3 = true;
                  break;
               case ']':
                  var3 = false;
                  break;
               default:
                  this.addToPart(var4);
            }
         }
      }

      private void addToPart(String var1) {
         this.currentPart.append(var1);
      }

      public Predicate<String> getTableMatcher() {
         return getMatcher(this.tablePart.toString(), this.tablePartIsDone);
      }

      private static Predicate<String> getMatcher(String var0, boolean var1) {
         if (StringUtils.isBlank(var0)) {
            return Predicates.alwaysTrue();
         } else if (var0.contains("*")) {
            return new StringMatcherPredicate(new WildcardStringMatcher(false), var0);
         } else {
            return var0.endsWith(" ") && var1
               ? new StringMatcherPredicate(StringMatchers.EQUALS_IGNORE_CASE, var0.trim())
               : new StringMatcherPredicate(StringMatchers.STARTS_WITH_IGNORE_CASE, var0);
         }
      }

      public Predicate<String> getColumnMatcher() {
         return getMatcher(this.columnPart.toString(), false);
      }

      public TableSearchFilter getInfoProviderFilter() {
         EnumSet var1 = EnumSet.of(DatabaseObjectType.TABLE, DatabaseObjectType.VIEW);
         String var2 = this.tablePart.toString().trim();
         if (!this.tablePartIsDone) {
            var2 = var2 + "%";
         }

         return new TableSearchFilter(var2, var1);
      }
   }
}
