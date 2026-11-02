package org.etlforge.ui.dialog;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.ui.widget.window.WindowUtils;
import org.etlforge.util.preference.KongaPreferences;

public final class DialogLocationManager {
   private static final String LOCATION_PREF_KEY = "DialogLocationManager#Location";
   private static final String SIZE_PREF_KEY = "DialogLocationManager#Size";
   private final Map<JDialog, DialogLocationManager.Handler> managedDialogs = Maps.newIdentityHashMap();
   private boolean ensureNotBiggerThanRootWindow = true;

   public void setEnsureNotBiggerThanRootWindow(boolean var1) {
      this.ensureNotBiggerThanRootWindow = var1;
   }

   public void manageDialog(JDialog var1, String var2) {
      Preconditions.checkNotNull(var1, "dlg");
      this.sanityCheck();
      if (!this.managedDialogs.containsKey(var1)) {
         String var3 = var1.getClass().getName();
         if (StringUtils.isNotEmpty(var2)) {
            var3 = var3 + var2;
         }

         KongaPreferences var4 = KongaPreferences.getPreferences(var1.getClass(), var2);
         DialogLocationManager.Handler var5 = new DialogLocationManager.Handler(var3, var1, var4, this.ensureNotBiggerThanRootWindow);
         this.managedDialogs.put(var1, var5);
      }
   }

   private void sanityCheck() {
      for (Entry var2 : this.managedDialogs.entrySet()) {
         if (!((JDialog)var2.getKey()).isVisible()) {
            System.err.println("Closed dialog still location-managed: " + ((DialogLocationManager.Handler)var2.getValue()).name);
         }
      }
   }

   public void manageDialog(JDialog var1, Class<?> var2) {
      Preconditions.checkNotNull(var1, "dlg");
      Preconditions.checkNotNull(var2, "key");
      this.sanityCheck();
      if (!this.managedDialogs.containsKey(var1)) {
         KongaPreferences var3 = KongaPreferences.getPreferences(var2);
         DialogLocationManager.Handler var4 = new DialogLocationManager.Handler(var2.getName(), var1, var3, this.ensureNotBiggerThanRootWindow);
         this.managedDialogs.put(var1, var4);
      }
   }

   public void setInitialLocation(JDialog var1) {
      DialogLocationManager.Handler var2 = this.managedDialogs.get(var1);
      if (var2 != null) {
         var2.setInitialLocation();
      }
   }

   public void removeDialog(JDialog var1) {
      DialogLocationManager.Handler var2 = this.managedDialogs.remove(var1);
      if (var2 != null) {
         var1.removeComponentListener(var2);
      }
   }

   private static class Handler extends ComponentAdapter {
      public final String name;
      private final JDialog dialog;
      private final KongaPreferences preferences;
      private final boolean ensureNotBiggerThanRootWindow;

      public Handler(String var1, JDialog var2, KongaPreferences var3, boolean var4) {
         this.name = var1;
         this.dialog = var2;
         this.preferences = var3;
         this.ensureNotBiggerThanRootWindow = var4;
         var2.addComponentListener(this);
      }

      @Override
      public void componentMoved(ComponentEvent var1) {
         if (this.preferences != null) {
            this.preferences.putPoint("DialogLocationManager#Location", this.dialog.getLocation());
         }
      }

      @Override
      public void componentResized(ComponentEvent var1) {
         if (this.sizeShouldBeStored()) {
            this.storeNewSize();
         }
      }

      private boolean sizeShouldBeStored() {
         return SystemUtils.IS_OS_WINDOWS && this.dialog.isResizable() && this.preferences != null;
      }

      private void storeNewSize() {
         this.preferences.putDimension("DialogLocationManager#Size", this.dialog.getSize());
      }

      public void setInitialLocation() {
         this.setInitialLocationOnScreen();
         this.setInitialSize();
         WindowUtils.ensureWindowCompletelyVisibleOnScreen(this.dialog);
      }

      private void setInitialLocationOnScreen() {
         Point var1 = this.getStoredLocation();
         if (var1 == null) {
            this.dialog.setLocationRelativeTo(this.dialog.getOwner());
         } else {
            this.dialog.setLocation(var1);
         }
      }

      private void setInitialSize() {
         if (this.dialog.isResizable()) {
            Dimension var1 = this.dialog.getSize();
            Dimension var2 = this.getStoredSize();
            if (var2 != null) {
               int var3 = Math.max(var1.width, var2.width);
               int var4 = Math.max(var1.height, var2.height);
               Dimension var5 = new Dimension(var3, var4);
               if (this.ensureNotBiggerThanRootWindow) {
                  this.ensureNotBiggerThanRootWindow(var5);
               }

               this.dialog.setSize(var5);
            }
         }
      }

      private void ensureNotBiggerThanRootWindow(Dimension var1) {
         JFrame var2 = this.getRootFrame();
         if (var2 != null) {
            WindowUtils.ensureChildWindowNotBiggerThanOwner(var1, var2, 10);
         }
      }

      private JFrame getRootFrame() {
         Window var1 = this.dialog.getOwner();

         while (var1 != null && !(var1 instanceof JFrame)) {
            var1 = var1.getOwner();
         }

         return (JFrame)var1;
      }

      private Point getStoredLocation() {
         return this.preferences == null ? null : this.preferences.getPoint("DialogLocationManager#Location", null);
      }

      private Dimension getStoredSize() {
         return this.preferences == null ? null : this.preferences.getDimension("DialogLocationManager#Size", null);
      }
   }
}
