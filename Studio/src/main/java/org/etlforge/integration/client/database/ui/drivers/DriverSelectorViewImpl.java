package org.etlforge.integration.client.database.ui.drivers;

import com.google.common.collect.Lists;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.database.ui.resources.DatabaseUiResources;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.server.KnownServer;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ui.AvailableServersComboBox;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.driver.DriverType;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.label.KongaLabel;

final class DriverSelectorViewImpl implements DriverSelectorView {
   private final AvailableServersComboBox serverSelector;
   private final DriverTypeSelector typeSelector;
   private final DriverList driverList;
   private final List<DriverSelectorViewListener> listeners = new CopyOnWriteArrayList<>();
   private Action refreshAction;

   public DriverSelectorViewImpl() {
      this.serverSelector = new AvailableServersComboBox();
      this.serverSelector.addActionListener(new DriverSelectorViewImpl.ServerSelectionListener());
      this.typeSelector = new DriverTypeSelector();
      this.typeSelector.addChangeListener(new DriverSelectorViewImpl.TypeSelectionListener());
      this.driverList = this.createDriverList();
   }

   private DriverList createDriverList() {
      DriverList var1 = new DriverList();
      var1.addSelectionListener(new DriverSelectorViewImpl.DriverSelectionListener());
      var1.setDefaultAction(new DriverSelectorViewImpl.FinalSelectionAction());
      var1.setFilter(this.typeSelector.getFilter());
      return var1;
   }

   @Override
   public DriverDescriptor getSelectedDriver() {
      return this.driverList.getSelectedDriver();
   }

   @Override
   public void setAvailableDrivers(Iterable<DriverDescriptor> var1) {
      this.driverList.setDrivers(var1);
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            DriverSelectorViewImpl.this.requestFocus();
         }
      });
   }

   @Override
   public void setDriverTypes(EnumSet<DriverType> var1) {
      this.typeSelector.setSelectedTypes(var1);
   }

   @Override
   public EnumSet<DriverType> getDriverTypes() {
      return this.typeSelector.getSelectedTypes();
   }

   @Override
   public void setKnownServers(Iterable<KnownServer> var1) {
      this.serverSelector.setKnownServers(Lists.newArrayList(var1));
   }

   @Override
   public ServerInfo getSelectedServer() {
      return this.serverSelector.getSelectedServer();
   }

   @Override
   public void setSelectedServer(ServerInfo var1) {
      this.serverSelector.setSelectedServer(var1);
   }

   @Override
   public void setRefreshAction(Action var1) {
      this.refreshAction = var1;
   }

   @Override
   public void addListener(DriverSelectorViewListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public void removeListener(DriverSelectorViewListener var1) {
      this.listeners.remove(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      DriverSelectorViewImpl.Layout var2 = new DriverSelectorViewImpl.Layout();
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      if (this.driverList.isEmpty()) {
         this.serverSelector.requestFocus();
      } else {
         this.driverList.requestFocus();
      }
   }

   private class DriverSelectionListener implements SelectionListener {
      private DriverSelectionListener() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         DriverDescriptor var2 = DriverSelectorViewImpl.this.getSelectedDriver();

         for (DriverSelectorViewListener var4 : DriverSelectorViewImpl.this.listeners) {
            var4.driverSelectionChanged(var2);
         }
      }
   }

   private class FinalSelectionAction extends AbstractSingleObjectSelectionAction<DriverDescriptorUi> {
      public FinalSelectionAction() {
         super(DriverDescriptorUi.class);
      }

      protected void run(DriverDescriptorUi var1, ActionEvent var2) {
         for (DriverSelectorViewListener var4 : DriverSelectorViewImpl.this.listeners) {
            var4.finalSelectionWasMade(var1.getDriver());
         }
      }
   }

   private class Layout {
      private final OneColumnPanel col;

      public Layout() {
         JPanel var2 = new JPanel();
         this.col = new OneColumnPanel(var2);
         this.col.withInsets(5, 0, 5, 0);
         this.col.setWeightX(1.0);
         if (DriverSelectorViewImpl.this.refreshAction != null) {
            ActionKeyBinding.install(DriverSelectorViewImpl.this.refreshAction, var2, 1);
         }
      }

      public void decorate(UiCanvas var1) {
         if (this.includeServerSelector()) {
            this.layoutCaption();
            this.col.add(DriverSelectorViewImpl.this.serverSelector);
         }

         this.col.withInsets(10, 0, 5, 0);
         this.layoutControlRow();
         this.layoutDriverList();
         this.col.decorate(var1);
      }

      private boolean includeServerSelector() {
         return !ClientInfo.isDataLoader();
      }

      private void layoutCaption() {
         KongaLabel var1 = TextStyles.InfoText.makeLabel(DatabaseUiResources.getString("DriverSelector.Caption"));
         var1.setBorder(Empty.border(2, 0, 2, 0));
         this.col.add(var1);
      }

      private void layoutControlRow() {
         BoxBuilder var1 = BoxBuilder.horizontal();
         var1.add(DriverSelectorViewImpl.this.typeSelector).glue();
         if (DriverSelectorViewImpl.this.refreshAction != null) {
            var1.rigid(10).add(DriverSelectorViewImpl.this.refreshAction);
         }

         this.col.add(var1);
      }

      private void layoutDriverList() {
         this.col.fillOut(new BorderLayoutBuilder(DriverSelectorViewImpl.this.driverList));
      }
   }

   private class ServerSelectionListener implements ActionListener {
      private ServerSelectionListener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ServerInfo var2 = DriverSelectorViewImpl.this.serverSelector.getSelectedServer();

         for (DriverSelectorViewListener var4 : DriverSelectorViewImpl.this.listeners) {
            var4.serverSelectionChanged(var2);
         }
      }
   }

   private class TypeSelectionListener implements ChangeListener {
      private TypeSelectionListener() {
      }

      @Override
      public void stateChanged(ChangeEvent var1) {
         EnumSet var2 = DriverSelectorViewImpl.this.typeSelector.getSelectedTypes();

         for (DriverSelectorViewListener var4 : DriverSelectorViewImpl.this.listeners) {
            var4.driverTypeSelectionChanged(var2);
         }

         DriverSelectorViewImpl.this.driverList.setFilter(DriverSelectorViewImpl.this.typeSelector.getFilter());
      }
   }
}
