package org.etlforge.integration.uidelegate.entity.renderer;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.validation.ValidationStatus;

public class IntegrationEntityTableCellRenderer extends DefaultTableCellRenderer {
   private final boolean longMode;
   private boolean validationDecorating;

   public IntegrationEntityTableCellRenderer() {
      this(false);
   }

   public IntegrationEntityTableCellRenderer(boolean var1) {
      this.longMode = var1;
   }

   public void setValidationDecorating(boolean var1) {
      this.validationDecorating = var1;
   }

   @Override
   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
      this.setFont(TextStyles.DefaultListText.getFont());
      if (var2 instanceof IntegrationEntity) {
         IntegrationEntity var7 = (IntegrationEntity)var2;
         ValidationStatus var8 = this.validationDecorating ? var7.getValidationStatus() : ValidationStatus.VALID;
         RendererUtils.decorateRenderer(this, var7, var8, this.longMode);
      } else {
         this.setIcon(null);
         this.setToolTipText(null);
      }

      return this;
   }
}
