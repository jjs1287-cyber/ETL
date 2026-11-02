package org.etlforge.integration.client.database.ui.drivers;

import java.util.EnumSet;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.integration.client.database.DriverDescriptorFilter;
import org.etlforge.integration.client.database.DriverTypeFilter;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.database.driver.DriverType;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.event.ChangeListeners;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.util.preference.KongaPreferences;

public final class DriverTypeSelector implements UiProvider {
   private static final String ODBC_PREFERENCE = "odbc";
   private static final String JDBC_PREFERENCE = "jdbc";
   private final DriverTypeFilter filter = new DriverTypeFilter();
   private final KongaPreferences preferences = KongaPreferences.getPreferences(DriverTypeSelector.class);
   private final JCheckBox odbcSelector = this.createSelector("&ODBC", "odbc", !ClientInfo.isDataLoader());
   private final JCheckBox jdbcSelector = this.createSelector("&JDBC", "jdbc", true);
   private final ChangeListeners changeListeners = new ChangeListeners();

   public DriverTypeSelector() {
      this.updateFilter();
   }

   public DriverDescriptorFilter getFilter() {
      return this.filter;
   }

   public EnumSet<DriverType> getSelectedTypes() {
      EnumSet var1 = EnumSet.noneOf(DriverType.class);
      if (this.odbcSelector.isSelected()) {
         var1.add(DriverType.ODBC);
      }

      if (this.jdbcSelector.isSelected()) {
         var1.add(DriverType.JDBC);
      }

      return var1;
   }

   public void setSelectedTypes(EnumSet<DriverType> var1) {
      this.odbcSelector.setSelected(var1.contains(DriverType.ODBC));
      this.jdbcSelector.setSelected(var1.contains(DriverType.JDBC));
   }

   @Override
   public void decorate(UiCanvas var1) {
      UiProvider var2 = this.createTwoColumnLayout();
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.odbcSelector.requestFocusInWindow();
   }

   public void addChangeListener(ChangeListener var1) {
      this.changeListeners.add(var1);
   }

   public void removeChangeListener(ChangeListener var1) {
      this.changeListeners.remove(var1);
   }

   private JCheckBox createSelector(String var1, final String var2, boolean var3) {
      boolean var4 = this.preferences.getBoolean(var2, var3);
      KongaCheckBox var5 = new KongaCheckBox(var1, var4);
      var5.addSelectionChangeListener(new Receiver<Boolean>() {
         public void handle(Boolean var1) {
            DriverTypeSelector.this.preferences.putBoolean(var2, var1);
            DriverTypeSelector.this.updateFilter();
         }
      });
      return var5;
   }

   private void updateFilter() {
      this.filter.setAcceptsJdbcDrivers(this.jdbcSelector.isSelected());
      this.filter.setAcceptsOdbcDrivers(this.odbcSelector.isSelected());
      this.changeListeners.call().stateChanged(new ChangeEvent(this));
   }

   private UiProvider createTwoColumnLayout() {
      BoxBuilder var1 = BoxBuilder.horizontal();
      var1.addAllWithSpace(10, "Type of drivers to include:", this.odbcSelector, this.jdbcSelector);
      return var1;
   }
}
