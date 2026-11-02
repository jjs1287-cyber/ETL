package org.etlforge.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import org.etlforge.collections.KongaSetUtils;

public class ResourceBundleStringResources implements StringResources {
   private final ResourceBundle bundle;
   private final Set<String> existingKeys;
   private final MnemonicStringFactory mnemonicFactory;
   private List<StringResources> parents;

   public ResourceBundleStringResources(String var1, MnemonicStringFactory var2) {
      this.bundle = ResourceBundle.getBundle(var1);
      this.existingKeys = KongaSetUtils.asSet(this.bundle.getKeys());
      this.mnemonicFactory = var2;
   }

   @Override
   public MnemonicStringFactory getMnemonicsFactory() {
      return this.mnemonicFactory;
   }

   @Override
   public boolean contains(String var1) {
      return this.existingKeys.contains(var1);
   }

   @Override
   public String getString(String var1) {
      return this.getString(var1, "!" + var1 + "!");
   }

   @Override
   public String getString(String var1, String var2) {
      if (this.contains(var1)) {
         return this.bundle.getString(var1);
      }

      if (this.parents != null) {
         for (StringResources var4 : this.parents) {
            if (var4.contains(var1)) {
               return var4.getString(var1);
            }
         }
      }

      return var2;
   }

   @Override
   public MnemonicString getMnemonicString(String var1) {
      String var2 = this.getString(var1);
      return var2 == null ? null : this.mnemonicFactory.fromString(var2);
   }

   public void addParent(StringResources var1) {
      if (var1 != null) {
         if (this.parents == null) {
            this.parents = new ArrayList<>();
         }

         this.parents.add(var1);
      }
   }
}
