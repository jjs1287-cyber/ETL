package org.etlforge.integration.design.netsuite.config;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class NetSuiteCustomSearchSubtype implements Serializable, Comparable<NetSuiteCustomSearchSubtype> {
   private final String searchObject;
   private final String searchNetSuiteSubType;
   private final List<String> searchResponseObjectNames;

   public NetSuiteCustomSearchSubtype(String var1) {
      this.searchObject = var1;
      this.searchNetSuiteSubType = null;
      this.searchResponseObjectNames = null;
   }

   public NetSuiteCustomSearchSubtype(String var1, String var2, String... var3) {
      this(var1, var2, Arrays.asList(var3));
   }

   public NetSuiteCustomSearchSubtype(String var1, String var2, List<String> var3) {
      this.searchObject = var1;
      this.searchNetSuiteSubType = var2;
      this.searchResponseObjectNames = var3;
   }

   public int compareTo(NetSuiteCustomSearchSubtype var1) {
      return this.searchObject.compareTo(this.searchObject);
   }

   public String getSearchObject() {
      return this.searchObject;
   }

   public String getSearchNetSuiteSubType() {
      return this.searchNetSuiteSubType;
   }

   public List<String> getSearchResponseObjectNames() {
      return this.searchResponseObjectNames;
   }
}
