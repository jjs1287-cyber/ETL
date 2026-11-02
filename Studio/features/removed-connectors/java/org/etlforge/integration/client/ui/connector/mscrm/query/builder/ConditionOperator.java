package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Ordering;
import java.util.EnumSet;
import org.etlforge.integration.mscrm.metadata.MSCrmDataType;

enum ConditionOperator {
   EQ {
      @Override
      public String toString() {
         return "=";
      }

      @Override
      public String getXml() {
         return "eq";
      }
   },
   NE {
      @Override
      public String toString() {
         return "<>";
      }

      @Override
      public String getXml() {
         return "ne";
      }
   },
   GT {
      @Override
      public String toString() {
         return ">";
      }

      @Override
      public String getXml() {
         return "gt";
      }
   },
   GE {
      @Override
      public String toString() {
         return ">=";
      }

      @Override
      public String getXml() {
         return "ge";
      }
   },
   LE {
      @Override
      public String toString() {
         return "<=";
      }

      @Override
      public String getXml() {
         return "le";
      }
   },
   LT {
      @Override
      public String toString() {
         return "<";
      }

      @Override
      public String getXml() {
         return "lt";
      }
   },
   LIKE,
   NOT_LIKE {
      @Override
      public String toString() {
         return "not-like";
      }
   },
   IN(OperatorValueType.MULTIPLE_VALUES),
   NOT_IN(OperatorValueType.MULTIPLE_VALUES) {
      @Override
      public String toString() {
         return "not-in";
      }
   },
   BETWEEN(OperatorValueType.RANGE),
   NOT_BETWEEN(OperatorValueType.RANGE) {
      @Override
      public String toString() {
         return "not-between";
      }
   },
   NULL(OperatorValueType.NO_VALUE),
   NOT_NULL(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "not-null";
      }
   },
   YESTERDAY(OperatorValueType.NO_VALUE),
   TODAY(OperatorValueType.NO_VALUE),
   TOMORROW(OperatorValueType.NO_VALUE),
   LAST_SEVEN_DAYS(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "last-seven-days";
      }
   },
   NEXT_SEVEN_DAYS(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "next-seven-days";
      }
   },
   LAST_WEEK(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "last-week";
      }
   },
   THIS_WEEK(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "this-week";
      }
   },
   NEXT_WEEK(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "next-week";
      }
   },
   LAST_MONTH(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "last-month";
      }
   },
   THIS_MONTH(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "this-month";
      }
   },
   NEXT_MONTH(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "next-month";
      }
   },
   ON,
   ON_OR_BEFORE {
      @Override
      public String toString() {
         return "on-or-before";
      }
   },
   ON_OR_AFTER {
      @Override
      public String toString() {
         return "on-or-after";
      }
   },
   LAST_YEAR(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "last-year";
      }
   },
   THIS_YEAR(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "this-year";
      }
   },
   NEXT_YEAR(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "next-year";
      }
   },
   LAST_X_HOURS {
      @Override
      public String toString() {
         return "last-x-hours";
      }
   },
   NEXT_X_HOURS {
      @Override
      public String toString() {
         return "next-x-hours";
      }
   },
   LAST_X_DAYS {
      @Override
      public String toString() {
         return "last-x-days";
      }
   },
   NEXT_X_DAYS {
      @Override
      public String toString() {
         return "next-x-days";
      }
   },
   LAST_X_WEEKS {
      @Override
      public String toString() {
         return "last-x-weeks";
      }
   },
   NEXT_X_WEEKS {
      @Override
      public String toString() {
         return "next-x-weeks";
      }
   },
   LAST_X_MONTHS {
      @Override
      public String toString() {
         return "last-x-months";
      }
   },
   NEXT_X_MONTHS {
      @Override
      public String toString() {
         return "next-x-months";
      }
   },
   OLDER_THAN_X_MONTHS {
      @Override
      public String toString() {
         return "olderthan-x-months";
      }
   },
   LAST_X_YEARS {
      @Override
      public String toString() {
         return "last-x-years";
      }
   },
   NEXT_X_YEARS {
      @Override
      public String toString() {
         return "next-x-years";
      }
   },
   EQ_USER_ID(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "eq-userid";
      }
   },
   NE_USER_ID(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "ne-userid";
      }
   },
   EQ_USER_TEAMS(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "eq-userteams";
      }
   },
   EQ_BUSINESS_ID(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "eq-businessid";
      }
   },
   NE_BUSINESS_ID(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "ne-businessid";
      }
   },
   EQ_USER_LANGUAGE(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "eq-userlanguage";
      }
   },
   THIS_FISCAL_YEAR(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "this-fiscal-year";
      }
   },
   THIS_FISCAL_PERIOD(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "this-fiscal-period";
      }
   },
   NEXT_FISCAL_YEAR(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "next-fiscal-year";
      }
   },
   NEXT_FISCAL_PERIOD(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "next-fiscal-period";
      }
   },
   LAST_FISCAL_YEAR(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "last-fiscal-year";
      }
   },
   LAST_FISCAL_PERIOD(OperatorValueType.NO_VALUE) {
      @Override
      public String toString() {
         return "last-fiscal-period";
      }
   },
   LAST_X_FISCAL_YEARS {
      @Override
      public String toString() {
         return "last-x-fiscal-years";
      }
   },
   LAST_X_FISCAL_PERIODS {
      @Override
      public String toString() {
         return "last-x-fiscal-periods";
      }
   },
   NEXT_X_FISCAL_YEARS {
      @Override
      public String toString() {
         return "next-x-fiscal-years";
      }
   },
   NEXT_X_FISCAL_PERIODS {
      @Override
      public String toString() {
         return "next-x-fiscal-periods";
      }
   },
   IN_FISCAL_YEAR {
      @Override
      public String toString() {
         return "in-fiscal-year";
      }
   },
   IN_FISCAL_PERIOD {
      @Override
      public String toString() {
         return "in-fiscal-period";
      }
   },
   IN_FISCAL_PERIOD_AND_YEAR(OperatorValueType.MULTIPLE_VALUES) {
      @Override
      public String toString() {
         return "in-fiscal-period-and-year";
      }
   },
   IN_OR_BEFORE_FISCAL_PERIOD_AND_YEAR(OperatorValueType.MULTIPLE_VALUES) {
      @Override
      public String toString() {
         return "in-or-before-fiscal-period-and-year";
      }
   },
   IN_OR_AFTER_FISCAL_PERIOD_AND_YEAR(OperatorValueType.MULTIPLE_VALUES) {
      @Override
      public String toString() {
         return "in-or-after-fiscal-period-and-year";
      }
   },
   BEGINS_WITH {
      @Override
      public String toString() {
         return "begins-with";
      }
   },
   NOT_BEGINS_WITH {
      @Override
      public String toString() {
         return "not-begins-with";
      }
   },
   ENDS_WITH {
      @Override
      public String toString() {
         return "ends-with";
      }
   },
   NOT_ENDS_WITH {
      @Override
      public String toString() {
         return "not-ends-with";
      }
   };

   private final OperatorValueType valueType;
   private static final ImmutableMultimap<MSCrmDataType, ConditionOperator> typeAvailability;

   ConditionOperator() {
      this(OperatorValueType.SINGLE_VALUE);
   }

   ConditionOperator(OperatorValueType var3) {
      this.valueType = var3;
   }

   public final OperatorValueType getValueType() {
      return this.valueType;
   }

   public String getXml() {
      return this.toString();
   }

   public static Optional<ConditionOperator> fromXml(String var0) {
      for (ConditionOperator var4 : values()) {
         if (var4.getXml().equals(var0)) {
            return Optional.of(var4);
         }
      }

      return Optional.absent();
   }

   @Override
   public String toString() {
      return this.name().toLowerCase();
   }

   public static ImmutableList<ConditionOperator> operatorList() {
      return Ordering.usingToString().immutableSortedCopy(EnumSet.allOf(ConditionOperator.class));
   }

   public boolean isApplicableTo(MSCrmDataType var1) {
      return typeAvailability.containsKey(var1) ? typeAvailability.containsEntry(var1, this) : true;
   }

   static {
      HashMultimap var0 = HashMultimap.create();
      typeAvailability = ImmutableMultimap.copyOf(var0);
   }
}
