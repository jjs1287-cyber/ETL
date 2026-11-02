package org.etlforge.ui.widget.overlay;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.MouseInputAdapter;
import org.etlforge.ui.DimensionUtils;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.AbstractWaitService;
import org.etlforge.ui.wait.WaitIndicator;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.WidgetUtils;
import org.etlforge.ui.widget.window.MouseResizeHotSpot;

public final class KongaLayeredPane extends JLayeredPane implements OverlayContainer, WaitService {
   private static final int GAP = 5;
   private Component base;
   private final JComponent glassPane = new KongaLayeredPane.GlassPane(false);
   private final Map<JComponent, KongaLayeredPane.OverlayData> overlays = Maps.newHashMap();
   private final KongaLayeredPane.TransientOverlays transientOverlays = new KongaLayeredPane.TransientOverlays();
   private final List<OverlayContainerListener> listeners = new CopyOnWriteArrayList<>();
   private final KongaLayeredPane.WaitLayer waitService = new KongaLayeredPane.WaitLayer();
   private Supplier<WaitIndicator> waitIndicatorFactory = new DefaultWaitIndicatorFactory();
   private Insets baseInsets = new Insets(5, 5, 5, 5);

   public KongaLayeredPane() {
      this.addComponentListener(new ComponentAdapter() {
         @Override
         public void componentResized(ComponentEvent var1) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  KongaLayeredPane.this.updateContainerSize();
               }
            });
         }
      });
      this.addAncestorListener(new KongaLayeredPane.CleanupWorker());
   }

   public KongaLayeredPane(Component var1) {
      this();
      this.setBaseComponent(var1);
   }

   public void setBaseComponent(Component var1) {
      if (this.base != null) {
         this.remove(this.base);
      }

      this.base = var1;
      if (var1 != null) {
         this.add(var1, DEFAULT_LAYER);
         this.setPreferredSize(var1.getPreferredSize());
      }
   }

   public Component getBaseComponent() {
      return this.base;
   }

   public void baseComponentsPreferredSizeMayHaveChanged() {
      if (this.base != null) {
         Dimension var1 = this.base.getPreferredSize();
         this.setPreferredSize(var1);
         this.revalidate();
      }
   }

   public void setBaseInsets(Insets var1) {
      this.baseInsets = new Insets(var1.top, var1.left, var1.bottom, var1.right);
   }

   public final UiProvider asUiProvider() {
      return new UiProviderAdapter(this);
   }

   @Override
   public void addOverlay(JComponent var1, OverlayPositioner var2) {
      this.addOverlayImpl(var1, null, var2);
   }

   private void addOverlayImpl(JComponent var1, Integer var2, OverlayPositioner var3) {
      boolean var4 = isBlocking(var1);
      boolean var5 = var1.isFocusCycleRoot();
      var1.setFocusCycleRoot(var4);
      if (var2 == null) {
         var2 = this.getLayerForNewOverlay();
      }

      KongaLayeredPane.OverlayData var6 = new KongaLayeredPane.OverlayData(var2, var3, var4, var5);
      setInitialOverlaySize(var1);
      this.setOverlayPosition(var1, var3);
      if (var4 && !this.isBlocking()) {
         this.addGlassPane();
      }

      if (isTransient(var1)) {
         this.transientOverlays.add(var1);
      }

      this.overlays.put(var1, var6);
      this.add(var1, var2);
      var1.requestFocusInWindow();
      this.fireOverlayAdded(var1);
      var6.install(var1);
      this.revalidate();
      this.repaint();
   }

   private Integer getLayerForNewOverlay() {
      Integer var1 = MODAL_LAYER;

      for (KongaLayeredPane.OverlayData var3 : this.overlays.values()) {
         if (var3.layer > var1) {
            var1 = var3.layer;
         }
      }

      return var1 + 1;
   }

   public void removeAllOverlays() {
      for (JComponent var3 : Lists.newArrayList(this.overlays.keySet())) {
         this.removeOverlay(var3);
      }
   }

   @Override
   public void removeOverlay(JComponent var1) {
      KongaLayeredPane.OverlayData var2 = this.overlays.remove(var1);
      if (var2 != null) {
         this.remove(var1);
         var2.uninstall(var1);
         var1.setFocusCycleRoot(var2.wasFocusCycleRoot);
         if (var2.blocking && !this.isBlocking()) {
            this.remove(this.glassPane);
         }

         this.transientOverlays.remove(var1);
         this.fireOverlayRemoved(var1);
         this.revalidate();
         this.repaint();
      }
   }

   @Override
   public void addListener(OverlayContainerListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public void removeListener(OverlayContainerListener var1) {
      this.listeners.remove(var1);
   }

   @Override
   public WaitLock startWait() {
      return this.waitService.startWait();
   }

   @Override
   public WaitLock startWait(int var1) {
      return this.waitService.startWait(var1);
   }

   public boolean isInWaitState() {
      return this.waitService.isInWaitState();
   }

   public void setWaitIndicatorFactory(Supplier<WaitIndicator> var1) {
      this.waitIndicatorFactory = var1;
   }

   public Supplier<WaitIndicator> getWaitIndicatorFactory() {
      return this.waitIndicatorFactory;
   }

   private void fireOverlayAdded(JComponent var1) {
      OverlayContainerEvent var2 = new OverlayContainerEvent(this, var1);

      for (OverlayContainerListener var4 : this.listeners) {
         var4.overlayAdded(var2);
      }
   }

   private void fireOverlayRemoved(JComponent var1) {
      OverlayContainerEvent var2 = new OverlayContainerEvent(this, var1);

      for (OverlayContainerListener var4 : this.listeners) {
         var4.overlayRemoved(var2);
      }
   }

   private void updateContainerSize() {
      this.updateOverlayPositions();
      Dimension var1 = this.getSize();
      this.glassPane.setSize(var1);
      this.waitService.setSheetSize(var1);
      if (this.base != null) {
         this.base.setSize(var1);
      }

      UiUtils.revalidate(this);
   }

   private boolean isBlocking() {
      for (KongaLayeredPane.OverlayData var2 : this.overlays.values()) {
         if (var2.blocking) {
            return true;
         }
      }

      return false;
   }

   private static void setInitialOverlaySize(JComponent var0) {
      Dimension var1 = var0.getSize();
      if (DimensionUtils.isEmpty(var1)) {
         var1 = var0.getPreferredSize();
         var0.setSize(var1);
      }
   }

   public void updateOverlayPositions() {
      if (!this.overlays.isEmpty()) {
         for (Entry var2 : this.overlays.entrySet()) {
            Point var3 = ((KongaLayeredPane.OverlayData)var2.getValue()).position;
            if (var3 != null) {
               ((JComponent)var2.getKey()).setLocation(var3);
            } else {
               this.setOverlayPosition((JComponent)var2.getKey(), ((KongaLayeredPane.OverlayData)var2.getValue()).positioner);
            }
         }

         this.revalidate();
         this.repaint();
      }
   }

   private void setOverlayPosition(JComponent var1, OverlayPositioner var2) {
      if (this.shouldCheckSize(var1) && !this.hasBeenResizedByUser(var1)) {
         Dimension var3 = var1.getSize();
         Dimension var4 = var1.getPreferredSize();
         if (var4.width > var3.width) {
            var3.width = var4.width;
         }

         if (var4.height > var3.height) {
            var3.height = var4.height;
         }

         var1.setSize(var3);
      }

      Point var5 = var2.getPosition(this, var1);
      var1.setLocation(var5);
      if (this.shouldCheckSize(var1)) {
         this.checkOverlaySize(var1);
      }

      if (isFillHorizontally(var1)) {
         Dimension var6 = var1.getSize();
         var1.setSize(this.getWidth() - this.baseInsets.left - this.baseInsets.right, var6.height);
         var1.setLocation(this.baseInsets.left, var1.getY());
      }

      if (isFillVertically(var1)) {
         Dimension var7 = var1.getSize();
         var1.setSize(var7.width, this.getHeight() - this.baseInsets.top - this.baseInsets.bottom);
         var1.setLocation(var1.getX(), this.baseInsets.top);
      }
   }

   private boolean shouldCheckSize(JComponent var1) {
      boolean var2 = var1.getClientProperty("konga.overlay.fixedSize") == Boolean.TRUE;
      if (var2) {
         return false;
      }

      boolean var3 = var1.getClientProperty("konga.overlay.animating") == Boolean.TRUE;
      return !var3;
   }

   private boolean hasBeenResizedByUser(JComponent var1) {
      KongaLayeredPane.OverlayData var2 = this.overlays.get(var1);
      return var2 != null && var2.hasBeenResizedByUser();
   }

   private void checkOverlaySize(JComponent var1) {
      int var2 = Math.max(var1.getX() + var1.getWidth() - this.getWidth(), 0);
      int var3 = Math.max(var1.getY() + var1.getHeight() - this.getHeight(), 0);
      if (var2 > 0 || var3 > 0) {
         Dimension var4 = var1.getSize();
         var4.width -= var2;
         var4.height -= var3;
         var1.setSize(var4);
      }
   }

   private void addGlassPane() {
      this.add(this.glassPane, PALETTE_LAYER);
   }

   private static boolean isBlocking(JComponent var0) {
      return isPropertyTrue(var0, "konga.overlay.blocking");
   }

   private static boolean isTransient(JComponent var0) {
      return isPropertyTrue(var0, "konga.overlay.transient");
   }

   private static boolean isFillHorizontally(JComponent var0) {
      return isPropertyTrue(var0, "konga.overlay.fillHorizontally");
   }

   private static boolean isFillVertically(JComponent var0) {
      return isPropertyTrue(var0, "konga.overlay.fillVertically");
   }

   private static boolean isPropertyTrue(JComponent var0, String var1) {
      return var0.getClientProperty(var1) == Boolean.TRUE;
   }

   public void setWaitServiceVerbose(boolean var1) {
      this.waitService.setVerbose(var1);
   }

   private class CleanupWorker implements AncestorListener {
      private CleanupWorker() {
      }

      @Override
      public void ancestorRemoved(AncestorEvent var1) {
         if (!KongaLayeredPane.this.isDisplayable()) {
            KongaLayeredPane.this.transientOverlays.uninstallListener();
         }
      }

      @Override
      public void ancestorAdded(AncestorEvent var1) {
      }

      @Override
      public void ancestorMoved(AncestorEvent var1) {
      }
   }

   private static class GlassPane extends JComponent {
      private final Color color = org.etlforge.ui.UIManager2.getColor("konga.overlay.scrim", new Color(1.0F, 1.0F, 1.0F, 0.65F));

      public GlassPane(boolean var1) {
         WidgetUtils.configureGlassPane(this, var1);
      }

      @Override
      protected void paintComponent(Graphics var1) {
         Color var2 = var1.getColor();
         Rectangle var3 = var1.getClipBounds();
         var1.setColor(this.color);
         var1.fillRect(var3.x, var3.y, var3.width, var3.height);
         var1.setColor(var2);
      }
   }

   private static class OverlayData {
      public final Integer layer;
      public final OverlayPositioner positioner;
      public final boolean blocking;
      public final boolean wasFocusCycleRoot;
      public Point position;
      private KongaLayeredPane.OverlayData.MouseHandler mouseHandler;
      private boolean hasBeenResizedByUser;

      public OverlayData(Integer var1, OverlayPositioner var2, boolean var3, boolean var4) {
         this.layer = var1;
         this.positioner = var2;
         this.blocking = var3;
         this.wasFocusCycleRoot = var4;
      }

      public void install(JComponent var1) {
         boolean var2 = var1.getClientProperty("konga.overlay.fixedPosition") == Boolean.TRUE;
         boolean var3 = var1.getClientProperty("konga.overlay.resizable") == Boolean.TRUE;
         if (!var2 || var3) {
            this.mouseHandler = new KongaLayeredPane.OverlayData.MouseHandler(var3);
            var1.addMouseListener(this.mouseHandler);
            var1.addMouseMotionListener(this.mouseHandler);
         }
      }

      public void uninstall(JComponent var1) {
         var1.removeMouseListener(this.mouseHandler);
         var1.removeMouseMotionListener(this.mouseHandler);
      }

      public boolean hasBeenResizedByUser() {
         return this.hasBeenResizedByUser;
      }

      private class MouseHandler extends MouseInputAdapter {
         private Cursor savedCursor;
         private Point lastPoint;
         private boolean dragging;
         private boolean inResizeZone;
         private boolean resizing;
         private final boolean resizeSupported;

         public MouseHandler(boolean var2) {
            this.resizeSupported = var2;
         }

         @Override
         public void mouseMoved(MouseEvent var1) {
            if (!this.dragging) {
               if (this.enteredResizeZone(var1)) {
                  this.inResizeZone = true;
                  this.savedCursor = var1.getComponent().getCursor();
                  var1.getComponent().setCursor(Cursor.getPredefinedCursor(5));
               } else if (this.inResizeZone) {
                  this.inResizeZone = false;
                  var1.getComponent().setCursor(this.savedCursor);
               }
            }
         }

         private boolean enteredResizeZone(MouseEvent var1) {
            return this.resizeSupported && !this.inResizeZone && MouseResizeHotSpot.SOUTHEAST_CORNER.isActive(var1.getComponent(), var1);
         }

         @Override
         public void mouseDragged(MouseEvent var1) {
            this.dragging = true;
            Point var2 = this.getPointOnScreen(var1);
            int var3 = var2.x - this.lastPoint.x;
            int var4 = var2.y - this.lastPoint.y;
            this.lastPoint = var2;
            if (this.resizing) {
               this.resize(var1, var3, var4);
            } else {
               this.move(var1, var3, var4);
            }
         }

         private void resize(MouseEvent var1, int var2, int var3) {
            Dimension var4 = var1.getComponent().getSize();
            var4.width += var2;
            var4.height += var3;
            var1.getComponent().setSize(var4);
            OverlayData.this.hasBeenResizedByUser = true;
         }

         private void move(MouseEvent var1, int var2, int var3) {
            OverlayData.this.position = var1.getComponent().getLocation();
            OverlayData.this.position.x += var2;
            OverlayData.this.position.y += var3;
            var1.getComponent().setLocation(OverlayData.this.position);
         }

         @Override
         public void mousePressed(MouseEvent var1) {
            this.lastPoint = this.getPointOnScreen(var1);
            if (this.inResizeZone) {
               this.resizing = true;
            } else {
               this.savedCursor = var1.getComponent().getCursor();
               var1.getComponent().setCursor(Cursor.getPredefinedCursor(13));
            }
         }

         @Override
         public void mouseReleased(MouseEvent var1) {
            if (this.resizing) {
               JComponent var2 = (JComponent)var1.getComponent().getParent();
               var2.revalidate();
               var2.repaint();
            }

            this.resizing = false;
            this.dragging = false;
            var1.getComponent().setCursor(this.savedCursor);
            this.savedCursor = null;
         }

         private Point getPointOnScreen(MouseEvent var1) {
            Point var2 = var1.getPoint();
            SwingUtilities.convertPointToScreen(var2, var1.getComponent());
            return var2;
         }
      }
   }

   private class TransientOverlays implements AWTEventListener {
      private final List<JComponent> overlays = Lists.newArrayList();

      private TransientOverlays() {
      }

      public void add(JComponent var1) {
         if (this.overlays.isEmpty()) {
            this.installListener();
         }

         this.overlays.add(var1);
      }

      public void remove(JComponent var1) {
         this.overlays.remove(var1);
         if (this.overlays.isEmpty()) {
            this.uninstallListener();
         }
      }

      private void installListener() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Toolkit.getDefaultToolkit().addAWTEventListener(TransientOverlays.this, 16L);
            }
         });
      }

      public void uninstallListener() {
         Toolkit.getDefaultToolkit().removeAWTEventListener(this);
      }

      @Override
      public void eventDispatched(AWTEvent var1) {
         if (var1.getID() == 500) {
            Component var2 = ((MouseEvent)var1).getComponent();
            Iterator var3 = this.overlays.iterator();

            while (var3.hasNext()) {
               JComponent var4 = (JComponent)var3.next();
               if (!SwingUtilities.isDescendingFrom(var2, var4)) {
                  var3.remove();
                  KongaLayeredPane.this.removeOverlay(var4);
               }
            }

            if (this.overlays.isEmpty()) {
               this.uninstallListener();
            }
         }
      }
   }

   private class WaitLayer extends AbstractWaitService {
      private final JComponent sheet = new KongaLayeredPane.GlassPane(true);
      private WaitIndicator waitIndicator;

      public WaitLayer() {
      }

      @Override
      protected void goToWaitingState() {
         this.report("I've been asked to go into waiting state");
         if (this.waitIndicator != null) {
            this.report("Hey, I'm already waiting!!!");
         }

         this.sheet.setSize(KongaLayeredPane.this.getSize());
         this.waitIndicator = KongaLayeredPane.this.waitIndicatorFactory.get();
         KongaLayeredPane.this.add(this.sheet, Integer.valueOf(1000));
         KongaLayeredPane.this.addOverlayImpl(this.waitIndicator.getUi(), 1001, new OverlayCenterPositioner());
         this.waitIndicator.start();
         KongaLayeredPane.this.revalidate();
         KongaLayeredPane.this.repaint();
      }

      @Override
      protected void stopWaiting() {
         this.report("I've been asked to leave waiting state");
         KongaLayeredPane.this.remove(this.sheet);
         if (this.waitIndicator != null) {
            KongaLayeredPane.this.removeOverlay(this.waitIndicator.getUi());
            this.waitIndicator.stop();
            this.waitIndicator = null;
         }

         KongaLayeredPane.this.revalidate();
         KongaLayeredPane.this.repaint();
      }

      public void setSheetSize(Dimension var1) {
         this.sheet.setSize(var1);
      }
   }
}
