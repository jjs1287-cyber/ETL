package com.etlforge.integration.client.ui.sfdc.query.wizard;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.ui.UiThreadSupplier;

final class UiThreadSafeQueryTarget implements QueryTarget {
   private final QueryTarget impl;

   public UiThreadSafeQueryTarget(QueryTarget var1) {
      this.impl = Preconditions.checkNotNull(var1);
   }

   @Override
   public void applyTo(final Transformation var1) {
      UiThreadSupplier.call(new Supplier<Void>() {
         public Void get() {
            UiThreadSafeQueryTarget.this.impl.applyTo(var1);
            return null;
         }
      });
   }
}
