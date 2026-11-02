package org.etlforge.util.kongaprotocol;

public final class KongaModuleStatusBitFields {
   public static final int rsUNDEFINED = 0;
   public static final int rsSTOPPED = 1;
   public static final int rsRUNNING = 2;
   public static final int rsSTOPPING = 4;
   public static final int rsERROR = 8;
   public static final int rsSINGLE_THREAD = 16;
   public static final int rsRESOURCE_DISTRIBUTION = 32;
   public static final int rsRESOURCE_DISTRIBUTION_ERROR = 64;
   public static final int rsDISABLED = 128;
   public static final int rsUNREACHABLE = 256;
   public static final int rsSTARTING = 512;

   private KongaModuleStatusBitFields() {
   }
}
