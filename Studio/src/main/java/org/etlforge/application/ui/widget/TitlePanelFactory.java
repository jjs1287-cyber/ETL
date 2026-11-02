package org.etlforge.application.ui.widget;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.focus.FocusUtils;

public class TitlePanelFactory {
   private TitlePanelFactory.TitlePanel panel;
   private boolean active = true;
   private boolean enabled = true;
   private boolean includeToolBar = true;
   private String title;
   private Border border;
   private Color background;
   private TextStyle titleStyle;

   public final TitlePanelFactory.TitlePanel createPanel() {
      this.panel = new TitlePanelFactory.TitlePanel(this.title);
      if (this.border != null) {
         this.panel.setBorder(this.border);
      }

      if (this.background != null) {
         this.panel.setBackground(this.background);
      }

      if (this.titleStyle != null) {
         this.panel.setTitleStyle(this.titleStyle);
      }

      this.panel.setAdditionalComponents(this.getAdditionalToolbarComponents());
      if (this.includeToolBar) {
         JButton[] var1 = this.getToolBarButtons();
         if (var1.length > 0) {
            ApplicationToolBar var2 = new ApplicationToolBar(this.getToolBarButtons());
            this.panel.setToolBars(var2.getToolBars());
         }
      }

      if (this.active) {
         this.panel.activated();
      }

      this.panel.setEnabled(this.enabled);
      return this.panel;
   }

   protected JComponent[] getAdditionalToolbarComponents() {
      return new JComponent[0];
   }

   protected JButton[] getToolBarButtons() {
      return new JButton[0];
   }

   public final void setTitle(String var1) {
      if (this.panel != null) {
         this.panel.setTitle(var1);
      } else {
         this.title = var1;
      }
   }

   public final String getTitle() {
      return this.title;
   }

   public final void setEnabled(boolean var1) {
      if (this.panel != null) {
         this.panel.setEnabled(var1);
      } else {
         this.enabled = var1;
      }
   }

   public final void setActive(boolean var1) {
      if (this.panel != null) {
         if (var1) {
            this.panel.activated();
         } else {
            this.panel.deactivated();
         }
      } else {
         this.active = var1;
      }
   }

   public final void setBorder(Border var1) {
      if (this.panel != null) {
         this.panel.setBorder(var1);
      } else {
         this.border = var1;
      }
   }

   public void setBackground(Color var1) {
      if (this.panel != null) {
         this.panel.setBackground(var1);
      } else {
         this.background = var1;
      }
   }

   public void setTitleStyle(TextStyle var1) {
      if (this.panel != null) {
         this.panel.setTitleStyle(var1);
      } else {
         this.titleStyle = var1;
      }
   }

   public final void setIncludeToolBar(boolean var1) {
      this.includeToolBar = var1;
   }

   public static class TitlePanel implements UiProvider {
      private final JPanel panel;
      private String title;
      private final JLabel titleLabel;
      private List<JToolBar> toolBars;
      private JComponent[] additionalComponents;
      private Color background;
      private TextStyle titleStyle;

      TitlePanel(String var1) {
         this.title = var1;
         this.titleLabel = TextStyles.TreeTitleUnfocusedText.makeLabel(var1);
         this.panel = createPanel();
         this.layoutPanel(false);
      }

      private static JPanel createPanel() {
         JPanel var0 = new JPanel(new BorderLayout());
         var0.setBorder(StyleSheet.TreeTitleBorder);
         var0.setBackground(Colors.TREE_TITLE_UNFOCUSED_BACKGROUND);
         FocusUtils.makeComponentMouseFocusable(var0);
         return var0;
      }

      @Override
      public void decorate(UiCanvas var1) {
         var1.display(this.panel);
      }

      @Override
      public void requestFocus() {
      }

      public void setEnabled(boolean var1) {
         UiUtils.setEnabledRecursively(this.panel, var1);
      }

      public void setBorder(Border var1) {
         this.panel.setBorder(var1);
      }

      public void setBackground(Color var1) {
         this.background = var1;
         this.panel.setBackground(var1);
      }

      public void setTitleStyle(TextStyle var1) {
         this.titleStyle = var1;
         var1.makeOver(this.titleLabel);
      }

      private void layoutPanel(boolean var1) {
         if (var1) {
            this.panel.removeAll();
         }

         this.panel.add(this.titleLabel, "West");
         if (this.hasAdditionalComponents()) {
            this.layoutAdditionalComponents();
         } else {
            this.panel.add(Box.createHorizontalGlue(), "Center");
         }

         if (this.toolBars != null) {
            this.layoutToolbars();
         }
      }

      private boolean hasAdditionalComponents() {
         return this.additionalComponents != null && this.additionalComponents.length > 0;
      }

      private void layoutAdditionalComponents() {
         Horizontal.box().nonOpaque().addAllWithSpace(10, this.additionalComponents).glue().addToParent(this.panel, "Center");
      }

      private void layoutToolbars() {
         Horizontal.box().nonOpaque().addAllWithSpace(5, this.toolBars).addToParent(this.panel, "East");
      }

      String getTitle() {
         return this.title;
      }

      void setTitle(String var1) {
         this.title = var1;
         this.titleLabel.setText(var1);
      }

      void setToolBars(List<JToolBar> var1) {
         this.toolBars = var1;
         this.layoutPanel(true);
      }

      void setAdditionalComponents(JComponent[] var1) {
         this.additionalComponents = var1;
         this.layoutPanel(true);
      }

      public void activated() {
         if (this.background == null) {
            this.panel.setBackground(Colors.TREE_TITLE_FOCUSED_BACKGROUND);
         }

         if (this.titleStyle == null) {
            TextStyles.TreeTitleFocusedText.makeOver(this.titleLabel);
         }
      }

      public void deactivated() {
         if (this.background == null) {
            this.panel.setBackground(Colors.TREE_TITLE_UNFOCUSED_BACKGROUND);
         }

         if (this.titleStyle == null) {
            TextStyles.TreeTitleUnfocusedText.makeOver(this.titleLabel);
         }
      }
   }
}
