package org.etlforge.integration.salesforce.parser;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public final class CompoundSupportedQueryPolicy implements SupportedQueryPolicy {
   private final ImmutableList<SupportedQueryPolicy> parts;
   private SupportedQueryPolicy veto;

   public CompoundSupportedQueryPolicy(SupportedQueryPolicy var1, SupportedQueryPolicy var2) {
      this(var1, var2, new SupportedQueryPolicy[0]);
   }

   public CompoundSupportedQueryPolicy(SupportedQueryPolicy var1, SupportedQueryPolicy var2, SupportedQueryPolicy... var3) {
      Builder var4 = ImmutableList.builder();
      var4.add(var1);
      var4.add(var2);
      var4.add(var3);
      this.parts = var4.build();
   }

   @Override
   public boolean isSupported(ParsedQuery var1) {
      for (SupportedQueryPolicy var3 : this.parts) {
         if (!var3.isSupported(var1)) {
            this.veto = var3;
            return false;
         }
      }

      this.veto = null;
      return true;
   }

   @Override
   public String whyNot() {
      Preconditions.checkState(this.veto != null, "whyNot should not be called for a valid query.");
      return this.veto.whyNot();
   }
}
