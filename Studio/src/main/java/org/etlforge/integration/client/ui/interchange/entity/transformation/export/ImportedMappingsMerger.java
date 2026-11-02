package org.etlforge.integration.client.ui.interchange.entity.transformation.export;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.List;
import org.etlforge.collections.Collections2;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.ExternalIdNameMappingFilter;
import org.etlforge.integration.salesforce.wsdl.SoapHeaderMappingFilter;
import org.etlforge.integration.structure.mapping.MapStringFormat;
import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.integration.structure.mapping.MappingFunctions;

final class ImportedMappingsMerger {
   public static List<String> merge(Transformation var0, List<String> var1) {
      return isPartnerWsdlRequest(var0) ? mergeImpl(var0, var1) : var1;
   }

   private static boolean isPartnerWsdlRequest(Transformation var0) {
      return var0.isPartOfSalesforcePartnerWizard() && var0.getSalesforceWizardRole().isRequest();
   }

   private static List<String> mergeImpl(Transformation var0, List<String> var1) {
      Predicate var2 = toKeep(var0);
      List var3 = var0.getMappings();
      List var4 = Collections2.keep(var3, var2);
      HashSet var5 = Sets.newHashSet(Iterables.transform(var4, MappingFunctions.TO_TARGET_PATH));
      List var6 = var4;

      for (String var8 : var1) {
         Mapping var9 = MapStringFormat.parse(var8);
         if (!var5.contains(var9.getTargetPath())) {
            var6.add(var9);
         }
      }

      return KongaListUtils.transform(var6, MapStringFormat.MAPPING_TO_STRING);
   }

   private static Predicate<Mapping> toKeep(Transformation var0) {
      SoapHeaderMappingFilter var1 = new SoapHeaderMappingFilter();
      return var0.getSalesforceWizardRole().method == SfdcMethod.UPSERT ? Predicates.or(var1, new ExternalIdNameMappingFilter()) : var1;
   }

   private ImportedMappingsMerger() {
   }
}
