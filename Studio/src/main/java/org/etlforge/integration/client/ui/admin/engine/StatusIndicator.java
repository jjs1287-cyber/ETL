package org.etlforge.integration.client.ui.admin.engine;

import com.google.common.collect.Maps;
import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.integration.client.admin.ServerStatus;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.DialogLayout;

final class StatusIndicator implements UiProvider {
   private static final Icon OFF_ICON = StatusLightIcons.NONE;
   private static final Icon GREEN = StatusLightIcons.GREEN;
   private static final Icon RED = StatusLightIcons.RED;
   private static final Icon YELLOW = StatusLightIcons.YELLOW;
   private static final List<ServerStatus> SUPPORTED_STATII = new ArrayList<>();
   private static final Map<ServerStatus, Icon> STATUS_ICONS = Maps.newHashMap();
   private JPanel panel = new JPanel(new DialogLayout(5, 5));
   private Map<ServerStatus, StatusIndicator.StatusLight> lights;

   public StatusIndicator(EnumSet<ServerStatus> var1) {
      this.panel.setAlignmentX(0.0F);
      this.createLights();
      this.setStatus(var1);
   }

   public void setStatus(EnumSet<ServerStatus> var1) {
      for (StatusIndicator.StatusLight var3 : this.lights.values()) {
         var3.turnOff();
      }

      for (ServerStatus var5 : var1) {
         this.lights.get(var5).turnOn();
      }

      this.panel.repaint();
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.panel);
   }

   @Override
   public void requestFocus() {
   }

   private void createLights() {
      this.lights = Maps.newHashMap();

      for (ServerStatus var2 : SUPPORTED_STATII) {
         StatusIndicator.StatusLight var3 = new StatusIndicator.StatusLight(STATUS_ICONS.get(var2));
         this.panel.add(new JLabel(var3));
         JLabel var4 = new JLabel(var2.toString());
         var4.setToolTipText(var2.getDescription());
         this.panel.add(var4);
         this.lights.put(var2, var3);
      }
   }

   static {
      SUPPORTED_STATII.add(ServerStatus.RUNNING);
      SUPPORTED_STATII.add(ServerStatus.STARTING);
      SUPPORTED_STATII.add(ServerStatus.STOPPING);
      SUPPORTED_STATII.add(ServerStatus.STOPPED);
      SUPPORTED_STATII.add(ServerStatus.ERROR);
      SUPPORTED_STATII.add(ServerStatus.SINGLE_THREAD);
      SUPPORTED_STATII.add(ServerStatus.DISABLED);
      SUPPORTED_STATII.add(ServerStatus.UNDEFINED);
      SUPPORTED_STATII.add(ServerStatus.UNREACHABLE);
      Collections.sort(SUPPORTED_STATII);
      STATUS_ICONS.put(ServerStatus.RUNNING, GREEN);
      STATUS_ICONS.put(ServerStatus.STARTING, YELLOW);
      STATUS_ICONS.put(ServerStatus.STOPPING, YELLOW);
      STATUS_ICONS.put(ServerStatus.STOPPED, RED);
      STATUS_ICONS.put(ServerStatus.ERROR, RED);
      STATUS_ICONS.put(ServerStatus.SINGLE_THREAD, GREEN);
      STATUS_ICONS.put(ServerStatus.DISABLED, RED);
      STATUS_ICONS.put(ServerStatus.UNDEFINED, RED);
      STATUS_ICONS.put(ServerStatus.UNREACHABLE, RED);
   }

   private static class StatusLight implements Icon {
      private boolean isOn = false;
      private Icon onIcon;

      public StatusLight(Icon var1) {
         this.onIcon = var1;
      }

      public void turnOn() {
         this.isOn = true;
      }

      public void turnOff() {
         this.isOn = false;
      }

      @Override
      public int getIconHeight() {
         return this.isOn ? this.onIcon.getIconHeight() : StatusIndicator.OFF_ICON.getIconHeight();
      }

      @Override
      public int getIconWidth() {
         return this.isOn ? this.onIcon.getIconWidth() : StatusIndicator.OFF_ICON.getIconWidth();
      }

      @Override
      public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
         if (this.isOn) {
            this.onIcon.paintIcon(var1, var2, var3, var4);
         } else {
            StatusIndicator.OFF_ICON.paintIcon(var1, var2, var3, var4);
         }
      }
   }
}
