package org.etlforge.integration.client.ui.core.main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.view.ViewLauncher;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.widget.button.KongaButton;

final class LaunchAdminButton {
   private final LaunchAdminButton.ButtonImpl button = new LaunchAdminButton.ButtonImpl();

   public LaunchAdminButton(final ViewLauncher var1, final ViewLauncher var2) {
      this.button.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1x) {
            var1.dismiss();
            var2.activate(null);
         }
      });
   }

   public JComponent getUi() {
      return Horizontal.box().glue().add(this.button).rigid(24).nonOpaque().container();
   }

   private static class ButtonImpl extends KongaButton {
      public ButtonImpl() {
         super("Launch &Admin Console", ApplicationResources.getIcon("ArrowRight.White.16"));
         this.setHorizontalTextPosition(10);
         this.setDisplayedMnemonicIndex(-1);
         WizardStyleSheet.SECTION_STYLE.makeOver(this);
         this.setForeground(Color.WHITE);
         this.setMargin(null);
         this.setBorder(null);
         this.setContentAreaFilled(false);
      }

      @Override
      public void paint(Graphics var1) {
         boolean var2 = this.getModel().isRollover();
         boolean var3 = this.getModel().isArmed();
         boolean var4 = this.getModel().isPressed();
         if (!var2 && !var3 && !var4) {
            super.paint(var1);
         } else {
            Graphics2D var5 = (Graphics2D)var1;
            Composite var6 = var5.getComposite();
            float var7 = var4 ? 0.4F : (var3 ? 0.5F : 0.6F);
            AlphaComposite var8 = AlphaComposite.SrcOver.derive(var7);
            var5.setComposite(var8);
            super.paint(var5);
            var5.setComposite(var6);
         }
      }
   }
}
