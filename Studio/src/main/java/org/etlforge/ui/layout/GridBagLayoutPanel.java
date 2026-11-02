package org.etlforge.ui.layout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.canvas.ContainerCanvas;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.panel.KongaPanel;

public abstract class GridBagLayoutPanel extends AbstractLayoutBuilder implements UiProvider {
   public static final GridBagLayoutPanel.Anchor NORTHWEST = GridBagLayoutPanel.Anchor.NORTHWEST;
   public static final GridBagLayoutPanel.Anchor NORTH = GridBagLayoutPanel.Anchor.NORTH;
   public static final GridBagLayoutPanel.Anchor NORTHEAST = GridBagLayoutPanel.Anchor.NORTHEAST;
   public static final GridBagLayoutPanel.Anchor EAST = GridBagLayoutPanel.Anchor.EAST;
   public static final GridBagLayoutPanel.Anchor SOUTHEAST = GridBagLayoutPanel.Anchor.SOUTHEAST;
   public static final GridBagLayoutPanel.Anchor SOUTH = GridBagLayoutPanel.Anchor.SOUTH;
   public static final GridBagLayoutPanel.Anchor SOUTHWEST = GridBagLayoutPanel.Anchor.SOUTHWEST;
   public static final GridBagLayoutPanel.Anchor WEST = GridBagLayoutPanel.Anchor.WEST;
   public static final GridBagLayoutPanel.Anchor CENTER = GridBagLayoutPanel.Anchor.CENTER;
   public static final GridBagLayoutPanel.Fill NONE = GridBagLayoutPanel.Fill.NONE;
   public static final GridBagLayoutPanel.Fill HORIZONTAL = GridBagLayoutPanel.Fill.HORIZONTAL;
   public static final GridBagLayoutPanel.Fill VERTICAL = GridBagLayoutPanel.Fill.VERTICAL;
   public static final GridBagLayoutPanel.Fill BOTH = GridBagLayoutPanel.Fill.BOTH;
   private final JPanel panel;

   protected GridBagLayoutPanel(JPanel var1) {
      if (var1 != null) {
         this.panel = var1;
         this.panel.setLayout(new GridBagLayout());
      } else {
         this.panel = new KongaPanel(new GridBagLayout());
      }
   }

   public final JPanel container() {
      return this.panel;
   }

   public GridBagLayoutPanel withBorder(Border var1) {
      this.panel.setBorder(var1);
      return this;
   }

   public GridBagLayoutPanel withEmptyBorder(int var1) {
      return this.withBorder(Empty.border(var1));
   }

   public GridBagLayoutPanel nonOpaque() {
      this.setOpaque(false, false);
      return this;
   }

   public GridBagLayoutPanel deeplyNonOpaque() {
      this.setOpaque(false, true);
      return this;
   }

   public GridBagLayoutPanel setOpaque(boolean var1, boolean var2) {
      if (var2) {
         UiUtils.setOpaqueRecursivelySkipDefaults(this.panel, var1);
      } else {
         this.panel.setOpaque(var1);
      }

      return this;
   }

   public final GridBagLayoutPanel setEnabled(boolean var1) {
      UiUtils.setEnabledRecursively(this.panel, var1);
      return this;
   }

   public final GridBagLayoutPanel setFocusCycleRoot(boolean var1) {
      this.panel.setFocusCycleRoot(var1);
      return this;
   }

   public final GridBagLayoutPanel setPainter(ComponentPainter var1) {
      if (this.panel instanceof KongaPanel) {
         ((KongaPanel)this.panel).setPainter(var1);
      }

      return this;
   }

   public final void addToParent(Container var1, Object var2) {
      var1.add(this.panel, var2);
   }

   protected final void add(Object var1, GridBagConstraints var2) {
      if (var1 instanceof Component) {
         this.add(this.forComponent((Component)var1), var2);
      } else if (var1 instanceof String) {
         this.add(this.forString((String)var1), var2);
      } else if (var1 instanceof Action) {
         this.add(this.forAction((Action)var1), var2);
      } else if (var1 instanceof Icon) {
         this.add(this.forIcon((Icon)var1), var2);
      } else if (var1 instanceof GridBagLayoutPanel) {
         this.add((GridBagLayoutPanel)var1, var2);
      } else if (var1 != null) {
         this.add(this.forObject(var1), var2);
      }
   }

   protected final void add(Component var1, GridBagConstraints var2) {
      Component var3 = var1 instanceof JCheckBox ? InvisiblePanel.of(var1) : var1;
      this.panel.add(var3, var2);
   }

   protected final void add(UiProvider var1, GridBagConstraints var2) {
      ContainerCanvas.decorate(var1, this.panel, var2);
   }

   protected final void add(GridBagLayoutPanel var1, GridBagConstraints var2) {
      this.panel.add(var1.panel, var2);
   }

   @Override
   public final void decorate(UiCanvas var1) {
      var1.display(this.panel);
   }

   @Override
   public final void requestFocus() {
      this.traverseAndSetFocus(this.panel);
   }

   private boolean traverseAndSetFocus(Container var1) {
      Component[] var2 = var1.getComponents();
      if (var2.length > 0) {
         for (Component var6 : var2) {
            if (this.isFocusable(var6)) {
               var6.requestFocusInWindow();
               return true;
            }

            if (var6 instanceof Container) {
               boolean var7 = this.traverseAndSetFocus((Container)var6);
               if (var7) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean isFocusable(Component var1) {
      return var1.isFocusable() && !(var1 instanceof JLabel) && !(var1 instanceof JPanel);
   }

   public final GridBagLayoutPanel removeAll() {
      this.panel.removeAll();
      this.refresh();
      return this;
   }

   public final GridBagLayoutPanel refresh() {
      UiUtils.revalidate(this.panel);
      return this;
   }

   public GridBagLayoutPanel withBackground(Color var1) {
      this.panel.setBackground(var1);
      return this;
   }

   public final GridBagLayoutPanel setAlignmentX(float var1) {
      this.panel.setAlignmentX(var1);
      return this;
   }

   public final GridBagLayoutPanel setAlignmentY(float var1) {
      this.panel.setAlignmentY(var1);
      return this;
   }

   public GridBagLayoutPanel withFocusTraversalPolicy(FocusTraversalPolicy var1) {
      this.panel.setFocusTraversalPolicy(var1);
      return this;
   }

   public GridBagLayoutPanel asFocusCycleRoot() {
      this.panel.setFocusCycleRoot(true);
      return this;
   }

   public final boolean isEmpty() {
      return this.panel.getComponentCount() == 0;
   }

   public enum Anchor {
      NORTHWEST(18),
      NORTH(11),
      NORTHEAST(12),
      EAST(13),
      SOUTHEAST(14),
      SOUTH(15),
      SOUTHWEST(16),
      WEST(17),
      CENTER(10);

      private final int swingCode;

      Anchor(int var3) {
         this.swingCode = var3;
      }

      int getSwingCode() {
         return this.swingCode;
      }
   }

   public enum Fill {
      NONE(0),
      HORIZONTAL(2),
      VERTICAL(3),
      BOTH(1);

      private final int swingCode;

      Fill(int var3) {
         this.swingCode = var3;
      }

      int getSwingCode() {
         return this.swingCode;
      }
   }
}
