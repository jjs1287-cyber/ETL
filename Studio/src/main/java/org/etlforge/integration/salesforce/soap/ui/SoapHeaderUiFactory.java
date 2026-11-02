package org.etlforge.integration.salesforce.soap.ui;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import java.lang.reflect.Constructor;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.etlforge.integration.data.entity.id.SalesforceConnectorId;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.soap.DefaultSoapHeaderLookup;
import org.etlforge.integration.salesforce.soap.header.AllOrNoneHeader;
import org.etlforge.integration.salesforce.soap.header.AllowFieldTruncationHeader;
import org.etlforge.integration.salesforce.soap.header.AssignmentRuleHeader;
import org.etlforge.integration.salesforce.soap.header.DebuggingHeader;
import org.etlforge.integration.salesforce.soap.header.EmailHeader;
import org.etlforge.integration.salesforce.soap.header.OwnerChangeOptionsHeader;
import org.etlforge.integration.salesforce.soap.header.QueryOptions;
import org.etlforge.integration.salesforce.soap.header.SessionHeader;
import org.etlforge.integration.salesforce.soap.header.UserTerritoryDeleteHeader;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.lang.KongaClassUtils;
import org.etlforge.util.name.Nameables;

public final class SoapHeaderUiFactory {
   private final Map<Class<? extends SoapHeader>, Class<? extends SoapHeaderUi>> lookup = Maps.newHashMap();
   private Supplier<SalesforceConnectorId> connectorIdSupplier;
   private final SfdcMethod method;
   private Predicate<SoapHeader> filter;

   public SoapHeaderUiFactory(SfdcMethod var1, String var2) {
      this.method = var1;
      this.filter = SoapHeaderUiFilters.forMethodAndObject(var1, var2);
      this.lookup.put(AllOrNoneHeader.class, AllOrNoneHeaderUi.class);
      this.lookup.put(AllowFieldTruncationHeader.class, AllowFieldTruncationHeaderUi.class);
      this.lookup.put(AssignmentRuleHeader.class, AssignmentRuleHeaderUi.class);
      this.lookup.put(UserTerritoryDeleteHeader.class, UserTerritoryDeleteHeaderUi.class);
      this.lookup.put(QueryOptions.class, QueryOptionsUi.class);
      this.lookup.put(EmailHeader.class, EmailHeaderUi.class);
      this.lookup.put(DebuggingHeader.class, DebuggingHeaderUi.class);
      this.lookup.put(OwnerChangeOptionsHeader.class, OwnerChangeOptionsHeaderUi.class);
   }

   public void addFilter(Predicate<? super SoapHeader> var1) {
      this.filter = Predicates.and(this.filter, var1);
   }

   public void installSessionHeaderUiFactory(Supplier<SalesforceConnectorId> var1) {
      this.connectorIdSupplier = var1;
   }

   public SoapHeaderUi createUi(SoapHeader var1) {
      if (!this.filter.apply(var1)) {
         return null;
      }

      if (SessionHeader.class.equals(var1.getClass())) {
         return this.connectorIdSupplier == null ? null : new SessionHeaderUi(this.connectorIdSupplier);
      }

      Class var2 = this.lookup.get(var1.getClass());
      if (var2 == null) {
         return null;
      }

      Constructor var3 = KongaClassUtils.getSingleArgumentConstructor(var2, var1.getClass());
      if (var3 == null) {
         throw new RuntimeException(
            "Client configuration error. This is a bug. The error is: No matching constructor found in the class " + var2.getName() + "."
         );
      }

      try {
         return (SoapHeaderUi)var3.newInstance(var1);
      } catch (Exception var5) {
         throw new RuntimeException(
            "Client configuration error. This is a bug. The error is: Could not instantiate the SOAP header UI " + var2.getName() + ".", var5
         );
      }
   }

   public Comparator<SoapHeader> getPreferredOrder() {
      List var1 = Nameables.getNames(DefaultSoapHeaderLookup.getDefaultHeaders(this.method));
      return Ordering.<String>explicit(var1).onResultOf(Nameables.toNameFunction());
   }
}
