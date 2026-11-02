package org.etlforge.ui.widget.tabbedpane;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.TabbedPaneUI;
import org.etlforge.ui.Empty;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.MnemonicStringFactory;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.event.KeyStrokeProcessor;
import org.etlforge.ui.event.MouseActionListener;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.ComponentIcon;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.KongaWidget;
import org.etlforge.ui.widget.label.LabelUtils;
import org.etlforge.ui.widget.popup.KongaPopupMenu;
import org.etlforge.ui.widget.popup.PopupMenuUtils;

public final class KongaTabbedPane implements UiProvider, KongaWidget {
   private final KongaTabbedPane.Tabs tabs;
   private TabReorderHandler reorderHandler;
   private final JLayeredPane layeredPane = new JLayeredPane();
   private JComponent glassPane;

   public KongaTabbedPane() {
      this.tabs = new KongaTabbedPane.Tabs();
      this.configureLayeredPane();
   }

   public KongaTabbedPane(int var1, int var2) {
      this.tabs = new KongaTabbedPane.Tabs(var1, var2);
      this.configureLayeredPane();
   }

   public KongaTabbedPane(int var1) {
      this.tabs = new KongaTabbedPane.Tabs(var1);
      this.configureLayeredPane();
   }

   private void configureLayeredPane() {
      this.layeredPane.add(this.tabs, JLayeredPane.DEFAULT_LAYER);
      this.layeredPane.addComponentListener(new ComponentAdapter() {
         @Override
         public void componentResized(ComponentEvent var1) {
            Dimension var2 = KongaTabbedPane.this.layeredPane.getSize();
            KongaTabbedPane.this.tabs.setSize(var2);
            if (KongaTabbedPane.this.glassPane != null) {
               KongaTabbedPane.this.glassPane.setSize(var2);
            }
         }
      });
   }

   public void setKeyboardNavigationEnabled(boolean var1) {
      this.tabs.setKeyboardNavigationEnabled(var1);
   }

   public void setCloseable(boolean var1) {
      this.tabs.setCloseable(var1);
   }

   public void setCanCloseOtherTabs(boolean var1) {
      this.tabs.setCanCloseOthers(var1);
   }

   public void setCloseHandler(KongaTabbedPaneCloseHandler var1) {
      this.tabs.setCloseHandler(var1);
   }

   public void setUnselectedTabBackground(Color var1) {
      this.tabs.setUnselectedTabBackground(var1);
   }

   public KongaTabbedPane disableCtrlUp() {
      this.tabs.getInputMap(1).put(KeyUtils.getCtrlDown(38), "none");
      return this;
   }

   @Override
   public boolean isEnabled() {
      return this.tabs.isEnabled();
   }

   @Override
   public void setEnabled(boolean var1) {
      this.tabs.setEnabled(var1);
   }

   @Override
   public void addFocusListener(FocusListener var1) {
      this.tabs.addFocusListener(var1);
   }

   @Override
   public void removeFocusListener(FocusListener var1) {
      this.tabs.removeFocusListener(var1);
   }

   public void setPreferredSize(Dimension var1) {
      this.tabs.setPreferredSize(var1);
   }

   public void addTab(String var1, Component var2) {
      this.addTab(var1, null, var2, null);
   }

   public void addTab(String var1, UiProvider var2) {
      this.addTab(var1, BorderLayoutBuilder.fillWith(var2));
   }

   public void addTab(String var1, Icon var2, UiProvider var3) {
      this.addTab(var1, var2, BorderLayoutBuilder.fillWith(var3));
   }

   public void addTab(String var1, Icon var2, Component var3) {
      this.addTab(var1, var2, var3, null);
   }

   public void addTab(String var1, Icon var2, Component var3, String var4) {
      this.tabs.addTab(var1, var2, var3, var4);
   }

   public void insertTab(String var1, Icon var2, Component var3, String var4, int var5) {
      this.tabs.insertTab(var1, var2, var3, var4, var5);
   }

