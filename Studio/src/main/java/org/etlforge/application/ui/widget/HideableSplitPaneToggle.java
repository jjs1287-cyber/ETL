package org.etlforge.application.ui.widget;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JToggleButton;
import org.etlforge.ui.widget.button.KongaToggleButton;

public final class HideableSplitPaneToggle {
   private final JToggleButton toggle;
   private final HideableSplitPane splitPane;
   private String tooltipWhenHidden;
   private String tooltipWhenDisplayed;

   public HideableSplitPaneToggle(HideableSplitPane var1, Icon var2) {
      this(var1, null, var2);
   }

   public HideableSplitPaneToggle(HideableSplitPane var1, String var2, Icon var3) {
      this(var1, new KongaToggleButton(var2, var3));
   }

   public HideableSplitPaneToggle(HideableSplitPane var1, JToggleButton var2) {
      this.splitPane = var1;
      this.toggle = var2;
      this.updateToggle();
      var2.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            HideableSplitPaneToggle.this.toggleSplitPane();
         }
      });
      var1.addPropertyChangeListener("hidden", new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            HideableSplitPaneToggle.this.updateToggle();
         }
      });
   }

   private void toggleSplitPane() {
      boolean var1 = !this.toggle.isSelected();
      this.splitPane.setDividerHidden(var1);
   }

   private void updateToggle() {
      boolean var1 = this.splitPane.isDividerHidden();
      this.toggle.setSelected(!var1);
      if (var1) {
         this.toggle.setToolTipText(this.tooltipWhenHidden);
      } else {
         this.toggle.setToolTipText(this.tooltipWhenDisplayed);
      }
   }

   public JToggleButton getToggle() {
      return this.toggle;
   }

   public void setToolTipWhenHidden(String var1) {
      this.tooltipWhenHidden = var1;
      this.updateToggle();
   }

   public void setToolTipWhenDisplayed(String var1) {
      this.tooltipWhenDisplayed = var1;
      this.updateToggle();
   }
}
