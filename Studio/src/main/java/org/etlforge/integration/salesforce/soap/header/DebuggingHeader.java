package org.etlforge.integration.salesforce.soap.header;

import com.google.common.base.Preconditions;
import java.util.List;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

public final class DebuggingHeader extends SoapHeader {
   private final DebuggingHeader.Level level;

   public DebuggingHeader() {
      this(DebuggingHeader.Level.NONE);
   }

   public DebuggingHeader(DebuggingHeader.Level var1) {
      super("DebuggingHeader");
      Preconditions.checkNotNull(var1, "level");
      this.level = var1;
   }

   public DebuggingHeader.Level getLevel() {
      return this.level;
   }

   @Override
   public String getStringRepresentation() {
      return this.level.name();
   }

   public static DebuggingHeader fromString(String var0) {
      try {
         return new DebuggingHeader(DebuggingHeader.Level.valueOf(var0));
      } catch (Exception var2) {
         var2.printStackTrace();
         return new DebuggingHeader();
      }
   }

   @Override
   protected void collectMappings(List<SoapHeader.Mapping> var1) {
      var1.add(SoapHeader.Mapping.forString("debugLevel$", this.level.getXmlValue()));
   }

   @Override
   public int hashCode() {
      return this.level.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof DebuggingHeader && this.level == ((DebuggingHeader)var1).level;
   }

   @Override
   public String toString() {
      return this.level.toString();
   }

   public enum Level {
      NONE("None"),
      DEBUG_ONLY("DebugOnly"),
      DB("Db");

      private final String xmlValue;

      Level(String var3) {
         this.xmlValue = var3;
      }

      public String getXmlValue() {
         return this.xmlValue;
      }
   }
}
