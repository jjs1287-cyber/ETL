package org.etlforge.integration.client.ui.interchange.transformation.wizard;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.ArrayList;
import java.util.List;

public final class DefaultWizardConfiguration implements WizardConfiguration {
   private final ImmutableSet<WizardType> types;

   public DefaultWizardConfiguration(List<WizardType> var1, List<WizardType> var2) {
      Builder var3 = ImmutableSet.builder();
      var3.addAll(var1);
      var3.addAll(var2);
      var3.add(WizardTypes.NONE);
      this.types = var3.build();
   }

   @Override
   public List<WizardType> getSupportedInputTypes() {
      ArrayList var1 = Lists.newArrayList();

      for (WizardType var3 : this.types) {
         if (var3.isSupportedAsInput()) {
            var1.add(var3);
         }
      }

      return var1;
   }

   @Override
   public List<WizardType> getSupportedOutputTypes() {
      ArrayList var1 = Lists.newArrayList();

      for (WizardType var3 : this.types) {
         if (var3.isSupportedAsOutput()) {
            var1.add(var3);
         }
      }

      return var1;
   }
}
