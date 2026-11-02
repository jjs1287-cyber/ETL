package com.etlforge.integration.client.deploy.ui;

import javax.swing.table.DefaultTableCellRenderer;
import org.etlforge.integration.client.ui.icons.ClientIcons;

class UserTableRenderer extends DefaultTableCellRenderer {
   @Override
   protected void setValue(Object var1) {
      super.setValue(var1);
      this.setIcon(ClientIcons.USER_16);
   }
}
