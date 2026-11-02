package org.etlforge.integration.salesforce.mapping;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.integration.structure.mapping.MappingFactory;
import org.etlforge.util.properties.PropertiesReader;

public final class PropertiesToMappingFactory {
   public static List<Mapping> fromFile(File var0) throws IOException {
      Properties var1 = PropertiesReader.read(var0);
      return fromProperties(var1);
   }

   @VisibleForTesting
   static List<Mapping> fromProperties(Properties var0) {
      ArrayList var1 = Lists.newArrayList();

      for (String var3 : var0.stringPropertyNames()) {
         String var4 = var0.getProperty(var3);
         Mapping var5 = MappingFactory.newMapping(var4, "<trans>" + var3 + "</trans>");
         var1.add(var5);
      }

      return var1;
   }

   private PropertiesToMappingFactory() {
   }
}
