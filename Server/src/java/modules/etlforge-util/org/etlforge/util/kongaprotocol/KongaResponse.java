package org.etlforge.util.kongaprotocol;

import com.google.common.base.Throwables;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.etlforge.util.net.CommandResult;

public class KongaResponse extends KongaPacket implements Iterable<CommandResult> {
   private final List<CommandResult> m_commandResults = new ArrayList<>();
   private String m_errorMessage = null;
   private int m_errorCode = 0;

   public KongaResponse(String var1) {
      super(var1);
   }

   public void addCommandResult(CommandResult var1) {
      this.m_commandResults.add(var1);
   }

   void setError(int var1, String var2) {
      if (var1 == 0) {
         this.m_errorCode = 0;
         this.m_errorMessage = null;
      }

      if (var2 == null) {
         throw new IllegalArgumentException("errorCode!=0 but errorMessage is null!");
      }

      if (var1 < 0) {
         this.m_errorCode = -var1;
      } else {
         this.m_errorCode = var1;
      }

      this.m_errorMessage = var2;
   }

   public int getErrorCode() {
      if (this.m_errorCode != 0) {
         return this.m_errorCode;
      }

      for (CommandResult var2 : this.m_commandResults) {
         Throwable var3 = var2.getException();
         if (var3 != null) {
            return 1;
         }
      }

      return this.m_errorCode;
   }

   public String getErrorString() {
      if (this.m_errorMessage != null && this.m_errorMessage.length() != 0) {
         return this.m_errorMessage;
      }

      StringBuilder var1 = new StringBuilder();

      for (CommandResult var3 : this.m_commandResults) {
         Throwable var4 = var3.getException();
         if (var4 != null) {
            String var5 = var3.getCommandName();
            if (var1.length() > 0) {
               var1.append("\n");
            }

            var1.append("The command ")
               .append(var5)
               .append(" returned the following error:\n")
               .append(var4.getMessage())
               .append("\n\nServer stack trace:\n")
               .append(Throwables.getStackTraceAsString(var4));
         }
      }

      return var1.length() == 0 ? null : var1.toString();
   }

   public int getNumberOfCommands() {
      return this.m_commandResults.size();
   }

   @Override
   public Iterator<CommandResult> iterator() {
      return this.m_commandResults.iterator();
   }
}
