package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import java.awt.Dimension;
import java.util.EnumMap;
import javax.swing.JPanel;
import org.etlforge.application.ui.wizard.NumberedSectionsLayout;
import org.etlforge.integration.mscrm.metadata.MSCrmAttribute;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.CardPanel;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;

final class ConditionUi implements DataEntryUi<Condition> {
   private final SingleAttributeSelector attributeSelector;
   private final OperatorSelector operatorSelector;
   private final CardPanel valuePanel = new CardPanel();
   private final EnumMap<OperatorValueType, ConditionValueUi> cachedValueUis = Maps.newEnumMap(OperatorValueType.class);
   private ConditionValueUi valueUi;

   public ConditionUi(MSCrmObject var1) {
      this.attributeSelector = new SingleAttributeSelector(var1);
      this.operatorSelector = new OperatorSelector();
      this.valuePanel.add(empty(), "empty");
      this.operatorSelector.addSelectionListener(new ConditionUi.OperatorSelectionListener());
   }

   private static JPanel empty() {
      JPanel var0 = new JPanel();
      var0.setPreferredSize(new Dimension(200, 140));
      return var0;
   }

   public void setCondition(Condition var1) {
      this.attributeSelector.setSelectedAttribute(var1.getAttribute());
      this.operatorSelector.setSelectedOperator(var1.getOperator());
      this.valueUi = this.showValueUi(var1.getOperator());
      this.valueUi.setValue(var1.getValue());
   }

   private ConditionValueUi showValueUi(ConditionOperator var1) {
      if (var1 == null) {
         this.valuePanel.show("empty");
         return null;
      }

      OperatorValueType var2 = var1.getValueType();
      String var3 = var2.name();
      ConditionValueUi var4;
      if (this.cachedValueUis.containsKey(var2)) {
         this.valuePanel.show(var3);
         var4 = this.cachedValueUis.get(var2);
      } else {
         var4 = ConditionValueUi.forType(var2);
         this.cachedValueUis.put(var2, var4);
         this.valuePanel.add(var4, var3);
      }

      this.valuePanel.show(var3);
      return var4;
   }

   @Override
   public Optional<Condition> getEnteredValue() {
      MSCrmAttribute var1 = this.attributeSelector.getSelectedAttribute();
      if (var1 != null) {
         ConditionOperator var2 = this.operatorSelector.getSelectedOperator();
         if (var2 != null) {
            Optional var3 = this.valueUi.getEnteredValue();
            if (var3.isPresent()) {
               Condition var4 = new Condition(var1, var2, (ConditionValue)var3.get());
               return Optional.of(var4);
            }
         }
      }

      return Optional.absent();
   }

   @Override
   public EnteredDataValidation validate() {
      if (!this.attributeSelector.hasSelection()) {
         return EnteredDataValidation.error("Please select an attribute.");
      } else {
         return !this.operatorSelector.hasSelection() ? EnteredDataValidation.error("Please select an operator.") : this.valueUi.validate();
      }
   }

   @Override
   public void focusInvalidComonent() {
      if (!this.attributeSelector.hasSelection()) {
         this.attributeSelector.requestFocus();
      } else if (!this.operatorSelector.hasSelection()) {
         this.operatorSelector.requestFocus();
      } else {
         this.valueUi.requestFocus();
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      NumberedSectionsLayout var2 = new NumberedSectionsLayout();
      var2.setFills(new GridBagLayoutPanel.Fill[]{GridBagLayoutPanel.Fill.HORIZONTAL, GridBagLayoutPanel.Fill.HORIZONTAL, GridBagLayoutPanel.Fill.BOTH});
      var2.setWeights(new double[]{1.0, 1.0, 1.0}, new double[]{0.0, 0.0, 1.0});
      var2.setAnchor(GridBagLayoutPanel.Anchor.NORTHWEST);
      var2.addSection("Select Attribute", this.attributeSelector);
      var2.addSection("Select Operator", this.operatorSelector);
      var2.addSection("Enter Value", this.valuePanel);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.attributeSelector.requestFocus();
   }

   private class OperatorSelectionListener implements SelectionListener {
      private OperatorSelectionListener() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         ConditionOperator var2 = ConditionUi.this.operatorSelector.getSelectedOperator();
         ConditionUi.this.valueUi = ConditionUi.this.showValueUi(var2);
      }
   }
}
