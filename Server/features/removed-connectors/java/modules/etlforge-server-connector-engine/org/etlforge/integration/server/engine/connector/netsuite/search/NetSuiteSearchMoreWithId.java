package org.etlforge.integration.server.engine.connector.netsuite.search;

import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;

public class NetSuiteSearchMoreWithId {
   static final String searchMoreWithIdOperationName = "searchMoreWithId";
   final String searchId;
   final Integer pageIndex;

   public NetSuiteSearchMoreWithId(String var1, Integer var2) {
      this.searchId = var1;
      this.pageIndex = var2;
   }

   String getOperationName() {
      return "searchMoreWithId";
   }

   String toXml(ConnectorNetSuiteNamespaces var1) {
      StringBuilder var2 = new StringBuilder();
      XmlElementUtils.startElementTag(var2, "searchMoreWithId", "", var1.getMessageNamespace());
      XmlElementUtils.elementValue(var2, "searchId", this.searchId);
      XmlElementUtils.elementValue(var2, "pageIndex", this.pageIndex.toString());
      XmlElementUtils.endElementTag(var2, "searchMoreWithId");
      return var2.toString();
   }

   void toXmlFile(ConnectorNetSuiteNamespaces var1, String var2) {
      ConnectorExecutionSystem.writeFileUTF8(var2, this.toXml(var1));
   }
}
