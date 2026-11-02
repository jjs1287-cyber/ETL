package org.etlforge.integration.client.ui.interchange.component.page.entity.dependencies;

import java.awt.Point;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.widget.AnimationPreference;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.list.IntegrationEntityListDecorator;
import org.etlforge.system.Disposable;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.overlay.OverlayAlert;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;
import org.etlforge.ui.widget.overlay.OverlayRelativePositioner;
import org.etlforge.util.Property;

public final class DisplayedEntityDependencies implements Disposable {
   private final EntityDependenciesModel model;
   private final Action displayAction;
   private final IntegrationEntityPage page;
   private final KongaButton trigger;
   private final Binding actionEnabler;

   public DisplayedEntityDependencies(IntegrationEntityPage var1) {
      this.page = var1;
      this.model = var1.getPageModel().getDependencies();
      this.displayAction = new DisplayedEntityDependencies.DisplayAction();
      this.actionEnabler = new DisplayedEntityDependencies.ActionEnabler(this.model.getDependants());
      this.actionEnabler.syncUi();
      this.trigger = this.createTrigger();
   }

   private KongaButton createTrigger() {
      KongaButton var1 = KongaButton.forToolbar(this.displayAction);
      if (IntegrationEntityPage.USE_MEDIUM_SIZED_BUTTONS_IN_TOOLBAR) {
         var1.setUseMediumActionIcon(true);
      } else {
         var1.setUseSmallActionIcon(true);
      }

      return var1;
   }

   @Override
   public void dispose() {
      this.actionEnabler.dispose();
      this.model.dispose();
   }

   public JButton getActiveButton() {
      return this.trigger;
   }

   private class ActionEnabler extends AbstractBinding<List<IntegrationEntity>> {
      private ActionEnabler(Property<List<IntegrationEntity>> var2) {
         super(var2);
      }

      protected void updateUiWithValue(List<IntegrationEntity> var1) {
         boolean var2 = var1 != null && !var1.isEmpty();
         DisplayedEntityDependencies.this.displayAction.setEnabled(var2);
      }
   }

   private static class DialogDisplayer implements DisplayedEntityDependencies.Displayer {
      private final Window owner;
      private KongaDialog dialog;

      public DialogDisplayer(Window var1) {
         this.owner = var1;
      }

      @Override
      public void display(final DependenciesDisplayer var1, String var2) {
         this.dialog = UiProviderDialogDisplayer.createDialog(this.owner, var1, var2, ModalityType.APPLICATION_MODAL, KongaDialog.OK_ONLY, null);
         this.dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent var1x) {
               var1.dispose();
            }
         });
         this.dialog.setResizable(false);
         this.dialog.setVisible(true);
         this.dialog = null;
      }

      @Override
      public void close() {
         if (this.dialog != null) {
            this.dialog.dispose();
         }
      }
   }

   private class DisplayAction extends AbstractAction {
      public DisplayAction() {
         super("", ClientIcons.DEPENDENT_ITEMS_16);
         this.putValue("ShortDescription", Strings.get("EntityPage.Dependencies.Trigger.ToolTip"));
         this.putValue("KongaMediumIconKey", ClientIcons.getIcon("DependentItems.24"));
         this.putValue("SwingLargeIconKey", ClientIcons.getIcon("DependentItems.32"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         BusyWorker var2 = new BusyWorker(DisplayedEntityDependencies.this.page.getWaitService()) {
            @Override
            protected void doWork() {
               DisplayAction.this.runImpl();
            }
         };
         var2.setDelay(50);
         var2.run();
      }

      private void runImpl() {
         if (!DisplayedEntityDependencies.this.model.isEmpty()) {
            DependenciesDisplayer var1 = new DependenciesDisplayer(DisplayedEntityDependencies.this.model);
            String var2 = Strings.get("EntityPage.Dependencies.Dialog.Title");
            DisplayedEntityDependencies.Displayer var3 = this.getDisplayer();
            var1.setOpenPageAction(DisplayedEntityDependencies.this.new OpenPageAction(var3));
            var3.display(var1, var2);
         }
      }

      private DisplayedEntityDependencies.Displayer getDisplayer() {
         OverlayContainer var1 = DisplayedEntityDependencies.this.page.getOverlayContainer();
         if (var1 == null) {
            Window var2 = this.getDialogOwner();
            return new DisplayedEntityDependencies.DialogDisplayer(var2);
         } else {
            return DisplayedEntityDependencies.this.new OverlayDisplayer(var1);
         }
      }

      private Window getDialogOwner() {
         Window var1 = SwingUtilities.getWindowAncestor(DisplayedEntityDependencies.this.trigger);
         if (var1 == null) {
            var1 = DisplayedEntityDependencies.this.page.getWindow().getFrame();
         }

         return var1;
      }
   }

   private interface Displayer {
      void display(DependenciesDisplayer var1, String var2);

      void close();
   }

   private class OpenPageAction extends AbstractSingleObjectSelectionAction<IntegrationEntityListDecorator> {
      private final DisplayedEntityDependencies.Displayer displayer;

      public OpenPageAction(DisplayedEntityDependencies.Displayer var2) {
         super(IntegrationEntityListDecorator.class);
         this.displayer = var2;
      }

      protected void run(IntegrationEntityListDecorator var1, ActionEvent var2) {
         this.openPage(var1.getEntity());
         this.displayer.close();
      }

      private void openPage(final IntegrationEntity var1) {
         (new BusyWorker(DisplayedEntityDependencies.this.page.getWindow()) {
            @Override
            protected void doWork() {
               EntityExplorerSupport var1x = DisplayedEntityDependencies.this.page.getEntityExplorerSupport();
               if (var1x != null) {
                  var1x.openPageFor(var1);
               }
            }
         }).run();
      }
   }

   private class OverlayDisplayer implements DisplayedEntityDependencies.Displayer {
      private final OverlayContainer container;
      private OverlayAlert overlay;

      public OverlayDisplayer(OverlayContainer var2) {
         this.container = var2;
      }

      @Override
      public void display(final DependenciesDisplayer var1, String var2) {
         this.overlay = new OverlayAlert(var1, var2) {
            @Override
            protected void configureOverlay(OverlayDecorator var1x) {
               super.configureOverlay(var1x);
               if (AnimationPreference.get()) {
                  var1x.setFadeIn(true);
               }
            }

            @Override
            protected void onClosed() {
               var1.dispose();
               OverlayDisplayer.this.overlay = null;
               DisplayedEntityDependencies.this.page.restoreFocus(50);
            }
         };
         this.overlay.setAlpha(0.75F);
         this.overlay.show(this.container, new OverlayRelativePositioner(DisplayedEntityDependencies.this.trigger, new Point(8, 8)));
      }

      @Override
      public void close() {
         if (this.overlay != null) {
            this.overlay.hide();
         }
      }
   }
}
