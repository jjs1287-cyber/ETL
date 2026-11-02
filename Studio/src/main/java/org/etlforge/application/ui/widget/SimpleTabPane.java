package org.etlforge.application.ui.widget;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.DropLocation;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.DimensionUtils;
import org.etlforge.ui.Empty;
import org.etlforge.ui.GraphicsState;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.event.ChangeListeners;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.CardPanel;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.UiUtils;

public class SimpleTabPane implements UiProvider {
   public static final boolean SUPPORTED = isSupported();
   private static final int TRIANGLE_WIDTH = 10;
   private static final Color ACTIVE = Colors.LIGHT_BLUE_BACKGROUND;
   private static final Color INACTIVE = new Color(100, 145, 173);
   private final BoxBuilder tabRow;
   private final CardPanel contentPane;
   private final Map<SimpleTabPane.TabLabel, String> cardNames;
   private int cardCounter;
   private final List<SimpleTabPane.TabLabel> tabs;
   private final List<Object> tabObjects;
   private final List<JComponent> tabContents;
   private int tabDistance;
   private TextStyle activeTabStyle;
   private TextStyle inactiveTabStyle;
   private int selectedIndex;
   private final JComponent ui;
   private final SimpleTabPane.TabInputHandler tabInputHandler;
   private int hoverDelay;
   private SimpleTabPane.Hoverer hoverer;
   private final ChangeListeners changeListeners;
   private boolean dndEnabled;
   private int dragIndex = -1;
   private int dropIndex = -1;
   private SimpleTabPane.DndCallback dndCallback;
   private boolean blockChangeEvents;
   private final SimpleTabPane.Nabokov nabokov;

   private static boolean isSupported() {
      return ApplicationConfiguration.getBoolean("SimpleTabPane.Supported", false);
   }

   public SimpleTabPane() {
      this.tabRow = BoxBuilder.horizontal(new SimpleTabPane.TabRow()).withBorder(Empty.border(9, 5, 0, 5));
      this.contentPane = new CardPanel();
      this.nabokov = new SimpleTabPane.Nabokov();
      this.contentPane.add(this.nabokov.layout(), "nabokov");
      this.cardNames = Maps.newIdentityHashMap();
      this.tabs = Lists.newArrayList();
      this.tabObjects = Lists.newArrayList();
      this.tabContents = Lists.newArrayList();
      this.tabDistance = 8;
      TextStyle var1 = TextStyles.DefaultBoldLarger;
      this.activeTabStyle = var1;
      this.inactiveTabStyle = var1;
      this.selectedIndex = -1;
      this.hoverDelay = -1;
      this.tabInputHandler = new SimpleTabPane.TabInputHandler();
      this.setBackground(Colors.ETLFORGE_BLUE);
      this.ui = new JPanel(new BorderLayout());
      this.changeListeners = new ChangeListeners();
   }

   public final void setBackground(Color var1) {
      this.tabRow.withBackground(var1);
      this.contentPane.setBackground(var1);
      this.nabokov.setForeground(var1);
   }

   public final void setTabDistance(int var1) {
      this.tabDistance = var1;
   }

   public final void setBorder(Border var1) {
      this.ui.setBorder(var1);
   }

   public final void setActiveTabStyle(TextStyle var1) {
      this.activeTabStyle = var1;
   }

   public final void setInactiveTabStyle(TextStyle var1) {
      this.inactiveTabStyle = var1;
   }

   public final void setHoverDelay(int var1) {
      this.cancelHoverer();
      this.hoverDelay = var1;
   }

   public int getHoverDelay() {
      return this.hoverDelay;
   }

   private void cancelHoverer() {
      if (this.hoverer != null) {
         this.hoverer.cancel();
         this.hoverer = null;
      }
   }

   public final void addTab(String var1, Icon var2, JComponent var3) {
      this.tabObjects.add(var3);
      this.addTabImpl(var1, var2, var3);
   }

   private void addTabImpl(String var1, Icon var2, JComponent var3) {
      boolean var4 = this.getTabCount() == 0;
      this.cancelHoverer();
      SimpleTabPane.TabLabel var5 = new SimpleTabPane.TabLabel(var1, var2);
      this.addTab(var5);
      this.addContent(var5, var3);
      if (var4) {
         this.layoutUi();
      }

      this.refreshUi();
      this.fireStateChanged();
   }

   private void refreshUi() {
      UiUtils.revalidate(this.ui);
   }

