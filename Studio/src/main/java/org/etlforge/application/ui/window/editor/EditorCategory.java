package org.etlforge.application.ui.window.editor;

import com.google.common.base.Preconditions;
import javax.swing.Icon;
import org.apache.commons.lang.StringUtils;

public final class EditorCategory {
   public final String name;
   public final Icon icon;

   public static EditorCategory defaultCategory(Editor var0) {
      return new EditorCategory(var0.getTitle(), var0.getSmallIcon());
   }

   public EditorCategory(String var1, Icon var2) {
      Preconditions.checkArgument(StringUtils.isNotBlank(var1), "name may not be null or empty");
      this.name = var1;
      this.icon = var2;
   }

   @Override
   public int hashCode() {
      return this.name.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 instanceof EditorCategory) {
         EditorCategory var2 = (EditorCategory)var1;
         return this.name.equals(var2.name);
      } else {
         return false;
      }
   }

   @Override
   public String toString() {
      return this.name;
   }
}
