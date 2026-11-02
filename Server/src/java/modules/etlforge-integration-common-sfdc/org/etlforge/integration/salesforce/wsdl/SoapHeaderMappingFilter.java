package org.etlforge.integration.salesforce.wsdl;

import com.google.common.base.Predicate;
import org.etlforge.integration.structure.mapping.Mapping;

public final class SoapHeaderMappingFilter implements Predicate<Mapping> {
   public boolean apply(Mapping var1) {
      String var2 = var1.getTargetPath().pathWithoutBrackets();
      return var2.startsWith("root$transaction.header$");
   }

   @Override
   public String toString() {
      return "Soap Header Mapping";
   }
}
