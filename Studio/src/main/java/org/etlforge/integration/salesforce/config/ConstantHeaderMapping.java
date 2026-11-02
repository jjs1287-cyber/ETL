package org.etlforge.integration.salesforce.config;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;

public final class ConstantHeaderMapping {
   private final String targetPath;
   private final Object value;

   public ConstantHeaderMapping(String var1, Object var2) {
      Preconditions.checkArgument(StringUtils.isNotEmpty(var1), "targetPath may not be null or empty");
      Preconditions.checkNotNull(var2, "value");
      this.targetPath = var1;
      this.value = var2;
   }

   public String getTargetPath() {
      return this.targetPath;
   }

   public Object getValue() {
      return this.value;
   }
}
