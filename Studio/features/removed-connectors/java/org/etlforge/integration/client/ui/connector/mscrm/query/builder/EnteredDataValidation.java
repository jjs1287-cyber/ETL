package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Preconditions;
import org.etlforge.system.ArgCheck;

final class EnteredDataValidation {
   public static final EnteredDataValidation OK = new EnteredDataValidation(null);
   private final String error;

   public static EnteredDataValidation error(String var0) {
      return new EnteredDataValidation(ArgCheck.checkNotEmpty(var0));
   }

   private EnteredDataValidation(String var1) {
      this.error = var1;
   }

   public boolean isValid() {
      return this.error == null;
   }

   public String whyNot() {
      Preconditions.checkState(this.error != null, "There were no validation errors.");
      return this.error;
   }
}