   private void layoutUi() {
      this.ui.removeAll();
      if (!this.tabs.isEmpty()) {
         this.ui.add(this.tabRow.container(), "North");
      }

      this.ui.add(this.contentPane.container(), "Center");
   }

   private void addTab(SimpleTabPane.TabLabel var1) {
      if (this.dndEnabled) {
         this.installDragSupport(var1);
      }

      this.selectedIndex = this.tabs.size();
      this.tabs.add(var1);
      String var2 = String.valueOf(this.cardCounter);
      this.cardNames.put(var1, var2);
      this.cardCounter++;
      var1.addMouseListener(this.tabInputHandler);
      this.rebuildTabRow();
   }

   private void rebuildTabRow() {
      this.decorateTabs();
      this.tabRow.removeAll();
      this.tabRow.addAllWithSpace(this.tabDistance, this.tabs);
   }

   private void decorateTabs() {
      int var1 = 0;

      for (int var2 = 0; var2 < this.tabs.size(); var2++) {
         boolean var3 = this.selectedIndex == var2;
         SimpleTabPane.TabLabel var4 = this.tabs.get(var2);
         var4.setActive(var3);
         TextStyle var5 = var3 ? this.activeTabStyle : this.inactiveTabStyle;
         var5.makeOver(var4);
         Dimension var6 = var4.getUnadjustedPreferredSize();
         if (var6.height > var1) {
            var1 = var6.height;
         }
      }

      for (SimpleTabPane.TabLabel var8 : this.tabs) {
         var8.minHeight = var1;
      }
   }

   private void addContent(SimpleTabPane.TabLabel var1, JComponent var2) {
      this.tabContents.add(var2);
      this.displayContent(var1, var2);
   }

   private void displayContent(SimpleTabPane.TabLabel var1, JComponent var2) {
      String var3 = this.cardNames.get(var1);
      if (!this.contentPane.contains(var3)) {
         this.contentPane.add(var2, var3);
      }

      this.contentPane.show(var3);
   }

   public final void addTab(String var1, Icon var2, UiProvider var3) {
      this.tabObjects.add(var3);
      JPanel var4 = BorderLayoutBuilder.fillWith(var3);
      this.addTabImpl(var1, var2, var4);
   }

   @Override
   public final void decorate(UiCanvas var1) {
      this.layoutUi();
      var1.display(this.ui);
   }

   @Override
   public final void requestFocus() {
      if (!this.tabs.isEmpty() && this.selectedIndex != -1) {
         Object var1 = this.tabObjects.get(this.selectedIndex);
         if (var1 instanceof JComponent) {
            ((JComponent)var1).requestFocusInWindow();
         } else if (var1 instanceof UiProvider) {
            ((UiProvider)var1).requestFocus();
         }
      }
   }

   public final void setSelectedIndex(int var1) {
      if (var1 != this.selectedIndex) {
         this.forceSelectedIndex(var1);
      }
   }

   private void forceSelectedIndex(int var1) {
      this.selectedIndex = var1;
      this.cancelHoverer();
      JComponent var2 = this.tabContents.get(var1);
      SimpleTabPane.TabLabel var3 = this.tabs.get(var1);
      this.displayContent(var3, var2);
      this.rebuildTabRow();
      this.refreshUi();
      this.fireStateChanged();
   }

   private void fireStateChanged() {
      if (!this.blockChangeEvents) {
         ChangeEvent var1 = new ChangeEvent(this);
         this.changeListeners.call().stateChanged(var1);
      }
   }

   public final int getSelectedIndex() {
      return this.selectedIndex;
   }

   public final JLabel getTabComponentAt(int var1) {
      return this.tabs.get(var1);
   }

   public final int getTabCount() {
      return this.tabs.size();
   }

   public final void setTitleAt(int var1, String var2) {
      SimpleTabPane.TabLabel var3 = this.tabs.get(var1);
      var3.setText(var2);
   }

   public final Object getTabObjectAt(int var1) {
      return this.tabObjects.get(var1);
   }

   public final JComponent getTabContentAt(int var1) {
      return this.tabContents.get(var1);
   }

