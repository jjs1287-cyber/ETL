package org.etlforge.integration.data.structure.text.validation;

import org.etlforge.util.persist.Persistor;

public enum InvalidRecordAction {
   SKIP_FIELD,
   USE_DEFAULT_VALUE,
   SKIP_ROW,
   RAISE_ERROR;

   void writeTo(Persistor var1) {
      var1.putString("action", this.name());
   }

   static InvalidRecordAction readFrom(Persistor var0) {
      String var1 = var0.getString("action");
      return valueOf(var1);
   }

   @Override
   public String toString() {
      switch (this) {
         case SKIP_FIELD:
            return "Skip Field";
         case USE_DEFAULT_VALUE:
            return "Use Default Value";
         case SKIP_ROW:
            return "Skip Row";
         case RAISE_ERROR:
            return "Raise Error";
         default:
            throw new RuntimeException();
      }
   }
}
