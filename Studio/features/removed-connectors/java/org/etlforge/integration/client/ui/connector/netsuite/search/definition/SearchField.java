package org.etlforge.integration.client.ui.connector.netsuite.search.definition;

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.List;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.SearchFieldSaxHandler;

public abstract class SearchField implements Serializable {
   private final List<NetSuiteSearchOperator> operators = Lists.newArrayList();

   protected SearchField() {
   }

   public List<NetSuiteSearchOperator> getNetSuiteSearchOperators() {
      return this.operators;
   }

   public List<String> getPredefinedList() {
      return null;
   }

   public abstract int definedValueNumber();

   public abstract SearchFieldSaxHandler newSearchFieldValueSaxHandler();

   public void addOperator(String var1) {
      NetSuiteSearchOperator var2 = NetSuiteSearchOperator.valueOf(var1);
      this.operators.add(var2);
   }
}
