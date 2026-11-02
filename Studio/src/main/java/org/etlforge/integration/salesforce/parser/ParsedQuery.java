package org.etlforge.integration.salesforce.parser;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.etlforge.string.PrefixRemover;
import org.etlforge.system.ArgCheck;

public final class ParsedQuery {
   private final String objectName;
   private final ImmutableList<String> fieldNames;
   private final ImmutableList<ParsedQuery> subQueries;
   private final String errorMessage;

   public static ParsedQuery invalid(String var0) {
      return new ParsedQuery(ArgCheck.checkNotEmpty(var0, "errorMessage"), false);
   }

   public ParsedQuery(String var1) {
      this(var1, null, null);
   }

   public ParsedQuery(String var1, List<String> var2) {
      this(var1, var2, null);
   }

   public ParsedQuery(String var1, List<String> var2, List<ParsedQuery> var3) {
      this.objectName = var1;
      this.fieldNames = var2 == null ? ImmutableList.of() : ImmutableList.copyOf(var2);
      this.subQueries = var3 == null ? ImmutableList.of() : ImmutableList.copyOf(var3);
      this.errorMessage = null;
   }

   private ParsedQuery(String var1, boolean var2) {
      this.errorMessage = var1;
      this.objectName = null;
      this.fieldNames = ImmutableList.of();
      this.subQueries = ImmutableList.of();
   }

   public boolean isValid() {
      return this.errorMessage == null;
   }

   public boolean isInvalid() {
      return !this.isValid();
   }

   public String getErrorMessage() {
      return this.errorMessage;
   }

   public String getObjectName() {
      return this.objectName;
   }

   public ImmutableList<String> getFieldNames() {
      return this.fieldNames;
   }

   public ImmutableList<String> getFieldNamesWithoutObjectQualifier() {
      return ImmutableList.copyOf(Iterables.transform(this.fieldNames, this.objectNameRemover()));
   }

   private PrefixRemover objectNameRemover() {
      return objectNameRemover(this.objectName);
   }

   private static PrefixRemover objectNameRemover(String var0) {
      String var1 = var0.toLowerCase() + ".";
      return PrefixRemover.ignoringCase(var1);
   }

   public String getFieldNameWithoutObjectQualifier(String var1) {
      return getFieldNameWithoutObjectQualifier(this.objectName, var1);
   }

   public static String getFieldNameWithoutObjectQualifier(String var0, String var1) {
      PrefixRemover var2 = objectNameRemover(var0);
      return var2.apply(var1);
   }

   public boolean hasSubQueries() {
      return !this.subQueries.isEmpty();
   }

   public ImmutableList<ParsedQuery> getSubQueries() {
      return this.subQueries;
   }

   public boolean hasFieldInfo() {
      return !this.fieldNames.isEmpty();
   }

   public boolean hasSameFieldsAs(ParsedQuery var1) {
      HashSet var2 = Sets.newHashSet(this.fieldNames);
      HashSet var3 = Sets.newHashSet(var1.fieldNames);
      return var2.equals(var3);
   }

   public boolean isChildToParentQuery() {
      for (String var2 : this.fieldNames) {
         String var3 = StringUtils.removeStart(var2.toLowerCase(), this.objectName.toLowerCase() + ".");
         if (var3.contains(".")) {
            return true;
         }
      }

      return false;
   }

   public boolean isRelationshipQuery() {
      return this.hasSubQueries() || this.isChildToParentQuery();
   }

   public boolean callsAggregateFunction() {
      if ("AggregateResult".equals(this.objectName)) {
         return true;
      }

      for (ParsedQuery var2 : this.subQueries) {
         if (var2.callsAggregateFunction()) {
            return true;
         }
      }

      return false;
   }

   public boolean supportsTargetHeader() {
      return this.hasFieldInfo() && !this.hasSubQueries();
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.objectName, this.fieldNames, this.subQueries);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof ParsedQuery)) {
         return false;
      }

      ParsedQuery var2 = (ParsedQuery)var1;
      return Objects.equal(this.objectName, var2.objectName) && this.fieldNames.equals(var2.fieldNames) && this.subQueries.equals(var2.subQueries);
   }

   @Override
   public String toString() {
      return this.objectName == null
         ? "INVALID QUERY"
         : Objects.toStringHelper(this).add("object", this.objectName).add("fields", this.fieldNames).add("sub-queries?", this.hasSubQueries()).toString();
   }
}
