package org.etlforge.integration.etlforgeconnector.netsuite;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.etlforge.integration.design.netsuite.config.NetSuiteCustomSearchSubtype;

public class NetSuiteCustomSearch implements Serializable {
   private final String customSearchObjectName;
   private final Map<String, NetSuiteCustomSearchSubtype> subTypes = Maps.newHashMap();
   private final Set<String> subTypeNames = Sets.newTreeSet();

   public NetSuiteCustomSearch(String var1) {
      this.customSearchObjectName = var1;
   }

   void put(String var1, String var2, List<String> var3) {
      NetSuiteCustomSearchSubtype var4 = new NetSuiteCustomSearchSubtype(var1, var2, var3);
      this.put(var1, var4);
   }

   public Map<String, NetSuiteCustomSearchSubtype> getSubTypes() {
      return this.subTypes;
   }

   void put(String var1, NetSuiteCustomSearchSubtype var2) {
      this.subTypeNames.add(var1);
      this.subTypes.put(var1.toLowerCase(), var2);
   }

   public NetSuiteCustomSearchSubtype get(String var1) {
      return this.subTypes.get(var1.toLowerCase());
   }

   public Set<String> getSubTypeNames() {
      return this.subTypeNames;
   }

   public String getCustomSearchObjectName() {
      return this.customSearchObjectName;
   }

   public int size() {
      return this.subTypes.size();
   }
}
