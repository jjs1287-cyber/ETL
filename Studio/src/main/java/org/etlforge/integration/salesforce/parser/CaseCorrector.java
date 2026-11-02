package org.etlforge.integration.salesforce.parser;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.salesforce.SalesforceException;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProvider;
import org.etlforge.lang.KongaStringUtils;

public final class CaseCorrector {
   private final String rootObjectName;
   private final SObjectSchemaProvider schemaProvider;
   private Map<String, CaseCorrector.Names> nameMap;

   public CaseCorrector(String var1, SObjectSchemaProvider var2) {
      this.rootObjectName = var1;
      this.schemaProvider = var2;
   }

   public ParsedQuery correctCase(ParsedQuery var1) throws SalesforceException {
      if (var1.isInvalid()) {
         return var1;
      }

      this.nameMap = Maps.newHashMap();
      SObjectSchema var2 = this.schemaProvider.describeObject(this.rootObjectName);
      return this.correctCaseImpl(var1, var2);
   }

   private ParsedQuery correctCaseImpl(ParsedQuery var1, SObjectSchema var2) throws SalesforceException {
      if (var1.isInvalid()) {
         return var1;
      }

      CaseCorrector.Names var3 = new CaseCorrector.Names(var2);
      this.nameMap.put(var2.getName().toLowerCase(), var3);
      List var4 = this.correctFieldNames(var1.getFieldNames(), var3);
      List var5 = this.correctNestedQueries(var1, var3);
      return new ParsedQuery(var2.getName(), var4, var5);
   }

   private List<String> correctFieldNames(List<String> var1, CaseCorrector.Names var2) throws SalesforceException {
      ArrayList var3 = Lists.newArrayList();

      for (String var5 : var1) {
         String var6 = this.correctField(var5, var2);
         var3.add(var6);
      }

      return var3;
   }

   private String correctField(String var1, CaseCorrector.Names var2) throws SalesforceException {
      StringBuilder var3 = new StringBuilder();
      Iterable var4 = Splitter.on('.').split(var1);
      this.buildCorrectedField(var4.iterator(), var3, var2);
      return var3.toString();
   }

   private void buildCorrectedField(Iterator<String> var1, StringBuilder var2, CaseCorrector.Names var3) throws SalesforceException {
      while (var1.hasNext()) {
         String var4 = (String)var1.next();
         if (var2.length() > 0) {
            var2.append(".");
         }

         if (var4.equalsIgnoreCase(this.rootObjectName)) {
            var2.append(this.rootObjectName);
         } else {
            Optional var5 = var3.checkIfField(var4);
            if (var5.isPresent()) {
               var2.append((String)var5.get());
            } else {
               Optional var6 = var3.checkIfParent(var4);
               if (var6.isPresent()) {
                  var2.append(((CaseCorrector.Relation)var6.get()).name);
                  String var7 = ((CaseCorrector.Relation)var6.get()).objectType;
                  this.buildCorrectedField(var1, var2, this.describeObject(var7));
               } else {
                  var2.append(var4);
               }
            }
         }
      }
   }

   private CaseCorrector.Names describeObject(String var1) throws SalesforceException {
      CaseCorrector.Names var2 = this.nameMap.get(var1);
      if (var2 == null) {
         SObjectSchema var3 = this.schemaProvider.describeObject(var1);
         var2 = new CaseCorrector.Names(var3);
         this.nameMap.put(var3.getName(), var2);
      }

      return var2;
   }

   private List<ParsedQuery> correctNestedQueries(ParsedQuery var1, CaseCorrector.Names var2) throws SalesforceException {
      ImmutableList var3 = var1.getSubQueries();
      if (var3.isEmpty()) {
         return var3;
      }

      ArrayList var4 = Lists.newArrayList();

      for (Object __etlItem114 : var3) {
         ParsedQuery var6 = (ParsedQuery) __etlItem114;
         String var7 = var6.getObjectName();
         String var8 = KongaStringUtils.substringAfterLastIfPresent(var7, ".");
         Optional var9 = var2.checkIfChild(var8);
         if (var9.isPresent()) {
            String var10 = ((CaseCorrector.Relation)var9.get()).name;
            String var11 = StringUtils.startsWithIgnoreCase(var7, this.rootObjectName + ".") ? this.rootObjectName + "." + var10 : var10;
            String var12 = ((CaseCorrector.Relation)var9.get()).objectType;
            List var13 = this.correctFieldNames(var6.getFieldNames(), this.describeObject(var12));
            var4.add(new ParsedQuery(var11, var13));
         } else {
            var4.add(var6);
         }
      }

      return var4;
   }

   private static class Names {
      private final Map<String, String> fieldNames = Maps.newHashMap();
      private final Map<String, CaseCorrector.Relation> parents = Maps.newHashMap();
      private final Map<String, CaseCorrector.Relation> children = Maps.newHashMap();

      public Names(SObjectSchema var1) {
         for (SObjectSchema.Field var3 : var1.getFields()) {
            this.fieldNames.put(var3.getName().toLowerCase(), var3.getName());
         }

         for (SObjectSchema.Parent var7 : var1.getParents()) {
            String var4 = var7.getName();
            this.parents.put(var4.toLowerCase(), new CaseCorrector.Relation(var4, var7.getObjectTypeInQuery()));
         }

         for (SObjectSchema.Child var8 : var1.getChildren()) {
            String var9 = var8.getName();
            this.children.put(var9.toLowerCase(), new CaseCorrector.Relation(var9, var8.getObjectType()));
         }
      }

      public Optional<String> checkIfField(String var1) {
         String var2 = this.fieldNames.get(var1.toLowerCase());
         return Optional.fromNullable(var2);
      }

      public Optional<CaseCorrector.Relation> checkIfParent(String var1) {
         CaseCorrector.Relation var2 = this.parents.get(var1.toLowerCase());
         return Optional.fromNullable(var2);
      }

      public Optional<CaseCorrector.Relation> checkIfChild(String var1) {
         CaseCorrector.Relation var2 = this.children.get(var1.toLowerCase());
         return Optional.fromNullable(var2);
      }
   }

   private static class Relation {
      public final String name;
      public final String objectType;

      public Relation(String var1, String var2) {
         this.name = var1;
         this.objectType = var2;
      }
   }
}