   public void removeAll() {
      this.tabs.removeAll();
   }

   public int getTabCount() {
      return this.tabs.getTabCount();
   }

   public int getSelectedIndex() {
      return this.tabs.getSelectedIndex();
   }

   public void setSelectedIndex(int var1) {
      this.tabs.setSelectedIndex(var1);
   }

   public String getTitleAt(int var1) {
      return this.tabs.getTitleAt(var1);
   }

   public Component getComponentAt(int var1) {
      return this.tabs.getComponentAt(var1);
   }

   public Icon getIconAt(int var1) {
      return this.tabs.getIconAt(var1);
   }

   public String getToolTipAt(int var1) {
      return this.tabs.getToolTipTextAt(var1);
   }

   public Color getForegroundAt(int var1) {
      return this.tabs.getForegroundAt(var1);
   }

   public void setForegroundAt(int var1, Color var2) {
      this.tabs.setForegroundAt(var1, var2);
   }

   public Color getBackgroundAt(int var1) {
      return this.tabs.getBackgroundAt(var1);
   }

   public void setBackgroundAt(int var1, Color var2) {
      this.tabs.setBackgroundAt(var1, var2);
   }

   public Component getTabComponentAt(int var1) {
      return this.tabs.getTabComponentAt(var1);
   }

   public void setTitleAt(int var1, String var2) {
      this.tabs.setTitleAt(var1, var2);
   }

   public void setIconAt(int var1, Icon var2) {
      this.tabs.setIconAt(var1, var2);
   }

   public void removeTabAt(int var1) {
      this.tabs.remove(var1);
   }

   public void setInterpretTitlesAsMnemonicStrings(boolean var1) {
      this.tabs.setInterpretTitlesAsMnemonicStrings(var1);
   }

   public void setAdditionalTabActions(Action... var1) {
      this.tabs.setAdditionalTabActions(var1);
   }

   public void installKeyBinding(Action var1) {
      this.installKeyBinding(var1, 1);
   }

