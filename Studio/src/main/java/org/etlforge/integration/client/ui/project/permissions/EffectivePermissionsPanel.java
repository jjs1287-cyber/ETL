package org.etlforge.integration.client.ui.project.permissions;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.project.permission.PermissionsManager;
import org.etlforge.integration.data.access.Permission;
import org.etlforge.integration.data.access.Permissions;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.button.KongaCheckBox;

final class EffectivePermissionsPanel {
   private final JPanel panel = new JPanel(new GridBagLayout());

   public EffectivePermissionsPanel(PermissionsViewImpl var1, IntegrationEntity var2, User var3) {
      PermissionsManager var4 = var1.getPermissions();
      Permissions var5 = var4.getEffectivePermissions(var3, var2);
      this.layoutPanel(var5);
   }

   public EffectivePermissionsPanel(PermissionsViewImpl var1, IntegrationEntity var2, Group var3) {
      PermissionsManager var4 = var1.getPermissions();
      Permissions var5 = var4.getEffectivePermissions(var3, var2);
      this.layoutPanel(var5);
   }

   public JComponent getDisplayer() {
      return this.panel;
   }

   private void layoutPanel(Permissions var1) {
      GridBagConstraints var2 = this.getInitialConstraints();
      this.panel.add(TextStyles.DefaultBold.makeLabel(Strings.get("Permissions.Label.EffectivePermissions")), var2);
      var2.gridwidth = 1;

      for (Permission var6 : PermissionsPanel.SUPPORTED_PERMISSIONS) {
         this.addPermissionBox(var1, var6, var2);
      }

      this.panel.setBorder(this.createBorder());
   }

   private GridBagConstraints getInitialConstraints() {
      GridBagConstraints var1 = new GridBagConstraints();
      var1.anchor = 17;
      var1.gridwidth = 2;
      var1.insets = new Insets(5, 5, 5, 5);
      var1.gridx = 0;
      var1.gridy = 0;
      return var1;
   }

   private void addPermissionBox(Permissions var1, Permission var2, GridBagConstraints var3) {
      var3.gridy++;
      var3.gridx = 0;
      this.panel.add(new JLabel(var2.toString()), var3);
      boolean var4 = var1 != null && var1.isAllowed(var2);
      KongaCheckBox var5 = new KongaCheckBox(var4);
      var5.setEnabled(false);
      var3.gridx = 1;
      this.panel.add(var5, var3);
   }

   private Border createBorder() {
      return new BorderBuilder().empty(10).etched().empty(10);
   }
}
