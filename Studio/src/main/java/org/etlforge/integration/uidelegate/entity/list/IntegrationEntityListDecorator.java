package org.etlforge.integration.uidelegate.entity.list;

import com.google.common.base.Preconditions;
import java.awt.Color;
import javax.swing.Icon;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.IconStore;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;

public class IntegrationEntityListDecorator implements ListDecorator {
   private final IntegrationEntity entity;
   private boolean indicateValidationStatus;

   public IntegrationEntityListDecorator(IntegrationEntity var1) {
      this.entity = Preconditions.checkNotNull(var1);
   }

   public void setIndicateValidationStatus(boolean var1) {
      this.indicateValidationStatus = var1;
   }

   @Override
   public void decorate(ListDecor var1, boolean var2) {
      var1.setText(this.entity.getName());
      var1.setIcon(this.getIcon());
      var1.setToolTip(ToolTipStringFactory.getToolTip(this.entity));
      if (!var2) {
         Color var3 = this.getTextColor();
         if (var3 != null) {
            var1.setTextColor(var3);
         }
      }
   }

   protected Icon getIcon() {
      return this.indicateValidationStatus ? IconStore.validationDecoratedIcon(this.entity) : this.getDefaultIcon(this.entity);
   }

   protected Icon getDefaultIcon(IntegrationEntity var1) {
      return IconStore.defaultIcon(this.getEntity());
   }

   public final IntegrationEntity getEntity() {
      return this.entity;
   }

   protected Color getTextColor() {
      return null;
   }

   @Override
   public final String toString() {
      return this.entity.getName();
   }
}
