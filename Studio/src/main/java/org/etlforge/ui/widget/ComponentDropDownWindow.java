package org.etlforge.ui.widget;

import com.google.common.base.Preconditions;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.window.WindowUtils;

public class ComponentDropDownWindow {
   private final Component owner;
   private final UiProvider content;
   private boolean trackWidthOfOwner;

   public ComponentDropDownWindow(Component var1, UiProvider var2) {
      this.owner = Preconditions.checkNotNull(var1);
      this.content = Preconditions.checkNotNull(var2);
   }

   public final void setTrackWidthOfOwner(boolean var1) {
      this.trackWidthOfOwner = var1;
   }

   public final JWindow buildWindow() {
      Window var1 = SwingUtilities.getWindowAncestor(this.owner);
      final JWindow var2 = new JWindow(var1);
      this.content.decorate(new UiCanvas() {
         @Override
         public void display(Component var1) {
            var2.add(var1);
         }
      });
      var2.addWindowListener(new WindowAdapter() {
         @Override
         public void windowOpened(WindowEvent var1) {
            ComponentDropDownWindow.this.onWindowOpened();
         }
      });
      var2.setFocusableWindowState(false);
      var2.pack();
      this.setWindowBounds(var2);
      return var2;
   }

   protected void onWindowOpened() {
   }

   private void setWindowBounds(JWindow var1) {
      if (this.trackWidthOfOwner) {
         this.setTrackTriggerWidthBounds(var1);
      } else {
         this.setFreeBounds(var1);
      }

      WindowUtils.shrinkWindowIfNeededToFitScreen(var1);
   }

   private void setTrackTriggerWidthBounds(JWindow var1) {
      Point var2 = new Point(0, this.owner.getHeight() + 2);
      SwingUtilities.convertPointToScreen(var2, this.owner);
      var1.setLocation(var2);
      Dimension var3 = var1.getSize();
      if (var3.width < this.owner.getWidth()) {
         var3.width = this.owner.getWidth();
         var1.setSize(var3);
      }
   }

   private void setFreeBounds(JWindow var1) {
      Point var2 = this.getFreeBoundsLocation();
      SwingUtilities.convertPointToScreen(var2, this.owner);
      var1.setLocation(var2);
      WindowUtils.ensureWindowCompletelyVisibleOnScreen(var1);
   }

   protected Point getFreeBoundsLocation() {
      return new Point(0, this.owner.getHeight() + 2);
   }
}
