package org.etlforge.ui.widget;

import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.widget.button.ButtonUtils;

public final class StackedSections implements UiProvider {
   private final List<StackedSections.Section> sections = Lists.newArrayList();
   private final OneColumnPanel stack = new OneColumnPanel();
   private boolean exclusive;

   public StackedSections() {
      this.stack.setAnchor(GridBagLayoutPanel.Anchor.NORTHWEST);
      this.stack.setWeightX(1.0);
   }

   public void setExclusive(boolean var1) {
      this.exclusive = var1;
   }

   public void addSection(String var1, Icon var2, JComponent var3) {
      this.addSection(var1, var2, UiProviderAdapter.of(var3));
   }

   public void addSection(String var1, Icon var2, UiProvider var3) {
      StackedSections.Section var4 = new StackedSections.Section(var1, var2, var3);
      this.sections.add(var4);
      this.rebuildUi();
   }

   private void rebuildUi() {
      this.stack.container().removeAll();
      int var1 = this.countOpenSections();

      for (StackedSections.Section var3 : this.sections) {
         if (var3.isVisible()) {
            this.stack.setFill(GridBagLayoutPanel.Fill.BOTH);
            this.stack.setWeightY(1.0 / var1);
         } else {
            this.stack.setFill(GridBagLayoutPanel.Fill.HORIZONTAL);
            this.stack.setWeightY(0.0);
         }

         this.stack.add(var3);
      }

      if (var1 == 0) {
         JComponent var4 = (JComponent)Box.createVerticalGlue();
         this.stack.fillOut(var4);
      }

      this.stack.refresh();
   }

   private int countOpenSections() {
      int var1 = 0;

      for (StackedSections.Section var3 : this.sections) {
         if (var3.isVisible()) {
            var1++;
         }
      }

      return var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.stack.decorate(var1);
   }

   @Override
   public void requestFocus() {
      if (!this.sections.isEmpty()) {
         this.sections.get(0).requestFocus();
      }
   }

   public int getSectionCount() {
      return this.sections.size();
   }

   public void openSection(int var1) {
      this.sections.get(var1).setVisible(true);
   }

   private class Section implements ActionListener, UiProvider {
      private final JToggleButton button;
      private final UiProvider panel;

      public Section(String var2, Icon var3, UiProvider var4) {
         this.panel = var4;
         MnemonicString var5 = UiResourceBundle.MNEMONIC_FACTORY.fromString(var2);
         this.button = new JToggleButton(var5.getText(), var3);
         ButtonUtils.applyMnemonics(this.button, var5);
         this.button.setHorizontalAlignment(10);
         this.updateButtonFont();
         this.button.addActionListener(this);
      }

      private void updateButtonFont() {
         Font var1 = this.button.getFont();
         int var2 = this.isVisible() ? 1 : 0;
         Font var3 = var1.deriveFont(var2);
         this.button.setFont(var3);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.toggle();
      }

      private void toggle() {
         boolean var1 = this.isVisible();
         this.updateButtonFont();
         if (var1 && StackedSections.this.exclusive) {
            this.closeOthers();
         }

         StackedSections.this.rebuildUi();
         if (var1) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  Section.this.requestFocus();
               }
            });
         }
      }

      private void closeOthers() {
         for (StackedSections.Section var2 : StackedSections.this.sections) {
            if (var2 != this) {
               var2.hideSilently();
            }
         }
      }

      private void hideSilently() {
         this.button.setSelected(false);
         this.updateButtonFont();
      }

      @Override
      public void decorate(UiCanvas var1) {
         if (this.isVisible()) {
            BorderLayoutBuilder var2 = new BorderLayoutBuilder();
            var2.north(this.button).center(this.panel);
            var2.decorate(var1);
         } else {
            var1.display(this.button);
         }
      }

      @Override
      public void requestFocus() {
         if (this.isVisible()) {
            this.panel.requestFocus();
         } else {
            this.button.requestFocusInWindow();
         }
      }

      public boolean isVisible() {
         return this.button.isSelected();
      }

      public void setVisible(boolean var1) {
         if (var1 != this.isVisible()) {
            this.button.doClick();
         }
      }
   }
}
