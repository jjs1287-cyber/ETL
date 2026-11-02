package org.etlforge.integration.client.ui.interchange.component.page.panel;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.integration.client.ui.interchange.util.InterchangeViewUtils;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public abstract class IntegrationDataPanel extends InputPanel {
   public abstract IntegrationData getDisplayedObject();

   public abstract void apply() throws IntegrationDataPanelException;

   public void display() {
      InterchangeViewUtils.configurePropertyFields(this.getUi());
   }

   public abstract void setInitialFocus();

   protected JPanel getSubComponentPanel(InputPanel var1) {
      return this.getSubComponentPanel(var1.getUi());
   }

   protected JPanel getSubComponentPanel(JComponent var1) {
      BoxBuilder var2 = BoxBuilder.horizontal();
      return var2.add(var1).glue().nonOpaque().container();
   }

   protected IntegrationDataPanel.Layout getDefaultLayout() {
      return new IntegrationDataPanel.Layout();
   }

   protected final class Layout {
      private final TwoColumnPanel topGrid = new TwoColumnPanel();
      private final OneColumnPanel column;

      private Layout() {
         this.topGrid.withLeftInsets(10, 5, 5, 10).withRightInsets(10, 5, 5, 0);
         this.topGrid.withLeftFill(GridBagLayoutPanel.Fill.HORIZONTAL).withRightFill(GridBagLayoutPanel.Fill.HORIZONTAL);
         this.topGrid.withLeftWeights(0.0, 0.0).withRightWeights(0.1, 0.0);
         this.topGrid.setOpaque(false, false);
         this.column = new OneColumnPanel().nonOpaque().withInsets(0, 5, 15, 0);
         this.column.setWeightX(1.0);
         BorderLayoutBuilder var2 = BorderLayoutBuilder.from(IntegrationDataPanel.this.getUi(), 0, 10);
         var2.north(InvisiblePanel.of(this.topGrid)).center(this.column);
         var2.withEmptyBorder(10, 5, 0, 5);
      }

      public IntegrationDataPanel.Layout addToTopGrid(InputField var1) {
         return this.addToTopGrid(var1.getLabel(), var1.getInputComponent());
      }

      public IntegrationDataPanel.Layout addToTopGrid(JLabel var1, JComponent var2) {
         this.topGrid.addRow(var1, var2);
         return this;
      }

      public IntegrationDataPanel.Layout add(InputField var1) {
         BoxBuilder var2 = BoxBuilder.horizontal().addAllWithSpace(5, var1.getLabel(), var1.getInputComponent()).glue();
         return this.add(var2.container());
      }

      public IntegrationDataPanel.Layout add(InputPanel var1) {
         return this.add(var1.getUi());
      }

      public IntegrationDataPanel.Layout add(InputPanel var1, boolean var2) {
         return this.add(var1.getUi(), var2);
      }

      public IntegrationDataPanel.Layout add(Component var1) {
         return this.add(var1, false);
      }

      public IntegrationDataPanel.Layout add(Component var1, boolean var2) {
         this.prepareAdd(var2);
         if (var2) {
            this.column.add(var1);
         } else {
            this.column.add(InvisiblePanel.of(var1));
         }

         return this;
      }

      public IntegrationDataPanel.Layout add(UiProvider var1) {
         return this.add(var1, false);
      }

      public IntegrationDataPanel.Layout add(UiProvider var1, boolean var2) {
         this.prepareAdd(var2);
         if (var2) {
            this.column.add(var1);
         } else {
            this.column.add(InvisiblePanel.of(var1));
         }

         return this;
      }

      public IntegrationDataPanel.Layout gap(int var1) {
         return this.add(Box.createRigidArea(new Dimension(1, var1)));
      }

      public IntegrationDataPanel.Layout glue() {
         return this.add(Box.createVerticalGlue(), true);
      }

      public void setBottomGap(int var1) {
         this.column.withInsets(0, 5, var1, 0);
      }

      private void prepareAdd(boolean var1) {
         if (var1) {
            this.column.setFillOut();
         } else {
            this.column.setFill(OneColumnPanel.HORIZONTAL);
            this.column.setWeightY(0.0);
         }
      }
   }
}
