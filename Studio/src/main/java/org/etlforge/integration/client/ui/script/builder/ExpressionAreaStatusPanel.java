package org.etlforge.integration.client.ui.script.builder;

import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.data.script.ScriptStatus;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.util.string.StringLimiter;

public final class ExpressionAreaStatusPanel implements UiProvider {
   private ScriptBuilderModel model;
   private PropertyChangeListener modelListener;
   private final ExpressionArea area;
   private final ScriptModeSelector modeSelector;
   private final JLabel statusLabel;
   private final JLabel rowNumber;
   private final JLabel columnNumber;

   public ExpressionAreaStatusPanel(ExpressionArea var1, ScriptModeSelector var2) {
      this.area = var1;
      this.modeSelector = var2;
      this.statusLabel = createLabel("Shows script status", null);
      this.rowNumber = createLabel("Shows the row number of the current location", TextStyles.DefaultMonoSpaced);
      this.columnNumber = createLabel("Shows the column number of the current location", TextStyles.DefaultMonoSpaced);
      this.register();
   }

   public void setModel(ScriptBuilderModel var1) {
      if (this.model != null) {
         this.model.removePropertyChangeListener(this.modelListener);
      } else {
         this.modelListener = new ExpressionAreaStatusPanel.ModelListener();
      }

      if (var1 != null) {
         var1.addPropertyChangeListener(this.modelListener);
         this.updateStatus(var1.getStatus());
      } else {
         this.updateStatus(ScriptStatus.NONE);
      }

      this.model = var1;
   }

   private void register() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ExpressionAreaStatusPanel.this.area.getTextPane().addCaretListener(new CaretListener() {
               @Override
               public void caretUpdate(CaretEvent var1) {
                  ExpressionAreaStatusPanel.this.updateRowAndColumnNumbers();
               }
            });
         }
      });
   }

   @Override
   public void decorate(UiCanvas var1) {
      NColumnPanel var2 = new NColumnPanel(3).withBorder(new BorderBuilder().etched().empty(2, 0, 2, 0));
      var2.setWeights(0, 1.0, 0.0);
      var2.setVGap(0);
      var2.setFill(0, GridBagLayoutPanel.Fill.HORIZONTAL);
      var2.setAnchors(GridBagLayoutPanel.Anchor.WEST, GridBagLayoutPanel.Anchor.EAST, GridBagLayoutPanel.Anchor.EAST);
      var2.add(this.statusLabel).add(this.modeSelector).add(this.layoutRowAndColumnLabels());
      var2.decorate(var1);
   }

   private Object layoutRowAndColumnLabels() {
      return Horizontal.box().of(this.rowNumber, " : ", this.columnNumber).strut(10);
   }

   @Override
   public void requestFocus() {
   }

   public void updateStatus(ScriptStatus var1) {
      String var2 = var1.getMessage();
      String var3 = var2.replace("\n", "  ");
      StringLimiter var4 = new StringLimiter(var3);
      this.statusLabel.setText(var4.getLimitedString(150));
      String var5 = var2.replace("\n", "<br>");
      this.statusLabel.setToolTipText(var2.isEmpty() ? null : "<html>" + var5 + "</html>");
      this.statusLabel.setIcon(getIcon(var1));
   }

   private static Icon getIcon(ScriptStatus var0) {
      switch (var0.getState()) {
         case VALID:
            return null;
         case INVALID:
            return CommonIcons.ERROR_16;
         case WARNING:
            return CommonIcons.WARNING_16;
         default:
            throw new IllegalStateException("Unexpected ScriptStatus.State: " + var0.getState());
      }
   }

   public void updateRowAndColumnNumbers() {
      EventQueue.invokeLater(
         new Runnable() {
            @Override
            public void run() {
               ExpressionAreaStatusPanel.this.updateRowAndColumnLabels(
                  ExpressionAreaStatusPanel.this.area.getCurrentRow(), ExpressionAreaStatusPanel.this.area.getCurrentColumn()
               );
            }
         }
      );
   }

   private void updateRowAndColumnLabels(int var1, int var2) {
      String var3 = StringUtils.leftPad(String.valueOf(var1), 4, ' ');
      this.rowNumber.setText(var3);
      String var4 = StringUtils.rightPad(String.valueOf(var2), 3, ' ');
      this.columnNumber.setText(var4);
   }

   private static JLabel createLabel(String var0, TextStyle var1) {
      JLabel var2 = new JLabel(" ");
      var2.setToolTipText(var0);
      if (var1 != null) {
         var1.makeOver(var2);
      }

      return var2;
   }

   private class ModelListener implements PropertyChangeListener {
      private ModelListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if ("Status".equals(var1.getPropertyName())) {
            ExpressionAreaStatusPanel.this.updateStatus(ExpressionAreaStatusPanel.this.model.getStatus());
         }
      }
   }
}
