package org.etlforge.integration.client.ui.project.permissions;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.integration.data.access.Permission;
import org.etlforge.integration.data.access.Permissions;
import org.etlforge.integration.user.Role;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.event.ChangeListeners;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaCheckBox;

final class PermissionsPanel implements ActionListener, UiProvider {
   private JPanel displayer;
   private JLabel leftLabel;
   private JLabel rightLabel;
   private JCheckBox allowFullAccessCheck;
   private JCheckBox denyFullAccessCheck;
   private List<PermissionCheckBoxes> permissionChecks;
   private PermissionCheckBoxes readChecks;
   private JCheckBox recursiveCheck;
   private boolean recursiveEnabled;
   private PermissionsPanel.State originalState;
   private final ChangeListeners changeListeners = new ChangeListeners();
   static final Permission[] SUPPORTED_PERMISSIONS = new Permission[]{
      Permission.READ, Permission.WRITE, Permission.EXECUTE, Permission.DELETE, Permission.CHANGE_PERMISSIONS
   };
   private static final EnumSet<Permission> FULL_CONTROL = EnumSet.noneOf(Permission.class);
   private static final EnumSet<Permission> ALLOW_READ;

   public PermissionsPanel() {
      this.createComponents();
      this.layoutComponents();
      this.setRecursiveEnabled(false);
      this.setActive(false);
   }

   public void update(Role var1, Permissions var2) {
      this.leftLabel.setText(Strings.get("Permissions.Label.PermissionsFor"));
      this.rightLabel.setText(var1.getName() + ":");
      this.setPermissions(var2);
   }

   public Permissions getPermissions() {
      if (this.allowFullAccessCheck.isSelected()) {
         return new Permissions(FULL_CONTROL, null);
      }

      if (this.denyFullAccessCheck.isSelected()) {
         return new Permissions(null, FULL_CONTROL);
      }

      EnumSet var1 = EnumSet.noneOf(Permission.class);
      EnumSet var2 = EnumSet.noneOf(Permission.class);

      for (PermissionCheckBoxes var4 : this.permissionChecks) {
         var4.apply(var1, var2);
      }

      return new Permissions(var1, var2);
   }

   public boolean isRecursive() {
      return this.recursiveEnabled && this.recursiveCheck.isSelected();
   }

   public void setRecursiveEnabled(boolean var1) {
      this.recursiveEnabled = var1;
      this.recursiveCheck.setEnabled(var1);
   }

   public void setActive(boolean var1) {
      UiUtils.setEnabledRecursively(this.displayer, var1);
      if (var1) {
         this.setRecursiveEnabled(this.recursiveEnabled);
      }
   }

   public boolean isDirty() {
      return this.originalState != null && !this.getState().equals(this.originalState);
   }

   public void setClean() {
      this.originalState = this.getState();
   }

   public void clear() {
      this.allowFullAccessCheck.setSelected(false);
      this.denyFullAccessCheck.setSelected(false);

      for (PermissionCheckBoxes var2 : this.permissionChecks) {
         var2.setState(null, null);
      }

      this.recursiveCheck.setSelected(false);
   }

   public void addChangeListener(ChangeListener var1) {
      this.changeListeners.add(var1);
   }

