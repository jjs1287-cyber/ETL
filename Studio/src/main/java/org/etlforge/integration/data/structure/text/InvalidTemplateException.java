package org.etlforge.integration.data.structure.text;

public class InvalidTemplateException extends Exception {
   public InvalidTemplateException(String var1) {
      super(var1);
   }

   public InvalidTemplateException(Throwable var1) {
      this("It seems the sample file is invalid. Please consult the documentation, or contact ETLForge Support.", var1);
   }

   public InvalidTemplateException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
