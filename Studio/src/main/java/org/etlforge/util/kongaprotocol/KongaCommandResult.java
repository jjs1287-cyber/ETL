package org.etlforge.util.kongaprotocol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.etlforge.util.net.CommandResult;

public class KongaCommandResult implements CommandResult {
   private String commandName;
   private List<String> retVals = new ArrayList<>();
   private Throwable exception = null;

   public KongaCommandResult(String var1) {
      this.commandName = var1;
   }

   public void addReturnValue(String var1) {
      this.retVals.add(var1);
   }

   @Override
   public String getCommandName() {
      return this.commandName;
   }

   @Override
   public int getNumberOfReturnValues() {
      return this.retVals.size();
   }

   public void setException(Throwable var1) {
      this.exception = var1;
   }

   @Override
   public Iterator<String> iterator() {
      return this.retVals.iterator();
   }

   @Override
   public Throwable getException() {
      return this.exception;
   }
}
