package com.etlforge.integration.server.sfdc;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import org.etlforge.system.ArgCheck;

public final class GetServerTimestampInput {
   public final String serviceEndpoint;
   public final String sessionId;
   public final Optional<String> timeZoneId;

   public static GetServerTimestampInput usingOrgTimeZone(String var0, String var1) {
      return new GetServerTimestampInput(var0, var1, null);
   }

   public GetServerTimestampInput(String var1, String var2, String var3) {
      this.serviceEndpoint = ArgCheck.checkNotEmpty(var1, "serviceEndpoint");
      this.sessionId = ArgCheck.checkNotEmpty(var2, "sessionId");
      this.timeZoneId = Optional.fromNullable(Strings.emptyToNull(var3));
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this)
         .add("service endpoint", this.serviceEndpoint)
         .add("session ID", this.sessionId)
         .add("timezone", this.timeZoneId)
         .toString();
   }
}
