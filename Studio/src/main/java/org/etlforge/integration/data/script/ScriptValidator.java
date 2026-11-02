package org.etlforge.integration.data.script;

import com.google.common.base.Preconditions;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public final class ScriptValidator {
   private SourceDataElementContainer sourceDEs = SourceDataElementContainer.EMPTY;

   public void setSourceDataElementContainer(SourceDataElementContainer var1) {
      Preconditions.checkNotNull(var1, "sourceDEs");
      this.sourceDEs = var1;
   }

   public ScriptStatus validate(String var1) {
      if (StringUtils.isBlank(var1)) {
         return ScriptStatus.warning("The script is empty");
      }

      Transform var2 = new Transform();
      var2.setSourceDataElementContainer(this.sourceDEs);
      List var3 = var2.getRpnTokenVector(var1);
      if (var3 != null) {
         for (String reference : NativeJavaScript.references(var1)) {
            if (reference.startsWith("S:") && !this.sourceDEs.contains(reference.substring(2)))
               return ScriptStatus.invalid("Undefined source field: " + reference.substring(2));
         }
         return this.validate(var3);
      }

      String var4 = var2.m_err != null ? var2.m_err.getFormattedErrorMessage() : "Unknown error";
      return ScriptStatus.invalid(var4);
   }

   public ScriptStatus validate(List<Transform.Token> var1) {
      HashSet var2 = new HashSet<>(Transform.getDEVector(var1, 405));
      if (!var2.isEmpty()) {
         StringBuilder var3 = new StringBuilder();

         for (Object __etlItem37 : var2) {
            String var5 = (String) __etlItem37;
            if (!this.sourceDEs.contains(var5)) {
               if (var3.length() > 0) {
                  var3.append(",");
               }

               var3.append(var5);
            }
         }

         if (var3.length() > 0) {
            return ScriptStatus.invalid("Undefined token: " + var3);
         }
      }

      return ScriptStatus.VALID;
   }
}