   private void fireStateChanged() {
      this.changeListeners.call().stateChanged(new ChangeEvent(this));
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.displayer);
   }

   @Override
   public void requestFocus() {
      this.allowFullAccessCheck.requestFocusInWindow();
   }

   private void setPermissions(Permissions var1) {
      if (var1 == null) {
         this.setActive(false);
      } else {
         this.setActive(true);
         this.updateChecks(var1);
      }

      this.originalState = this.getState();
   }

   private PermissionsPanel.State getState() {
      return new PermissionsPanel.State(this.getPermissions(), this.recursiveEnabled ? this.recursiveCheck.isSelected() : false);
   }

   private void updateChecks(Permissions var1) {
      boolean var2 = var1.isEverythingDenied();
      boolean var3 = !var2 && var1.isFullAccess();
      this.denyFullAccessCheck.setSelected(var2);
      this.allowFullAccessCheck.setSelected(var3);
      EnumSet var4 = null;
      EnumSet var5 = null;
      if (var2) {
         var5 = FULL_CONTROL;
      } else if (var3) {
         var4 = FULL_CONTROL;
      } else {
         var4 = var1.getAllowedPermissions();
         var5 = var1.getDeniedPermissions();
      }

      for (PermissionCheckBoxes var7 : this.permissionChecks) {
         var7.setState(var4, var5);
      }

      this.updateOtherPermissionsFromRead();
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      Object var2 = var1.getSource();
      if (var2 == this.allowFullAccessCheck) {
         this.onFullAccessClicked();
      } else if (var2 == this.denyFullAccessCheck) {
         this.onDenyAllAccessClicked();
      }

      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            PermissionsPanel.this.updateReadFromOtherPermissions();
            PermissionsPanel.this.fireStateChanged();
         }
      });
   }

   private void onFullAccessClicked() {
      if (this.allowFullAccessCheck.isSelected()) {
         this.denyFullAccessCheck.setSelected(false);

         for (PermissionCheckBoxes var2 : this.permissionChecks) {
            var2.setState(FULL_CONTROL, null);
         }
      }
   }

   private void onDenyAllAccessClicked() {
      if (this.denyFullAccessCheck.isSelected()) {
         this.allowFullAccessCheck.setSelected(false);

         for (PermissionCheckBoxes var2 : this.permissionChecks) {
            var2.setState(null, FULL_CONTROL);
         }
      }
   }

   private void createComponents() {
      this.leftLabel = new JLabel(Strings.get("Permissions.Label.Permissions"));
      this.rightLabel = new JLabel(" ");
      this.allowFullAccessCheck = this.createCheckBox("", false);
      this.denyFullAccessCheck = this.createCheckBox("", false);
      this.permissionChecks = new ArrayList<>();

      for (Permission var4 : SUPPORTED_PERMISSIONS) {
         PermissionCheckBoxes var5 = new PermissionCheckBoxes(var4, this.allowFullAccessCheck, this.denyFullAccessCheck);
         this.permissionChecks.add(var5);
         if (var4 == Permission.READ) {
            this.readChecks = var5;
            this.readChecks.addActionListener(new PermissionsPanel.ReadListener());
         } else {
            var5.addActionListener(this);
         }
      }

      this.recursiveCheck = this.createCheckBox(Strings.get("Permissions.Label.ApplyRecursive"), false);
   }

   private JCheckBox createCheckBox(String var1, boolean var2) {
      KongaCheckBox var3 = new KongaCheckBox(var1, var2);
      var3.addActionListener(this);
      return var3;
   }

   private void updateOtherPermissionsFromRead() {
      if (this.readChecks != null) {
         boolean var1 = this.readChecks.isAllowed() && !this.readChecks.isDenied();
         if (!var1) {
            for (PermissionCheckBoxes var3 : this.permissionChecks) {
               if (var3 != this.readChecks) {
                  var3.unselectAllow();
               }
            }
         }
      }
   }

   private void updateReadFromOtherPermissions() {
      if (this.readChecks != null) {
         for (PermissionCheckBoxes var2 : this.permissionChecks) {
            if (var2 != this.readChecks && var2.isAllowed() && !var2.isDenied()) {
               this.readChecks.setState(ALLOW_READ, null);
               break;
            }
         }
      }
   }

   private void layoutComponents() {
      NColumnPanel var1 = new NColumnPanel(3, NColumnPanel.WEST);
      var1.setInsets(5, 7, 5, 7);
      this.layoutLabels(var1);
      this.layoutAllowAndDenyLabels(var1);
      this.layoutAllowAndDenyCheckBoxes(var1);
      this.layoutPermissionRows(var1);
      if (this.recursiveCheck != null) {
         this.layoutRecursiveCheck(var1);
      }

      this.displayer = var1.invisible();
   }

   private void layoutPermissionRows(NColumnPanel var1) {
      for (int var2 = 0; var2 < SUPPORTED_PERMISSIONS.length; var2++) {
         this.layoutPermissionRow(var1, var2);
      }
   }

   private void layoutRecursiveCheck(NColumnPanel var1) {
      var1.setInsets(15, 7, 5, 7).setAnchor(NColumnPanel.WEST);
      var1.add(this.recursiveCheck, 3);
   }

   private void layoutPermissionRow(NColumnPanel var1, int var2) {
      PermissionCheckBoxes var3 = this.permissionChecks.get(var2);
      var1.setAnchors(NColumnPanel.WEST, NColumnPanel.CENTER, NColumnPanel.CENTER);
      var1.addRow(new JLabel(SUPPORTED_PERMISSIONS[var2].toString()), var3.getAllowBoxDecorator(), var3.getDenyBoxDecorator());
   }

   private void layoutAllowAndDenyCheckBoxes(NColumnPanel var1) {
      var1.setAnchors(NColumnPanel.WEST, NColumnPanel.CENTER, NColumnPanel.CENTER);
      var1.addRow(new JLabel(Strings.get("Permissions.Label.FullControl")), this.allowFullAccessCheck, this.denyFullAccessCheck);
   }

   private void layoutAllowAndDenyLabels(NColumnPanel var1) {
      var1.setAnchor(NColumnPanel.CENTER);
      var1.addRow(null, new JLabel(Strings.get("Permissions.Label.Allow")), new JLabel(Strings.get("Permissions.Label.Deny")));
   }

   private void layoutLabels(NColumnPanel var1) {
      BoxBuilder var2 = Horizontal.box().of(this.leftLabel, this.rightLabel).glue();
      var1.add(var2.container(), 3);
   }

   static {
      for (Permission var3 : SUPPORTED_PERMISSIONS) {
         FULL_CONTROL.add(var3);
      }

      ALLOW_READ = EnumSet.of(Permission.READ);
   }

   private class ReadListener implements ActionListener {
      private ReadListener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               PermissionsPanel.this.fireStateChanged();
               PermissionsPanel.this.updateOtherPermissionsFromRead();
            }
         });
      }
   }

   private static class State {
      private final Permissions permissions;
      private final boolean recursive;

      public State(Permissions var1, boolean var2) {
         this.permissions = var1;
         this.recursive = var2;
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (var1 instanceof PermissionsPanel.State) {
            PermissionsPanel.State var2 = (PermissionsPanel.State)var1;
            if (this.recursive != var2.recursive) {
               return false;
            } else {
               return this.permissions == null ? var2.permissions == null : this.permissions.equals(var2.permissions);
            }
         } else {
            return false;
         }
      }

      @Override
      public int hashCode() {
         int var1 = 17;
         var1 = 31 * var1 + (this.recursive ? 0 : 1);
         return 31 * var1 + (this.permissions != null ? this.permissions.hashCode() : 0);
      }

      @Override
      public String toString() {
         return this.permissions.toString();
      }
   }
}
