package org.etlforge.integration.client.ui.interchange.location.export;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.entity.selector.EntitySelector;
import org.etlforge.integration.client.ui.entity.selector.EntityTableSelector;
import org.etlforge.integration.client.ui.interchange.location.resources.LocationResources;
import org.etlforge.integration.data.ExportableProperty;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.label.KongaLabel;

final class ExportSettingsDialog extends KongaDialog {
   private final ExportSettingsDialog.SelectionListenerImpl selectionListener = new ExportSettingsDialog.SelectionListenerImpl();
   private final ExportablePropertySelector propertySelector;
   private final EntitySelector<IntegrationEntity> destinationSelector;

   private ExportSettingsDialog(JFrame var1, DataLocation var2, List<? extends IntegrationEntity> var3) {
      super(var1, LocationResources.getString("ExportSettingsDialog.Title"), true);
      this.propertySelector = this.createPropertySelector(var2.getExportableProperties());
      this.destinationSelector = this.createDestinationSelector(var3);
      this.createContentPane();
      this.setOkButtonAsDefault();
      this.updateOkButton();
      this.manageLocation();
   }

   @Override
   protected void onDialogOpened() {
      super.onDialogOpened();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ExportSettingsDialog.this.propertySelector.requestFocus();
         }
      });
   }

   public static ExportSettingsRunner display(final ApplicationWindow var0, final DataLocation var1, final List<? extends IntegrationEntity> var2) {
      return UiThreadSupplier.call(new Supplier<ExportSettingsRunner>() {
         public ExportSettingsRunner get() {
            ExportSettingsDialog var1x = new ExportSettingsDialog(ExportSettingsDialog.getFrame(var0), var1, var2);
            var1x.setVisible(true);
            return var1x.wasOkPressed() ? var1x.getExporter() : null;
         }
      });
   }

   private static JFrame getFrame(ApplicationWindow var0) {
      return var0 != null ? var0.getFrame() : UiUtils.getActiveFrame();
   }

   private ExportSettingsRunner getExporter() {
      OrderedSelection var1 = this.propertySelector.getSelection();
      ExportableProperty[] var2 = new ExportableProperty[var1.size()];
      int var3 = 0;

      for (Object var5 : var1.items()) {
         var2[var3++] = (ExportableProperty)var5;
      }

      return new ExportSettingsRunner(var2, this.destinationSelector.getSelectedEntities());
   }

   private void createContentPane() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 10);
      this.addPropertySelector(var1);
      this.addDestinationsList(var1);
      this.standardLayout(var1, OK_CANCEL);
   }

   private void addPropertySelector(BorderLayoutBuilder var1) {
      JPanel var2 = BorderLayoutBuilder.fillWith(this.propertySelector);
      var1.north(var2);
   }

   private void addDestinationsList(BorderLayoutBuilder var1) {
      String var2 = LocationResources.getString("ExportSettingsDialog.Destinations");
      KongaLabel var3 = TextStyles.InfoText.makeLabel(var2);
      BorderLayoutBuilder var4 = new BorderLayoutBuilder(0, 3);
      var4.north(var3).center(this.destinationSelector);
      var1.center(var4);
   }

   private ExportablePropertySelector createPropertySelector(Iterable<? extends ExportableProperty> var1) {
      ExportablePropertyCheckBoxSelector var2 = new ExportablePropertyCheckBoxSelector(var1);
      var2.addSelectionListener(this.selectionListener);
      return var2;
   }

   private EntitySelector<IntegrationEntity> createDestinationSelector(List<? extends IntegrationEntity> var1) {
      EntityTableSelector var2 = new EntityTableSelector<>(Lists.newArrayList(var1), IntegrationEntity.class);
      var2.getSelectionSource().addSelectionListener(this.selectionListener);
      return var2;
   }

   private void updateOkButton() {
      this.getOKButton().setEnabled(this.hasApplicableSelection());
   }

   private boolean hasApplicableSelection() {
      return this.destinationSelector.getSelectedEntities().size() > 0 && !this.propertySelector.getSelection().isEmpty();
   }

   private class SelectionListenerImpl implements SelectionListener {
      private SelectionListenerImpl() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         ExportSettingsDialog.this.updateOkButton();
      }
   }
}