   public void installKeyBinding(Action var1, int var2) {
      ActionKeyBinding.install(var1, this.tabs, var2);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.layeredPane);
   }

   @Override
   public void requestFocus() {
   }

   public void addChangeListener(ChangeListener var1) {
      this.tabs.addChangeListener(var1);
   }

   public void removeChangeListener(ChangeListener var1) {
      this.tabs.removeChangeListener(var1);
   }

   public JComponent asComponent() {
      return this.tabs;
   }

   public void enableTabReordering(TabReorderHandler var1) {
      Preconditions.checkNotNull(var1, "handler");
      if (this.reorderHandler == null) {
         this.tabs.setTransferHandler(new KongaTabbedPane.TransferHandlerImpl());
         DragSource var2 = new DragSource();
         var2.createDefaultDragGestureRecognizer(this.tabs, 2, new DragGestureListener() {
            @Override
            public void dragGestureRecognized(DragGestureEvent var1) {
               KongaTabbedPane.this.tabs.getTransferHandler().exportAsDrag(KongaTabbedPane.this.tabs, var1.getTriggerEvent(), 2);
            }
         });
      }

      this.reorderHandler = var1;
   }

   @Override
   public UiProvider asUiProvider() {
      return this;
   }

   @Override
   public void setPainter(ComponentPainter var1) {
   }

   public void selectFirstTab() {
      if (this.getTabCount() > 0) {
         this.setSelectedIndex(0);
      }
   }

   private void showGlassPane() {
      this.glassPane.setSize(this.layeredPane.getSize());
      this.layeredPane.add(this.glassPane, JLayeredPane.DRAG_LAYER);
   }

   private void removeGlassPane() {
      if (this.glassPane != null) {
         this.layeredPane.remove(this.glassPane);
      }
   }

   public void setUI(TabbedPaneUI var1) {
      this.tabs.setUI(var1);
   }

   public void setBackground(Color var1) {
      this.tabs.setBackground(var1);
   }

   public void setOpaque(boolean var1) {
      this.tabs.setOpaque(var1);
   }

   public void updatePreferredSize() {
      this.layeredPane.setPreferredSize(this.tabs.getPreferredSize());
   }

   private static class Tabs extends JTabbedPane implements KeyStrokeProcessor, Serializable {
      private static final MnemonicStringFactory MNEMONIC_FACTORY = MnemonicStringFactory.newCharMarkerFactory('&');
      private final Map<KeyStroke, Action> navigationActions = Maps.newHashMap();
      private final KongaTabbedPane.Tabs.CloseTabAction closeTabAction = new KongaTabbedPane.Tabs.CloseTabAction();
      private final KongaTabbedPane.Tabs.CloseOtherTabsAction closeOtherTabsAction = new KongaTabbedPane.Tabs.CloseOtherTabsAction();
      private final KongaTabbedPane.Tabs.CloseAllTabsAction closeAllTabsAction = new KongaTabbedPane.Tabs.CloseAllTabsAction();
      private final List<JLabel> tabComponents = new ArrayList<>();
      private boolean keyboardNavigationEnabled = true;
      private boolean closeable;
      private boolean canCloseOthers = true;
      private Color unselectedTabBackground;
      private KongaTabbedPaneCloseHandler closeHandler = new KongaTabbedPane.Tabs.DefaultCloseHandler();
      private int dragIndex = -1;
      private int dropIndex = -1;
      private Point dropLocation;
      private Icon dragIcon;
      private boolean interpretTitlesAsMnemonicStrings;
      private Action[] additionalActions;

      public Tabs() {
         this.init();
      }

      public Tabs(int var1, int var2) {
         super(var1, var2);
         this.init();
      }

      public Tabs(int var1) {
         super(var1);
         this.init();
      }

      private void init() {
         this.initActionMap();
         this.addMouseListener(new KongaTabbedPane.Tabs.TabMouseListener());
      }

      public void setKeyboardNavigationEnabled(boolean var1) {
         this.keyboardNavigationEnabled = var1;
      }

      public void setCloseable(boolean var1) {
         this.closeable = var1;
      }

      public void setCanCloseOthers(boolean var1) {
         this.canCloseOthers = var1;
      }

      public void setUnselectedTabBackground(Color var1) {
         this.unselectedTabBackground = var1;
      }

      public void setInterpretTitlesAsMnemonicStrings(boolean var1) {
         this.interpretTitlesAsMnemonicStrings = var1;
      }

      public void setAdditionalTabActions(Action... var1) {
         this.additionalActions = (Action[])var1.clone();
      }

      @Override
      public Color getBackgroundAt(int var1) {
         return this.unselectedTabBackground != null && var1 != this.getSelectedIndex() ? this.unselectedTabBackground : super.getBackgroundAt(var1);
      }

      public void setCloseHandler(KongaTabbedPaneCloseHandler var1) {
         this.closeHandler = var1 != null ? var1 : new KongaTabbedPane.Tabs.DefaultCloseHandler();
      }

      @Override
      public void setIconAt(int var1, Icon var2) {
         super.setIconAt(var1, var2);
         this.tabComponents.get(var1).setIcon(var2);
      }

      @Override
      public void setTitleAt(int var1, String var2) {
         super.setTitleAt(var1, var2);
         MnemonicString var3 = this.getMnemonicTitle(var2);
         LabelUtils.applyMnemonics(this.tabComponents.get(var1), var3);
      }

      @Override
      public void setToolTipTextAt(int var1, String var2) {
         super.setToolTipTextAt(var1, var2);
         this.tabComponents.get(var1).setToolTipText(var2);
      }

      @Override
      public void insertTab(String var1, Icon var2, Component var3, String var4, int var5) {
         super.insertTab(var1, var2, var3, var4, var5);
         MnemonicString var6 = this.getMnemonicTitle(var1);
         JLabel var7 = this.createTabComponent(var6, var2, var4);
         this.setTabComponentAt(var5, var7);
         char var8 = var6.getMnemonic();
         if (var8 != 0) {
            this.setMnemonicAt(var5, var8);
         }

         this.tabComponents.add(var5, var7);
         this.updateCloseActions();
      }

      public void setDropIndex(int var1) {
         if (var1 != this.dropIndex) {
            this.dropIndex = var1;
            this.repaint();
         }
      }

      public void startDrag() {
         int var1 = this.getSelectedIndex();
         if (var1 >= 0) {
            this.dragIndex = var1;
            Component var2 = this.getComponentAt(var1);
            if (var2 != null) {
               this.dragIcon = new ComponentIcon(var2, 0.5);
            } else {
               this.dragIcon = null;
            }
         }
      }

      public int getDragIndex() {
         return this.dragIndex;
      }

      public void setDropLocation(Point var1) {
         Point var2 = this.dropLocation;
         this.dropLocation = new Point(var1);
         if (this.dragIcon != null) {
            if (var2 != null) {
               int var3 = Math.min(var1.x, var2.x);
               int var4 = Math.min(var1.y, var2.y);
               int var5 = Math.max(var1.x, var2.x) + this.dragIcon.getIconWidth();
               int var6 = var5 - var3;
               int var7 = Math.max(var1.y, var2.y) + this.dragIcon.getIconHeight();
               int var8 = var7 - var4;
               this.repaint(var3, var4, var6, var8);
            } else {
               this.repaint();
            }
         }
      }

      public void stopDrag() {
         this.dragIndex = -1;
         this.dropIndex = -1;
         this.dropLocation = null;
         this.dragIcon = null;
      }

      @Override
      public void paint(Graphics var1) {
         super.paint(var1);
         if (this.dragIcon != null && this.dropLocation != null) {
            this.paintDragIcon(var1);
         }
      }

      private void paintDragIcon(Graphics var1) {
         Graphics2D var2 = (Graphics2D)var1;
         Color var3 = var2.getColor();
         Composite var4 = var2.getComposite();
         var2.setColor(Color.DARK_GRAY);
         var2.draw(new Rectangle(this.dropLocation.x, this.dropLocation.y, this.dragIcon.getIconWidth() - 1, this.dragIcon.getIconHeight() - 1));
         var2.setComposite(AlphaComposite.getInstance(3, 0.85F));
         this.dragIcon.paintIcon(this, var1, this.dropLocation.x, this.dropLocation.y);
         var2.setComposite(var4);
         var2.setColor(var3);
      }

      @Override
      protected void paintComponent(Graphics var1) {
         super.paintComponent(var1);
         Rectangle var2 = null;
         if (this.dropIndex >= this.getTabCount()) {
            Rectangle var3 = this.getBoundsAt(this.dropIndex - 1);
            var2 = new Rectangle(var3.x + var3.width - 2, var3.y - 1, 4, var3.height + 1);
         } else if (this.dropIndex >= 0) {
            Rectangle var4 = this.getBoundsAt(this.dropIndex);
            var2 = new Rectangle(var4.x - 2, var4.y - 1, 4, var4.height + 1);
         }

         if (var2 != null) {
            Color var5 = var1.getColor();
            var1.setColor(Color.DARK_GRAY);
            var1.drawRect(var2.x, var2.y, var2.width, var2.height);
            var1.setColor(Color.GREEN);
            var1.fillRect(var2.x + 1, var2.y + 1, var2.width - 1, var2.height - 1);
            var1.setColor(var5);
         }
      }

      private MnemonicString getMnemonicTitle(String var1) {
         return this.interpretTitlesAsMnemonicStrings ? MNEMONIC_FACTORY.fromString(var1) : MnemonicStringFactory.NO_MNEMONIC_FACTORY.fromString(var1);
      }

      private JLabel createTabComponent(MnemonicString var1, Icon var2, String var3) {
         JLabel var4 = new JLabel("", var2, 10);
         var4.setBorder(Empty.border(2));
         LabelUtils.applyMnemonics(var4, var1);
         var4.setToolTipText(var3);
         return var4;
      }

      @Override
      public void removeTabAt(int var1) {
         this.tabComponents.remove(var1);
         super.removeTabAt(var1);
         this.updateCloseActions();
      }

      @Override
      public boolean process(KeyStroke var1, Object var2) {
         if (!this.keyboardNavigationEnabled) {
            return false;
         }

         Action var3 = this.navigationActions.get(var1);
         if (var3 == null) {
            return false;
         }

         int var4 = this.getSelectedIndex();
         ActionUtils.run(var3, this, (String)var3.getValue("ActionCommandKey"));
         return var4 != this.getSelectedIndex();
      }

      private void initActionMap() {
         this.navigationActions.put(KeyUtils.getCtrlDown(9), new KongaTabbedPane.Tabs.MoveNext());
         this.navigationActions.put(KeyUtils.getCtrlShiftDown(9), new KongaTabbedPane.Tabs.MovePrevious());
      }

      private void updateCloseActions() {
         switch (this.getTabCount()) {
            case 0:
               this.closeTabAction.setEnabled(false);
               this.closeOtherTabsAction.setEnabled(false);
               this.closeAllTabsAction.setEnabled(false);
               break;
            case 1:
               this.closeTabAction.setEnabled(true);
               this.closeOtherTabsAction.setEnabled(false);
               this.closeAllTabsAction.setEnabled(true);
               break;
            default:
               this.closeTabAction.setEnabled(true);
               this.closeOtherTabsAction.setEnabled(true);
               this.closeAllTabsAction.setEnabled(true);
         }
      }

      private KongaTabbedPaneCloseEvent getCloseEvent() {
         return new KongaTabbedPaneCloseEvent(this, this.getSelectedIndex());
      }

      private void showPopupMenu(MouseEvent var1) {
         KongaPopupMenu var2 = new KongaPopupMenu();
         var2.add(this.closeTabAction);
         if (this.canCloseOthers) {
            var2.addActions(this.closeOtherTabsAction, this.closeAllTabsAction);
         }

         if (this.additionalActions != null) {
            var2.addActions(this.additionalActions);
         }

         var2.show(var1.getComponent(), var1.getX(), var1.getY());
      }

      private class CloseAllTabsAction extends AbstractAction {
         public CloseAllTabsAction() {
            super(PackageResources.CloseAllTabsAction.NAME);
            this.putValue("ShortDescription", PackageResources.CloseAllTabsAction.TOOLTIP);
            this.setEnabled(false);
         }

         @Override
         public void actionPerformed(ActionEvent var1) {
            Tabs.this.closeHandler.closeAllTabsRequested(Tabs.this.getCloseEvent());
         }
      }

      private class CloseOtherTabsAction extends AbstractAction {
         public CloseOtherTabsAction() {
            super(PackageResources.CloseOtherTabsAction.NAME);
            this.putValue("ShortDescription", PackageResources.CloseOtherTabsAction.TOOLTIP);
            this.setEnabled(false);
         }

         @Override
         public void actionPerformed(ActionEvent var1) {
            Tabs.this.closeHandler.closeOtherTabsRequested(Tabs.this.getCloseEvent());
         }
      }

      private class CloseTabAction extends AbstractAction {
         public CloseTabAction() {
            super(PackageResources.CloseTabAction.NAME, PackageResources.CLOSE_ICON);
            this.putValue("ShortDescription", PackageResources.CloseTabAction.TOOLTIP);
            this.setEnabled(false);
         }

         @Override
         public void actionPerformed(ActionEvent var1) {
            Tabs.this.closeHandler.closeCurrentTabRequested(Tabs.this.getCloseEvent());
         }
      }

      private class DefaultCloseHandler implements KongaTabbedPaneCloseHandler, Serializable {
         private DefaultCloseHandler() {
         }

         @Override
         public void closeAllTabsRequested(KongaTabbedPaneCloseEvent var1) {
            Tabs.this.removeAll();
         }

         @Override
         public void closeCurrentTabRequested(KongaTabbedPaneCloseEvent var1) {
            Tabs.this.removeTabAt(Tabs.this.getSelectedIndex());
         }

         @Override
         public void closeOtherTabsRequested(KongaTabbedPaneCloseEvent var1) {
            ArrayList var2 = new ArrayList();

            for (int var3 = Tabs.this.getTabCount() - 1; var3 >= 0; var3--) {
               if (var3 != Tabs.this.getSelectedIndex()) {
                  var2.add(var3);
               }
            }

            for (Object __etlItem665 : var2) {
               Integer var4 = (Integer) __etlItem665;
               Tabs.this.removeTabAt(var4);
            }
         }
      }

      private class MoveNext extends KongaTabbedPane.Tabs.NavigationAction {
         public MoveNext() {
            super("next_tab");
         }

         @Override
         protected int getNextIndex(int var1, int var2) {
            int var3 = ++var1;
            return var3 == var2 ? 0 : var3;
         }
      }

      private class MovePrevious extends KongaTabbedPane.Tabs.NavigationAction {
         public MovePrevious() {
            super("previous_tab");
         }

         @Override
         protected int getNextIndex(int var1, int var2) {
            int var3 = --var1;
            return var3 >= 0 ? var3 : var2 - 1;
         }
      }

      private abstract class NavigationAction extends AbstractAction {
         protected NavigationAction(String var2) {
            this.putValue("ActionCommandKey", var2);
         }

         @Override
         public final void actionPerformed(ActionEvent var1) {
            int var2 = Tabs.this.getTabCount();
            if (var2 >= 2) {
               int var3 = this.getNextIndex(Tabs.this.getSelectedIndex(), var2);
               Tabs.this.setSelectedIndex(var3);
            }
         }

         protected abstract int getNextIndex(int var1, int var2);
      }

      private class TabMouseListener extends MouseActionListener {
         private TabMouseListener() {
         }

         @Override
         protected void onPopupTrigger(final MouseEvent var1) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  if (Tabs.this.getTabCount() > 0 && Tabs.this.closeable && PopupMenuUtils.isPopupTrigger(var1)) {
                     Tabs.this.showPopupMenu(var1);
                  }
               }
            });
         }
      }
   }

   private class TransferHandlerImpl extends TransferHandler {
      private TransferHandlerImpl() {
      }

      private void cleanup() {
         KongaTabbedPane.this.tabs.stopDrag();
         KongaTabbedPane.this.removeGlassPane();
      }

      @Override
      protected void exportDone(JComponent var1, Transferable var2, int var3) {
         this.cleanup();
      }

      @Override
      public boolean canImport(TransferSupport var1) {
         if (var1.isDataFlavorSupported(KongaTabbedPane.TransferableTab.FLAVOR)) {
            if (KongaTabbedPane.this.tabs.getDragIndex() >= 0) {
               this.findDropIndex(var1);
               return true;
            }
         } else {
            this.switchToTabForDropLocation(var1);
         }

         this.cleanup();
         return false;
      }

      private void switchToTabForDropLocation(TransferSupport var1) {
         Point var2 = var1.getDropLocation().getDropPoint();
         int var3 = KongaTabbedPane.this.tabs.indexAtLocation(var2.x, var2.y);
         if (var3 >= 0 && var3 < KongaTabbedPane.this.tabs.getTabCount()) {
            KongaTabbedPane.this.tabs.setSelectedIndex(var3);
         }
      }

      private void findDropIndex(TransferSupport var1) {
         try {
            Point var2 = var1.getDropLocation().getDropPoint();
            KongaTabbedPane.this.tabs.setDropLocation(var2);
            if (KongaTabbedPane.this.tabs.indexAtLocation(var2.x, var2.y) != KongaTabbedPane.this.tabs.getDragIndex()) {
               int var3 = this.getIndex(var2, KongaTabbedPane.this.tabs.getDragIndex());
               if (var3 >= 0) {
                  KongaTabbedPane.this.tabs.setDropIndex(var3);
               }
            }
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      @Override
      protected Transferable createTransferable(JComponent var1) {
         if (var1 == KongaTabbedPane.this.tabs && KongaTabbedPane.this.tabs.getTabCount() > 1 && KongaTabbedPane.this.tabs.getSelectedIndex() >= 0) {
            if (KongaTabbedPane.this.glassPane == null) {
               KongaTabbedPane.this.glassPane = new JComponent() {};
               KongaTabbedPane.this.glassPane.addMouseListener(new MouseAdapter() {});
               KongaTabbedPane.this.glassPane.setTransferHandler(this);
            }

            KongaTabbedPane.this.showGlassPane();
            KongaTabbedPane.this.tabs.startDrag();
            return new KongaTabbedPane.TransferableTab(KongaTabbedPane.this);
         } else {
            return null;
         }
      }

      @Override
      public int getSourceActions(JComponent var1) {
         return var1 == KongaTabbedPane.this.tabs ? 2 : 0;
      }

      @Override
      public boolean importData(TransferSupport var1) {
         try {
            if (KongaTabbedPane.this.tabs.getDragIndex() >= 0) {
               try {
                  Point var2 = var1.getDropLocation().getDropPoint();
                  int var3 = this.getIndex(var2, KongaTabbedPane.this.tabs.getDragIndex());
                  if (var3 >= 0 && var3 != KongaTabbedPane.this.tabs.getDragIndex()) {
                     if (var3 > KongaTabbedPane.this.tabs.getDragIndex()) {
                        var3--;
                     }

                     KongaTabbedPane.this.reorderHandler.handle(KongaTabbedPane.this, KongaTabbedPane.this.tabs.getDragIndex(), var3);
                     return true;
                  }
               } catch (Exception var8) {
                  var8.printStackTrace();
               }
            }

            return false;
         } finally {
            KongaTabbedPane.this.tabs.setDropIndex(-1);
         }
      }

      private int getIndex(Point var1, int var2) {
         int var3 = KongaTabbedPane.this.tabs.indexAtLocation(var1.x, var1.y);
         if (var3 == -1) {
            if (this.isBelowTabs(var1.y)) {
               return -1;
            }

            Rectangle var4 = KongaTabbedPane.this.tabs.getBoundsAt(0);
            var3 = var1.x < var4.x ? 0 : KongaTabbedPane.this.tabs.getTabCount();
         } else {
            Rectangle var5 = KongaTabbedPane.this.tabs.getBoundsAt(var3);
            if (var1.x > var5.x + var5.width / 2) {
               var3++;
            }
         }

         return var3;
      }

      private boolean isBelowTabs(int var1) {
         for (int var2 = 0; var2 < KongaTabbedPane.this.tabs.getTabCount(); var2++) {
            Rectangle var3 = KongaTabbedPane.this.tabs.getBoundsAt(var2);
            if (var3.y + var3.height > var1) {
               return false;
            }
         }

         return true;
      }
   }

   private static class TransferableTab implements Transferable {
      public static final DataFlavor FLAVOR = new DataFlavor(KongaTabbedPane.class, "This is not actually used");
      private final KongaTabbedPane tabs;

      public TransferableTab(KongaTabbedPane var1) {
         this.tabs = var1;
      }

      @Override
      public Object getTransferData(DataFlavor var1) throws UnsupportedFlavorException, IOException {
         return this.tabs;
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
