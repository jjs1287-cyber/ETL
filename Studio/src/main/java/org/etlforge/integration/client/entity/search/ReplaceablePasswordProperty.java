package org.etlforge.integration.client.entity.search;

import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class ReplaceablePasswordProperty implements ReplaceableProperty<Password> {
   private final KeyValueStringProperty impl;

   public ReplaceablePasswordProperty(String var1, String var2) {
      this.impl = new KeyValueStringProperty(var1, var2);
   }

   @Override
   public String getName() {
      return this.impl.getName();
   }

   @Override
   public Class<Password> getValueType() {
      return Password.class;
   }

   public Password getValue(IntegrationEntity var1) {
      String var2 = this.impl.getValue(var1);
      return var2 == null ? null : Password.decrypt(var2);
   }

   public void setValue(IntegrationEntity var1, Password var2) {
      String var3 = var2 == null ? null : var2.encrypted();
      this.impl.setValue(var1, var3);
   }
}
