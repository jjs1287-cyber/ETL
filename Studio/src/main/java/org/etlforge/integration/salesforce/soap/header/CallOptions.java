package org.etlforge.integration.salesforce.soap.header;

import com.google.common.base.Strings;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.lang.Objects;

public final class CallOptions extends SoapHeader {
   public static final String NAME = "CallOptions";
   public static final String CLIENT = "client";
   public static final String DEFAULT_NS = "defaultNamespace";
   private final String client;
   private final String defaultNamespace;

   public CallOptions() {
      this("", "");
   }

   public CallOptions(String var1, String var2) {
      super("CallOptions");
      this.client = Strings.nullToEmpty(var1);
      this.defaultNamespace = Strings.nullToEmpty(var2);
   }

   public String getClient() {
      return this.client;
   }

   public String getDefaultNamespace() {
      return this.defaultNamespace;
   }

   @Override
   public String getStringRepresentation() {
      int var1 = this.client.length();
      return var1 == 0 && this.defaultNamespace.isEmpty() ? "" : var1 + "|" + this.client + this.defaultNamespace;
   }

   @Override
   protected void collectMappings(List<SoapHeader.Mapping> var1) {
      if (StringUtils.isNotEmpty(this.client)) {
         var1.add(SoapHeader.Mapping.forString("client$", this.client));
      }

      if (StringUtils.isNotEmpty(this.defaultNamespace)) {
         var1.add(SoapHeader.Mapping.forString("defaultNamespace$", this.defaultNamespace));
      }
   }

   @Override
   public String toString() {
      return String.format("client: [%s], defaultNamespace: [%s]", this.client, this.defaultNamespace);
   }

   public static CallOptions fromString(String var0) {
      if (var0.isEmpty()) {
         return new CallOptions();
      }

      try {
         int var1 = Integer.parseInt(StringUtils.substringBefore(var0, "|"));
         String var2 = var0.substring(2, 2 + var1);
         String var3 = var0.substring(2 + var1);
         return new CallOptions(var2, var3);
      } catch (Exception var4) {
         return new CallOptions();
      }
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.client, this.defaultNamespace);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof CallOptions)) {
         return false;
      }

      CallOptions var2 = (CallOptions)var1;
      return this.client.equals(var2.client) && this.defaultNamespace.equals(var2.defaultNamespace);
   }
}
