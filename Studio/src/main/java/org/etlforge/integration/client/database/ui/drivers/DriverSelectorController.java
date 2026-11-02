package org.etlforge.integration.client.database.ui.drivers;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import javax.swing.AbstractAction;
import org.etlforge.application.Application;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.integration.client.database.ui.resources.DatabaseUiResources;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.driver.DriverType;

public final class DriverSelectorController {
   private final DriverSelectorView view;
   private final DriverDownloader downloader;
   private final DriverSelectorController.RefreshAction refreshAction;
   private ListMultimap<ServerGuid, DriverDescriptor> driverCache;

   public DriverSelectorController(DriverSelectorView var1, DriverDownloader var2) {
      Preconditions.checkNotNull(var1, "view");
      Preconditions.checkNotNull(var2, "downloader");
      this.view = var1;
      this.downloader = var2;
      this.refreshAction = new DriverSelectorController.RefreshAction();
      this.driverCache = ArrayListMultimap.create();
      this.configureView(var1);
   }

   private void configureView(DriverSelectorView var1) {
      var1.addListener(new DriverSelectorController.ViewListener());
      var1.setRefreshAction(this.refreshAction);
      this.refreshAction.setServer(var1.getSelectedServer());
      this.refreshAction.setDriverTypes(var1.getDriverTypes());
   }

   public void setDriverCache(Multimap<ServerGuid, DriverDescriptor> var1) {
      this.driverCache = ArrayListMultimap.create(var1);
      this.updateDriverList();
   }

   private void updateDriverList() {
      this.updateDriverList(this.view.getSelectedServer());
   }

   private void updateDriverList(ServerInfo var1) {
      if (var1 != null) {
         List var2 = this.driverCache.get(var1.getGuid());
         this.view.setAvailableDrivers(var2);
      } else {
         this.view.setAvailableDrivers(new ArrayList<>());
      }
   }

   private void downloadDrivers(ServerInfo var1, EnumSet<DriverType> var2) {
      this.downloader.downloadDrivers(var1, var2);
   }

   private class RefreshAction extends AbstractAction {
      private ServerInfo server;
      private EnumSet<DriverType> types;

      public RefreshAction() {
         ActionDecorators.REFRESH.decorate(this);
         this.types = EnumSet.noneOf(DriverType.class);
         this.putValue("ShortDescription", DatabaseUiResources.getString("DriverSelector.Refresh.ToolTip"));
         this.setEnabled(false);
      }

      public void setServer(ServerInfo var1) {
         this.server = var1;
         this.updateEnabledState();
      }

      private void updateEnabledState() {
         this.setEnabled(this.server != null && !this.types.isEmpty());
      }

      public void setDriverTypes(EnumSet<DriverType> var1) {
         this.types = var1;
         this.updateEnabledState();
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         Application.getWorker().submitForParallel(new Runnable() {
            @Override
            public void run() {
               DriverSelectorController.this.downloadDrivers(RefreshAction.this.server, RefreshAction.this.types);
            }
         });
      }
   }

   private class ViewListener extends DriverSelectorViewListener.Adapter {
      private ViewListener() {
      }

      @Override
      public void driverTypeSelectionChanged(EnumSet<DriverType> var1) {
         DriverSelectorController.this.refreshAction.setDriverTypes(var1);
      }

      @Override
      public void serverSelectionChanged(ServerInfo var1) {
         DriverSelectorController.this.refreshAction.setServer(var1);
         DriverSelectorController.this.updateDriverList(var1);
      }
   }
}