   public final void removeTabAt(int var1) {
      this.cancelHoverer();
      SimpleTabPane.TabLabel var2 = this.tabs.remove(var1);
      this.cardNames.remove(var2);
      this.tabObjects.remove(var1);
      this.tabContents.remove(var1);
      if (this.tabs.isEmpty()) {
         this.selectedIndex = -1;
         this.contentPane.removeAllBut("nabokov");
      } else if (var1 <= this.selectedIndex) {
         this.selectedIndex = Math.max(0, this.selectedIndex - 1);
         JComponent var3 = this.tabContents.get(this.selectedIndex);
         SimpleTabPane.TabLabel var4 = this.tabs.get(this.selectedIndex);
         this.displayContent(var4, var3);
      }

      this.rebuildTabRow();
      if (this.getTabCount() == 0) {
         this.layoutUi();
      }

      this.refreshUi();
      this.fireStateChanged();
   }

   public void installKeyBinding(Action var1) {
      this.installKeyBinding(var1, 1);
   }

   public void installKeyBinding(Action var1, int var2) {
      ActionKeyBinding.install(var1, this.ui, var2);
   }

   public final void addChangeListener(ChangeListener var1) {
      this.changeListeners.add(var1);
   }

   public final void removeChangeListener(ChangeListener var1) {
      this.changeListeners.remove(var1);
   }

   protected void handleLeftClick(MouseEvent var1, int var2) {
      this.setSelectedIndex(var2);
   }

   protected void handleRightClick(int var1) {
   }

   protected void handleHover(int var1) {
   }

   public void enableTabReordering(SimpleTabPane.DndCallback var1) {
      if (!this.dndEnabled) {
         this.dndCallback = var1 == null ? SimpleTabPane.DndCallback.NONE : var1;
         this.dndEnabled = true;

         for (SimpleTabPane.TabLabel var3 : this.tabs) {
            this.installDragSupport(var3);
         }

         this.tabRow.container().setTransferHandler(new SimpleTabPane.TawRowTransferHandler());
      }
   }

   private void installDragSupport(final SimpleTabPane.TabLabel var1) {
      var1.setTransferHandler(new SimpleTabPane.TabTransferHandler(var1));
      DragSource var2 = new DragSource();
      var2.createDefaultDragGestureRecognizer(var1, 2, new DragGestureListener() {
         @Override
         public void dragGestureRecognized(DragGestureEvent var1x) {
            var1.getTransferHandler().exportAsDrag(var1, var1x.getTriggerEvent(), 2);
         }
      });
   }

   private void setDropIndex(int var1) {
      this.dropIndex = var1;
      this.tabRow.container().repaint();
   }

   public interface DndCallback {
      SimpleTabPane.DndCallback NONE = new SimpleTabPane.DndCallback() {
         @Override
         public void tabWasMoved(int var1, int var2) {
         }
      };

      void tabWasMoved(int var1, int var2);
   }

   private class DropHandler {
      private DropHandler() {
      }

      public boolean doImport(TransferSupport var1) {
         if (this.isNewTabOrder()) {
            if (SimpleTabPane.this.dragIndex < SimpleTabPane.this.dropIndex) {
               --SimpleTabPane.this.dropIndex;
               assert SimpleTabPane.this.dragIndex != SimpleTabPane.this.dropIndex;
            }

            SimpleTabPane.this.blockChangeEvents = true;
            SimpleTabPane.TabLabel var2 = SimpleTabPane.this.tabs.get(SimpleTabPane.this.dragIndex);
            String var3 = SimpleTabPane.this.cardNames.get(var2);
            Object var4 = SimpleTabPane.this.tabObjects.get(SimpleTabPane.this.dragIndex);
            JComponent var5 = SimpleTabPane.this.tabContents.get(SimpleTabPane.this.dragIndex);
            SimpleTabPane.this.removeTabAt(SimpleTabPane.this.dragIndex);
            SimpleTabPane.this.tabs.add(SimpleTabPane.this.dropIndex, var2);
            SimpleTabPane.this.cardNames.put(var2, var3);
            SimpleTabPane.this.tabObjects.add(SimpleTabPane.this.dropIndex, var4);
            SimpleTabPane.this.tabContents.add(SimpleTabPane.this.dropIndex, var5);
            SimpleTabPane.this.rebuildTabRow();
            SimpleTabPane.this.refreshUi();
            SimpleTabPane.this.forceSelectedIndex(SimpleTabPane.this.dropIndex);
            SimpleTabPane.this.blockChangeEvents = false;
            SimpleTabPane.this.fireStateChanged();
            SimpleTabPane.this.dndCallback.tabWasMoved(SimpleTabPane.this.dragIndex, SimpleTabPane.this.dropIndex);
         }

         return true;
      }

