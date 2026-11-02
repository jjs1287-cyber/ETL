package org.etlforge.integration.client.database.ui.drivers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.etlforge.integration.client.database.DriverDescriptorFilter;
import org.etlforge.integration.client.database.ui.resources.DatabaseUiResources;
import org.etlforge.integration.database.driver.DriverDescriptor;

public final class DefaultDriverDescriptorListModel implements DriverDescriptorListModel {
   private final ImmutableList<DriverDescriptorUi> unfiltered;
   private ImmutableList<DriverDescriptorUi> filtered;
   private final List<ListDataListener> listeners;
   private DriverDescriptorFilter filter;

   public DefaultDriverDescriptorListModel() {
      this(new ArrayList<>());
   }

   public DefaultDriverDescriptorListModel(Iterable<DriverDescriptor> var1) {
      this.unfiltered = this.createUnfilteredList(var1);
      this.filtered = this.unfiltered;
      this.listeners = new CopyOnWriteArrayList<>();
   }

   private ImmutableList<DriverDescriptorUi> createUnfilteredList(Iterable<DriverDescriptor> var1) {
      ArrayList var2 = Lists.newArrayList();

      for (DriverDescriptor var4 : var1) {
         var2.add(new DriverDescriptorUi(var4));
      }

      return ImmutableList.copyOf(var2);
   }

   @Override
   public Object fromSelection(Object var1) {
      return var1 instanceof DriverDescriptorUi ? ((DriverDescriptorUi)var1).getDriver() : null;
   }

   @Override
   public Object toSelection(Object var1) {
      return var1 instanceof DriverDescriptor ? new DriverDescriptorUi((DriverDescriptor)var1) : null;
   }

   @Override
   public void setFilter(DriverDescriptorFilter var1) {
      this.filter = var1;
      if (var1 == null) {
         this.filtered = this.unfiltered;
      } else {
         this.filtered = this.createFilteredList();
      }

      this.fireFilterChanged();
   }

   private ImmutableList<DriverDescriptorUi> createFilteredList() {
      ArrayList var1 = Lists.newArrayList();

      for (DriverDescriptorUi var3 : this.unfiltered) {
         if (this.filter.accepts(var3.getDriver())) {
            var1.add(var3);
         }
      }

      return ImmutableList.copyOf(var1);
   }

   @Override
   public DriverDescriptorFilter getFilter() {
      return this.filter;
   }

   @Override
   public Object getElementAt(int var1) {
      return this.filtered.isEmpty() ? DatabaseUiResources.getString("DriverSelector.NoDrivers") : this.filtered.get(var1);
   }

   @Override
   public int getSize() {
      return this.filtered.isEmpty() ? 1 : this.filtered.size();
   }

   @Override
   public void addListDataListener(ListDataListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public void removeListDataListener(ListDataListener var1) {
      this.listeners.remove(var1);
   }

   private void fireFilterChanged() {
      ListDataEvent var1 = new ListDataEvent(this, 0, 0, this.getSize());

      for (ListDataListener var3 : this.listeners) {
         var3.contentsChanged(var1);
      }
   }
}
