package org.etlforge.integration.salesforce.wsdl;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

public final class RelatedObjectsFinder {
   public static List<RelatedObject> find(SObjectSchema var0, Function<String, SObjectSchema> var1) {
      ArrayList var2 = Lists.newArrayList();

      for (SObjectSchema.Field var4 : var0.getFields()) {
         if (var4.isCreateable() || var4.isUpdateable()) {
            String var5 = var4.getRelationshipName();
            if (!Strings.isNullOrEmpty(var5)) {
               List var6 = var4.getReferenceTo();
               if (var6.size() == 1) {
                  String var7 = (String)var6.get(0);
                  SObjectSchema var8 = (SObjectSchema)var1.apply(var7);
                  if (var8 != null) {
                     ArrayList var9 = Lists.newArrayList();

                     for (SObjectSchema.Field var11 : var8.getFields()) {
                        if (var11.isExternalId()) {
                           var9.add(var11);
                        }
                     }

                     if (!var9.isEmpty()) {
                        var2.add(new RelatedObject(var5, var4.getName(), var7, var9));
                     }
                  }
               }
            }
         }
      }

      return var2;
   }

   private RelatedObjectsFinder() {
   }
}