      private boolean isNewTabOrder() {
         if (SimpleTabPane.this.dropIndex == 0) {
            return SimpleTabPane.this.dragIndex > 0;
         } else {
            int var1 = SimpleTabPane.this.tabs.size() - 1;
            if (SimpleTabPane.this.dropIndex == SimpleTabPane.this.tabs.size()) {
               return SimpleTabPane.this.dragIndex < var1;
            } else {
               return SimpleTabPane.this.dragIndex == var1
                  ? SimpleTabPane.this.dropIndex < SimpleTabPane.this.dragIndex
                  : Math.abs(SimpleTabPane.this.dragIndex - SimpleTabPane.this.dropIndex) >= 2;
            }
         }
      }
   }

   private class Hoverer implements Runnable {
      private final int index;
      private DelayedJob job;

      public Hoverer(int var2, int var3) {
         this.index = var2;
         this.job = new DelayedJob(var3, this);
         this.job.schedule();
      }

      public void cancel() {
         this.job.cancel();
         this.job = null;
      }

      @Override
      public void run() {
         SimpleTabPane.this.handleHover(this.index);
         SimpleTabPane.this.hoverer = null;
         this.job = null;
      }
   }

   private static class Nabokov {
      private final JLabel[] labels = new JLabel[]{
         new JLabel("Who rides so late in the night and the wind?"),
         new JLabel("It is the writer's grief. It is the wild"),
         new JLabel("March wind. It is the father with his child.")
      };

      public Nabokov() {
         Font var1 = this.labels[0].getFont();
         Font var2 = var1.deriveFont(var1.getSize() + 1.0F);

         for (JLabel var6 : this.labels) {
            var6.setFont(var2);
            var6.setHorizontalAlignment(11);
         }
      }

      public JComponent layout() {
         OneColumnPanel var1 = new OneColumnPanel();
         var1.withInsets(0, 0, 2, 0).setWeightX(1.0).setFill(GridBagLayoutPanel.Fill.HORIZONTAL);
         var1.addAll(this.labels);
         BorderLayoutBuilder var2 = new BorderLayoutBuilder();
         var2.center(" ").south(var1).deeplyNonOpaque();
         return var2.container();
      }

      public void setForeground(Color var1) {
         Color var2 = new Color(darker(var1.getRed()), darker(var1.getGreen()), darker(var1.getBlue()), var1.getAlpha());

         for (JLabel var6 : this.labels) {
            var6.setForeground(var2);
         }
      }

      private static int darker(int var0) {
         return Math.max(0, var0 - 1);
      }
   }

   private class TabInputHandler extends MouseAdapter {
      private TabInputHandler() {
      }

      @Override
      public void mousePressed(MouseEvent var1) {
         SimpleTabPane.this.cancelHoverer();
         if (var1.getClickCount() == 1) {
            if (var1.isPopupTrigger()) {
               this.handlePopupTrigger(var1);
            } else if (var1.getButton() == 1) {
               int var2 = this.indexForEvent(var1);
               SimpleTabPane.this.handleLeftClick(var1, var2);
            }
         }
      }

      private int indexForEvent(MouseEvent var1) {
         Component var2 = var1.getComponent();
         return SimpleTabPane.this.tabs.indexOf(var2);
      }

      @Override
      public void mouseReleased(MouseEvent var1) {
         SimpleTabPane.this.cancelHoverer();
         if (var1.isPopupTrigger()) {
            this.handlePopupTrigger(var1);
         }
      }

      private void handlePopupTrigger(MouseEvent var1) {
         int var2 = this.indexForEvent(var1);
         SimpleTabPane.this.handleRightClick(var2);
      }

      @Override
      public void mouseEntered(MouseEvent var1) {
         SimpleTabPane.this.cancelHoverer();
         int var2 = SimpleTabPane.this.getHoverDelay();
         if (var2 >= 0) {
            int var3 = this.indexForEvent(var1);
            SimpleTabPane.this.hoverer = SimpleTabPane.this.new Hoverer(var3, var2);
         }
      }

      @Override
      public void mouseExited(MouseEvent var1) {
         SimpleTabPane.this.cancelHoverer();
      }
   }

   private class TabLabel extends JLabel {
      private boolean active;
      private int triangleBase;
      public int minHeight = -1;

      public TabLabel(String var2, Icon var3) {
         super(var2, var3, 10);
         this.setBorder(Empty.border(4, 12, 3, 16));
      }

      public final void setActive(boolean var1) {
         if (var1 != this.active) {
            this.active = var1;
            this.repaint();
         }
      }

