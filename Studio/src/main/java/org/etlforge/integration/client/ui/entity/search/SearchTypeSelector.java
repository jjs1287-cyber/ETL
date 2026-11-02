package org.etlforge.integration.client.ui.entity.search;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.client.ui.entity.resources.EntityResources;
import org.etlforge.ui.Empty;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.widget.combobox.KongaComboBox;
import org.etlforge.ui.widget.label.KongaLabel;

final class SearchTypeSelector implements UiProvider {
   private final KongaComboBox selector;

   public SearchTypeSelector(EntitySearchModel var1) {
      this.selector = this.createSelector(var1);
      this.addListeners(var1);
   }

   private KongaComboBox createSelector(EntitySearchModel var1) {
      KongaComboBox var2 = new KongaComboBox(var1.getSearchTypes());
      var2.setRenderer(new SearchTypeSelector.Renderer());
      var2.setSelectedItem(var1.getSearchType());
      return var2;
   }

   private void addListeners(final EntitySearchModel var1) {
      var1.addPropertyChangeListener(new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1x) {
            if ("search_type".equals(var1x.getPropertyName())) {
               SearchTypeSelector.this.selector.setSelectedItem(var1.getSearchType());
            }
         }
      });
      this.selector.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1x) {
            var1.setSearchType((SearchType)SearchTypeSelector.this.selector.getSelectedItem());
         }
      });
   }

   @Override
   public void decorate(UiCanvas var1) {
      OneColumnPanel var2 = new OneColumnPanel();
      var2.setWeightX(1.0);
      JLabel var3 = this.createLabel();
      var3.setBorder(Empty.border(0, 0, 2, 0));
      var2.addAll(var3, this.selector);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.selector.requestFocusInWindow();
   }

   public void layoutInGrid(TwoColumnLayout var1) {
      var1.addRow(this.createLabel(), this.selector);
   }

   private JLabel createLabel() {
      MnemonicString var1 = ApplicationResources.MNEMONIC_FACTORY.fromString(EntityResources.getString("Search.Mode.Caption"));
      KongaLabel var2 = new KongaLabel(var1);
      var2.setLabelFor(this.selector);
      return var2;
   }

   private static class Renderer extends DefaultListCellRenderer {
      private Renderer() {
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         this.setText(((SearchType)var2).getName());
         return this;
      }
   }
}
