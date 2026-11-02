package org.etlforge.integration.design.netsuite.definition;

import com.google.common.collect.Sets;
import java.util.Calendar;
import java.util.Set;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;

public class NetSuiteWsdlVersionFactory {
   private static NetSuiteWsdlVersionFactory.NetSuiteWsdlPossibleVersions versions = new NetSuiteWsdlVersionFactory.NetSuiteWsdlPossibleVersions();

   public static NetSuiteWsdlVersion fromNamespace(String var0) {
      return fromVersionedNameExists(var0);
   }

   private static NetSuiteWsdlVersion fromVersionedNameExists(String var0) {
      NetSuiteWsdlVersion var1 = fromVersionedName(var0);
      if (var1 != null) {
         return var1;
      } else {
         throw new ConnectorNetSuiteDefinitionError("No version found in " + var0);
      }
   }

   public static NetSuiteWsdlVersion fromVersionedName(String var0) {
      for (NetSuiteWsdlVersion var2 : versions.possibleVersions) {
         if (var0.contains(var2.toWsdlVersion())) {
            return var2;
         }
      }

      return null;
   }

   private static class NetSuiteWsdlPossibleVersions {
      private final Set<NetSuiteWsdlVersion> possibleVersions = Sets.newHashSet();

      NetSuiteWsdlPossibleVersions() {
         int var1 = Calendar.getInstance().get(1);

         for (int var2 = 2010; var2 <= var1 + 2; var2++) {
            for (int var3 = 1; var3 <= 3; var3++) {
               this.possibleVersions.add(new NetSuiteWsdlVersion(var2, var3));
            }
         }
      }
   }
}
