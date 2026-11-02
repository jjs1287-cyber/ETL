package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import java.awt.EventQueue;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.GoToNextPageSelectionAction;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.integration.mscrm.metadata.Relationship;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionSource;

final class LinkRelationshipSelectionPage extends AbstractWizardPage {
   private final ImmutableList<MSCrmObject> parentPath;
   private final RelationshipSelector ui;
   private final QueryBuilderController controller;
   private final Object lock = new Object();
   private Relationship relationship;
   private MSCrmObject metadata;
   private Future<MSCrmObject> metadataJob;

   public LinkRelationshipSelectionPage(Wizard var1, LinkEntitiesModel var2, QueryBuilderController var3) {
      super(var1, "Select a Relationship");
      this.parentPath = var2.getPath();
      this.ui = new RelationshipSelector(var2);
      this.controller = var3;
      var3.setWaitService(this.getWaitService());
      this.installValidStateBinding();
      this.ui.setDefaultAction(new GoToNextPageSelectionAction(this));
   }

   private void installValidStateBinding() {
      LinkRelationshipSelectionPage.ValidStateBinding var1 = new LinkRelationshipSelectionPage.ValidStateBinding();
      var1.install(this.ui.asSelectionSource());
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(this.getTitle());
      var1.add(this.ui, true, true);
      return var1.asCenterBorderLayout();
   }

   @Override
   public void requestFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            LinkRelationshipSelectionPage.this.ui.requestFocus();
         }
      });
   }

   @Override
   public boolean prepareForForwardNavigation() {
      Future var1 = null;
      synchronized (this.lock) {
         this.cancelMetaDataJob();
         this.metadata = null;
         this.relationship = this.askUiForSelectedRelationship();
         this.metadataJob = this.controller.retrieveMetaData(this.relationship.getLinkEntityName(), NoReceiver.create());
         var1 = this.metadataJob;
      }

      try {
         MSCrmObject var10 = (MSCrmObject)var1.get();
         synchronized (this.lock) {
            this.metadata = var10;
         }

         return true;
      } catch (InterruptedException var6) {
         Thread.currentThread().interrupt();
      } catch (ExecutionException var7) {
      }

      return false;
   }

   private void cancelMetaDataJob() {
      synchronized (this.lock) {
         if (this.metadataJob != null) {
            this.metadataJob.cancel(true);
         }

         this.metadataJob = null;
      }
   }

   private Relationship askUiForSelectedRelationship() {
      return UiThreadSupplier.call(new Supplier<Relationship>() {
         public Relationship get() {
            return LinkRelationshipSelectionPage.this.ui.getSelectedRelationship();
         }
      });
   }

   @Override
   protected void dispose() {
      this.cancelMetaDataJob();
   }

   @Override
   public boolean prepareForBackwardNavigation() {
      this.cancelMetaDataJob();
      return true;
   }

   public Relationship getRelationship() {
      synchronized (this.lock) {
         return this.relationship;
      }
   }

   public LinkEntityModel createLinkEntityModel() {
      synchronized (this.lock) {
         ImmutableList var2 = KongaListUtils.concatenateToImmutableList(this.parentPath, Collections.singleton(this.metadata));
         return new LinkEntityModel(var2, this.relationship);
      }
   }

   private class ValidStateBinding implements SelectionListener {
      private ValidStateBinding() {
      }

      public void install(SelectionSource var1) {
         var1.addSelectionListener(this);
         this.updateCanContinue(var1.getSelection());
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         this.updateCanContinue(var1.getSelection());
      }

      private void updateCanContinue(Selection var1) {
         LinkRelationshipSelectionPage.this.setCanContinue(!var1.isEmpty());
      }
   }
}
