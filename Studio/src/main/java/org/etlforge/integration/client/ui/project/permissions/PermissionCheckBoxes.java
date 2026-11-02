package org.etlforge.integration.client.ui.project.permissions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;
import javax.swing.JCheckBox;
import org.etlforge.integration.data.access.Permission;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.button.KongaCheckBox;

final class PermissionCheckBoxes implements ActionListener {
   private Permission permission;
   private JCheckBox allowBox;
   private JCheckBox denyBox;
   private JCheckBox allowAllBox;
   private JCheckBox denyAllBox;

   public PermissionCheckBoxes(Permission var1, JCheckBox var2, JCheckBox var3) {
      this.permission = var1;
      this.allowAllBox = var2;
      this.denyAllBox = var3;
      this.allowBox = this.createCheckBox();
      this.denyBox = this.createCheckBox();
   }

   private JCheckBox createCheckBox() {
      KongaCheckBox var1 = new KongaCheckBox();
      var1.addActionListener(this);
      return var1;
   }

   public boolean isAllowed() {
      return this.allowBox.isSelected();
   }

   public boolean isDenied() {
      return this.denyBox.isSelected();
   }

   public void unselectAllow() {
      this.allowBox.setSelected(false);
   }

   public void apply(EnumSet<Permission> var1, EnumSet<Permission> var2) {
      if (this.allowBox.isSelected()) {
         var1.add(this.permission);
      } else if (this.denyBox.isSelected()) {
         var2.add(this.permission);
      }
   }

   public void setState(EnumSet<Permission> var1, EnumSet<Permission> var2) {
      boolean var3 = var2 != null && var2.contains(this.permission);
      this.denyBox.setSelected(var3);
      boolean var4 = !var3 && var1 != null && var1.contains(this.permission);
      this.allowBox.setSelected(var4);
   }

   public UiProvider getAllowBoxDecorator() {
      return new UiProvider() {
         @Override
         public void decorate(UiCanvas var1) {
            var1.display(PermissionCheckBoxes.this.allowBox);
         }

         @Override
         public void requestFocus() {
            PermissionCheckBoxes.this.allowAllBox.requestFocusInWindow();
         }
      };
   }

   public UiProvider getDenyBoxDecorator() {
      return new UiProvider() {
         @Override
         public void decorate(UiCanvas var1) {
            var1.display(PermissionCheckBoxes.this.denyBox);
         }

         @Override
         public void requestFocus() {
            PermissionCheckBoxes.this.denyBox.requestFocusInWindow();
         }
      };
   }

   public void addActionListener(ActionListener var1) {
      this.allowBox.addActionListener(var1);
      this.denyBox.addActionListener(var1);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      Object var2 = var1.getSource();
      if (var2 == this.allowBox) {
         this.onAllowBoxClick();
      } else if (var2 == this.denyBox) {
         this.onDenyBoxClick();
      }
   }

   private void onAllowBoxClick() {
      if (this.allowBox.isSelected()) {
         this.denyBox.setSelected(false);
         this.denyAllBox.setSelected(false);
      } else {
         this.allowAllBox.setSelected(false);
      }
   }

   private void onDenyBoxClick() {
      if (this.denyBox.isSelected()) {
         this.allowBox.setSelected(false);
         this.allowAllBox.setSelected(false);
      } else {
         this.denyAllBox.setSelected(false);
      }
   }
}
