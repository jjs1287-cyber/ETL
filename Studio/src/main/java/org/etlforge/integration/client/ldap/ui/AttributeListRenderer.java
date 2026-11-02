package org.etlforge.integration.client.ldap.ui;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;

public class AttributeListRenderer extends DefaultListCellRenderer {
   @Override
   public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
      super.getListCellRendererComponent(var1, var2, var3, var4, var5);
      if (var2 instanceof LdapStructureAttribute) {
         this.customize((LdapStructureAttribute)var2);
      } else {
         this.setToolTipText(null);
      }

      return this;
   }

   private void customize(LdapStructureAttribute var1) {
      this.setText("[A] " + var1.getName());
      this.setToolTipText(new AttributeToolTip(var1).get());
   }
}
