package org.etlforge.integration.data.entity.webservicecall;

import java.util.Comparator;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.util.StringKeyValuePairsPersistor;
import org.etlforge.util.persist.Persistor;

public final class WsdlFile extends StringKeyValuePairs {
   public static final Comparator<WsdlFile> LOCATOR_ORDER = new Comparator<WsdlFile>() {
      public int compare(WsdlFile var1, WsdlFile var2) {
         return var1.getLocator().compareToIgnoreCase(var2.getLocator());
      }
   };
   public static final String WSDL_LOCATOR = "wsdl_id";
   public static final int STATUS_VALID = 0;
   public static final int STATUS_VALID_WITH_WARNINGS = 1;
   public static final String WSDL_URL = "wsdlurl";
   public static final String WSDL_NAME = "wsdlname";
   public static final String WSDL_STATUS = "status";
   public static final String APPLICATION_MESSAGE = "applicationmessage";
   public static final String USER_COMMENT = "usercomment";
   private static final String[] PROPERTIES = new String[]{"wsdl_id", "wsdlname", "wsdlurl", "status", "applicationmessage", "usercomment"};

   public static final String[] getPropertyKeys() {
      return (String[])PROPERTIES.clone();
   }

   public final String[] getPropertyValues() {
      String[] var1 = getPropertyKeys();
      int var2 = var1.length;
      String[] var3 = new String[var2];

      for (int var4 = 0; var4 < var2; var4++) {
         String var5 = this.get(var1[var4]);
         var3[var4] = var5;
      }

      return var3;
   }

   public WsdlFile() {
   }

   public WsdlFile(String var1) {
      this.setLocator(var1);
   }

   public WsdlFile(StringKeyValuePairs var1) {
      super(var1);
   }

   public String getLocator() {
      return this.get("wsdl_id");
   }

   public void setLocator(String var1) {
      this.put("wsdl_id", var1);
   }

   public boolean isRemote() {
      String var1 = this.getLocator();
      return var1 == null ? false : var1.indexOf("://") >= 0;
   }

   public WsdlFile getCopy() {
      return new WsdlFile(this);
   }

   public void writeTo(Persistor var1) {
      StringKeyValuePairsPersistor.save(this, var1);
   }

   public static WsdlFile fromPersistor(Persistor var0) {
      StringKeyValuePairs var1 = StringKeyValuePairsPersistor.restoreFrom(var0);
      return new WsdlFile(var1);
   }
}
