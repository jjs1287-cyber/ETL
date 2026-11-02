package com.etlforge.integration.client.ui.sfdc.page.soql;

import com.google.common.base.Preconditions;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.etlforge.integration.salesforce.parser.ParsedQuery;
import org.etlforge.integration.salesforce.parser.SupportedQueryPolicy;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public final class SoqlModel extends AbstractPropertyBasedModel {
   public static final String QUERY_STRING = "soql";
   public static final String PARSED_QUERY = "parsedQuery";
   public static final String HEADER_AVAILABLE = "headerAvailable";
   private final String objectName;
   private final Property<String> queryString = this.newProperty("soql");
   private final Property<ParsedQuery> parsedQuery = this.newProperty("parsedQuery");
   private final Property<Boolean> headerAvailable = this.newProperty("headerAvailable");
   private volatile SupportedQueryPolicy supportedQueryPolicy = SupportedQueryPolicy.NO_RESTRICTIONS;
   private final SoqlModel.InternalQueryStringListener internalQueryStringListener = new SoqlModel.InternalQueryStringListener();
   private final Map<String, ParsedQuery> knownQueries = Collections.synchronizedMap(new HashMap<>());

   public SoqlModel(String var1, String var2, ParsedQuery var3) {
      this.objectName = var1;
      this.queryString.set(var2);
      this.setQueryInfo(var2, var3);
      this.queryString.addListener(this.internalQueryStringListener);
   }

   public void setQueryInfo(String var1, ParsedQuery var2) {
      this.internalQueryStringListener.blocked = true;
      this.queryString.set(var1);
      this.internalQueryStringListener.blocked = false;
      this.parsedQuery.set(var2);
      this.headerAvailable.set(var2.supportsTargetHeader());
      this.knownQueries.put(var1, var2);
   }

   public SupportedQueryPolicy getSupportedQueryPolicy() {
      return this.supportedQueryPolicy;
   }

   public void setSupportedQueryPolicy(SupportedQueryPolicy var1) {
      this.supportedQueryPolicy = Preconditions.checkNotNull(var1);
   }

   public String getObjectName() {
      return this.objectName;
   }

   public String getQueryString() {
      return this.queryString.get();
   }

   public ParsedQuery getParsedQuery() {
      return this.parsedQuery.get();
   }

   private class InternalQueryStringListener implements PropertyChangeListener {
      public boolean blocked;

      private InternalQueryStringListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (!this.blocked) {
            String var2 = (String)var1.getNewValue();
            if (!var2.isEmpty()) {
               ParsedQuery var3 = SoqlModel.this.knownQueries.get(var2);
               assert var3 != null;
               SoqlModel.this.setQueryInfo(var2, var3);
            }
         }
      }
   }
}
