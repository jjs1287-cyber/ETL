package org.etlforge.integration.client.project.persistor.v10;

public class XmlLoadingException extends Exception {
   public XmlLoadingException(String var1) {
      super(var1);
   }

   public XmlLoadingException(String var1, Exception var2) {
      super(var1, var2);
   }

   public XmlLoadingException(Exception var1) {
      super(var1.getMessage(), var1);
   }
}
