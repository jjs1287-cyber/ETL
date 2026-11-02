package org.etlforge.integration.client.ui.interchange.component.launcher;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D.Double;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.CardPanel;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.focus.FocusUtils;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.label.KongaLabel;

final class LaunchScreen {
   private final LaunchScreen.Tabs tabs;
   private final JComponent contentPane;
   private final List<LaunchOption> options;

   public LaunchScreen(List<LaunchOption> var1) {
      this.options = var1;
      this.tabs = new LaunchScreen.Tabs(var1);
      this.contentPane = BorderLayoutBuilder.fillWith(this.tabs);
      this.selectDefaultOption();
   }

   public void selectDefaultOption() {
      LaunchOption var1 = this.getDefaultOption();
      this.tabs.selectOption(var1);
   }

   private LaunchOption getDefaultOption() {
      for (LaunchOption var2 : this.options) {
         if (var2.canBeDefaultChoice()) {
            return var2;
         }
      }

      return this.options.get(0);
   }

   public JComponent getContentPane() {
      return this.contentPane;
   }

   public void requestFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            LaunchScreen.this.options.get(0).requestFocus();
         }
      });
   }

   private static class Tabs implements UiProvider {
      private static final int TAB_GAP = 16;
      private static final Color SELECTED_BACKGROUND = Colors.NEUTRAL_BACKGROUND;
      private static final Color UNSELECTED_BACKGROUND = org.etlforge.application.ui.look.Colors.PANEL_BACKGROUND;
      private final List<LaunchOption> options;
      private final List<LaunchScreen.Tabs.Tab> tabs;
      private final CardPanel cards;
      private final LaunchScreen.Tabs.MouseHandler mouseHandler;

      public Tabs(List<LaunchOption> var1) {
         this.options = var1;
         this.tabs = Lists.newArrayList();
         this.cards = new CardPanel();
         this.mouseHandler = new LaunchScreen.Tabs.MouseHandler();

         for (LaunchOption var3 : var1) {
            this.addOption(var3);
         }
      }

      private void addOption(LaunchOption var1) {
         LaunchScreen.Tabs.Tab var2 = this.createTab(var1);
         this.tabs.add(var2);
         JComponent var3 = this.createContent(var1);
         this.cards.add(var3, var1.getName());
      }

      private LaunchScreen.Tabs.Tab createTab(LaunchOption var1) {
         LaunchScreen.Tabs.Tab var2 = new LaunchScreen.Tabs.Tab(var1.getName());
         var2.setIcon(var1.getIcon());
         FocusUtils.makeComponentMouseFocusable(var2);
         this.applyMnemonic(var2);
         var2.addMouseListener(this.mouseHandler);
         return var2;
      }

      private void applyMnemonic(final KongaLabel var1) {
         if (var1.getDisplayedMnemonic() > 0) {
            InputMap var2 = var1.getInputMap(2);
            KeyStroke var3 = KeyStroke.getKeyStroke(var1.getDisplayedMnemonic(), 8, false);
            Object var4 = var2.get(var3);
            if (var4 == null) {
               var4 = "switch-to-tab";
               var2.put(var3, var4);
            }

            ActionMap var5 = var1.getActionMap();
            var5.put(var4, new AbstractAction() {
               @Override
               public void actionPerformed(ActionEvent var1x) {
                  Tabs.this.selectTab(var1);
               }
            });
         }
      }

      private void selectTab(KongaLabel var1) {
         int var2 = this.tabs.indexOf(var1);
         LaunchOption var3 = this.options.get(var2);
         this.selectOption(var3);
      }

      private JComponent createContent(LaunchOption var1) {
         JPanel var2 = BorderLayoutBuilder.fillWith(var1);
         var2.setOpaque(true);
         UiUtils.setPanelBackgrounds(var2, SELECTED_BACKGROUND);
         var2.setBorder(Empty.border(24, 32, 20, 32));
         if (var1.needsExternalScroll()) {
            KongaScrollPane var3 = new KongaScrollPane(var2);
            var3.setBorder(new LineBorder(SELECTED_BACKGROUND));
            return var3;
         } else {
            return var2;
         }
      }

      public void selectOption(LaunchOption var1) {
         int var2 = this.options.indexOf(var1);
         JLabel var3 = this.tabs.get(var2);
         this.decorateTabs(var3);
         this.cards.show(var1.getName());
         var1.activated();
      }

      private void decorateTabs(JLabel var1) {
         for (LaunchScreen.Tabs.Tab var3 : this.tabs) {
            var3.setSelected(var3 == var1);
         }
      }

      @Override
      public void decorate(UiCanvas var1) {
         BoxBuilder var2 = this.layoutTabs();
         BorderLayoutBuilder var3 = new BorderLayoutBuilder();
         var3.center(this.cards);
         var3.south(var2);
         var3.decorate(var1);
      }

      private BoxBuilder layoutTabs() {
         return Horizontal.box().topAligned().withBorder(Empty.border(0, 8, 16, 0)).withBackground(Colors.FIELD_BACKGROUND).addAllWithSpace(16, this.tabs);
      }

      @Override
      public void requestFocus() {
         this.tabs.get(0).requestFocusInWindow();
      }

      private class MouseHandler extends MouseAdapter {
         private MouseHandler() {
         }

         @Override
         public void mouseEntered(MouseEvent var1) {
            ((LaunchScreen.Tabs.Tab)var1.getComponent()).setHighlighted(true);
         }

         @Override
         public void mouseExited(MouseEvent var1) {
            ((LaunchScreen.Tabs.Tab)var1.getComponent()).setHighlighted(false);
         }

         @Override
         public void mousePressed(MouseEvent var1) {
            LaunchScreen.Tabs.Tab var2 = (LaunchScreen.Tabs.Tab)var1.getComponent();
            Tabs.this.selectTab(var2);
         }
      }

      private static class Tab extends KongaLabel {
         private static final TextStyle SELECTED_STYLE = WizardStyleSheet.CAPTION_STYLE;
         private static final TextStyle UNSELECTED_STYLE = TextStyle.adjustSize(SELECTED_STYLE, -2);
         private static final Border SELECTED_BORDER = Empty.border(12);
         private static final Border UNSELECTED_BORDER = Empty.border(8);
         private static final Color SELECTED_HIGHLIGHT = Color.LIGHT_GRAY;
         private static final Color UNSELECTED_HIGHLIGHT = Color.GRAY;
         private boolean selected;
         private boolean highlighted;

         public Tab(String var1) {
            super(UiResourceBundle.MNEMONIC_FACTORY.fromString(var1));
         }

         public void setSelected(boolean var1) {
            this.selected = var1;
            if (var1) {
               this.highlighted = false;
            }

            TextStyle var2 = var1 ? SELECTED_STYLE : UNSELECTED_STYLE;
            var2.makeOver(this);
            this.setBorder(var1 ? SELECTED_BORDER : UNSELECTED_BORDER);
            this.repaint();
         }

         public void setHighlighted(boolean var1) {
            this.highlighted = var1;
            this.repaint();
         }

         @Override
         protected void paintComponent(Graphics var1) {
            Graphics2D var2 = (Graphics2D)var1;
            Color var3 = var2.getColor();
            RenderingHints var4 = var2.getRenderingHints();
            Color var5 = this.selected ? LaunchScreen.Tabs.SELECTED_BACKGROUND : LaunchScreen.Tabs.UNSELECTED_BACKGROUND;
            var2.setColor(var5);
            UiUtils.makeAntiAliased(var2);
            Double var6 = new Double(0.0, 0.0, this.getWidth() - 1, this.getHeight(), 8.0, 8.0);
            var2.fill(var6);
            var2.setRenderingHints(var4);
            this.drawTopPart(var2);
            var2.setColor(var3);
            super.paintComponent(var2);
            if (this.highlighted) {
               Double var7 = new Double(4.0, 4.0, this.getWidth() - 8, this.getHeight() - 8, 8.0, 8.0);
               UiUtils.makeAntiAliased(var2);
               var2.setColor(this.selected ? SELECTED_HIGHLIGHT : UNSELECTED_HIGHLIGHT);
               var2.draw(var7);
               var2.setColor(var3);
               var2.setRenderingHints(var4);
            }
         }

         private void drawTopPart(Graphics2D var1) {
            int var2 = this.selected ? 0 : 2;
            Rectangle var3 = new Rectangle(0, var2, this.getWidth() - 1, 10);
            var1.fill(var3);
            if (!this.selected) {
               Rectangle var4 = new Rectangle(0, 0, this.getWidth() - 1, 2);
               var1.setColor(LaunchScreen.Tabs.UNSELECTED_BACKGROUND.darker());
               var1.fill(var4);
            }
         }
      }
   }
}


