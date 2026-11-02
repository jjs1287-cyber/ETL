package org.etlforge.integration.design.netsuite.search.wsdl;

import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.Map;
import org.etlforge.integration.design.util.XsdGlobalElementName;

public class NetSuiteSeachRow implements Serializable {
   private final Map<String, XsdGlobalElementName> searchRowBasic = Maps.newHashMap();
   private final String searchRowName;

   public NetSuiteSeachRow(String var1) {
      this.searchRowName = var1;
   }

   public void put(String var1, String var2, String var3) {
      XsdGlobalElementName var4 = XsdGlobalElementName.newType(var2, var3);
      this.searchRowBasic.put(var1, var4);
   }

   public String getSearchRowName() {
      return this.searchRowName;
   }

   public Map<String, XsdGlobalElementName> getRowBasics() {
      return this.searchRowBasic;
   }
}
