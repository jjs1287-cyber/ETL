package org.etlforge.integration.mscrm;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.integration.mscrm.metadata.MSCrmAttribute;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.integration.mscrm.metadata.PrimaryIdPredicate;

public enum FunctionType implements ConnectorObject {
   CREATE {
      @Override
      protected Predicate<MSCrmAttribute> getAttributeFilter(MSCrmObject var1) {
         return new Predicate<MSCrmAttribute>() {
            public boolean apply(MSCrmAttribute var1) {
               return var1.isValidForCreate();
            }
         };
      }
   },
   UPDATE {
      @Override
      protected Predicate<MSCrmAttribute> getAttributeFilter(MSCrmObject var1) {
         return new Predicate<MSCrmAttribute>() {
            public boolean apply(MSCrmAttribute var1) {
               return var1.isPrimaryId() || var1.isValidForUpdate();
            }
         };
      }
   },
   UPSERT {
      @Override
      protected Predicate<MSCrmAttribute> getAttributeFilter(MSCrmObject var1) {
         return new Predicate<MSCrmAttribute>() {
            public boolean apply(MSCrmAttribute var1) {
               return var1.isPrimaryId() || var1.isValidForUpsert();
            }
         };
      }
   },
   DELETE {
      @Override
      protected Predicate<MSCrmAttribute> getAttributeFilter(MSCrmObject var1) {
         return new PrimaryIdPredicate(var1);
      }
   },
   RETRIEVE {
      @Override
      protected Predicate<MSCrmAttribute> getAttributeFilter(MSCrmObject var1) {
         return new Predicate<MSCrmAttribute>() {
            public boolean apply(MSCrmAttribute var1) {
               return var1.isValidForRead();
            }
         };
      }
   };

   FunctionType() {
   }

   @Override
   public String getName() {
      return this.name();
   }

   @Override
   public String getDescription() {
      return "";
   }

   public final Iterable<MSCrmAttribute> getMatchingAttributes(MSCrmObject var1) {
      return Iterables.filter(var1.getAttributes(), this.getAttributeFilter(var1));
   }

   protected abstract Predicate<MSCrmAttribute> getAttributeFilter(MSCrmObject var1);
}
