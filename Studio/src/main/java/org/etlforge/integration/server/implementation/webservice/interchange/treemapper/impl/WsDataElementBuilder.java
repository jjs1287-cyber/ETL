package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.impl;

import org.etlforge.integration.common.server.ClientServerCommunicationUtils;
import org.etlforge.integration.debug.client.de.DataElement;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsDataElement;

final class WsDataElementBuilder {
   public static WsDataElement build(DataElement var0) {
      if (var0 == null) {
         return null;
      }

      WsDataElement var1 = new WsDataElement();
      var1.setIsArray(var0.isArray());
      String var2 = ClientServerCommunicationUtils.encodeString(var0.getName());
      var1.setName(var2);
      String var3 = ClientServerCommunicationUtils.encodeString(var0.getValue());
      var1.setValue(var3);
      var1.setScope(var0.getScope().name());
      var1.setType(var0.getTypeName());
      return var1;
   }

   private WsDataElementBuilder() {
   }
}
