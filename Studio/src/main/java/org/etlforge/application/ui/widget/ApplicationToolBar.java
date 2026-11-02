package org.etlforge.application.ui.widget;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.etlforge.ui.Empty;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.widget.button.ButtonUtils;

public final class ApplicationToolBar {
   private final int orientation;
   private JPanel panel;
   private List<JToolBar> toolBars;
   private JButton[] buttons;
   private boolean opaque;

   public ApplicationToolBar() {
      this(0);
   }

   public ApplicationToolBar(int var1) {
      this.orientation = var1;
   }

   public ApplicationToolBar(JButton[] var1) {
      this(var1, 0);
   }

   public ApplicationToolBar(JButton[] var1, int var2) {
      this.setButtons(var1);
      this.orientation = var2;
   }

   public void setToolBars(JToolBar[] var1) {
      Preconditions.checkNotNull(var1, "toolBars");
      this.toolBars = new ArrayList<>();

      for (JToolBar var5 : var1) {
         this.customizeToolBar(var5);
         this.toolBars.add(var5);
      }

      this.layoutToolBars();
   }

   public void setButtons(JButton[] var1) {
      this.buttons = (JButton[])var1.clone();
      this.createToolBars();
      this.layoutToolBars();
   }

   public JPanel getPanel() {
      return this.panel;
   }

   private void createToolBars() {
      this.toolBars = new ArrayList<>();
      JToolBar var1 = null;

      for (JButton var5 : this.buttons) {
         if (var5 == null) {
            if (var1 != null) {
               this.toolBars.add(var1);
               var1 = null;
            }
         } else {
            if (var1 == null) {
               var1 = this.createEmptyToolBar();
            }

            var1.add(var5);
         }
      }

      if (var1 != null) {
         this.toolBars.add(var1);
      }
   }

   private JToolBar createEmptyToolBar() {
      JToolBar var1 = new JToolBar(this.orientation);
      this.customizeToolBar(var1);
      return var1;
   }

   private void customizeToolBar(JToolBar var1) {
      var1.setBorder(Empty.border());
      var1.setFloatable(false);
      var1.setRollover(true);
      var1.setOpaque(this.opaque);
   }

   public void setToolBarOpaque(boolean var1) {
      this.opaque = var1;
   }

   public static void configureIconOnlyButton(JButton var0, Action var1) {
      Preconditions.checkNotNull(var1, "action");
      var0.setAction(var1);
      var0.setIcon((Icon)var1.getValue("SmallIcon"));
      configureIconOnlyButton(var0);
   }

   public static void configureIconOnlyButton(JButton var0) {
      var0.setText("");
      var0.setFocusPainted(false);
      var0.setOpaque(false);
   }

   public static void configureTextOnlyButton(JButton var0, boolean var1) {
      var0.setFocusPainted(false);
      var0.setOpaque(false);
      var0.setContentAreaFilled(false);
      if (var1) {
         ButtonUtils.enableRolloverForButton(var0);
      }
   }

   private void layoutToolBars() {
      this.panel = new JPanel();
      this.panel.setOpaque(false);
      this.layoutToolBarPanel(this.panel);
   }

   protected void layoutToolBarPanel(JPanel var1) {
      BoxBuilder var2 = this.isHorizontal() ? BoxBuilder.horizontal(var1) : BoxBuilder.vertical(var1);
      var2.addAllWithSpace(5, this.toolBars).glue();
   }

   private boolean isHorizontal() {
      return this.orientation == 0;
   }

   public final List<JToolBar> getToolBars() {
      return new ArrayList<>(this.toolBars);
   }
}