      public Dimension getUnadjustedPreferredSize() {
         return super.getPreferredSize();
      }

      @Override
      public Dimension getPreferredSize() {
         return DimensionUtils.withHeightAtLeast(super.getPreferredSize(), this.minHeight);
      }

      @Override
      public Dimension getMinimumSize() {
         int var1 = 10;
         Icon var2 = this.getIcon();
         if (var2 != null) {
            var1 += var2.getIconWidth();
         }

         Dimension var3 = this.getPreferredSize();
         return new Dimension(var1, var3.height);
      }

      @Override
      public Dimension getMaximumSize() {
         return this.getPreferredSize();
      }

      @Override
      public void paintComponent(Graphics var1) {
         Graphics2D var2 = (Graphics2D)var1;
         GraphicsState var3 = new GraphicsState(var2, GraphicsState.COLOR, GraphicsState.RENDERING_HINTS);
         this.triangleBase = (int)(this.getHeight() * 0.4);
         var1.setColor(this.active ? SimpleTabPane.ACTIVE : SimpleTabPane.INACTIVE);
         UiUtils.makeAntiAliased(var2);
         this.drawBaseRectangle(var2);
         this.drawTriangle(var2);
         this.drawTopRectangles(var2);
         var3.restore();
         super.paintComponent(var1);
      }

      private void drawBaseRectangle(Graphics2D var1) {
         byte var2 = 0;
         int var3 = this.triangleBase;
         int var4 = this.getWidth();
         int var5 = this.getHeight();
         var1.fillRect(var2, var3, var4, var5);
      }

      private void drawTriangle(Graphics2D var1) {
         Polygon var2 = new Polygon(new int[]{0, 10, 10}, new int[]{this.triangleBase, this.triangleBase, 0}, 3);
         var1.fill(var2);
      }

      private void drawTopRectangles(Graphics2D var1) {
         byte var2 = 10;
         byte var3 = 1;
         int var4 = this.getWidth();
         int var5 = this.triangleBase;
         var1.fillRect(var2, var3, var4, var5);
         var3 = 0;
         byte var8 = 1;
         Color var6 = var1.getColor().darker();
         var1.setColor(var6);
         var1.fillRect(var2, var3, var4, var8);
      }

      public Shape getLeftDropIndicator() {
         Rectangle var1 = this.getBounds();
         int var2 = Math.max(0, var1.x - SimpleTabPane.this.tabDistance);
         int var3 = var2;
         int var4 = var1.x - 1;
         int var5 = var4;
         int var6 = var5 + 10 - 1;
         int var7 = var2;
         int var8 = var1.y;
         int var9 = var8 + var1.height;
         int var10 = var9;
         int var11 = var8 + this.triangleBase;
         int var12 = var8;
         int var13 = var8;
         return new Polygon(new int[]{var2, var3, var4, var5, var6, var7}, new int[]{var8, var9, var10, var11, var12, var13}, 6);
      }
   }

   private class TabRow extends JPanel {
      private TabRow() {
      }

      @Override
      protected void paintComponent(Graphics var1) {
         super.paintComponent(var1);
         if (SimpleTabPane.this.dropIndex >= 0) {
            this.drawDropIndicator(var1);
         }
      }

      private void drawDropIndicator(Graphics var1) {
         Graphics2D var2 = (Graphics2D)var1;
         GraphicsState var3 = new GraphicsState(var2, GraphicsState.COLOR, GraphicsState.RENDERING_HINTS);
         UiUtils.makeAntiAliased(var2);
         Shape var4 = this.createDropIndicator();
         var2.setColor(Color.GREEN);
         var2.fill(var4);
         var2.setColor(Color.DARK_GRAY);
         var2.draw(var4);
         var3.restore();
      }

      private Shape createDropIndicator() {
         if (SimpleTabPane.this.dropIndex == SimpleTabPane.this.tabs.size()) {
            SimpleTabPane.TabLabel var3 = this.getTab(SimpleTabPane.this.dropIndex - 1);
            Rectangle var2 = var3.getBounds();
            return new Rectangle(var2.x + var2.width, var2.y, 5, var2.height);
         } else {
            SimpleTabPane.TabLabel var1 = this.getTab(SimpleTabPane.this.dropIndex);
            return var1.getLeftDropIndicator();
         }
      }

