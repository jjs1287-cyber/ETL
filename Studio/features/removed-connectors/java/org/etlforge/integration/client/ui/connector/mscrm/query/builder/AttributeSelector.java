package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.etlforge.integration.mscrm.metadata.MSCrmAttribute;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.property.BooleanBinding;
import org.etlforge.ui.util.KeyValueToolTip;
import org.etlforge.ui.widget.SelectorTable;
import org.etlforge.ui.widget.SelectorTableBindings;
import org.etlforge.ui.widget.SelectorTableFilterField;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.util.string.StringLimiter;

final class AttributeSelector implements UiProvider {
   private final KongaCheckBox useAllAttrsCheckBox = new KongaCheckBox("&Use All Attributes");
   private final SelectorTable<MSCrmAttribute> attributeList = createAttributeList();
   private final SelectorTableFilterField<MSCrmAttribute> filterField = SelectorTableFilterField.forTable(
         this.attributeList, new AttributeSelector.FilterFunction()
      )
      .withLabel("&Filter: ");
   private final AttributeSelector.ClearSelectionAction clearSelectionAction;

   public AttributeSelector(AttributeSelectionModel var1) {
      this.clearSelectionAction = new AttributeSelector.ClearSelectionAction(var1);
      this.bindToModel(var1);
   }

   private static SelectorTable<MSCrmAttribute> createAttributeList() {
      SelectorTable var0 = SelectorTable.empty(MSCrmAttribute.class);
      var0.setVisibleRows(10);
      var0.setObjectCellRenderer(new AttributeSelector.Renderer());
      return var0;
   }

   private void bindToModel(AttributeSelectionModel var1) {
      SelectorTableBindings var2 = new SelectorTableBindings(var1, "available_attrs", "selected_attrs", "disabled_attrs");
      Bindings var3 = Bindings.of(
         var2.install(this.attributeList),
         BooleanBinding.bind(var1, "use_all_attrs", this.useAllAttrsCheckBox),
         new AttributeSelector.IndividualAttributesSelectionEnabledBinding(var1)
      );
      var3.syncUi();
   }

   @Override
   public void decorate(UiCanvas var1) {
      new BorderLayoutBuilder(0, 5).northToSouth(this.layoutUseAllCheckBoxAndFilterField(), this.attributeList, this.layoutButtons()).decorate(var1);
   }

   private UiProvider layoutUseAllCheckBoxAndFilterField() {
      return Vertical.grid(5).of(this.useAllAttrsCheckBox, this.filterField);
   }

   private UiProvider layoutButtons() {
      Action var1 = this.getSelectAllAction();
      Grid var2 = Horizontal.grid().of(var1, this.clearSelectionAction).withHGap(10);
      return Horizontal.Align.east(var2);
   }

   private Action getSelectAllAction() {
      Action var1 = this.attributeList.getSelectAllAction();
      var1.putValue("Name", "Select &All");
      return var1;
   }

   @Override
   public void requestFocus() {
      this.attributeList.requestFocus();
   }

   private static class ClearSelectionAction extends AbstractAction {
      private final AttributeSelectionModel model;

      public ClearSelectionAction(AttributeSelectionModel var1) {
         super("&Remove All");
         this.model = var1;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.model.clearSelection();
      }
   }

   private static class Filter implements Predicate<MSCrmAttribute> {
      private final String filter;

      public Filter(String var1) {
         this.filter = var1.toLowerCase();
      }

      public boolean apply(MSCrmAttribute var1) {
         return var1.getName().toLowerCase().contains(this.filter);
      }
   }

   private static class FilterFunction implements Function<String, Predicate<MSCrmAttribute>> {
      private FilterFunction() {
      }

      public Predicate<MSCrmAttribute> apply(String var1) {
         return var1.isEmpty() ? Predicates.alwaysTrue() : new AttributeSelector.Filter(var1);
      }
   }

   private class IndividualAttributesSelectionEnabledBinding extends AbstractBinding<Boolean> {
      public IndividualAttributesSelectionEnabledBinding(AttributeSelectionModel var2) {
         super(var2, "use_all_attrs");
      }

      protected void updateUiWithValue(Boolean var1) {
         boolean var2 = !var1;
         AttributeSelector.this.attributeList.setEnabled(var2);
         AttributeSelector.this.filterField.setEnabled(var2);
      }
   }

   private static class Renderer extends DefaultTableCellRenderer {
      private final AttributeSelector.Renderer.ToolTipProvider tooltip = new AttributeSelector.Renderer.ToolTipProvider();

      private Renderer() {
      }

      @Override
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         this.render((MSCrmAttribute)var2);
         return this;
      }

      private void render(MSCrmAttribute var1) {
         this.setText(var1.getName());
         this.tooltip.attribute = var1;
      }

      @Override
      public String getToolTipText() {
         return this.tooltip.get();
      }

      private static class ToolTipProvider implements Supplier<String> {
         public MSCrmAttribute attribute;

         private ToolTipProvider() {
         }

         public String get() {
            if (this.attribute == null) {
               return null;
            }

            LinkedHashMap var1 = Maps.newLinkedHashMap();
            var1.put("Description", StringLimiter.limit(this.attribute.getDescription(), 100));
            var1.put("Type", this.attribute.getType().toString());
            return KeyValueToolTip.createTable(var1);
         }
      }
   }
}
