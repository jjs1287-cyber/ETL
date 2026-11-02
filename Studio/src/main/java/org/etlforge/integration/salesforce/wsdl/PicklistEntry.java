package org.etlforge.integration.salesforce.wsdl;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import org.apache.axis.utils.StringUtils;

public final class PicklistEntry implements Serializable {
   private static final long serialVersionUID = -6235834009536464347L;
   private final String label;
   private final String value;

   public PicklistEntry(String var1, String var2) {
      this.value = Preconditions.checkNotNull(var2);
      this.label = StringUtils.isEmpty(var1) ? this.value : var1;
   }

   public String getLabel() {
      return this.label;
   }

   public String getValue() {
      return this.value;
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("label", this.label).add("value", this.value).toString();
   }
}
