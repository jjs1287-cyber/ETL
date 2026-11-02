package org.etlforge.integration.client.ui.interchange.entity.transformation.page;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.awt.EventQueue;
import java.util.logging.Logger;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.interchange.entity.MappingStructureRefresher;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.util.UiUtils;

public class TransformationPageRefresher {
   private final TransformationPage page;
   private volatile Logger logger = Logger.getAnonymousLogger();
   private volatile boolean savePageWhenDone;

   public TransformationPageRefresher(TransformationPage var1) {
      this.page = Preconditions.checkNotNull(var1, "page");
   }

   public final void setLogger(Logger var1) {
      this.logger = Preconditions.checkNotNull(var1, "logger");
   }

   public final void setSavePageWhenDone(boolean var1) {
      this.savePageWhenDone = var1;
   }

   public final void refresh() {
      this.applyCurrentFormulas();
      Transformation var1 = this.page.getObject();
      this.logger.info("Received request to refresh the structures for the transformation \"" + var1.getName() + "\"");
      MappingStructureRefresher var2 = this.createRefresher();

      try {
         MappingTreeStructure[] var3 = var2.refreshInputAndOuputStructures(var1);
         this.updateTree(var3 != null);
         if (var3 != null && this.savePageWhenDone) {
            this.savePage();
         }
      } catch (MappingTreeStructureException var4) {
         this.updateTree(false);
      }
   }

   private void applyCurrentFormulas() {
      UiThreadSupplier.call(new Supplier<Void>() {
         public Void get() {
            TreeMapper var1 = TransformationPageRefresher.this.page.getTreeMapper();
            var1.applyCurrentFormulas();
            return null;
         }
      });
   }

   private MappingStructureRefresher createRefresher() {
      MappingStructureRefresher var1 = new MappingStructureRefresher(this.page.getMappingStructureFactory());
      ManagedProject var2 = ProjectUtils.getManagedProject(this.page.getObject());
      var1.setCache(var2.getTransformationMappingStructureCache());
      var1.setLogger(this.logger);
      return var1;
   }

   private void updateTree(boolean var1) {
      this.logger.info("The refresh process has completed. Success status = " + var1);
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TransformationPageRefresher.this.removeCurrentTestData();
            TransformationPageRefresher.this.whenDone();
         }
      });
   }

   private void removeCurrentTestData() {
      this.page.getTreeDataService().removeAllTestData();
   }

   private void savePage() {
      UiUtils.nestedInvokeLater(2, new Runnable() {
         @Override
         public void run() {
            try {
               if (TransformationPageRefresher.this.page.isDirty()) {
                  TransformationPageRefresher.this.page.save();
               }
            } catch (SaveEditorException var2) {
               ErrorLog.log(TransformationPageRefresher.class, null, var2);
            }
         }
      });
   }

   protected final TransformationPage getPage() {
      return this.page;
   }

   protected void whenDone() {
   }
}
