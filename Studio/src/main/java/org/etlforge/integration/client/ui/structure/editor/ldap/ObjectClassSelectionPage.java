package org.etlforge.integration.client.ui.structure.editor.ldap;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.ldap.ui.SelectedObjectClasses;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.ldap.client.cache.LdapStructureServerCache;
import org.etlforge.ui.util.UiUtils;

public class ObjectClassSelectionPage extends AbstractWizardPage implements LdapStructureWizardPage {
   private final ObjectClassSelectionModel model;
   private final ObjectClassSelectionUi ui;
   private LdapStructureServerCache ldapCache;
   private final Semaphore cacheReaderLock = new Semaphore(1);

   public ObjectClassSelectionPage(Wizard var1) {
      super(var1, PackageResources.Wizard.ClassSelector.TITLE);
      this.model = new ObjectClassSelectionModel();
      this.ui = new ObjectClassSelectionUi(this, this.model);
      this.setHelpId("integration.transformation.wizard.ldap");
      this.layoutPage();
   }

   public void setLdapCache(LdapStructureServerCache var1) {
      this.ldapCache = var1;
      this.ui.setLdapCache(var1);
      ObjectClassCacheReader.install(this.model, var1, this.cacheReaderLock);
   }

   public void setLdapLocation(final LdapLocation var1) {
      Preconditions.checkNotNull(var1, "loc");
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            ObjectClassSelectionPage.this.model.setLocation(var1);
         }
      });
   }

   public void setStructureToEdit(LdapStructure var1) {
      Preconditions.checkNotNull(var1, "s");
      ObjectClassSelectionPage.SelectedObjectClassesCreator var2 = new ObjectClassSelectionPage.SelectedObjectClassesCreator(var1);
      if (EventQueue.isDispatchThread()) {
         var2.execute();
      } else {
         var2.run();
      }
   }

   public SelectedObjectClasses getSelectedClasses() {
      return this.model.getSelection();
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   @Override
   public void applyTo(LdapStructure var1) {
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(
         PackageResources.Wizard.ClassSelector.MAIN_CAPTION, PackageResources.Wizard.ClassSelector.SUB_CAPTION
      );
      var1.add(this.ui, true, true);
      return var1.asCenterBorderLayout();
   }

   private class SelectedObjectClassesCreator extends SwingWorker<SelectedObjectClasses, Void> {
      private final LdapStructure struct;

      public SelectedObjectClassesCreator(LdapStructure var2) {
         this.struct = var2;
      }

      protected SelectedObjectClasses doInBackground() throws Exception {
         if (this.acquireCacheLock()) {
            try {
               return SelectedObjectClasses.fromStructure(this.struct, ObjectClassSelectionPage.this.ldapCache, ObjectClassSelectionPage.this.getWaitService());
            } finally {
               ObjectClassSelectionPage.this.cacheReaderLock.release();
            }
         } else {
            return null;
         }
      }

      @Override
      protected void done() {
         try {
            SelectedObjectClasses var1 = this.get();
            if (var1 != null) {
               ObjectClassSelectionPage.this.model.setSelectedClasses(var1);
            }
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
         } catch (ExecutionException var3) {
            ErrorLog.attention(ObjectClassSelectionPage.class, null, var3.getCause());
         }
      }

      private boolean acquireCacheLock() {
         try {
            ObjectClassSelectionPage.this.cacheReaderLock.acquire();
            return true;
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
            var2.printStackTrace();
            return false;
         }
      }
   }
}
