package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Optional;
import java.util.List;
import org.etlforge.integration.mscrm.metadata.MSCrmAttribute;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneRowPanel;
import org.etlforge.ui.widget.combobox.EnumComboBox;
import org.etlforge.util.SortOrder;

final class OrderDefinitionUi implements DataEntryUi<Order> {
   private final SingleAttributeSelector attributeSelector;
   private final EnumComboBox<SortOrder> directionSelector = EnumComboBox.of(SortOrder.ASCENDING, SortOrder.DESCENDING);

   public OrderDefinitionUi(List<MSCrmAttribute> var1) {
      this.attributeSelector = new SingleAttributeSelector(var1);
      this.directionSelector.setSelectedItem(SortOrder.ASCENDING);
   }

   @Override
   public Optional<Order> getEnteredValue() {
      MSCrmAttribute var1 = this.attributeSelector.getSelectedAttribute();
      if (var1 != null) {
         SortOrder var2 = this.directionSelector.getSelectedItem();
         return Optional.of(new Order(var1.getName(), var2));
      } else {
         return Optional.absent();
      }
   }

   public void setOrder(Order var1) {
      this.attributeSelector.setSelectedAttributeByName(var1.getAttribute());
      this.directionSelector.setSelectedItem(var1.getDirection());
   }

   @Override
   public void decorate(UiCanvas var1) {
      new OneRowPanel()
         .setAnchor(GridBagLayoutPanel.Anchor.SOUTHWEST)
         .setFill(GridBagLayoutPanel.Fill.HORIZONTAL)
         .withInsets(0, 0, 0, 12)
         .setWeightX(0.5)
         .of(this.attributeSelector, this.directionSelector)
         .decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.attributeSelector.requestFocus();
   }

   @Override
   public EnteredDataValidation validate() {
      return this.attributeSelector.getSelectedAttribute() == null ? EnteredDataValidation.error("Please select an attribute.") : EnteredDataValidation.OK;
   }

   @Override
   public void focusInvalidComonent() {
      this.attributeSelector.requestFocus();
   }
}
