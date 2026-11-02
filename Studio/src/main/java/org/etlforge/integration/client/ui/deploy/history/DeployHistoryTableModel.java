package org.etlforge.integration.client.ui.deploy.history;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.deploy.history.DeployHistory;
import org.etlforge.integration.client.deploy.history.DeployResultFilter;
import org.etlforge.integration.client.deploy.history.FilteredDeployHistory;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.util.decorator.DateCell;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

public final class DeployHistoryTableModel extends TypedAbstractKongaTableModel<DeployResult> {
   private static final boolean INCLUDE_MESSAGE = false;
   public static final TableColumnMetaData DATE = new TableColumnMetaData(UiResourceBundle.getString("Date"), DateCell.class, false, 150);
   public static final TableColumnMetaData SERVER = new TableColumnMetaData(Strings.get("Deploy.Result.DeployedTo"), ServerTableItem.class, false, 150);
   public static final TableColumnMetaData USER = new TableColumnMetaData(Strings.get("Deploy.Result.DeployedAs"), UserTableItem.class, false, 75);
   public static final TableColumnMetaData OUTCOME = new TableColumnMetaData(Strings.get("Deploy.Result.Outcome"), OutcomeTableItem.class, false, 125);
   public static final TableColumnMetaData MESSAGE = new TableColumnMetaData(
      Strings.get("Deploy.History.Table.Message.Column"), MessageTableItem.class, false, 500
   );

   public DeployHistoryTableModel() {
      super(DeployResult.class, createColumnMetaData());
   }

   public DeployHistoryTableModel(DeployHistory var1) {
      super(DeployResult.class, createColumnMetaData(), var1.getAllResults());
   }

   private static TableColumnMetaData[] createColumnMetaData() {
      return new TableColumnMetaData[]{DATE, SERVER, USER, OUTCOME};
   }

   public DeployHistoryTableModel(DeployHistory var1, DeployResultFilter var2) {
      this(new FilteredDeployHistory(var1, var2));
   }

   protected Object[] getColumnDataForTypedObject(DeployResult var1) {
      ArrayList var2 = Lists.newArrayList();
      KongaListUtils.add(var2, new DateCell(var1.getTimestamp()), ServerTableItem.create(var1), UserTableItem.create(var1), OutcomeTableItem.create(var1));
      return var2.toArray();
   }
}
