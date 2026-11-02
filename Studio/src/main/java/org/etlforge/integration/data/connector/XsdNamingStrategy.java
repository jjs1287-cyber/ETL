package org.etlforge.integration.data.connector;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.file.InvalidCharactersReplacer;

public final class XsdNamingStrategy {
   private static final int FUNCTION_SPECIFIC_MAX_LENGTH = 30;
   private static final String REQUEST_POSTFIX = "request";
   private static final String RESPONSE_POSTFIX = "response";
   private final String connectorId;
   private final String functionSpecificName;
   private volatile Pattern pattern;

   public XsdNamingStrategy(String var1, String var2) {
      ArgCheck.checkNotEmpty(var1, "connectorId");
      Preconditions.checkNotNull(var2, "functionSpecificName");
      InvalidCharactersReplacer var3 = new InvalidCharactersReplacer("_");
      this.connectorId = var3.replace(var1).toLowerCase();
      this.functionSpecificName = tweakFunctionSpecificName(var2, var3);
   }

   private static String tweakFunctionSpecificName(String var0, InvalidCharactersReplacer var1) {
      String var2 = var1.replace(var0);
      if (var2.length() > 30) {
         var2 = var2.substring(0, 30);
      }

      return var2;
   }

   public String getRequestName(ConnectorCall var1) {
      return this.createXsdName(var1, "request");
   }

   public String createXsdName(ConnectorCall var1, String var2) {
      return this.functionSpecificName.isEmpty()
         ? String.format("etlforge.%s.%s.%s.xsd", this.connectorId, var1.getFunctionID(), var2)
         : String.format("etlforge.%s.%s.%s.%s.xsd", this.connectorId, var1.getFunctionID(), this.functionSpecificName, var2);
   }

   public String getResponseName(ConnectorCall var1) {
      return this.createXsdName(var1, "response");
   }

   public boolean isRecognizedFile(String var1) {
      if (this.pattern == null) {
         this.pattern = this.createRegex();
      }

      return this.pattern.matcher(var1).matches();
   }

   public String getDisplayName(String var1) {
      if (this.pattern == null) {
         this.pattern = this.createRegex();
      }

      Matcher var2 = this.pattern.matcher(var1);
      if (var2.matches()) {
         String var3 = var2.group(1);
         if (var3.toLowerCase().endsWith(".xsd")) {
            return var3;
         }
      }

      return var1;
   }

   private Pattern createRegex() {
      String var1 = "[0-9a-f]";
      String var2 = String.format("%s{8}-%s{4}-%s{4}-%s{4}-%s{12}", var1, var1, var1, var1, var1);
      String var3 = String.format("etlforge\\.%s\\.(?:%s)\\.(.+)", Pattern.quote(this.connectorId), var2);
      return Pattern.compile(var3);
   }

   @VisibleForTesting
   int getMaxLengthForRequest() {
      return this.getMaxNameLength("request");
   }

   @VisibleForTesting
   int getMaxLengthForResponse() {
      return this.getMaxNameLength("response");
   }

   private int getMaxNameLength(String var1) {
      return "etlforge.".length() + this.connectorId.length() + 1 + 36 + 1 + 30 + 1 + var1.length() + 4;
   }
}
