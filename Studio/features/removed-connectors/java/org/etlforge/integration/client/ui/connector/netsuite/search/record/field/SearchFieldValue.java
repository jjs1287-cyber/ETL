package org.etlforge.integration.client.ui.connector.netsuite.search.record.field;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import java.util.HashMap;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchOperator;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchCustomField;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.NetSuiteSearchCustomFieldId;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.XmlUtils;

public abstract class SearchFieldValue {
   private final NetSuiteSearchOperator selectedOperator;

   protected SearchFieldValue(NetSuiteSearchOperator var1) {
      this.selectedOperator = var1;
   }

   public NetSuiteSearchOperator getNetSuiteSearchOperator() {
      return this.selectedOperator;
   }

   public void toXml(StringBuilder var1, String var2, NetSuiteSearchCustomField var3, NetSuiteSearchCustomFieldId var4) {
      HashMap var5 = Maps.newHashMap();
      if (!this.isOperatorNotSet()) {
         var5.put("operator", this.selectedOperator.toString());
      }

      if (var3 != null) {
         var4.put(var5, var3);
         var5.put("xsi:type", var3.getSearchCustomFieldType());
      }

      XmlUtils.startElementTagAttributes(var1, var2, var5);
      this.toXmlSearchFieldValue(var1);
      XmlUtils.endElementTag(var1, var2);
   }

   protected abstract void toXmlSearchFieldValue(StringBuilder var1);

   private boolean isOperatorNotSet() {
      return this.selectedOperator == null || this.selectedOperator == NetSuiteSearchOperator.none;
   }

   protected static final void toXmlSearchFieldValue(StringBuilder var0, String var1) {
      toXmlSearchFieldValueImpl(var0, "searchValue", var1);
   }

   protected static final void toXmlSearchFieldValueImpl(StringBuilder var0, String var1, String var2) {
      if (!Strings.isNullOrEmpty(var2)) {
         XmlUtils.ElementValue(var0, var1, var2);
      }
   }

   protected static final void toXmlSearchFieldValue2(StringBuilder var0, String var1, String var2) {
      if (Strings.isNullOrEmpty(var1)) {
         toXmlSearchFieldValueImpl(var0, "searchValue", var2);
      } else {
         XmlUtils.ElementValue(var0, "searchValue", var1);
         toXmlSearchFieldValueImpl(var0, "searchValue2", var2);
      }
   }

   @Override
   public String toString() {
      return this.isOperatorNotSet() ? "" : "  operator=" + this.selectedOperator + "  ";
   }

   protected static void toString(StringBuilder var0, String var1) {
      if (!Strings.isNullOrEmpty(var1)) {
         append(var0, "searchValue", var1);
      }
   }

   protected static void toString2(StringBuilder var0, String var1, String var2) {
      if (Strings.isNullOrEmpty(var1)) {
         append(var0, "searchValue", var2);
      } else {
         append(var0, "searchValue", var1);
         if (Strings.isNullOrEmpty(var2)) {
            return;
         }

         var0.append(";");
         append(var0, "searchValue2", var2);
      }
   }

   protected static void append(StringBuilder var0, String var1, String var2) {
      if (!Strings.isNullOrEmpty(var2)) {
         var0.append("  ");
         var0.append(var1);
         var0.append('=');
         var0.append(var2);
      }
   }
}
