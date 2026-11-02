package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import org.etlforge.xml.XmlStringBuilder;

abstract class ConditionValue {
   public static final ConditionValue NONE = new ConditionValue() {
      @Override
      public void appendToXml(XmlStringBuilder var1) {
      }

      @Override
      public String getDisplay() {
         return "";
      }

      @Override
      public int hashCode() {
         return System.identityHashCode(this);
      }

      @Override
      public boolean equals(Object var1) {
         return var1 == this;
      }

      @Override
      public String toString() {
         return "NONE";
      }
   };

   public String getDisplay() {
      return this.toString();
   }

   public abstract void appendToXml(XmlStringBuilder var1);

   @Override
   public abstract int hashCode();

   @Override
   public abstract boolean equals(Object var1);
}
