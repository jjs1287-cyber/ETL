package org.etlforge.integration.debug.client;

import org.apache.commons.lang.StringUtils;
import org.etlforge.util.CSVUtils;

public final class TargetNode {
   private final String path;
   private final String valueType;
   private final int sqlDataType;
   private final String format;

   public TargetNode(String var1, String var2, int var3, String var4) {
      this.path = var1;
      this.valueType = var2;
      this.sqlDataType = var3;
      this.format = var4;
   }

   public String getPath() {
      return this.path;
   }

   public String encode() {
      String var1 = String.format(
         "etlforge.target.node.name=%s;etlforge.target.target.ValueType=%s;etlforge.target.target.SqlDataType=%d;",
         this.path,
         this.valueType,
         this.sqlDataType
      );
      if (!StringUtils.isEmpty(this.format)) {
         var1 = var1 + "etlforge.target.target.Format=" + CSVUtils.getValidCSVString(this.format, ";") + ";";
      }

      return var1;
   }

   @Override
   public String toString() {
      return this.path;
   }
}
