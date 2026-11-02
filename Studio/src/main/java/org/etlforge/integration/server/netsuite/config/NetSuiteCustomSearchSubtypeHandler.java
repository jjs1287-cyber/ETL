package org.etlforge.integration.server.netsuite.config;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.design.netsuite.config.NetSuiteCustomSearchSubtype;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.RelativePathRoot;
import org.etlforge.xml.sax.StartElementHandler;

public class NetSuiteCustomSearchSubtypeHandler {
   private String searchObject;
   private String searchNetSuiteSubType;
   private List<String> searchResponseObjectNames;
   List<NetSuiteCustomSearchSubtype> list = Lists.newArrayList();
   @RelativePathRoot
   public final String pathRoot;

   NetSuiteCustomSearchSubtypeHandler(String var1) {
      this.pathRoot = var1 + "customsearch";
   }

   @StartElementHandler("/")
   public void startCustomizationRef() {
      this.searchObject = null;
      this.searchNetSuiteSubType = null;
      this.searchResponseObjectNames = null;
   }

   @EndElementHandler("searchobjectkey")
   public void searchobject(String var1) {
      this.searchObject = var1;
   }

   @EndElementHandler("searchsubtype")
   public void searchsubtype(String var1) {
      this.searchNetSuiteSubType = var1;
   }

   @EndElementHandler("searchreponseobject")
   public void searchreponseobject(String var1) {
      if (this.searchResponseObjectNames == null) {
         this.searchResponseObjectNames = Lists.newArrayList();
      }

      this.searchResponseObjectNames.add(var1);
   }

   @EndElementHandler("/")
   public void endCustomField(String var1) {
      NetSuiteCustomSearchSubtype var2 = new NetSuiteCustomSearchSubtype(this.searchObject, this.searchNetSuiteSubType, this.searchResponseObjectNames);
      this.list.add(var2);
   }
}
