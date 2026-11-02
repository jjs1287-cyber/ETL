package org.etlforge.integration.activity.client.log;

import com.google.common.base.Preconditions;
import org.etlforge.xml.XmlStringBuilder;

public final class OperationLogEntryXmlRepresentation implements OperationLogEntryTextRepresentation {
   @Override
   public String getText(OperationLogEntry var1) {
      Preconditions.checkNotNull(var1, "logEntry");
      OperationLogEntryXmlRepresentation.Builder var2 = new OperationLogEntryXmlRepresentation.Builder(var1);
      return var2.build();
   }

   private static final class Builder {
      private final OperationLogEntry logEntry;
      private final XmlStringBuilder xml;

      public Builder(OperationLogEntry var1) {
         this.logEntry = var1;
         this.xml = new XmlStringBuilder();
      }

      public String build() {
         this.xml.addElement("OperationLogEntry");
         this.addOperationName();
         this.addStatus();
         this.addTimeReported();
         this.addShortMessage();
         this.addDetails();
         this.xml.closeElement();
         return this.xml.getXml();
      }

      private void addOperationName() {
         this.xml.addElement("Operation");
         this.xml.addAttribute("GUID", this.logEntry.getOperationId().toString());
         this.xml.setElementValue(this.logEntry.getOperationName());
         this.xml.closeElement();
      }

      private void addStatus() {
         this.xml.addElement("Status");
         this.xml.setElementValue(this.logEntry.getLogStatus().toString());
         this.xml.closeElement();
      }

      private void addTimeReported() {
         this.xml.addElement("TimeReported");
         this.xml.setElementValue(this.logEntry.getStartTime().toString());
         this.xml.closeElement();
      }

      private void addShortMessage() {
         this.xml.addElement("ShortMessage");
         this.xml.setElementValue(this.logEntry.getShortMessage());
         this.xml.closeElement();
      }

      private void addDetails() {
         this.xml.addElement("Details");

         for (OperationLogMessage var2 : this.logEntry.getDetails()) {
            this.addDetail(var2);
         }

         this.xml.closeElement();
      }

      private void addDetail(OperationLogMessage var1) {
         this.xml.addElement("Detail");
         this.xml.addElement("Caption");
         this.xml.setElementValue(var1.getEntryName());
         this.xml.closeElement();
         this.xml.addElement("Message");
         this.xml.setElementValue(var1.getMessage());
         this.xml.closeElement();
         this.xml.closeElement();
      }
   }
}
