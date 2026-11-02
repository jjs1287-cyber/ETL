package org.etlforge.application.ui.widget;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.button.KongaToggleButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class SectionedSplitPane implements UiProvider {
   private final HideableSplitPane splitPane;
   private final SectionedSplitPane.Orientation orientation;
   private final List<JToggleButton> buttons;
   private final List<SectionedSplitPane.Selector> selectors;
   private boolean opaque;
   private Color background;
   private Border controlPanelBorder;
   private boolean includeControlPanelInLayout;

   public SectionedSplitPane(HideableSplitPane var1) {
      this.splitPane = var1;
      var1.setDividerHidden(true);
      this.orientation = SectionedSplitPane.Orientation.fromSplitPane(var1);
      this.buttons = Lists.newArrayList();
      this.selectors = Lists.newArrayList();
      this.opaque = true;
      this.controlPanelBorder = new BorderBuilder().rounded(6, 6, Color.GRAY).empty(4, 2, 4, 2);
      this.includeControlPanelInLayout = true;
   }

   public HideableSplitPane getSplitPane() {
      return this.splitPane;
   }

   public void setOpaque(boolean var1) {
      this.opaque = var1;
   }

   public void setBackground(Color var1) {
      this.background = var1;
   }

   public void setControlPanelBorder(Border var1) {
      this.controlPanelBorder = var1;
   }

   public SectionedSplitPane.Selector addSection(String var1, Icon var2, JComponent var3) {
      return this.addSection(var1, var2, new BorderLayoutBuilder(var3));
   }

   public SectionedSplitPane.Selector addSection(String var1, Icon var2, UiProvider var3) {
      int var4 = this.buttons.size();
      SectionedSplitPane.SectionToggleAction var5 = new SectionedSplitPane.SectionToggleAction(var1, var2, var3, var4);
      KongaToggleButton var6 = new KongaToggleButton(var5);
      this.buttons.add(var6);
      SectionedSplitPane.Selector var7 = new SectionedSplitPane.Selector(var6);
      this.selectors.add(var7);
      return var7;
   }

   public boolean isEmpty() {
      return this.buttons.isEmpty();
   }

   public int getSectionCount() {
      return this.buttons.size();
   }

   public void toggleSection(int var1) {
      this.buttons.get(var1).doClick();
   }

   public void showSection(int var1) {
      JToggleButton var2 = this.buttons.get(var1);
      if (!var2.isSelected()) {
         var2.doClick();
      }
   }

   public void useSelectorIndexAsShortcut() {
      for (int var1 = 0; var1 < this.selectors.size() && var1 < 9; var1++) {
         SectionedSplitPane.Selector var2 = this.selectors.get(var1);
         KeyStroke var3 = KeyUtils.getCtrlDown(49 + var1);
         var2.setAccelerator(var3);
      }
   }

   public JComponent getUi() {
      return !this.isEmpty() && this.includeControlPanelInLayout
         ? this.orientation.layout(this.splitPane, this.buttons, this.opaque, this.background, this.controlPanelBorder)
         : this.splitPane;
   }

   @Override
   public void decorate(UiCanvas var1) {
      JComponent var2 = this.getUi();
      var1.display(var2);
   }

   @Override
   public void requestFocus() {
      if (!this.buttons.isEmpty()) {
         this.buttons.get(0).requestFocusInWindow();
      }
   }

   public JComponent getControlPanel() {
      return this.orientation.getControlPanel(this.buttons, this.opaque, this.background, this.controlPanelBorder);
   }

   public void setIncludeControlPanelInLayout(boolean var1) {
      this.includeControlPanelInLayout = var1;
   }

   private enum Orientation {
      TOP,
      LEFT,
      BOTTOM,
      RIGHT;

      public static SectionedSplitPane.Orientation fromSplitPane(HideableSplitPane var0) {
         switch (var0.getOrientation()) {
            case 1:
               switch (var0.getSign()) {
                  case -1:
                     return LEFT;
                  case 1:
                     return RIGHT;
               }
            case 0:
               switch (var0.getSign()) {
                  case -1:
                     return TOP;
                  case 1:
                     return BOTTOM;
               }
            default:
               throw new RuntimeException("Invalid orientation/sign: " + var0.getOrientation() + "/" + var0.getSign());
         }
      }

      public void addToSplitPane(HideableSplitPane var1, JComponent var2) {
         switch (this) {
            case TOP:
               var1.setTopComponent(var2);
               break;
            case LEFT:
               var1.setLeftComponent(var2);
               break;
            case BOTTOM:
               var1.setBottomComponent(var2);
               break;
            case RIGHT:
               var1.setRightComponent(var2);
         }
      }

      public JComponent layout(HideableSplitPane var1, List<JToggleButton> var2, boolean var3, Color var4, Border var5) {
         BorderLayoutBuilder var6 = new BorderLayoutBuilder(2, 2).setOpaque(var3);
         if (var4 != null) {
            var6.withBackground(var4);
         }

         JComponent var7 = this.getControlPanel(var2, var3, var4, var5);
         var6.center(var1);
         switch (this) {
            case TOP:
               var6.north(var7);
               break;
            case LEFT:
               var6.west(var7);
               break;
            case BOTTOM:
               var6.south(var7);
               break;
            case RIGHT:
               var6.east(var7);
         }

         return var6.container();
      }

      private JComponent getControlPanel(List<JToggleButton> var1, boolean var2, Color var3, Border var4) {
         JPanel var5 = InvisiblePanel.of(this.layoutButtons(var1, var4));
         if (var3 != null) {
            var5.setOpaque(true);
            var5.setBackground(var3);
         } else {
            var5.setOpaque(var2);
         }

         return var5;
      }

      private JComponent layoutButtons(List<JToggleButton> var1, Border var2) {
         switch (this) {
            case TOP:
            case BOTTOM:
               return horizontalControlPanel(var1, var2);
            case LEFT:
            case RIGHT:
               return verticalControlPanel(var1, var2);
            default:
               throw new RuntimeException("Unexpected orientation: " + this.name());
         }
      }

      private static JComponent verticalControlPanel(List<JToggleButton> var0, Border var1) {
         Grid var2 = Grid.oneColumn(2).nonOpaque().withBorder(var1);
         var2.addAll(var0);
         BorderLayoutBuilder var3 = new BorderLayoutBuilder();
         var3.north(var2).center(Box.createVerticalGlue()).nonOpaque();
         return var3.container();
      }

      private static JComponent horizontalControlPanel(List<JToggleButton> var0, Border var1) {
         Grid var2 = Grid.oneRow(2).nonOpaque().withBorder(var1).withBorder(var1);
         var2.addAll(var0);
         BorderLayoutBuilder var3 = new BorderLayoutBuilder();
         var3.north(var2).center(Box.createHorizontalGlue()).nonOpaque();
         return var3.container();
      }
   }

   private class SectionToggleAction extends AbstractAction {
      private final UiProvider section;
      private final int index;
      private int lastDividerLocation;

      public SectionToggleAction(String var2, Icon var3, UiProvider var4, int var5) {
         super(var2, var3);
         this.section = var4;
         this.index = var5;
         this.putValue("SwingSelectedKey", Boolean.FALSE);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (this.isSelected()) {
            this.showSection();
         } else {
            this.hideSection();
         }

         UiUtils.revalidate(SectionedSplitPane.this.splitPane);
      }

      private boolean isSelected() {
         return Boolean.TRUE.equals(this.getValue("SwingSelectedKey"));
      }

      private void showSection() {
         this.storeDividerLocation();
         this.addSectionToSplitPane();
         this.hideOtherSections();
         this.restoreDividerLocation();
         this.giveFocusToSection();
      }

      private void storeDividerLocation() {
         if (SectionedSplitPane.this.splitPane.isDividerHidden()) {
            this.lastDividerLocation = -1;
         } else {
            this.lastDividerLocation = SectionedSplitPane.this.splitPane.getDividerLocation();
         }
      }

      private void addSectionToSplitPane() {
         if (SectionedSplitPane.this.splitPane.isDividerHidden()) {
            SectionedSplitPane.this.splitPane.setDividerHidden(false);
         }

         JPanel var1 = BorderLayoutBuilder.fillWith(this.section);
         var1.setBorder(new LineBorder(Color.GRAY));
         SectionedSplitPane.this.orientation.addToSplitPane(SectionedSplitPane.this.splitPane, var1);
      }

      private void hideOtherSections() {
         for (int var1 = 0; var1 < SectionedSplitPane.this.buttons.size(); var1++) {
            if (var1 != this.index) {
               SectionedSplitPane.this.buttons.get(var1).setSelected(false);
            }
         }
      }

      private void restoreDividerLocation() {
         if (this.lastDividerLocation >= 0) {
            SectionedSplitPane.this.splitPane.setDividerLocation(this.lastDividerLocation);
         }
      }

      private void giveFocusToSection() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               SectionToggleAction.this.section.requestFocus();
            }
         });
      }

      private void hideSection() {
         SectionedSplitPane.this.orientation.addToSplitPane(SectionedSplitPane.this.splitPane, new JLabel(" "));
         SectionedSplitPane.this.splitPane.setDividerHidden(true);
      }
   }

   public static final class Selector {
      private final JToggleButton toggle;
      private Action shortcut;

      private Selector(JToggleButton var1) {
         this.toggle = var1;
      }

      public void setToolTip(String var1) {
         this.toggle.setToolTipText(var1);
      }

      public void setAccelerator(KeyStroke var1) {
         byte var2 = 2;
         if (this.shortcut != null) {
            ActionKeyBinding.uninstall(this.shortcut, this.toggle, var2);
         }

         this.shortcut = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               Selector.this.toggle.doClick();
            }
         };
         this.shortcut.putValue("AcceleratorKey", var1);
         ActionKeyBinding.install(this.shortcut, this.toggle, var2);
      }
   }
}
