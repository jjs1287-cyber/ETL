package org.etlforge.integration.salesforce.wsdl;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.Collection;
import org.etlforge.collections.Collections2;

public final class RelatedObjectsListEncoder {
   public static String encode(Collection<RelatedObject> var0) {
      if (var0.isEmpty()) {
         return "";
      }

      Iterable var1 = Iterables.transform(var0, new RelatedObjectsListEncoder.ToString());
      return Joiner.on("-:-").join(var1);
   }

   public static ImmutableList<RelatedObject> decode(String var0) {
      if (Strings.isNullOrEmpty(var0)) {
         return ImmutableList.of();
      }

      Iterable var1 = Splitter.on("-:-").split(var0);
      return FluentIterable.<String>from(var1).transform(new RelatedObjectsListEncoder.FromString()).filter(Predicates.notNull()).toImmutableList();
   }

   private RelatedObjectsListEncoder() {
   }

   private static class FromString implements Function<String, RelatedObject> {
      private FromString() {
      }

      public RelatedObject apply(String var1) {
         String[] var2 = var1.split("\\+\\+");
         if (var2.length != 4) {
            return null;
         }

         String var3 = var2[0];
         if (var3.isEmpty()) {
            return null;
         }

         String var4 = var2[1];
         if (var4.isEmpty()) {
            return null;
         }

         String var5 = var2[2];
         if (var5.isEmpty()) {
            return null;
         }

         Iterable var6 = Splitter.on("|").split(var2[3]);
         Iterable var7 = Iterables.transform(var6, FieldEncoder.decodeFunction("&"));
         return new RelatedObject(var3, var4, var5, var7);
      }
   }

   private static class ToString implements Function<RelatedObject, String> {
      private ToString() {
      }

      public String apply(RelatedObject var1) {
         ImmutableList var2 = var1.getExternalIds();
         String var3 = Collections2.join(var2, FieldEncoder.encodeFunction("&"), "|");
         return Joiner.on("++").join(var1.getRelationshipName(), var1.getFieldName(), var1.getObjectName(), var3);
      }
   }
}
