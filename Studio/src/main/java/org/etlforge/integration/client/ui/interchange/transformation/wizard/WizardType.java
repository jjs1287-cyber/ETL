package org.etlforge.integration.client.ui.interchange.transformation.wizard;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.system.ArgCheck;

public abstract class WizardType {
   private final String id;
   private final String name;
   private final WizardType.Nature nature;
   private final PageChainFactory sourceChainFactory;
   private final PageChainFactory targetChainFactory;

   public WizardType(String var1, String var2, WizardType.Nature var3, PageChainFactory var4, PageChainFactory var5) {
      Preconditions.checkArgument(var4 != null || var5 != null, "Both sourceChainFactory and targetChainFactory cannot be null");
      this.id = ArgCheck.checkNotBlank(var1);
      this.name = ArgCheck.checkNotBlank(var2);
      this.nature = Preconditions.checkNotNull(var3);
      this.sourceChainFactory = var4;
      this.targetChainFactory = var5;
   }

   public final String getId() {
      return this.id;
   }

   public final String getName() {
      return this.name;
   }

   public final WizardType.Nature getNature() {
      return this.nature;
   }

   public final PageChainFactory getSourceChainFactory() {
      return this.sourceChainFactory;
   }

   public final PageChainFactory getTargetChainFactory() {
      return this.targetChainFactory;
   }

   @Override
   public final String toString() {
      return this.id;
   }

   public final boolean isSupportedAsInput() {
      return this.sourceChainFactory != null;
   }

   public final boolean isSupportedAsOutput() {
      return this.targetChainFactory != null;
   }

   public boolean isTiedToLocatable(Locatable var1) {
      return false;
   }

   public abstract boolean isTiedToInput(Transformation var1);

   public abstract boolean isTiedToOutput(Transformation var1);

   @Override
   public final int hashCode() {
      return this.id.hashCode();
   }

   @Override
   public final boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof WizardType && this.id.equals(((WizardType)var1).id);
   }

   public enum Nature {
      FLAT,
      HIERARCHICAL,
      FLAT_OR_HIERARCHICAL;
   }
}
