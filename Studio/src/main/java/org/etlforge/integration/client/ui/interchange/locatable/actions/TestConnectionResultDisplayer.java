package org.etlforge.integration.client.ui.interchange.locatable.actions;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import java.awt.EventQueue;
import java.awt.Window;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.integration.client.datalocation.connection.MatchingFileList;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.location.resources.LocationResources;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.test.LocationSpecificConnectionDetails;
import org.etlforge.integration.data.test.TestConnectionResult;
import org.etlforge.integration.data.test.TestResult;
import org.etlforge.ui.widget.overlay.OverlayAlert;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;

public final class TestConnectionResultDisplayer {
   private final Window owner;
   private Optional<Supplier<OverlayContainer>> overlayContainerSupplier = Optional.absent();

   public TestConnectionResultDisplayer(Window var1) {
      this.owner = var1;
   }

   public void setOverlayContainerSupplier(Supplier<OverlayContainer> var1) {
      this.overlayContainerSupplier = Optional.of(var1);
   }

   public void showResult(IntegrationEntity var1, TestConnectionResult var2) {
      if (var2 != null) {
         if (var2.getOutcome() == TestResult.SUCCESS) {
            LocationSpecificConnectionDetails var3 = var2.getLocationSpecificDetails();
            if (var3 instanceof MatchingFileList) {
               this.showMatchingFiles(var1, (MatchingFileList)var3);
            } else {
               this.showGeneralSuccessMessage(var1);
            }
         } else {
            if(org.etlforge.integration.client.server.ServerConnectionService.NOT_CONNECTED_MESSAGE.equals(var2.getDetails())) {
               NonBlockingAlert.info(this.owner,org.etlforge.integration.client.server.ServerConnectionService.NOT_CONNECTED_MESSAGE,"Test Connection");
               return;
            }
            ErrorLog.attention(this.getClass(), "The connection test failed.", new Exception(var2.getDetails()));
         }
      }
   }

   private void showMatchingFiles(final IntegrationEntity var1, final MatchingFileList var2) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            MatchingFilesPresenter var1x = new MatchingFilesPresenter(var2);
            OverlayContainer var2x = TestConnectionResultDisplayer.this.getResultContainer(var1);
            if (var2x != null) {
               var1x.showInOverlay(var2x);
            } else {
               var1x.showInDialog(TestConnectionResultDisplayer.this.owner);
            }
         }
      });
   }

   private void showGeneralSuccessMessage(final IntegrationEntity var1) {
      final boolean variableEndpoint = var1 instanceof org.etlforge.integration.data.Locatable
         && ((org.etlforge.integration.data.Locatable)var1).getLocation() instanceof org.etlforge.integration.data.location.GlobalVariableLocation;
      final String var2 = variableEndpoint ? "Variable name is valid. Its value is checked when the operation runs." : LocationResources.getString("Connection.Success.Message");
      final String var3 = LocationResources.getString("Connection.Success.Title");
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            OverlayContainer var1x = TestConnectionResultDisplayer.this.getResultContainer(var1);
            if (var1x != null) {
               OverlayAlert var2x = this.createOverlay(var2, var3);
               var2x.show(var1x);
            } else {
               NonBlockingAlert.info(var2, var3);
            }
         }

         private OverlayAlert createOverlay(String var1x, String var2x) {
            return new OverlayAlert(var1x, ClientIcons.TEST_CONNECTION_16, var2x) {
               @Override
               protected void configureOverlay(OverlayDecorator var1x) {
                  super.configureOverlay(var1x);
                  var1x.setAlpha(0.8F);
               }
            };
         }
      });
   }

   private OverlayContainer getResultContainer(IntegrationEntity var1) {
      return this.overlayContainerSupplier.isPresent() ? this.overlayContainerSupplier.get().get() : null;
   }
}
