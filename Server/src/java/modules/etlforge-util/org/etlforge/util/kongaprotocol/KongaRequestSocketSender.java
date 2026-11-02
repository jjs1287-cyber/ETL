package org.etlforge.util.kongaprotocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.util.net.Arguments;
import org.etlforge.util.net.Command;

public class KongaRequestSocketSender implements KongaRequestSender {
   private final Socket socket;

   public KongaRequestSocketSender(Socket var1) {
      if (var1 == null) {
         throw new NullPointerException("socket");
      }

      this.socket = var1;
   }

   @Override
   public KongaResponse send(KongaRequest var1) throws KongaProtocolException {
      OutputStream var2 = null;

      try {
         var2 = this.socket.getOutputStream();
         var2.write(this.createRequestPackage(var1));
         var2.flush();
      } catch (IOException var7) {
         throw new KongaProtocolException(var7.getMessage(), var7);
      } finally {
         KongaIoUtils.close(var2);
      }

      return null;
   }

   private byte[] createRequestPackage(KongaRequest var1) throws IOException {
      byte[] var2 = this.createRequestBody(var1);
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();
      KongaProtocolUtils.appendString(var3, var1.getProtocolName());
      KongaProtocolUtils.appendInt(var3, var1.getProtocolVersion());
      String var4 = var1.getProtocolEndString();
      KongaProtocolUtils.appendInt(var3, var3.size() + 4 + var2.length + var4.length());
      var3.write(var2);
      KongaProtocolUtils.appendString(var3, var4);
      return var3.toByteArray();
   }

   private byte[] createRequestBody(KongaRequest var1) throws IOException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      this.writeHeader(var1, var2);
      this.writeCommands(var1, var2);
      return var2.toByteArray();
   }

   private void writeHeader(KongaRequest var1, ByteArrayOutputStream var2) throws IOException {
      this.writeStringWithLength(var2, var1.getModuleName());
      var2.write(PacketDirection.Request.getId());
      KongaProtocolUtils.appendInt(var2, 0);
      this.writeStringWithLength(var2, var1.getUserName());
      this.writeStringWithLength(var2, var1.getPassword());
   }

   private void writeCommands(KongaRequest var1, ByteArrayOutputStream var2) throws IOException {
      KongaProtocolUtils.appendInt(var2, var1.getNumberOfCommands());

      for (Command var4 : var1) {
         this.writeCommand(var2, var4);
      }
   }

   private void writeCommand(ByteArrayOutputStream var1, Command var2) throws IOException {
      this.writeStringWithLength(var1, var2.getCommandName());
      Arguments var3 = var2.getArguments();
      KongaProtocolUtils.appendInt(var1, var3.size());

      for (String var5 : var3) {
         this.writeStringWithLength(var1, var5);
      }
   }

   private void writeStringWithLength(ByteArrayOutputStream var1, String var2) throws IOException {
      KongaProtocolUtils.appendInt(var1, var2.length());
      KongaProtocolUtils.appendString(var1, var2);
   }
}
