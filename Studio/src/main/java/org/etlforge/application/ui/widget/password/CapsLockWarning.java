package org.etlforge.application.ui.widget.password;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.widget.panel.KongaPanel;
import org.etlforge.ui.widget.panel.KongaPanelAlphaAnimator;

public class CapsLockWarning {
   private JComponent detector;
   private KeyBindingInstaller keyBindings;
   private KongaPanel ui;
   private KongaPanelAlphaAnimator animator;
   private boolean isDisplayed;

   public CapsLockWarning() {
      this.createUi();
      this.initializeCapsLockWarning();
   }

   private void createUi() {
      JLabel var1 = new JLabel(PackageResources.CAPS_LOCK_WARNING);
      var1.setIcon(PackageResources.CAPS_LOCK_ICON);
      this.ui = new KongaPanel(new FlowLayout(3, 0, 0));
      this.ui.add(var1);
   }

   public void activate(JComponent var1) {
      this.detector = var1;
      this.keyBindings = new KeyBindingInstaller(var1, 2);
      this.keyBindings.addBinding(KeyStroke.getKeyStroke(20, 0), "caps_lock", new CapsLockWarning.CapsLockAction());
      this.keyBindings.install();
   }

   public void dispose() {
      if (this.keyBindings != null) {
         this.keyBindings.remove();
         this.keyBindings = null;
      }
   }

   public void addToParent(Container var1, Object var2) {
      var1.add(this.ui, var2);
   }

   private void initializeCapsLockWarning() {
      boolean var1 = false;

      try {
         var1 = Toolkit.getDefaultToolkit().getLockingKeyState(20);
      } catch (UnsupportedOperationException var3) {
         var1 = false;
      }

      if (var1) {
         this.showCapsLockWarning();
      } else {
         this.ui.setVisible(false);
      }

      this.isDisplayed = var1;
   }

   private void showCapsLockWarning() {
      if (this.animator != null) {
         this.animator.stop();
      }

      this.ui.setAlpha(0.0F);
      this.ui.setVisible(true);
      this.animator = new KongaPanelAlphaAnimator(this.ui);
      this.animator.setDuration(100);
      this.animator.show();
   }

   private void hideCapsLockWarning() {
      if (this.animator != null) {
         this.animator.stop();
      }

      this.animator = new KongaPanelAlphaAnimator(this.ui, new TimingTargetAdapter() {
         @Override
         public void end() {
            CapsLockWarning.this.ui.setVisible(false);
         }
      });
      this.animator.setDuration(100);
      this.animator.hide();
   }

   private void capsLockOn(boolean var1) {
      if (var1 != this.isDisplayed) {
         this.isDisplayed = var1;
         if (var1) {
            this.showCapsLockWarning();
         } else {
            this.hideCapsLockWarning();
         }

         if (this.detector != null) {
            this.detector.revalidate();
            this.detector.repaint();
         }
      }
   }

   private class CapsLockAction extends AbstractAction {
      private CapsLockAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         boolean var2 = false;

         try {
            var2 = Toolkit.getDefaultToolkit().getLockingKeyState(20);
         } catch (UnsupportedOperationException var4) {
            var2 = false;
         }

         CapsLockWarning.this.capsLockOn(var2);
      }
   }
}
