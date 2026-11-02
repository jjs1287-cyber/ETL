package org.etlforge.integration.client.plugin.ui.pipeline.view;

import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

public final class AvailabilityResultTableModel extends TypedAbstractKongaTableModel<AvailabilityProblem> {
   public static final TableColumnMetaData NAME = new TableColumnMetaData("Plugin", String.class, false, 250);
   public static final TableColumnMetaData PROBLEM = new TableColumnMetaData("Problem", AvailabilityProblem.class, false, 400);

   public AvailabilityResultTableModel() {
      super(AvailabilityProblem.class, NAME, PROBLEM);
   }

   protected Object[] getColumnDataForTypedObject(AvailabilityProblem var1) {
      return new Object[]{var1.pluginName, var1};
   }
}
