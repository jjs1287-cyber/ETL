package org.etlforge.util.kongaprotocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.etlforge.util.net.CommandResult;

public final class KongaProtocolUtils {
   public static final String KONGA = "KONGA";
   public static final String AGNOK = "AGNOK";
   private static final int DEFAULT_READ_TIMEOUT_SEC = 30;
   private static final int DEFAULT_READ_SLEEP_MS = 100;
   private static final int bufSize = 64;

   private static String getString(byte[] var0, int var1, int var2) {
      if (var1 < 0) {
         throw new IllegalArgumentException("offset cannot be a negative number.");
      }

      if (var2 < 1) {
         return "";
      }

      if (var1 + var2 > var0.length) {
         throw new IllegalArgumentException("Attempting to read outside of the supplied byte array.");
      }

      StringBuilder var3 = new StringBuilder("");

      for (int var4 = var1; var4 < var1 + var2; var4++) {
         var3.append((char)var0[var4]);
      }

      return var3.toString();
   }

   private static int getInt(byte[] var0, int var1) {
      if (var1 + 4 > var0.length) {
         throw new IllegalArgumentException("Attempting to read outside of the supplied byte array.");
      }

      int[] var2 = new int[]{var0[var1] & 255, var0[var1 + 1] & 255, var0[var1 + 2] & 255, var0[var1 + 3] & 255};
      return var2[3] + 256 * (var2[2] + 256 * (var2[1] + 256 * var2[0]));
   }

   public static void appendString(OutputStream var0, String var1) throws IOException {
      int var2 = var1.length();

      for (int var3 = 0; var3 < var2; var3++) {
         var0.write(var1.charAt(var3));
      }
   }

   public static void appendInt(OutputStream var0, int var1) throws IOException {
      byte[] var2 = new byte[]{(byte)(var1 >> 24 & 0xFF), (byte)(var1 >> 16 & 0xFF), (byte)(var1 >> 8 & 0xFF), (byte)(var1 & 0xFF)};
      var0.write(var2);
   }

   private static boolean hasEnoughInput(byte[] var0, int var1, int var2) {
      return var1 >= 0 && var2 >= 0 && var1 + var2 <= var0.length;
   }

   private static boolean hasEnoughInputForInt(byte[] var0, int var1) {
      return hasEnoughInput(var0, var1, 4);
   }

   private static int addCommands(byte[] var0, int var1, KongaRequest var2) throws KongaProtocolException {
      if (!hasEnoughInputForInt(var0, var1)) {
         throw new KongaProtocolException("Not enough input for number of commands.");
      }

      int var3 = getInt(var0, var1);
      var1 += 4;
      if (var3 < 1) {
         return var1;
      }

      for (int var4 = 0; var4 < var3; var4++) {
         if (!hasEnoughInputForInt(var0, var1)) {
            throw new KongaProtocolException("Not enough input for next command.");
         }

         int var5 = getInt(var0, var1);
         if (var5 < 1) {
            throw new KongaProtocolException("Empty command found in command sequence.");
         }

         var1 += 4;
         if (!hasEnoughInput(var0, var1, var5)) {
            throw new KongaProtocolException("Not enough input for command name");
         }

         String var6 = getString(var0, var1, var5);
         var1 += var5;
         KongaCommand var7 = new KongaCommand(var6);
         if (!hasEnoughInputForInt(var0, var1)) {
            throw new KongaProtocolException("Not enough input for number of arguments for command \"" + var6 + "\".");
         }

         int var8 = getInt(var0, var1);
         var1 += 4;

         for (int var9 = 0; var9 < var8; var9++) {
            if (!hasEnoughInputForInt(var0, var1)) {
               throw new KongaProtocolException("Not enough input for size of argument");
            }

            int var10 = getInt(var0, var1);
            var1 += 4;
            String var11 = getString(var0, var1, var10);
            var1 += var10;
            var7.getArguments().add(var11);
         }

         var2.addCommand(var7);
      }

      return var1;
   }

   private static void wrapPacket(OutputStream var0, byte[] var1, KongaPacket var2) throws IOException {
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();
      appendString(var3, var2.getProtocolName());
      appendInt(var3, var2.getProtocolVersion());
      appendInt(var3, var3.size() + 4 + var1.length + var2.getProtocolEndString().length());
      var3.write(var1);
      appendString(var3, var2.getProtocolEndString());
      var0.write(var3.toByteArray());
      var0.flush();
   }