      private SimpleTabPane.TabLabel getTab(int var1) {
         int var2 = 0;

         for (Component var6 : this.getComponents()) {
            if (var6 instanceof SimpleTabPane.TabLabel) {
               if (var2 == var1) {
                  return (SimpleTabPane.TabLabel)var6;
               }

               var2++;
            }
         }

         assert false : "Mismatch between dropIndex and number of tabs.";
         return null;
      }
   }

   private class TabTransferHandler extends TransferHandler {
      private final SimpleTabPane.TabLabel tab;
      private final SimpleTabPane.DropHandler dropHandler;

      public TabTransferHandler(SimpleTabPane.TabLabel var2) {
         this.tab = var2;
         this.dropHandler = SimpleTabPane.this.new DropHandler();
      }

      @Override
      public int getSourceActions(JComponent var1) {
         return 2;
      }

      @Override
      public boolean canImport(TransferSupport var1) {
         if (SimpleTabPane.this.dragIndex >= 0 && var1.isDataFlavorSupported(SimpleTabPane.TransferableTab.FLAVOR)) {
            int var2 = SimpleTabPane.this.tabs.indexOf(this.tab);
            boolean var3 = this.isPastHalfwayPoint(var1);
            int var4 = var3 ? var2 + 1 : var2;
            SimpleTabPane.this.setDropIndex(var4);
            return true;
         } else {
            SimpleTabPane.this.setDropIndex(-1);
            return false;
         }
      }

      private boolean isPastHalfwayPoint(TransferSupport var1) {
         DropLocation var2 = var1.getDropLocation();
         Point var3 = var2.getDropPoint();
         return var3.x > this.tab.getWidth() / 2;
      }

      @Override
      public boolean importData(TransferSupport var1) {
         return this.dropHandler.doImport(var1);
      }

      @Override
      protected Transferable createTransferable(JComponent var1) {
         if (SimpleTabPane.this.tabs.size() < 2) {
            return null;
         }

         SimpleTabPane.this.dragIndex = SimpleTabPane.this.tabs.indexOf(this.tab);
         return new SimpleTabPane.TransferableTab(SimpleTabPane.this.dragIndex);
      }

      @Override
      protected void exportDone(JComponent var1, Transferable var2, int var3) {
         SimpleTabPane.this.dragIndex = -1;
         SimpleTabPane.this.setDropIndex(-1);
      }
   }

   private class TawRowTransferHandler extends TransferHandler {
      private final SimpleTabPane.DropHandler dropHandler = SimpleTabPane.this.new DropHandler();

      private TawRowTransferHandler() {
      }

      @Override
      public boolean importData(TransferSupport var1) {
         return this.dropHandler.doImport(var1);
      }

      @Override
      public boolean canImport(TransferSupport var1) {
         if (SimpleTabPane.this.dragIndex >= 0 && var1.isDataFlavorSupported(SimpleTabPane.TransferableTab.FLAVOR)) {
            int var2 = this.getDropIndex(var1);
            SimpleTabPane.this.setDropIndex(var2);
            return true;
         } else {
            return false;
         }
      }

      private int getDropIndex(TransferSupport var1) {
         DropLocation var2 = var1.getDropLocation();
         Point var3 = var2.getDropPoint();

         for (int var4 = 0; var4 < SimpleTabPane.this.tabs.size(); var4++) {
            SimpleTabPane.TabLabel var5 = SimpleTabPane.this.tabs.get(var4);
            int var6 = this.getMidPoint(var5);
            if (var3.x < var6) {
               return var4;
            }
         }

         return SimpleTabPane.this.tabs.size();
      }

      private int getMidPoint(SimpleTabPane.TabLabel var1) {
         Rectangle var2 = var1.getBounds();
         return (int)var2.getCenterX();
      }

      @Override
      public int getSourceActions(JComponent var1) {
         return 0;
      }
   }

   private static class TransferableTab implements Transferable {
      public static final DataFlavor FLAVOR = new DataFlavor(int.class, "This is not actually used");
      private final int dragIndex;

      public TransferableTab(int var1) {
         this.dragIndex = var1;
      }

      @Override
      public Object getTransferData(DataFlavor var1) throws UnsupportedFlavorException, IOException {
         if (var1 == FLAVOR) {
            return this.dragIndex;
         } else {
            throw new UnsupportedFlavorException(var1);
         }
      }

      @Override
      public DataFlavor[] getTransferDataFlavors() {
         return new DataFlavor[]{FLAVOR};
      }

      @Override
      public boolean isDataFlavorSupported(DataFlavor var1) {
         return var1 == FLAVOR;
      }
   }
}
