package org.etlforge.integration.client.database.ui.drivers;

import java.awt.EventQueue;
import java.util.Comparator;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.collections.Iterables2;
import org.etlforge.integration.client.database.DriverDescriptorFilter;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListSelectionMode;

final class DriverList implements UiProvider {
   private DriverDescriptorListModel model = new DefaultDriverDescriptorListModel();
   private final KongaList list = this.createList();

   public DriverList() {
   }

   private KongaList createList() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setPrototypeValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
      var1.setLabel(KongaLabel.withMnemonic("&Drivers"));
      var1.setVisibleRowCount(8);
      var1.setSelectionMode(ListSelectionMode.SINGLE_SELECTION);
      return var1.newList(this.model);
   }

   public boolean isEmpty() {
      return this.model.getSize() == 0;
   }

   public void setDrivers(Iterable<DriverDescriptor> var1) {
      DriverDescriptorFilter var2 = this.model.getFilter();
      this.model = new DefaultDriverDescriptorListModel(this.sortByName(var1));
      this.model.setFilter(var2);
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            DriverList.this.list.setModel(DriverList.this.model);
         }
      });
   }

   private Iterable<DriverDescriptor> sortByName(Iterable<DriverDescriptor> var1) {
      return Iterables2.sortedCopy(var1, new Comparator<DriverDescriptor>() {
         public int compare(DriverDescriptor var1, DriverDescriptor var2) {
            return var1.getName().compareToIgnoreCase(var2.getName());
         }
      });
   }

   public DriverDescriptor getSelectedDriver() {
      IndexedSelection var1 = this.list.getSelection();
      Object var2 = var1.head();
      return var2 instanceof DriverDescriptorUi ? ((DriverDescriptorUi)var2).getDriver() : null;
   }

   public void setFilter(DriverDescriptorFilter var1) {
      this.model.setFilter(var1);
   }

   public void addSelectionListener(SelectionListener var1) {
      this.list.addSelectionListener(var1);
   }

   public void setDefaultAction(SelectionAction var1) {
      this.list.setDefaultAction(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.list.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.list.requestFocus();
      if (this.list.getSelection().isEmpty()) {
         this.list.selectFirst();
      }
   }
}
