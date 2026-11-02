package org.etlforge.integration.client.ui.interchange.entity;

import javax.swing.Action;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanel;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.client.ui.interchange.location.panel.DataLocationPanel;
import org.etlforge.integration.client.ui.interchange.location.panel.DataLocationPanelFactory;
import org.etlforge.integration.client.ui.interchange.location.panel.LocationPanelDisplayer;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionListeners;
import org.etlforge.ui.util.UiUtils;

public abstract class LocatablePanel extends IntegrationEntityPanel {
   private static final String NO_TYPE = "no_type";
   private Locatable locatable;
   private final LocatableTopPanel topPanel;
   private final LocationPanelDisplayer locationDisplayer;
   private final DataLocationPanelFactory panelFactory;
   private final SelectionListeners selectionListeners;

   protected LocatablePanel(ApplicationWindow var1, Locatable var2, SourceTarget var3, Action var4) {
      super(var1, (IntegrationEntity)var2);
      this.locatable = var2;
      this.panelFactory = new DataLocationPanelFactory((IntegrationEntity)var2, var3) {
         @Override
         protected void configurePanel(DataLocationPanel var1) {
            LocatablePanel.this.configureNewLocationPanel(var1);
         }
      };
      this.topPanel = this.createTopPanel(var2);
      this.locationDisplayer = new LocationPanelDisplayer(this, var4);
      this.selectionListeners = new SelectionListeners();
      this.layoutComponents();
      this.setLocatable(var2);
      this.addInputFields();
      UiUtils.setOpaqueRecursivelySkipDefaults(this.getUi(), false);
   }

   protected final void applyLocation() throws IntegrationDataPanelException {
      DataLocationPanel var1 = this.locationDisplayer.getCurrentLocationPanel();
      var1.apply();
   }

   protected final DataLocation getDisplayedLocation() {
      DataLocationPanel var1 = this.locationDisplayer.getCurrentLocationPanel();
      return var1.getDisplayedObject();
   }

   protected final LocatableTopPanel getTopPanel() {
      return this.topPanel;
   }

   protected void configureNewLocationPanel(DataLocationPanel var1) {
   }

   protected abstract LocatableTopPanel createTopPanel(Locatable var1);

   protected final Locatable getLocatable() {
      return this.locatable;
   }

   public final void onLocationTypeSelected(DataLocationType var1) {
      if (var1 == null) {
         this.locationDisplayer.showLocationPanel("no_type");
      } else {
         DataLocationPanel var2 = this.panelFactory.getPanel(var1);
         this.locationDisplayer.showDataLocationPanel(var2);
      }

      this.fireTypeSelectionChange(var1);
   }

   public final DataLocationType getSelectedLocationType() {
      return this.getTopPanel().getType();
   }

   public final DataLocationPanel getSelectedLocationPanel() {
      return this.locationDisplayer.getCurrentLocationPanel();
   }

   private void layoutComponents() {
      IntegrationDataPanel.Layout var1 = this.getDefaultLayout();
      var1.addToTopGrid(this.topPanel.getTypeSelectorField());
      var1.add(BorderLayoutBuilder.fillWith(this.locationDisplayer), true);
   }

   private void setLocatable(Locatable var1) {
      this.locatable = var1;
      this.topPanel.setLocatable(var1);
      DataLocation var2 = var1.getLocation();
      if (var2 != null) {
         DataLocationPanel var3 = this.panelFactory.getPanel(var2);
         this.locationDisplayer.showDataLocationPanel(var3);
         this.fireTypeSelectionChange(var2.getDataLocationType());
      } else {
         this.fireTypeSelectionChange(null);
      }
   }

   private void addInputFields() {
      this.addInputPanel(this.topPanel);
   }

   public final void showDataLocationPanel(DataLocationPanel var1) {
      this.locationDisplayer.showDataLocationPanel(var1);
      this.fireTypeSelectionChange(var1.getLocationType());
   }

   public final void addTypeSelectionListener(SelectionListener var1) {
      this.selectionListeners.add(var1);
   }

   public final void removeTypeSelectionListener(SelectionListener var1) {
      this.selectionListeners.remove(var1);
   }

   private void fireTypeSelectionChange(DataLocationType var1) {
      OrderedSelection var2 = var1 != null ? SelectionFactory.newOrderedSelection(var1) : OrderedSelection.EMPTY;
      this.selectionListeners.call().selectionChanged(new SelectionChangedEvent(this, var2));
   }
}
