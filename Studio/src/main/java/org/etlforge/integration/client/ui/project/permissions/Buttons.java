package org.etlforge.integration.client.ui.project.permissions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.widget.button.KongaButton;

final class Buttons implements ActionListener, UiProvider {
   private final PermissionsPage page;
   private JButton applyButton;
   private JButton resetButton;
   private JButton viewEffectiveButton;
   private boolean active;

   Buttons(PermissionsPage var1) {
      this.page = var1;
      this.active = true;
      this.createButtons();
      this.setActive(false);
   }

   void setActive(boolean var1) {
      if (var1 != this.active) {
         this.active = var1;
         boolean var2 = this.page.isDirty();
         this.applyButton.setEnabled(var2);
         this.resetButton.setEnabled(var2);
         this.viewEffectiveButton.setEnabled(var1);
      }
   }

   void update() {
      if (this.active) {
         boolean var1 = this.page.isDirty();
         this.applyButton.setEnabled(var1);
         this.resetButton.setEnabled(var1);
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      Vertical.grid().of(" ", this.applyButton, this.resetButton, this.viewEffectiveButton).withVGap(10).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.applyButton.requestFocusInWindow();
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      Object var2 = var1.getSource();
      if (var2 == this.applyButton) {
         this.page.onApply();
      } else if (var2 == this.resetButton) {
         this.page.onReset();
      } else if (var2 == this.viewEffectiveButton) {
         this.page.onViewEffective();
      }
   }

   private void createButtons() {
      this.applyButton = new KongaButton(Strings.get("Permissions.Buttons.Apply"));
      this.applyButton.addActionListener(this);
      this.resetButton = new KongaButton(Strings.get("Permissions.Buttons.Reset"));
      this.resetButton.addActionListener(this);
      this.viewEffectiveButton = new KongaButton(Strings.get("Permissions.Buttons.Effective"));
      this.viewEffectiveButton.addActionListener(this);
   }
}
