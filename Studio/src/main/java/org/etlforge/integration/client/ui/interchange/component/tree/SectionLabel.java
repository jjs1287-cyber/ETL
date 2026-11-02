package org.etlforge.integration.client.ui.interchange.component.tree;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D.Double;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.ToolTipManager;
import javax.swing.border.Border;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.focus.FocusUtils;

class SectionLabel extends JLabel {
   private Color highlightedBackground;
   private Color selectedForeground;
   private final Icon validIcon;
   private final Icon invalidIcon;
   private boolean selected;
   private boolean highlighted;
   private boolean invalid;
   private Border selectedBorder;
   private RootFolder rootFolder;
   private int itemCount;

   public SectionLabel(String var1, Icon var2, Icon var3) {
      super(var1, var2, 10);
      this.validIcon = var2;
      this.invalidIcon = var3;
      FocusUtils.makeComponentMouseFocusable(this);
      ToolTipManager.sharedInstance().registerComponent(this);
   }

   public final boolean isSelected() {
      return this.selected;
   }

   public final void setSelected(boolean var1) {
      this.selected = var1;
      this.setOpaque(var1);
      this.repaint();
   }

   public final void setHighlightedBackground(Color var1) {
      this.highlightedBackground = var1;
   }

   public final void setHighlighted(boolean var1) {
      this.highlighted = var1;
      this.repaint();
   }

   public final void setValidationStatus(ValidationStatus var1) {
      this.invalid = var1 == ValidationStatus.INVALID;
      this.repaint();
   }

   public void setSelectedForeground(Color var1) {
      this.selectedForeground = var1;
   }

   public final void setSelectedBorder(Border var1) {
      this.selectedBorder = var1;
   }

   @Override
   public final Border getBorder() {
      return this.selected ? this.selectedBorder : super.getBorder();
   }

   @Override
   public Color getForeground() {
      if (!this.isEnabled()) {
         return super.getForeground();
      } else if (this.selected) {
         return this.selectedForeground;
      } else {
         return this.invalid ? Colors.ERROR_TEXT_COLOR : org.etlforge.application.ui.look.Colors.TEXT_FOREGROUND;
      }
   }

   @Override
   public Icon getIcon() {
      if (!this.isEnabled()) {
         return super.getIcon();
      } else {
         return this.invalid ? this.invalidIcon : this.validIcon;
      }
   }

   @Override
   public void setOpaque(boolean var1) {
      boolean var2 = this.selected || var1;
      super.setOpaque(var2);
   }

   @Override
   public void paintComponent(Graphics var1) {
      if (this.needsHighlight()) {
         Graphics2D var2 = (Graphics2D)var1;
         Color var3 = var2.getColor();
         var2.setColor(this.highlightedBackground);
         RenderingHints var4 = var2.getRenderingHints();
         UiUtils.makeAntiAliased(var2);
         Double var5 = new Double(0.0, 0.0, this.getWidth() - 1, this.getHeight() - 1, 4.0, 4.0);
         var2.fill(var5);
         var2.setRenderingHints(var4);
         var2.setColor(var3);
      }

      super.paintComponent(var1);
   }

   private boolean needsHighlight() {
      return this.highlighted && !this.selected && this.isEnabled();
   }

   public void setRootFolder(RootFolder var1) {
      this.rootFolder = var1;
   }

   @Override
   public String getToolTipText() {
      return this.rootFolder == null ? null : ToolTipStringFactory.getToolTip(this.rootFolder);
   }

   public void setItemCount(int var1) {
      this.itemCount = var1;
      this.repaint();
   }

   @Override
   public String getText() {
      String var1 = super.getText();
      if (this.showItemCount() && this.itemCount > 0) {
         var1 = var1 + " (" + this.itemCount + ")";
      }

      return var1;
   }

   protected boolean showItemCount() {
      return true;
   }
}

