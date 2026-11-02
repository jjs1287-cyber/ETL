package org.etlforge.integration.server.backend.webservice.connector;

import com.google.common.base.Strings;
import org.etlforge.integration.common.server.ServerErrors;
import org.etlforge.integration.server.engine.connector.exception.ConnectorEndpointRuntimeException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorRuntimeException;
import org.etlforge.integration.server.ws.WsUtils;

public class ConnectorEngineWsExceptionUtils {
   ConnectorEngineWsExceptionUtils.MessageConversion conversion = new ConnectorEngineWsExceptionUtils.MessageConversion();

   WsStatus obtainBadWsStatus(Throwable var1, WsConnectorInput var2, String var3) {
      String var4 = Strings.isNullOrEmpty(var3) ? null : "\n Input File: " + var3;
      return this.obtainBadWsStatus(var1, var2.getEndpointParameters(), var2.getSelectedObjectNames(), var4);
   }

   WsStatus obtainBadWsStatus(Throwable var1, WsConnectorInput var2) {
      return this.obtainBadWsStatus(var1, var2.getEndpointParameters(), var2.getSelectedObjectNames(), null);
   }

   public WsStatus obtainBadWsStatus(Throwable var1) {
      return this.conversion.ConvertToWsStatus(-100, var1, null);
   }

   WsStatus obtainBadWsStatus(Throwable var1, WsEndpointParameters var2, String[] var3, String var4) {
      var1.printStackTrace();
      if (var1 instanceof Exception) {
         if (var1 instanceof ConnectorRuntimeException) {
            return var1 instanceof ConnectorEndpointRuntimeException
               ? this.conversion.ConvertToWsStatus(-401, var1, var2, null, null)
               : this.conversion.ConvertToWsStatus(-1, var1, var2, var3, var4);
         } else {
            return this.conversion.ConvertToWsStatus(-2, var1, var2, var3, var4);
         }
      } else {
         return this.conversion.ConvertToWsStatus(-3, var1, null);
      }
   }

   String toString(WsEndpointParameters var1) {
      return this.conversion.toString(var1);
   }

   private static class MessageConversion {
      static final String CONNECTERROR = "\n Connector Error: ";

      private MessageConversion() {
      }

      WsStatus ConvertToWsStatus(int var1, Throwable var2, WsEndpointParameters var3, String[] var4, String var5) {
         StringBuilder var6 = new StringBuilder();
         if (var3 != null) {
            var6.append(this.toString(var3));
         }

         if (var4 != null && var4.length > 0) {
            var6.append(this.toString(var4));
         }

         if (!Strings.isNullOrEmpty(var5)) {
            var6.append(var5);
         }

         return this.ConvertToWsStatus(var1, var2, var6.toString());
      }

      WsStatus ConvertToWsStatus(int var1, Throwable var2, String var3) {
         String var4 = this.toString(var2);
         return Strings.isNullOrEmpty(var3) ? new WsStatus(var1, var4) : new WsStatus(var1, var3 + "\n" + var4);
      }

      private String toString(String[] var1) {
         StringBuilder var2 = new StringBuilder();
         if (var1 != null && var1.length > 0) {
            var2.append("\nInput List: [");

            for (String var6 : var1) {
               var2.append(var6).append(',');
            }

            var2.deleteCharAt(var2.length() - 1);
            var2.append(']');
         }

         return var2.toString();
      }

      private void appendNonEmpty(StringBuilder var1, String var2, String var3) {
         if (!Strings.isNullOrEmpty(var3)) {
            var1.append(var2 + "=" + var3 + ";");
         }
      }

      private String toString(WsEndpointParameters var1) {
         StringBuilder var2 = new StringBuilder();
         if (var1 == null) {
            return var2.toString();
         }

         String var3 = var1.getEndpointTypeName().toUpperCase();
         var2.append("EndpointType=" + var1.getEndpointTypeName() + ";");
         boolean var4 = var3.equals("AUTODESK") || var3.equals("MSCRM") || var3.equals("MSGP") || var3.equals("SAP");
         if (var4) {
            this.appendNonEmpty(var2, "Server", var1.getHost());
         }

         this.appendNonEmpty(var2, "User", var1.getUser());

         for (WsNameValuePair var8 : var1.getAdditionalParameters()) {
            var2.append(var8.getName() + "=" + var8.getValue() + ";");
         }

         return var2.toString();
      }

      private String toString(Throwable var1) {
         StringBuilder var2 = new StringBuilder();
         boolean var3 = var1.getCause() != null;
         if (var3) {
            var2.append("\n Connector Error: ");
            var2.append(WsUtils.escapeExceptionMessageAndGetCause(var1));
            ServerErrors.appendStacktrace(var1, var2);
            return var2.toString();
         } else {
            return "\n Connector Error: " + var1.getMessage();
         }
      }
   }
}
