package com.etlforge.integration.salesforce.ui;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import java.util.List;
import org.etlforge.integration.salesforce.wsdl.SchemaEntity;

public enum NameDisplayMode implements Function<SchemaEntity, String> {
   LABEL {
      @Override
      public String getDisplayName(SchemaEntity var1) {
         return var1.getLabel();
      }

      @Override
      public String getToolTip(SchemaEntity var1) {
         return var1.getName();
      }

      @Override
      public String toString() {
         return "Label";
      }
   },
   API_NAME {
      @Override
      public String getDisplayName(SchemaEntity var1) {
         return var1.getName();
      }

      @Override
      public String getToolTip(SchemaEntity var1) {
         return var1.getLabel();
      }

      @Override
      public String toString() {
         return "API Name";
      }
   };

   NameDisplayMode() {
   }

   public abstract String getDisplayName(SchemaEntity var1);

   public abstract String getToolTip(SchemaEntity var1);

   public String apply(SchemaEntity var1) {
      return this.getDisplayName(var1);
   }

   public <T extends SchemaEntity> List<T> sortedCopy(Iterable<T> var1) {
      return this.toOrdering().sortedCopy(var1);
   }

   public Ordering<? super SchemaEntity> toOrdering() {
      return Ordering.<String>natural().onResultOf(this);
   }
}
