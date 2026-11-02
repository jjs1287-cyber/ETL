package org.etlforge.integration.salesforce.config;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.salesforce.SfdcMethod;

public abstract class SalesforceConfig {
   private static SalesforceConfig instance;
   public static final SalesforceConfig NONE = new SalesforceConfig() {
      @Override
      public String getSoapStyle() {
         return null;
      }

      @Override
      public String getSoapBodyUse() {
         return null;
      }

      @Override
      public String getSoapAction() {
         return null;
      }

      @Override
      public String getServiceName() {
         return null;
      }

      @Override
      public String getSalesforceNamespace() {
         return null;
      }

      @Override
      public String getSObjectNamespace() {
         return null;
      }

      @Override
      public String getSObjectLocalName() {
         return null;
      }

      @Override
      public String getPortName() {
         return null;
      }

      @Override
      public SfdcMethodConfig getMethodConfig(SfdcMethod var1) {
         return new DefaultSfdcMethodConfig(var1.name().toLowerCase());
      }

      @Override
      public String[] getFieldsInheritedFromSObject() {
         return null;
      }

      @Override
      public String getBindingName() {
         return null;
      }

      @Override
      public int getChunkSize() {
         return 0;
      }

      @Override
      public String getMappingTreePath(MappingTreePath var1) {
         return null;
      }
   };

   public static synchronized SalesforceConfig getInstance() {
      if (instance == null) {
         instance = loadInstance();
      }

      return instance;
   }

   private static SalesforceConfig loadInstance() {
      String var0 = "SalesforceConfig.xml";
      if (doesXmlConfigExist(var0)) {
         try {
            return new SalesforceXmlConfig(var0);
         } catch (Exception var2) {
            ErrorLog.log(SalesforceConfig.class, "The salesforce configuration is invalid. Please contact etlforge support.", var2);
         }
      }

      return NONE;
   }

   private static boolean doesXmlConfigExist(String var0) {
      return SalesforceConfig.class.getClassLoader().getResource(var0) != null;
   }

   public abstract SfdcMethodConfig getMethodConfig(SfdcMethod var1);

   public abstract String getSalesforceNamespace();

   public abstract String getServiceName();

   public abstract String getSoapAction();

   public abstract String getSoapStyle();

   public abstract String getSoapBodyUse();

   public abstract String getBindingName();

   public abstract String getPortName();

   public abstract String getSObjectNamespace();

   public abstract String getSObjectLocalName();

   public abstract String[] getFieldsInheritedFromSObject();

   public abstract int getChunkSize();

   public abstract String getMappingTreePath(MappingTreePath var1);
}
