package org.etlforge.integration.server.engine.connector.ebs.adapter;

import com.ubs.core.adapter.ebs.common.util.EbsConstants;
import com.ubs.core.adapter.ebs.connection.EbsExecutionConnection;
import com.ubs.core.adapter.ebs.interaction.EbsExecutionMonitor;
import com.ubs.core.adapter.ebs.interaction.EbsExecutionStatusRecord;
import com.ubs.core.adapter.exception.AdapterExecutionException;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.server.engine.connector.util.XmlUtils;

public class EbsMonitorWrapper {
   final EbsExecutionMonitor monitor;
   final int reqId;
   private EbsExecutionStatusRecord statusRec = null;
   public static final String responseSchema = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> <xs:schema version=\"1.0\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\t<xs:element name=\"output\">\t<xs:complexType><xs:sequence>\t<xs:element name=\"result\" type=\"xs:string\" />\t<xs:element name=\"status\" type=\"xs:string\" />\t<xs:element name=\"phase\" type=\"xs:string\" />\t<xs:element name=\"description\" type=\"xs:string\" minOccurs=\"0\" />\t</xs:sequence></xs:complexType>  \t</xs:element></xs:schema>";
   public static final String responseRoot = "output";

   EbsMonitorWrapper(EbsExecutionConnection var1, int var2) {
      this.monitor = new EbsExecutionMonitor(var1);
      this.reqId = var2;
   }

   void execute() throws AdapterExecutionException {
      this.statusRec = this.monitor.getStatus(this.reqId);
   }

   boolean isDone() {
      String var1 = this.statusRec.getExecutionStatus();
      return !EbsConstants.STATUS_RUNNING.equals(var1);
   }

   String toXmlString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(XmlUtils.xmlUtf8Header);
      XmlElementUtils.startElementTag(var1, "output");
      XmlElementUtils.elementValue(var1, "result", this.statusRec.getExecutionStatus());
      XmlElementUtils.elementValue(var1, "status", this.statusRec.getActualStatus());
      XmlElementUtils.elementValue(var1, "phase", this.statusRec.getActualPhase());
      String var2 = this.statusRec.getDescription();
      XmlElementUtils.elementValueOptional(var1, "description", var2);
      XmlElementUtils.endElementTag(var1, "output");
      return var1.toString();
   }
}
