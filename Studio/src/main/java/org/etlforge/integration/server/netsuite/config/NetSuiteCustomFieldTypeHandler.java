package org.etlforge.integration.server.netsuite.config;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldType;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.RelativePathRoot;
import org.etlforge.xml.sax.StartElementHandler;
import org.xml.sax.Attributes;

public class NetSuiteCustomFieldTypeHandler {
   private String fieldType;
   private String customFieldType;
   private String customFieldTypeSearch;
   private String customFieldTypeSearchColumn;
   List<NetSuiteCustomFieldType> list = Lists.newArrayList();
   @RelativePathRoot
   public final String pathRoot;

   NetSuiteCustomFieldTypeHandler(String var1) {
      this.pathRoot = var1 + "customfieldtype";
   }

   @StartElementHandler("/")
   public void startCustomField(Attributes var1) {
      this.fieldType = null;
      this.customFieldType = null;
      this.customFieldTypeSearch = null;
      this.customFieldTypeSearchColumn = null;
   }

   @EndElementHandler("type")
   public void endType(String var1) {
      this.fieldType = var1;
   }

   @EndElementHandler("fieldtype")
   public void endCustomFieldType(String var1) {
      this.customFieldType = var1;
   }

   @EndElementHandler("fieldtypesearch")
   public void endCustomFieldTypeSearch(String var1) {
      this.customFieldTypeSearch = var1;
   }

   @EndElementHandler("fieldtypesearchcolumn")
   public void endCustomFieldTypeSearchColumn(String var1) {
      this.customFieldTypeSearchColumn = var1;
   }

   @EndElementHandler("/")
   public void endCustomField(String var1) {
      NetSuiteCustomFieldType var2 = new NetSuiteCustomFieldType(
         this.fieldType, this.customFieldType, this.customFieldTypeSearch, this.customFieldTypeSearchColumn
      );
      this.list.add(var2);
   }
}
