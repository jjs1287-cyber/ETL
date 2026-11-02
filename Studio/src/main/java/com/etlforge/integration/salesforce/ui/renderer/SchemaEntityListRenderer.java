package com.etlforge.integration.salesforce.ui.renderer;

import com.etlforge.integration.salesforce.ui.NameDisplayMode;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.etlforge.integration.salesforce.wsdl.SchemaEntity;

public final class SchemaEntityListRenderer extends DefaultListCellRenderer {
   private final NameDisplayMode nameMode;

   public SchemaEntityListRenderer(NameDisplayMode var1) {
      this.nameMode = var1;
   }

   @Override
   public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
      super.getListCellRendererComponent(var1, var2, var3, var4, var5);
      if (var2 instanceof SchemaEntity) {
         SchemaEntityRenderingUtils.decorate(this, (SchemaEntity)var2, this.nameMode);
      } else {
         this.setToolTipText(null);
      }

      return this;
   }
}
