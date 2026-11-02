package org.etlforge.integration.server.netsuite.config;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.design.netsuite.config.NetSuiteCustomFieldObject;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.RelativePathRoot;
import org.etlforge.xml.sax.StartElementHandler;
import org.xml.sax.Attributes;

public class NetSuiteCustomFieldObjectHandler {
   private String objectName;
   private String getCustomizationType;
   private String appliesToObject;
   private String appliesToObjectColumn;
   private String objectColumnName;
   List<NetSuiteCustomFieldObject> list = Lists.newArrayList();
   @RelativePathRoot
   public final String pathRoot;

   NetSuiteCustomFieldObjectHandler(String var1) {
      this.pathRoot = var1 + "customfieldobject";
   }

   @StartElementHandler("/")
   public void startCustomizationRef(Attributes var1) {
      this.objectName = null;
      this.getCustomizationType = null;
      this.appliesToObject = null;
      this.appliesToObjectColumn = null;
      this.objectColumnName = null;
   }

   @EndElementHandler("objectName")
   public void objectName(String var1) {
      this.objectName = var1;
   }

   @EndElementHandler("getCustomizationType")
   public void getCustomizationType(String var1) {
      this.getCustomizationType = var1;
   }

   @EndElementHandler("appliesToObject")
   public void appliesToObject(String var1) {
      this.appliesToObject = var1;
   }

   @EndElementHandler("appliesToObjectColumn")
   public void appliesToObjectColumn(String var1) {
      this.appliesToObjectColumn = var1;
   }

   @EndElementHandler("objectColumnName")
   public void objectColumnName(String var1) {
      this.objectColumnName = var1;
   }

   @EndElementHandler("/")
   public void endCustomField(String var1) {
      NetSuiteCustomFieldObject var2 = new NetSuiteCustomFieldObject(
         this.objectName, this.getCustomizationType, this.appliesToObject, this.appliesToObjectColumn, this.objectColumnName
      );
      this.list.add(var2);
   }
}