   public static void serializeKongaRequest(OutputStream var0, KongaResponse var1) throws IOException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      String var3 = var1.getModuleName();
      appendInt(var2, var3.length());
      appendString(var2, var3);
      var2.write(1);
      appendInt(var2, 0);
      int var4 = var1.getErrorCode();
      appendInt(var2, var4);
      if (var4 > 0) {
         String var5 = var1.getErrorString();
         appendInt(var2, var5.length());
         appendString(var2, var5);
      } else {
         appendInt(var2, var1.getNumberOfCommands());

         for (CommandResult var6 : var1) {
            String var7 = var6.getCommandName();
            appendInt(var2, var7.length());
            appendString(var2, var7);
            appendInt(var2, var6.getNumberOfReturnValues());

            for (String var9 : var6) {
               appendInt(var2, var9.length());
               appendString(var2, var9);
            }
         }
      }

      wrapPacket(var0, var2.toByteArray(), var1);
   }

   public static KongaRequest deserializeKongaRequest(InputStream var0) throws IOException, KongaProtocolException, InterruptedException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      int var2 = 0;

      while (var0.available() == 0) {
         if (++var2 * 100 > 30000) {
            throw new IOException("Time out occurred waiting for the client to write data.");
         }

         Thread.sleep(100L);
      }

      while (var0.available() > 0) {
         byte[] var3 = new byte[64];
         int var4 = var0.read(var3);
         var1.write(var3, 0, var4);
      }

      int var17 = var1.size();
      if (var17 < 24) {
         throw new KongaProtocolException("The request byte stream is too short to be a Konga request (" + Integer.toString(var17) + ")");
      } else {
         byte[] var18 = var1.toByteArray();
         Object var16 = null;
         if (!getString(var18, 0, "KONGA".length()).equals("KONGA")) {
            throw new KongaProtocolException("Request does not begin with KONGA");
         } else if (!getString(var18, var17 - "AGNOK".length(), "AGNOK".length()).equals("AGNOK")) {
            throw new KongaProtocolException("Request does not end with AGNOK");
         } else {
            int var5 = "KONGA".length();
            int var6 = getInt(var18, var5);
            if (var6 != 1) {
               throw new KongaProtocolException("Unsupported protocol version: " + Integer.toString(var6));
            } else {
               var5 += 4;
               if (getInt(var18, var5) != var17) {
                  throw new KongaProtocolException("Actual request size does not match stated size");
               } else {
                  var5 += 4;
                  int var7 = getInt(var18, var5);
                  if (var7 < 0) {
                     throw new KongaProtocolException("No module name was specified.");
                  } else {
                     var5 += 4;
                     if (!hasEnoughInput(var18, var5, var7)) {
                        throw new KongaProtocolException("Not enough input to hold the module name.");
                     } else {
                        String var8 = getString(var18, var5, var7);
                        var5 += var7;
                        if (!hasEnoughInput(var18, var5, 1)) {
                           throw new KongaProtocolException("Not enough input to determine direction of packet");
                        } else if (var18[var5] != 0 && var18[var5] != 1) {
                           throw new KongaProtocolException("Direction byte has to be either 0 or 1.");
                        } else {
                           PacketDirection var9 = var18[var5] == 0 ? PacketDirection.Request : PacketDirection.Response;
                           if (var9 != PacketDirection.Request) {
                              throw new KongaProtocolException("This packet is not a request.");
                           } else if (!hasEnoughInputForInt(var18, ++var5)) {
                              throw new KongaProtocolException("Not enough input for the size of the module specific area.");
                           } else {
                              int var10 = getInt(var18, var5);
                              var5 += 4;
                              if (var10 > 0 && !hasEnoughInput(var18, var5, var10)) {
                                 throw new KongaProtocolException("Not enough input for module specific data.");
                              } else {
                                 var5 += var10;
                                 if (!hasEnoughInputForInt(var18, var5)) {
                                    throw new KongaProtocolException("Not enough input for the size of the username.");
                                 } else {
                                    int var11 = getInt(var18, var5);
                                    var5 += 4;
                                    if (!hasEnoughInput(var18, var5, var11)) {
                                       throw new KongaProtocolException("Not enough input for the user name.");
                                    } else {
                                       String var12 = getString(var18, var5, var11);
                                       var5 += var11;
                                       if (!hasEnoughInputForInt(var18, var5)) {
                                          throw new KongaProtocolException("Not enough input for the size of the password.");
                                       } else {
                                          int var13 = getInt(var18, var5);
                                          var5 += 4;
                                          if (!hasEnoughInput(var18, var5, var13)) {
                                             throw new KongaProtocolException("Not enough input for the password.");
                                          } else {
                                             String var14 = getString(var18, var5, var13);
                                             var5 += var13;
                                             KongaRequest var15 = new KongaRequest(var8, var12, var14);
                                             var5 = addCommands(var18, var5, var15);
                                             if (var5 != var17 - "AGNOK".length()) {
                                                throw new KongaProtocolException("Inconsisten end of packet.");
                                             } else {
                                                return var15;
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private KongaProtocolUtils() {
   }
}
