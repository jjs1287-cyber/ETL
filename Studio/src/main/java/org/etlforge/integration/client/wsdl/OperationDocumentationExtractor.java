package org.etlforge.integration.client.wsdl;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Operation;
import javax.wsdl.PortType;
import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;
import org.etlforge.integration.data.entity.webservicecall.WebServiceOperationIdentifier;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;
import org.w3c.dom.Element;

public class OperationDocumentationExtractor {
   public String getDocumentation(WebServiceInfo var1, WebServiceOperationInfo var2) {
      return this.getDocumentation(var1.getWsdlDefinition(), var2);
   }

   public String getDocumentation(Definition var1, WebServiceOperationInfo var2) {
      if (var1 == null) {
         return null;
      }

      PortType var3 = this.getPortType(var1, var2);
      if (var3 == null) {
         return null;
      }

      Operation var4 = this.getOperation(var3, var2);
      return var4 == null ? null : this.extractDocumentation(var4);
   }

   private PortType getPortType(Definition var1, WebServiceOperationInfo var2) {
      String var3 = var2.getProperty("Binding");

      for (Object var5 : var1.getBindings().values()) {
         Binding var6 = (Binding)var5;
         if (var6.getQName().getLocalPart().equals(var3)) {
            return var6.getPortType();
         }
      }

      return null;
   }

   private Operation getOperation(PortType var1, WebServiceOperationInfo var2) {
      WebServiceOperationIdentifier var3 = WebServiceOperationIdentifier.fromOperationInfo(var2);
      Operation var4 = var1.getOperation(var3.getOperationName(), var3.getInputName(), var3.getOutputName());
      if (var4 == null) {
         var4 = var1.getOperation(var3.getOperationName(), null, null);
      }

      return var4;
   }

   private String extractDocumentation(Operation var1) {
      Element var2 = var1.getDocumentationElement();
      return var2 != null ? var2.getTextContent() : null;
   }
}
