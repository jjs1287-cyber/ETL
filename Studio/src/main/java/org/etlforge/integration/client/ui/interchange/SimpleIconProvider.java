package org.etlforge.integration.client.ui.interchange;

import com.google.common.base.Preconditions;
import javax.swing.Icon;
import org.etlforge.integration.uidelegate.entity.EntityIcons;

public final class SimpleIconProvider implements TopLevelEntityCategory.IconProvider {
   private final Icon small;
   private final Icon medium;
   private final Icon large;

   public SimpleIconProvider(Icon var1, Icon var2, Icon var3) {
      this.small = Preconditions.checkNotNull(var1);
      this.medium = Preconditions.checkNotNull(var2);
      this.large = Preconditions.checkNotNull(var3);
   }

   @Override
   public Icon getIcon(EntityIcons.Size var1, EntityIcons.Category var2) {
      switch (var1) {
         case SMALL:
            return this.small;
         case MEDIUM:
            return this.medium;
         case LARGE:
            return this.large;
         default:
            throw new AssertionError();
      }
   }
}
