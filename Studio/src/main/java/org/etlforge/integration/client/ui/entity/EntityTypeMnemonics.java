package org.etlforge.integration.client.ui.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Collection;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.resources.UiResourceBundle;

public final class EntityTypeMnemonics {
   private final ImmutableMap<EntityType, MnemonicString> mnemonics;

   public EntityTypeMnemonics(Collection<EntityType> var1) {
      this.mnemonics = this.build(var1);
   }

   private ImmutableMap<EntityType, MnemonicString> build(Iterable<EntityType> var1) {
      Builder var2 = ImmutableMap.builder();

      for (EntityType var4 : var1) {
         var2.put(var4, UiResourceBundle.MNEMONIC_FACTORY.fromString(var4.getMnemonicString()));
      }

      return var2.build();
   }

   public MnemonicString getMnemonicString(EntityType var1) {
      Preconditions.checkArgument(this.mnemonics.containsKey(var1), var1 + " is not one of the types passed to the constructor");
      return this.mnemonics.get(var1);
   }
}
