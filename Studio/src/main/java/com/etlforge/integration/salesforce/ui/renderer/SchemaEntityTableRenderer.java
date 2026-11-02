package com.etlforge.integration.salesforce.ui.renderer;

import com.etlforge.integration.salesforce.ui.NameDisplayMode;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.etlforge.integration.salesforce.wsdl.SchemaEntity;

public final class SchemaEntityTableRenderer extends DefaultTableCellRenderer {
   private final NameDisplayMode nameMode;

   public SchemaEntityTableRenderer(NameDisplayMode var1) {
      this.nameMode = var1;
   }

   @Override
   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
      if (var2 instanceof SchemaEntity) {
         SchemaEntityRenderingUtils.decorate(this, (SchemaEntity)var2, this.nameMode);
      } else {
         this.setToolTipText(null);
      }

      return this;
   }
}
