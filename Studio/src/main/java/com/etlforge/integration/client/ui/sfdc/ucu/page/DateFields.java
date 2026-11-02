package com.etlforge.integration.client.ui.sfdc.ucu.page;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.mapping.NodePath;
import org.etlforge.util.name.Nameables;
import org.etlforge.xml.xsd.SchemaTypes;

final class DateFields {
   private final ImmutableSet<String> dateFields;
   private final ImmutableSet<String> dateTimeFields;

   public static DateFields empty() {
      return new DateFields();
   }

   public static DateFields fromSObject(SObjectSchema var0) {
      HashSet var1 = Sets.newHashSet();
      HashSet var2 = Sets.newHashSet();

      for (SObjectSchema.Field var4 : var0.getFields()) {
         String var5 = var4.getName();
         SObjectSchema.Field.SoapType var6 = var4.getSoapType();
         if (var6 == SObjectSchema.Field.SoapType.DATE) {
            var1.add(var5);
         } else if (var6 == SObjectSchema.Field.SoapType.DATETIME) {
            var2.add(var5);
         }
      }

      return new DateFields(var1, var2);
   }

   public static DateFields fromFieldList(List<CROM> var0) {
      if (var0.isEmpty()) {
         return empty();
      }

      ArrayList var1 = Lists.newArrayList();
      ArrayList var2 = Lists.newArrayList();
      String var3 = SchemaTypes.DATE_TIME.toString();
      String var4 = SchemaTypes.DATE.toString();

      for (CROM var6 : var0) {
         String var7 = var6.getSchemaTypeExpandedName();
         if (var4.equals(var7)) {
            var1.add(var6);
         } else if (var3.equals(var7)) {
            var2.add(var6);
         }
      }

      return new DateFields(var1, var2);
   }

   private DateFields() {
      this.dateFields = ImmutableSet.of();
      this.dateTimeFields = ImmutableSet.of();
   }

   private DateFields(List<CROM> var1, List<CROM> var2) {
      this(Nameables.getUniqueNames(var1), Nameables.getUniqueNames(var2));
   }

   private DateFields(Set<String> var1, Set<String> var2) {
      this.dateFields = ImmutableSet.copyOf(var1);
      this.dateTimeFields = ImmutableSet.copyOf(var2);
   }

   public boolean isEmpty() {
      return this.dateFields.isEmpty() && this.dateTimeFields.isEmpty();
   }

   public boolean isDateField(NodePath var1) {
      String var2 = var1.getLeafName();
      return this.dateFields.contains(var2);
   }

   public boolean isDateTimeField(NodePath var1) {
      String var2 = var1.getLeafName();
      return this.dateTimeFields.contains(var2);
   }

   public void reportTo(Logger var1) {
      var1.info("Date fields: " + this.dateFields + "\nDateTime fields: " + this.dateTimeFields);
   }
}
