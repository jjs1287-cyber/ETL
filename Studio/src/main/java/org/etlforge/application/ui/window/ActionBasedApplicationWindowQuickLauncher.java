package org.etlforge.application.ui.window;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;

final class ActionBasedApplicationWindowQuickLauncher implements QuickLauncher {
   private static final Border normalIconButtonBorder = Empty.border(1);
   private static final Border rollOverIconButtonBorder = new LineBorder(Color.LIGHT_GRAY);
   private final JButton button;

   public ActionBasedApplicationWindowQuickLauncher(Action var1) {
      this.button = new KongaButton(var1).setUseMediumActionIcon(true);
      this.button.setText(null);
      this.button.setContentAreaFilled(false);
      this.button.setBorder(normalIconButtonBorder);
      this.button.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseEntered(MouseEvent var1) {
            ActionBasedApplicationWindowQuickLauncher.this.button.setBorder(ActionBasedApplicationWindowQuickLauncher.rollOverIconButtonBorder);
         }

         @Override
         public void mouseExited(MouseEvent var1) {
            ActionBasedApplicationWindowQuickLauncher.this.button.setBorder(ActionBasedApplicationWindowQuickLauncher.normalIconButtonBorder);
         }
      });
      this.button.setVisible(false);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.button);
   }

   @Override
   public void requestFocus() {
      this.button.requestFocusInWindow();
   }

   @Override
   public void publish() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            ActionBasedApplicationWindowQuickLauncher.this.button.setVisible(true);
            UiUtils.revalidate(ActionBasedApplicationWindowQuickLauncher.this.button);
         }
      });
   }

   @Override
   public void hide() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            ActionBasedApplicationWindowQuickLauncher.this.button.setVisible(false);
            UiUtils.revalidate(ActionBasedApplicationWindowQuickLauncher.this.button);
         }
      });
   }
}
