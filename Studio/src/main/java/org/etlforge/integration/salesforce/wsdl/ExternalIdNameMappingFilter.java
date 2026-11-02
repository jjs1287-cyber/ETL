package org.etlforge.integration.salesforce.wsdl;

import com.google.common.base.Predicate;
import org.etlforge.integration.structure.mapping.Mapping;

public final class ExternalIdNameMappingFilter implements Predicate<Mapping> {
   public boolean apply(Mapping var1) {
      String var2 = var1.getTargetPath().path;
      return "root$transaction.body$upsert$externalIDFieldName$".equals(var2);
   }
}
