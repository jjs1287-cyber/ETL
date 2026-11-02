package org.etlforge.integration.client.ui.interchange.entity.webservice.util;

import java.text.MessageFormat;
import org.etlforge.integration.client.ui.interchange.entity.webservice.resources.WebServiceResources;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;

final class PackageResources {
   private static final String getString(String var0) {
      return WebServiceResources.getString(var0);
   }

   private static String getFormattedMessage(String var0, Object[] var1) {
      return MessageFormat.format(WebServiceResources.getString(var0), var1);
   }

   private PackageResources() {
   }

   static class DocumentationDisplayer {
      static final String TITLE = PackageResources.getString("DocumentationDisplayer.Title");
      static final String EMPTY = PackageResources.getString("DocumentationDisplayer.Empty");

      static String getCaption(WebServiceOperationInfo var0) {
         return PackageResources.getFormattedMessage("DocumentationDisplayer.Caption", new Object[]{var0.getProperty("Operation")});
      }
   }

   static class InfoViewer {
      static final String SERVICE_NAME = PackageResources.getString("OpInfo.ServiceName");
      static final String PORT_NAME = PackageResources.getString("OpInfo.PortName");
      static final String BINDING_NAME = PackageResources.getString("OpInfo.BindingName");
      static final String TRANSPORT_TYPE = PackageResources.getString("OpInfo.TransportType");
      static final String SOAP_ACTION = PackageResources.getString("OpInfo.SoapAction");
      static final String SOAP_VERSION = PackageResources.getString("OpInfo.SoapVersion");
   }

   static class OperationComboBox {
      static final String CAPTION = PackageResources.getString("OperationCombo.Caption");
      static final String TOOLTIP_NAME = PackageResources.getString("OperationCombo.ToolTip.Name");
      static final String TOOLTIP_INPUT = PackageResources.getString("OperationCombo.ToolTip.Input");
      static final String TOOLTIP_OUTPUT = PackageResources.getString("OperationCombo.ToolTip.Output");
      static final String EMPTY = PackageResources.getString("OperationCombo.Empty");
      static final String EMPTY_TOOLTIP = PackageResources.getString("OperationCombo.Empty.ToolTip");
   }
}
