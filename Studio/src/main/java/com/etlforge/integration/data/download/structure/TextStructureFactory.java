package com.etlforge.integration.data.download.structure;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.data.structure.text.validation.FieldValidation;

public final class TextStructureFactory {
   private final TextDocument textDocument;

   public TextStructureFactory(TextDocument var1) {
      Preconditions.checkNotNull(var1, "doc");
      this.textDocument = var1;
   }

   private static String mapKey(StringKeyValuePairs var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append(var0.get("document_id"));
      var1.append('-').append(var0.get("field_id"));
      var1.append('-').append(var0.get("parent_id", "0"));
      return var1.toString();
   }

   private static void getLines(Collection<StringKeyValuePairs> var0, List<String> var1, Collection<StringKeyValuePairs> var2, List<FieldValidation> var3) {
      HashMap var4 = Maps.newHashMap();

      for (StringKeyValuePairs var6 : var2) {
         var4.put(mapKey(var6), FieldValidation.fromKeyValues(var6));
      }

      String[] var13 = TextDocument.getDocumentDefinitionsColumnNames();

      for (StringKeyValuePairs var7 : var0) {
         StringBuilder var8 = new StringBuilder();

         for (String var12 : var13) {
            var8.append(var7.getString(var12));
            var8.append("\t");
         }

         var8.deleteCharAt(var8.length() - 1);
         var1.add(var8.toString());
         FieldValidation var15 = (FieldValidation)var4.get(mapKey(var7));
         var3.add(var15);
      }
   }

   public TextStructure createTextStructure(Collection<StringKeyValuePairs> var1, Collection<StringKeyValuePairs> var2) {
      ArrayList var3 = new ArrayList(var1.size());
      ArrayList var4 = new ArrayList(var1.size());
      getLines(var1, var3, var2, var4);
      return this.textDocument.createTextStructure(var3, var4);
   }
}
