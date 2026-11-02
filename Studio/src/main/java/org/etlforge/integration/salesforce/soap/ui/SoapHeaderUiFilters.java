package org.etlforge.integration.salesforce.soap.ui;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.SoapHeaderUtils;
import org.etlforge.integration.salesforce.soap.header.AllowFieldTruncationHeader;
import org.etlforge.integration.salesforce.soap.header.AssignmentRuleHeader;
import org.etlforge.integration.salesforce.soap.header.DebuggingHeader;
import org.etlforge.integration.salesforce.soap.header.EmailHeader;
import org.etlforge.integration.salesforce.soap.header.UserTerritoryDeleteHeader;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

public final class SoapHeaderUiFilters {
   public static Predicate<SoapHeader> forMethod(SfdcMethod var0) {
      switch (var0) {
         case UPSERT:
         case CREATE:
         case UPDATE:
            return new SoapHeaderUiFilters.RejectionFilter(EmailHeader.class, DebuggingHeader.class);
         case DELETE:
            return new SoapHeaderUiFilters.RejectionFilter(
               AllowFieldTruncationHeader.class, EmailHeader.class, UserTerritoryDeleteHeader.class, DebuggingHeader.class
            );
         default:
            return Predicates.alwaysTrue();
      }
   }

   public static Predicate<SoapHeader> forObject(String var0) {
      return new SoapHeaderUiFilters.AssignmentRuleFilter(var0);
   }

   public static Predicate<SoapHeader> forMethodAndObject(SfdcMethod var0, String var1) {
      Predicate var2 = forMethod(var0);
      Predicate var3 = forObject(var1);
      return Predicates.and(var2, var3);
   }

   private SoapHeaderUiFilters() {
   }

   private static class AssignmentRuleFilter implements Predicate<SoapHeader> {
      private final String objectName;

      public AssignmentRuleFilter(String var1) {
         this.objectName = var1;
      }

      public boolean apply(SoapHeader var1) {
         return var1 instanceof AssignmentRuleHeader ? SoapHeaderUtils.isAssignmentRuleApplicable(this.objectName) : true;
      }
   }

   private static final class RejectionFilter implements Predicate<SoapHeader> {
      private final ImmutableSet<Class<? extends SoapHeader>> hidden;

      public RejectionFilter(Class<? extends SoapHeader>... var1) {
         this.hidden = ImmutableSet.copyOf(var1);
      }

      public boolean apply(SoapHeader var1) {
         return !this.hidden.contains(var1.getClass());
      }
   }
}
