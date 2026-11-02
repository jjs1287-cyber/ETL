package org.etlforge.integration.salesforce.mapping;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.structure.mapping.Mapping;

public abstract class SdlToSoapMappingConverter {
   public static boolean isSupportedForRequest(SfdcMethod var0) {
      switch (var0) {
         case UPSERT:
         case CREATE:
         case UPDATE:
            return true;
         default:
            return false;
      }
   }

   public abstract ImmutableList<Mapping> toSoap(List<? extends Mapping> var1);

   public static SdlToSoapMappingConverter forRequest(SfdcMethod var0, String var1) {
      switch (var0) {
         case UPSERT:
         case CREATE:
         case UPDATE:
            return new UcuMappingConverter(var0, var1);
         default:
            throw new UnsupportedOperationException();
      }
   }
}
