package org.etlforge.integration.salesforce.bulkload;

import com.google.common.base.Joiner;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.etlforge.integration.salesforce.wsdl.FieldEncoder;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

public final class FieldListEncoder {
   public static String encode(Iterable<SObjectSchema.Field> var0) {
      Iterable var1 = Iterables.transform(var0, FieldEncoder.encodeFunction("&"));
      return Joiner.on("|").join(var1);
   }

   public static ImmutableList<SObjectSchema.Field> decode(String var0) {
      if (Strings.isNullOrEmpty(var0)) {
         return ImmutableList.of();
      }

      Iterable var1 = Splitter.on("|").split(var0);
      return FluentIterable.<String>from(var1).transform(FieldEncoder.decodeFunction("&")).filter(Predicates.notNull()).toImmutableList();
   }

   private FieldListEncoder() {
   }
}
