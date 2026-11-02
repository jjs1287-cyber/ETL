package org.etlforge.integration.client.database.ui.drivers;

import javax.swing.ListModel;
import org.etlforge.integration.client.database.DriverDescriptorFilter;
import org.etlforge.ui.selection.SelectionConverter;

public interface DriverDescriptorListModel extends ListModel, SelectionConverter {
   void setFilter(DriverDescriptorFilter var1);

   DriverDescriptorFilter getFilter();
}
