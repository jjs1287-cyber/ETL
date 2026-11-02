package org.etlforge.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.Icon;
import org.etlforge.collections.KongaSetUtils;

public class ResourceBundleIconProvider implements IconProvider {
   private final ResourceBundle bundle;
   private final Set<String> existingKeys;
   private List<IconProvider> parents;

   public ResourceBundleIconProvider(String var1) {
      this.bundle = ResourceBundle.getBundle(var1);
      this.existingKeys = KongaSetUtils.asSet(this.bundle.getKeys());
   }

   @Override
   public Icon getIcon(String var1) {
      if (this.existingKeys.contains(var1)) {
         return (Icon)this.bundle.getObject(var1);
      }

      if (this.parents != null) {
         for (IconProvider var3 : this.parents) {
            Icon var4 = var3.getIcon(var1);
            if (var4 != null) {
               return var4;
            }
         }
      }

      return null;
   }

   public void addParent(IconProvider var1) {
      if (var1 != null) {
         if (this.parents == null) {
            this.parents = new ArrayList<>();
         }

         this.parents.add(var1);
      }
   }
}
