package org.etlforge.integration.salesforce.soap.header;

import com.google.common.base.Strings;
import java.util.List;
import org.etlforge.integration.data.entity.id.SalesforceConnectorId;
import org.etlforge.integration.salesforce.SalesforceSessionVariables;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

public final class SessionHeader extends SoapHeader {
   private final String connectorId;

   public SessionHeader(SalesforceConnectorId var1) {
      super("SessionHeader");
      this.connectorId = var1 == null ? "" : var1.toString();
   }

   private SessionHeader(String var1) {
      super("SessionHeader");
      this.connectorId = var1;
   }

   @Override
   public String getStringRepresentation() {
      return this.connectorId;
   }

   public static SessionHeader fromString(String var0) {
      return new SessionHeader(Strings.nullToEmpty(var0));
   }

   @Override
   protected void collectMappings(List<SoapHeader.Mapping> var1) {
      String var2 = this.connectorId.isEmpty() ? "__Salesforce.SessionID" : SalesforceSessionVariables.getSessionId(this.connectorId);
      var1.add(SoapHeader.Mapping.forVariableUsingGet("sessionId$", var2));
   }

   @Override
   public String toString() {
      return "SessionHeader";
   }

   @Override
   public int hashCode() {
      return this.connectorId.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof SessionHeader && this.connectorId.equals(((SessionHeader)var1).connectorId);
   }
}
