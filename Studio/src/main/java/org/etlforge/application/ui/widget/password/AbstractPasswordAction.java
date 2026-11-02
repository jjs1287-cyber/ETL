package org.etlforge.application.ui.widget.password;

import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.widget.WidgetUtils;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayPositioner;
import org.etlforge.ui.widget.overlay.OverlayRelativePositioner;

public abstract class AbstractPasswordAction extends AbstractAction {
   private JComponent trigger;
   private PasswordDefinitionUiOverlay overlay;
   private Point overlayOffset;
   private OverlayPositioner positioner;
   private boolean includeReTypePassword;

   public AbstractPasswordAction() {
      this(ApplicationResources.STRINGS.getString("Password..."));
   }

   public AbstractPasswordAction(String var1) {
      super(var1);
      this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(ApplicationResources.STRINGS.getString("Password.Shortcut")));
      this.overlayOffset = new Point(8, 8);
   }

   public final void setIncludeReTypeField(boolean var1) {
      this.includeReTypePassword = var1;
   }

   protected abstract void handlePassword(char[] var1);

   public final void setTrigger(JComponent var1) {
      this.trigger = var1;
   }

   public final void setOverlayOffsetFromTrigger(int var1, int var2) {
      this.overlayOffset = new Point(var1, var2);
   }

   public final void setPositioner(OverlayPositioner var1) {
      this.positioner = var1;
   }

   @Override
   public final void actionPerformed(ActionEvent var1) {
      OverlayContainer var2 = this.trigger != null ? WidgetUtils.getOverlayContainerAncestor(this.trigger) : null;
      if (var2 != null) {
         this.displayInOverlay(var2);
      } else {
         this.displayInDialog();
      }
   }

   public final void closeOverlay() {
      if (this.overlay != null) {
         this.overlay.close();
         this.overlay = null;
      }
   }

   private void displayInOverlay(OverlayContainer var1) {
      this.closeOverlay();
      this.overlay = new PasswordDefinitionUiOverlay(new Receiver<char[]>() {
         public void handle(char[] var1) {
            AbstractPasswordAction.this.handlePassword(var1);
         }
      });
      this.overlay.setIncludeReTypeField(this.includeReTypePassword);
      this.overlay.setFocusReceiverWhenClosed(this.trigger);
      this.overlay.displayIn(var1, this.getPositioner(var1));
   }

   private OverlayPositioner getPositioner(OverlayContainer var1) {
      return this.positioner != null ? this.positioner : new OverlayRelativePositioner(this.trigger, this.overlayOffset);
   }

   private void displayInDialog() {
      char[] var1 = PasswordDialog.definePassword(this.includeReTypePassword);
      if (var1 != null) {
         this.handlePassword(var1);
      }
   }
}
