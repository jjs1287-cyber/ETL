package org.etlforge.integration.data.entity.sfquery;

import org.etlforge.integration.salesforce.parser.ParsedQuery;
import org.etlforge.integration.structure.mapping.NodePath;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.system.ArgCheck;

public final class QueryResponseFieldPath {
   private final String objectName;
   private final String responseRoot;
   private final String objectNode;

   public QueryResponseFieldPath(String var1) {
      this.objectName = ArgCheck.checkNotEmpty(var1);
      this.responseRoot = "root$transaction.response$body$queryResponse$result$records.";
      this.objectNode = this.responseRoot + var1;
   }

   public String getGeneratorPath() {
      return NodePath.toGeneratorPath(this.responseRoot);
   }

   public String getFieldPath(String var1) {
      return getFieldPath(this.objectNode + "$", this.objectName, var1);
   }

   private static String getFieldPath(String var0, String var1, String var2) {
      String var3 = ParsedQuery.getFieldNameWithoutObjectQualifier(var1, var2);
      String var4 = var3.replace('.', '$');
      return var0 + var4 + "$";
   }

   public String getPathToFieldInChildObject(String var1, String var2) {
      String var3 = KongaStringUtils.substringAfterLastIfPresent(var1, ".");
      String var4 = this.getPathToChildObject(var3);
      return getFieldPath(var4, var3, var2);
   }

   public String getPathToChildObject(String var1) {
      return this.objectNode + "$" + var1 + ".";
   }
}
