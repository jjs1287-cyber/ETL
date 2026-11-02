package org.etlforge.util.kongaprotocol;

public class KongaPacket {
   private final String moduleName;

   public KongaPacket(String var1) {
      this.moduleName = var1;
   }

   public String getModuleName() {
      return this.moduleName;
   }

   public String getProtocolName() {
      return "KONGA";
   }

   public String getProtocolEndString() {
      return "AGNOK";
   }

   public int getProtocolVersion() {
      return 1;
   }
}
