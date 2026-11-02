package org.etlforge.integration.data.entity.sfdc;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;

public final class SoapHeaders {
   private static final String PROPERTY_PREFIX = "sfdc.soapheader.";

   public static void store(IntegrationEntity var0, Set<SalesforceWizardEntity.Header> var1) {
      synchronized (var0.getDataLock()) {
         StringKeyValuePairs var3 = var0.getKeyValues();
         var3.wildcardRemove("sfdc.soapheader.*");

         for (SalesforceWizardEntity.Header var5 : var1) {
            var3.putString("sfdc.soapheader." + var5.name, var5.value);
         }
      }
   }

   public static Set<String> getExistingKeys(IntegrationEntity var0) {
      synchronized (var0.getDataLock()) {
         StringKeyValuePairs var2 = var0.getKeyValues();
         return var2.wildcardKeyList("sfdc.soapheader.*");
      }
   }

   public static Set<SalesforceWizardEntity.Header> read(IntegrationEntity var0) {
      HashSet var1 = Sets.newHashSet();
      synchronized (var0.getDataLock()) {
         StringKeyValuePairs var3 = var0.getKeyValues();

         for (String var6 : getExistingKeys(var0)) {
            String var7 = var3.get(var6);
            if (var7 != null) {
               var1.add(new SalesforceWizardEntity.Header(StringUtils.substringAfter(var6, "sfdc.soapheader."), var7));
            }
         }

         return var1;
      }
   }

   private SoapHeaders() {
   }
}
