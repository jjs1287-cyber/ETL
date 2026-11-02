package com.etlforge.integration.salesforce.ui;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.salesforce.wsdl.SchemaEntity;
import org.etlforge.string.StringMatcherPredicate;
import org.etlforge.string.StringMatchers;

public final class SchemaEntityNameFilterFunction implements Function<String, Predicate<SchemaEntity>> {
   private final NameDisplayMode nameDisplayMode;
   private SchemaEntityNameFilterFunction.MatchMode matchMode = SchemaEntityNameFilterFunction.MatchMode.CONTAINS;

   public SchemaEntityNameFilterFunction(NameDisplayMode var1) {
      this.nameDisplayMode = Preconditions.checkNotNull(var1);
   }

   public void setMatchMode(SchemaEntityNameFilterFunction.MatchMode var1) {
      this.matchMode = Preconditions.checkNotNull(var1);
   }

   public Predicate<SchemaEntity> apply(String var1) {
      if (var1.isEmpty()) {
         return null;
      }

      if (StringUtils.isBlank(var1)) {
         return Predicates.alwaysFalse();
      }

      Predicate var2 = this.matchMode.createTest(this.nameDisplayMode, var1);
      return new SchemaEntityNameFilterFunction.Matcher(this.nameDisplayMode, var2);
   }

   public enum MatchMode {
      CONTAINS {
         @Override
         protected Predicate<String> createTest(NameDisplayMode var1, String var2) {
            return new StringMatcherPredicate(StringMatchers.CONTAINS_IGNORE_CASE, var2);
         }
      },
      STARTS_WITH {
         @Override
         protected Predicate<String> createTest(NameDisplayMode var1, String var2) {
            boolean var3 = this.isExactMatchRequired(var1, var2);
            String var4 = var2.trim();
            StringMatchers var5 = var3 ? StringMatchers.EQUALS_IGNORE_CASE : StringMatchers.STARTS_WITH_IGNORE_CASE;
            return new StringMatcherPredicate(var5, var4);
         }

         private boolean isExactMatchRequired(NameDisplayMode var1, String var2) {
            return var1 == NameDisplayMode.API_NAME && this.endsWithSpace(var2);
         }

         private boolean endsWithSpace(String var1) {
            return var1.charAt(var1.length() - 1) == ' ';
         }
      };

      MatchMode() {
      }

      protected abstract Predicate<String> createTest(NameDisplayMode var1, String var2);
   }

   private static class Matcher implements Predicate<SchemaEntity> {
      private final NameDisplayMode nameDisplayMode;
      private final Predicate<String> test;

      public Matcher(NameDisplayMode var1, Predicate<String> var2) {
         this.nameDisplayMode = var1;
         this.test = var2;
      }

      public boolean apply(SchemaEntity var1) {
         String var2 = this.nameDisplayMode.getDisplayName(var1);
         return this.test.apply(var2);
      }
   }
}
