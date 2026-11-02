package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.etlforge.integration.data.entity.transformation.AutoMapper;
import org.etlforge.integration.salesforce.bulkload.Mapping;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.lang.KongaStringUtils;

final class BulkLoadAutoMapper {
   private final HeaderMappingsModel model;

   public BulkLoadAutoMapper(HeaderMappingsModel var1) {
      this.model = var1;
   }

   public void run() {
      List var1 = this.model.getMapList();
      Set var2 = this.getAvailableSourceFields(var1);
      Set var3 = this.getAvailableTargetFields(var1);
      AutoMapper var4 = new AutoMapper<>(new BulkLoadAutoMapper.SourceNameFunction(), new BulkLoadAutoMapper.TargetNameFunction());
      List var5 = var4.findMatches(var2, var3);
      this.updateModel(var5);
   }

   private Set<String> getAvailableSourceFields(List<Mapping> var1) {
      HashSet var2 = Sets.newHashSet(this.model.getSourceHeader());

      for (Mapping var4 : var1) {
         var2.remove(var4.source);
      }

      return var2;
   }

   private Set<SObjectSchema.Field> getAvailableTargetFields(List<Mapping> var1) {
      HashSet var2 = Sets.newHashSet(this.model.getAutoMappableTargetFields());

      for (Mapping var4 : var1) {
         var2.remove(var4.target);
      }

      return var2;
   }

   private void updateModel(List<AutoMapper.Match<String, SObjectSchema.Field>> var1) {
      ArrayList var2 = Lists.newArrayList();

      for (AutoMapper.Match var4 : var1) {
         var2.add(new Mapping((String)var4.source, (SObjectSchema.Field)var4.target));
      }

      if (!var2.isEmpty()) {
         this.model.addMappings(var2);
      }
   }

   private static class SourceNameFunction implements Function<String, String> {
      private SourceNameFunction() {
      }

      public String apply(String var1) {
         return stripQuotes(var1);
      }

      private static String stripQuotes(String var0) {
         return var0.length() < 2 ? var0 : KongaStringUtils.removeStartAndEnd(var0, "\"");
      }
   }

   private static class TargetNameFunction implements Function<SObjectSchema.Field, String> {
      private TargetNameFunction() {
      }

      public String apply(SObjectSchema.Field var1) {
         return var1.getName();
      }
   }
}
