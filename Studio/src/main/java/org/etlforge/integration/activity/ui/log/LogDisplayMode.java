package org.etlforge.integration.activity.ui.log;

import org.etlforge.integration.activity.ui.AbstractOperationLogModel;
import org.etlforge.integration.activity.ui.log.tree.LogEntryTreeWidget;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.ui.util.option.Option;
import org.etlforge.ui.util.option.OptionSelector;
import org.etlforge.util.preference.KongaPreferences;

public enum LogDisplayMode implements Option {
   TREE("Tree") {
      @Override
      public LogEntryWidget newDisplayer(AbstractOperationLogModel var1, OperationLogTableColumn... var2) {
         return new LogEntryTreeWidget(var1, var2);
      }
   },
   FLAT("Flat") {
      @Override
      public LogEntryWidget newDisplayer(AbstractOperationLogModel var1, OperationLogTableColumn... var2) {
         return new LogEntryTableWidget(var1, var2);
      }
   };

   private static final String PREF_KEY = "DisplayType";
   private static final KongaPreferences prefs = KongaPreferences.getPreferences(OperationLogPage.class);
   private String displayName;

   LogDisplayMode(String var3) {
      this.displayName = var3;
   }

   public abstract LogEntryWidget newDisplayer(AbstractOperationLogModel var1, OperationLogTableColumn... var2);

   @Override
   public String toString() {
      return this.displayName;
   }

   @Override
   public void decorate(OptionSelector var1) {
      var1.setText(this.displayName);
   }

   public static LogDisplayMode getPreferredMode() {
      return ClientInfo.isDataLoader() ? FLAT : prefs.getEnum("DisplayType", LogDisplayMode.class, TREE);
   }

   public static void storePreferredMode(LogDisplayMode var0) {
      prefs.putEnum("DisplayType", var0);
   }
}
