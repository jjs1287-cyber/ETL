package org.etlforge.integration.client.connect.wizard;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.connect.mapper.AutoMappingJob;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.interchange.entity.EntityWizardCompleter;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;

public abstract class AbstractConnectWizardCompleter<T extends IntegrationEntity> extends EntityWizardCompleter<T> {
   protected volatile Transformation mappingTx;
   private final List<Transformation> transformationsToOpen;
   private WaitService waitService = WaitService.NONE;
   private final CountDownLatch autoMapLatch = new CountDownLatch(1);
   private boolean openMappingTransformationAutomatically = true;

   public AbstractConnectWizardCompleter(Wizard<T> var1, IntegrationProject var2, ProjectPersistor var3, IntegrationEntity var4) {
      super(var1, var2, var3, var4);
      this.transformationsToOpen = Lists.newArrayList();
   }

   public final void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   public final void setOpenMappingTransformationAutomatically(boolean var1) {
      this.openMappingTransformationAutomatically = var1;
   }

   @Override
   protected final void saveAdditionalItems(ProjectPersistor var1) {
      for (IntegrationEntity var4 : this.getAdditionalItemsToSave()) {
         this.saveItem(var1, var4);
      }
   }

   protected Iterable<IntegrationEntity> getAdditionalItemsToSave() {
      return Lists.newArrayList();
   }

   private void saveItem(ProjectPersistor var1, IntegrationEntity var2) {
      try {
         var1.save(var2);
      } catch (InterchangeSavingException var4) {
         var4.printStackTrace();
      }
   }

   @Override
   protected final void doPostSaveWork() {
      final WaitLock var1 = this.waitService.startWait();
      Application.getWorker().submitForParallel(new ApplicationWorker.SafeRunnable() {
         @Override
         public void run() {
            AbstractConnectWizardCompleter.this.mappingTx = AbstractConnectWizardCompleter.this.getMappingTransformation();
            AbstractConnectWizardCompleter.this.openOtherTransformations();
            if (AbstractConnectWizardCompleter.this.openMappingTransformationAutomatically) {
               AbstractConnectWizardCompleter.this.openMappingTransformation();
            }

            var1.release();
            AbstractConnectWizardCompleter.this.doAdditionalPostSaveWork();
         }
      });
   }

   protected abstract Transformation getMappingTransformation();

   protected final Editor openWizardItem() {
      return UiThreadSupplier.call(new Supplier<Editor>() {
         public Editor get() {
            IntegrationEntity var1 = AbstractConnectWizardCompleter.this.getItemCreated();
            return UiCompanion.of(var1).openPage();
         }
      });
   }

   private void openOtherTransformations() {
      UiThreadSupplier.call(new Supplier<Void>() {
         public Void get() {
            for (Transformation var2 : AbstractConnectWizardCompleter.this.transformationsToOpen) {
               TransformationPage var3 = (TransformationPage)UiCompanion.of(var2).openPage();
               if (var3 != null) {
                  AbstractConnectWizardCompleter.this.addSaveJob(var3);
               }
            }

            return null;
         }
      });
   }

   private void addSaveJob(final TransformationPage var1) {
      var1.addPostPageLoadingJob(new Runnable() {
         @Override
         public void run() {
            try {
               var1.save();
            } catch (SaveEditorException var2) {
               ErrorLog.log(AbstractConnectWizardCompleter.this.getClass(), null, var2);
            }
         }
      });
   }

   protected final Editor openMappingTransformation() {
      return UiThreadSupplier.call(new Supplier<Editor>() {
         public Editor get() {
            if (AbstractConnectWizardCompleter.this.mappingTx == null) {
               AbstractConnectWizardCompleter.this.autoMapLatch.countDown();
               return null;
            } else {
               TransformationPage var1 = (TransformationPage)UiCompanion.of(AbstractConnectWizardCompleter.this.mappingTx).openPage();
               AbstractConnectWizardCompleter.this.prepareMappingTransformation(var1);
               return var1;
            }
         }
      });
   }

   private void prepareMappingTransformation(TransformationPage var1) {
      if (var1 != null && this.isAutoMap()) {
         AutoMappingJob var2 = this.createAutoMappingJob(var1);
         if (var2 == null) {
            throw new RuntimeException("AutoMap is enabled, but no AutoMapper job was created. This is ia bug.");
         }

         var2.setCompletionLatch(this.autoMapLatch);
         var1.addPostPageLoadingJob(var2);
      } else {
         this.autoMapLatch.countDown();
      }
   }

   protected abstract boolean isAutoMap();

   protected abstract AutoMappingJob createAutoMappingJob(TransformationPage var1);

   public final boolean waitForAutoMappingToComplete(long var1, TimeUnit var3) {
      try {
         return this.autoMapLatch.await(var1, var3);
      } catch (InterruptedException var5) {
         Thread.currentThread().interrupt();
         return false;
      }
   }

   protected void doAdditionalPostSaveWork() {
   }

   protected final void addTransformationToOpen(Transformation var1) {
      this.transformationsToOpen.add(var1);
   }

   protected final List<Transformation> getTransformationsToOpen() {
      return Lists.newArrayList(this.transformationsToOpen);
   }
}
