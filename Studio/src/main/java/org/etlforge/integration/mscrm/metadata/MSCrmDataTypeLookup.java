package org.etlforge.integration.mscrm.metadata;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.EnumSet;
import org.etlforge.lang.Enums;
import org.etlforge.string.StringTransforms;

public final class MSCrmDataTypeLookup {
   private final ImmutableMap<String, MSCrmDataType> dataTypes = Maps.uniqueIndex(
      EnumSet.allOf(MSCrmDataType.class), Functions.compose(StringTransforms.TO_LOWER_CASE, Enums.<MSCrmDataType>toNameFunction())
   );

   public MSCrmDataType getDataType(String var1) {
      MSCrmDataType var2 = this.dataTypes.get(var1.toLowerCase());
      return var2 == null ? MSCrmDataType.OTHER : var2;
   }
}
