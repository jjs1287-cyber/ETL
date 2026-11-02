package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import com.google.common.base.Preconditions;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteQueryPageSize;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.property.SingleSelectionBinding;
import org.etlforge.ui.widget.combobox.ComboBoxSelectionSource;
import org.etlforge.ui.widget.combobox.KongaComboBox;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.util.Property;

final class PageSizeSelector implements UiProvider {
   private final KongaComboBox dropdown = createDropdown();

   private static KongaComboBox createDropdown() {
      KongaComboBox var0 = new KongaComboBox(NetSuiteQueryPageSize.getSupportedSizes());
      var0.setMaximumRowCount(Math.min(16, var0.getItemCount()));
      var0.setPrototypeDisplayValue(NetSuiteQueryPageSize.DEFAULT);
      var0.setRenderer(new DefaultListCellRenderer() {
         @Override
         public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
            super.getListCellRendererComponent(var1, var2, var3, var4, var5);
            if (var2 == NetSuiteQueryPageSize.DEFAULT) {
               this.setText("Default page size    ");
            } else {
               this.setText(var2.toString());
            }

            return this;
         }
      });
      return var0;
   }

   public Binding bindWithModel(QueryBuilderModel var1) {
      Property var2 = var1.getProperty("pageSize");
      return SingleSelectionBinding.of(var2, new ComboBoxSelectionSource(this.dropdown), NetSuiteQueryPageSize.class);
   }

   @Override
   public void decorate(UiCanvas var1) {
      KongaLabel var2 = KongaLabel.withMnemonic("Search &page size:");
      this.dropdown.setLabel(var2);
      Horizontal.box().addAllWithSpace(10, var2, this.dropdown).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.dropdown.requestFocusInWindow();
   }

   public NetSuiteQueryPageSize getSelectedSize() {
      Object var1 = this.dropdown.getSelectedItem();
      return var1 instanceof NetSuiteQueryPageSize ? (NetSuiteQueryPageSize)var1 : NetSuiteQueryPageSize.DEFAULT;
   }

   public void setSelectedSize(NetSuiteQueryPageSize var1) {
      Preconditions.checkNotNull(var1);
      this.dropdown.setSelectedItem(var1);
   }
}
