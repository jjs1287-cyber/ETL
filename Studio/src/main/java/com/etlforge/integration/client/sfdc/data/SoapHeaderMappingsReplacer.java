package com.etlforge.integration.client.sfdc.data;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.integration.structure.mapping.MapStringFormat;
import org.etlforge.integration.structure.mapping.Mapping;

public final class SoapHeaderMappingsReplacer {
   private final ImmutableSet<SoapHeader> newHeaders;
   private Logger logger = Logger.getAnonymousLogger();

   public SoapHeaderMappingsReplacer(Set<SoapHeader> var1) {
      this.newHeaders = ImmutableSet.copyOf(var1);
   }

   public void setLogger(Logger var1) {
      this.logger = Preconditions.checkNotNull(var1);
   }

   public void replaceSoapHeaders(List<String> var1) {
      this.removeExistingHeaderMappings(var1);

      for (SoapHeader var3 : this.newHeaders) {
         List var4 = var3.getMapList();
         this.logger.info("Adding new mappings: " + Joiner.on('\n').join(var4));
         var1.addAll(var4);
      }
   }

   private void removeExistingHeaderMappings(List<String> var1) {
      String var2 = "root$transaction.header$";
      Iterator var3 = var1.iterator();

      while (var3.hasNext()) {
         String var4 = (String)var3.next();
         Mapping var5 = MapStringFormat.parse(var4);
         if (var5.getTargetPath().toString().startsWith(var2)) {
            var3.remove();
            this.logger.fine("Removing existing mapping: " + var4);
         }
      }
   }
}
