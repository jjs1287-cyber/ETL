package org.etlforge.integration.data.entity.webservicecall;

import com.google.common.base.Preconditions;
import javax.wsdl.Input;
import javax.wsdl.Operation;
import javax.wsdl.OperationType;
import javax.wsdl.Output;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;

public final class WebServiceOperationIdentifier {
   private final String name;
   private final String input;
   private final String output;

   public WebServiceOperationIdentifier(String var1, String var2, String var3) {
      Preconditions.checkArgument(StringUtils.isNotBlank(var1), "name cannot be null or blank");
      this.name = var1;
      this.input = var2;
      this.output = var3;
   }

   public static WebServiceOperationIdentifier fromOperation(Operation var0) {
      String var1 = var0.getName();
      OperationType var2 = var0.getStyle();
      Input var3 = var0.getInput();
      String var4 = getInputName(var1, var2, var3);
      Output var5 = var0.getOutput();
      String var6 = getOutputName(var1, var2, var5);
      return new WebServiceOperationIdentifier(var1, var4, var6);
   }

   public static WebServiceOperationIdentifier fromOperationInfo(WebServiceOperationInfo var0) {
      String var1 = var0.getProperty("Operation");
      String var2 = var0.getProperty("InputName");
      String var3 = var0.getProperty("OutputName");
      return new WebServiceOperationIdentifier(var1, var2, var3);
   }

   public static WebServiceOperationIdentifier fromWebServiceCall(WebServiceCall var0) {
      String var1 = var0.getProperty("operation");
      String var2 = var0.getProperty("operation_input");
      String var3 = var0.getProperty("operation_output");
      return new WebServiceOperationIdentifier(var1, var2, var3);
   }

   private static String getInputName(String var0, OperationType var1, Input var2) {
      String var3 = var2 != null ? var2.getName() : null;
      if (var3 == null) {
         var3 = getDefaultInputName(var0, var1);
      }

      return var3;
   }

   private static String getDefaultInputName(String var0, OperationType var1) {
      if (var1 == OperationType.ONE_WAY) {
         return var0;
      } else if (var1 == OperationType.REQUEST_RESPONSE) {
         return var0 + "Request";
      } else {
         return var1 == OperationType.SOLICIT_RESPONSE ? var0 + "Solicit" : null;
      }
   }

   private static String getOutputName(String var0, OperationType var1, Output var2) {
      String var3 = var2 != null ? var2.getName() : null;
      if (var3 == null) {
         var3 = getDefaultOutputName(var0, var1);
      }

      return var3;
   }

   private static String getDefaultOutputName(String var0, OperationType var1) {
      if (var1 == OperationType.NOTIFICATION) {
         return var0;
      } else {
         return var1 != OperationType.REQUEST_RESPONSE && var1 != OperationType.SOLICIT_RESPONSE ? null : var0 + "Response";
      }
   }

   public String getOperationName() {
      return this.name;
   }

   public String getInputName() {
      return this.input;
   }

   public String getOutputName() {
      return this.output;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof WebServiceOperationIdentifier ? this.equals((WebServiceOperationIdentifier)var1) : false;
      }
   }

   private boolean equals(WebServiceOperationIdentifier var1) {
      return areEqual(this.name, var1.name) && areEqual(this.input, var1.input) && areEqual(this.output, var1.output);
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.name.hashCode();
      var1 = 31 * var1 + (this.input == null ? 0 : this.input.hashCode());
      return 31 * var1 + (this.output == null ? 0 : this.output.hashCode());
   }

   private static boolean areEqual(Object var0, Object var1) {
      return var0 == null ? var1 == null : var0.equals(var1);
   }
}
