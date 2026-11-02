package org.etlforge.integration.client.ui.interchange.location.panel;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.CardPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.border.TitledToolbar;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class LocationPanelDisplayer implements UiProvider {
   public static final String NO_LOCATION = "no_location";
   private final InputPanel parent;
   private DataLocationPanel displayedLocationPanel;
   private final CardPanel locationPanelContainer;
   private final Map<DataLocationType, JComponent> mapLocationPanels;
   private final Action testConnection;

   public LocationPanelDisplayer(InputPanel var1, Action var2) {
      Preconditions.checkNotNull(var1, "parent");
      this.parent = var1;
      this.testConnection = var2;
      this.mapLocationPanels = Maps.newHashMap();
      this.locationPanelContainer = new CardPanel();
      this.locationPanelContainer.add(new JPanel(), "no_location");
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.locationPanelContainer.decorate(var1);
   }

   @Override
   public void requestFocus() {
   }

   public void showDataLocationPanel(final DataLocationPanel var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            LocationPanelDisplayer.this.showDataLocationPanelImpl(var1);
         }
      });
   }

   private void showDataLocationPanelImpl(DataLocationPanel var1) {
      if (var1 != this.displayedLocationPanel) {
         if (this.displayedLocationPanel != null) {
            this.parent.removeInputPanel(this.displayedLocationPanel);
         }

         DataLocationType var2 = var1.getDisplayedObject().getDataLocationType();
         if (!this.mapLocationPanels.containsKey(var2)) {
            this.prepareLocationPanelForDisplay(var1);
            this.createConnectionParamsPanel(var2, var1);
         }

         UiUtils.setOpaqueRecursivelySkipDefaults(var1.getUi(), false);
         this.displayedLocationPanel = var1;
         this.locationPanelContainer.show(var2.name());
      }
   }

   private void prepareLocationPanelForDisplay(final DataLocationPanel var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            var1.display();
         }
      });
   }

   private void createConnectionParamsPanel(DataLocationType var1, DataLocationPanel var2) {
      String var3 = Strings.get("Location.Title");
      TitledToolbar var4 = new TitledToolbar(var2.getUi(), var3);
      if (this.testConnection != null) {
         var4.addButton(new KongaButton(this.testConnection).setUseSmallActionIcon(true));
      }

      StyleSheet.configureTitledToolbar(var4);
      JPanel var5 = InvisiblePanel.of(var4);
      this.locationPanelContainer.add(var5, var1.name());
      this.parent.addInputPanel(var2);
      this.mapLocationPanels.put(var1, var5);
   }

   public DataLocationPanel getCurrentLocationPanel() {
      return this.displayedLocationPanel;
   }

   public void showLocationPanel(String var1) {
      this.locationPanelContainer.show(var1);
   }
}
