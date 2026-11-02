package org.etlforge.integration.data.connector;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.Set;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.NetSuiteQueryId;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteQueryPageSize;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchAdvancedProperties;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchAdvancedPropertiesParser;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.version.Version;

public final class NetSuiteQuery extends NetSuiteFunction {
   @IdProperty
   public static final String NetSuite_QUERY_ID = "netsuitequery_id";
   @NameProperty
   public static final String NetSuite_QUERY_NAME = "netsuitequery_name";
   public static final String QUERY_XML = "queryxml";
   public static final String PAGE_SIZE = "pagesize";
   public static final String ADVANCED_SEARCH = "advancedsearch";
   public static final String QUERY_PARAMS = "queryParams";

   public NetSuiteQuery(String var1) {
      super(null, var1, EntityType.NetSuiteQuery);
   }

   public NetSuiteQuery(NetSuiteQueryId var1, String var2) {
      super(var1, var2, EntityType.NetSuiteQuery);
   }

   public NetSuiteQueryId getID() {
      return (NetSuiteQueryId)super.getID();
   }

   @Override
   public NetSuiteFunction.FunctionType getFunctionType() {
      return NetSuiteFunction.FunctionType.QUERY;
   }

   @Override
   public String[] getPropertyKeys() {
      return this.getPropertyKeys("netsuitequery_id", "netsuitequery_name");
   }

   public NetSuiteQuery.QueryParams getQueryParams() {
      synchronized (this.getDataLock()) {
         return new NetSuiteQuery.QueryParams(this);
      }
   }

   public void setQueryParams(NetSuiteQuery.QueryParams var1) {
      Preconditions.checkNotNull(var1);
      NetSuiteQuery.QueryParams var2;
      synchronized (this.getDataLock()) {
         var2 = this.getQueryParams();
         if (var2.equals(var1)) {
            return;
         }

         this.setProperty("queryxml", var1.xml);
         this.setProperty("pagesize", String.valueOf(var1.pageSize.getPageSize()));
         this.setProperty("advancedsearch", var1.advancedSearchProperty.toXml());
      }

      this.firePropertyChange("queryParams", var2, var1);
   }

   public String getQueryXml() {
      synchronized (this.getDataLock()) {
         return this.getProperty("queryxml", "");
      }
   }

   private NetSuiteQueryPageSize getPageSize() {
      int var1 = this.getIntProperty("pagesize", 0);
      return NetSuiteQueryPageSize.valueOf(var1);
   }

   public NetSuiteSearchAdvancedProperties getAdvancedSearchProperty() {
      synchronized (this.getDataLock()) {
         String var2 = this.getProperty("advancedsearch", "");
         return NetSuiteSearchAdvancedPropertiesParser.fromXml(var2);
      }
   }

   @Override
   protected void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
      boolean var2 = this.getQueryXml().isEmpty();
      if (var2) {
         var1.addMessage("Must define the Search XML", ValidationStatus.INVALID);
      }
   }

   @Override
   public boolean hasRequestTransformation() {
      return false;
   }

   @Override
   protected Set<String> getKeysToSkipWhenPersisting() {
      return Collections.singleton("queryxml");
   }

   @Override
   protected void writeAdditionalData(Persistor var1) {
      var1.putText("queryxml", this.getQueryXml());
   }

   @Override
   protected void restoreAdditionalTypeSpecificData(Persistor var1) {
      String var2 = var1.getText("queryxml");
      this.setProperty("queryxml", var2);
   }

   @Override
   public final Version getRequiredVersion() {
      return VersionFactory.newVersion(5, 5, 0, 10);
   }

   public static class QueryParams {
      private final String xml;
      private final NetSuiteQueryPageSize pageSize;
      private final NetSuiteSearchAdvancedProperties advancedSearchProperty;

      public QueryParams(NetSuiteQuery var1) {
         this(var1.getQueryXml(), var1.getPageSize(), var1.getAdvancedSearchProperty());
      }

      public QueryParams(String var1, NetSuiteQueryPageSize var2, NetSuiteSearchAdvancedProperties var3) {
         this.xml = Preconditions.checkNotNull(var1);
         this.pageSize = Preconditions.checkNotNull(var2);
         this.advancedSearchProperty = var3;
      }

      public String getXml() {
         return this.xml;
      }

      public NetSuiteSearchAdvancedProperties getAdvancedSearchProperty() {
         return this.advancedSearchProperty;
      }

      public NetSuiteQueryPageSize getPageSize() {
         return this.pageSize;
      }

      @Override
      public int hashCode() {
         return Objects.hashCode(this.pageSize, this.xml, this.advancedSearchProperty);
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (var1 instanceof NetSuiteQuery.QueryParams) {
            NetSuiteQuery.QueryParams var2 = (NetSuiteQuery.QueryParams)var1;
            if (this.pageSize == var2.pageSize && this.xml.equals(var2.xml)) {
               return this.advancedSearchProperty == null && var2.advancedSearchProperty == null
                  || this.advancedSearchProperty.equals(var2.advancedSearchProperty);
            }
         }

         return false;
      }
   }
}
