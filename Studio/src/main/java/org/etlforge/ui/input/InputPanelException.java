package org.etlforge.ui.input;

import java.util.List;
import org.etlforge.io.KongaIoUtils;

public class InputPanelException extends Exception {
   private InputFieldException[] fieldExceptions;

   public InputPanelException(String var1) {
      super(var1);
   }

   public InputPanelException(InputFieldException[] var1) {
      this.fieldExceptions = (InputFieldException[])var1.clone();
   }

   public InputPanelException(List<InputFieldException> var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("fieldExceptions can't be null.");
      }

      this.fieldExceptions = var1.toArray(new InputFieldException[var1.size()]);
   }

   public InputFieldException[] getFieldExceptions() {
      return (InputFieldException[])this.fieldExceptions.clone();
   }

   @Override
   public String getMessage() {
      if (this.fieldExceptions == null) {
         return super.getMessage();
      }

      StringBuilder var1 = new StringBuilder();
      int var2 = 0;

      for (InputFieldException var6 : this.fieldExceptions) {
         if (var2 > 0) {
            var1.append(KongaIoUtils.LINE_BREAK);
         }

         String var7 = var6.getInputField().getName();
         if (var7 != null && var7.length() > 0) {
            var7 = var7.trim();
            var1.append(var7);
            if (!var7.endsWith(":")) {
               var1.append(":");
            }

            var1.append(" ");
         }

         var1.append(var6.getMessage());
         var2++;
      }

      return var1.toString();
   }
}
