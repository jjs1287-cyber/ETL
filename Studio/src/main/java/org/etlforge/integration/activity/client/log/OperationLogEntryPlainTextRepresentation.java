package org.etlforge.integration.activity.client.log;

import com.google.common.base.Preconditions;
import java.io.PrintWriter;
import java.io.StringWriter;

public final class OperationLogEntryPlainTextRepresentation implements OperationLogEntryTextRepresentation {
   @Override
   public String getText(OperationLogEntry var1) {
      Preconditions.checkNotNull(var1, "logEntry");
      OperationLogEntryPlainTextRepresentation.Builder var2 = new OperationLogEntryPlainTextRepresentation.Builder(var1);
      return var2.build();
   }

   private static final class Builder {
      private final OperationLogEntry logEntry;
      private final PrintWriter writer;
      private final StringWriter stringBuffer;

      public Builder(OperationLogEntry var1) {
         this.logEntry = var1;
         this.stringBuffer = new StringWriter();
         this.writer = new PrintWriter(this.stringBuffer);
      }

      public String build() {
         this.printHeader();
         this.printTimeReported();
         this.printStatus();
         this.printShortMessage();
         this.printDetails();
         this.writer.close();
         return this.stringBuffer.getBuffer().toString();
      }

      private void printHeader() {
         this.writer.print("Operation Log Entry: ");
         this.writer.print(this.logEntry.getOperationName());
         this.writer.println(" [GUID = " + this.logEntry.getID() + "]");
      }

      private void printTimeReported() {
         this.writer.print("Time Reported: ");
         this.writer.println(this.logEntry.getStartTime());
      }

      private void printStatus() {
         this.writer.print("Status: ");
         this.writer.println(this.logEntry.getLogStatus());
      }

      private void printShortMessage() {
         this.writer.print("Short Message: ");
         this.writer.println(this.logEntry.getShortMessage());
      }

      private void printDetails() {
         this.writer.println();

         for (OperationLogMessage var2 : this.logEntry.getDetails()) {
            this.printDetail(var2);
         }
      }

      private void printDetail(OperationLogMessage var1) {
         this.writer.println(var1.getEntryName());
         this.writer.println(var1.getMessage());
         this.writer.println();
      }
   }
}
