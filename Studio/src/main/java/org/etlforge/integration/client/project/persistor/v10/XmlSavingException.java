package org.etlforge.integration.client.project.persistor.v10;

import org.etlforge.integration.data.persist.InterchangeSavingException;

public class XmlSavingException extends InterchangeSavingException {
   public XmlSavingException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public XmlSavingException(String var1) {
      super(var1);
   }

   public XmlSavingException(Throwable var1) {
      super(var1);
   }
}
