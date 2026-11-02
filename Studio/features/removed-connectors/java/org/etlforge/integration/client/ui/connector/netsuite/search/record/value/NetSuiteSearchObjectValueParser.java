package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

import com.google.common.base.Strings;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.ConnectorNetSuiteWsdlError;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchRecord;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.SAXException;

public class NetSuiteSearchObjectValueParser {
   private final NetSuiteSearchRecord searchRecord;

   static NetSuiteSearchRecordValue parse(NetSuiteSearchRecord var0, String var1) {
      if (Strings.isNullOrEmpty(var1)) {
         throw new RuntimeException("The query xml cannot be empty under normal condition");
      }

      NetSuiteSearchObjectValueParser var2 = new NetSuiteSearchObjectValueParser(var0);
      return var2.parse(var1);
   }

   private NetSuiteSearchObjectValueParser(NetSuiteSearchRecord var1) {
      this.searchRecord = var1;
   }

   NetSuiteSearchRecordValue parse(String var1) {
      NetSuiteSearchObjectValueAdvancedHandler var2 = new NetSuiteSearchObjectValueAdvancedHandler(this.searchRecord);

      try {
         SaxParsers.parseString(var1, var2, true);
      } catch (SAXException var4) {
         throw new ConnectorNetSuiteWsdlError(var4);
      }

      return var2.getSearchRecordValue();
   }
}
