package com.etlforge.integration.client.ui.sfdc.page.soql;

import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.List;
import org.etlforge.integration.salesforce.parser.ParsedQuery;

public final class CompoundChangePolicy implements SoqlChangePolicy {
   private final ImmutableList<SoqlChangePolicy> parts;

   public static CompoundChangePolicy of(SoqlChangePolicy... var0) {
      return new CompoundChangePolicy(Arrays.asList(var0));
   }

   public CompoundChangePolicy(List<SoqlChangePolicy> var1) {
      this.parts = ImmutableList.copyOf(var1);
   }

   @Override
   public void verifyChange(ParsedQuery var1) throws EditSoqlException {
      for (SoqlChangePolicy var3 : this.parts) {
         var3.verifyChange(var1);
      }
   }
}
