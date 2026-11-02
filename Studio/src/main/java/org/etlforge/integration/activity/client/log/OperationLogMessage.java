package org.etlforge.integration.activity.client.log;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

public final class OperationLogMessage {
   private final String name;
   private final String message;

   public OperationLogMessage(String var1, String var2) {
      this.name = Strings.nullToEmpty(var1);
      this.message = Strings.nullToEmpty(var2);
   }

   public String getEntryName() {
      return this.name;
   }

   public String getMessage() {
      return this.message;
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("field", this.name).add("message", this.message).toString();
   }
}
