package org.etlforge.integration.client.connect.mapper;

import java.awt.EventQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import javax.swing.SwingWorker;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;

public abstract class AbstractAutoMappingJob implements AutoMappingJob {
   private final TransformationPage page;
   private volatile CountDownLatch completionLatch;

   protected AbstractAutoMappingJob(TransformationPage var1) {
      this.page = var1;
   }

   @Override
   public final void setCompletionLatch(CountDownLatch var1) {
      this.completionLatch = var1;
   }

   protected final Node getStructureRootNode(TreeComponent var1) {
      return var1.getVisibleRoot();
   }

   @Override
   public final void run() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TreeMapper var1 = AbstractAutoMappingJob.this.page.getTreeMapper();
            Node var2 = AbstractAutoMappingJob.this.getSourceNode(var1);
            Node var3 = AbstractAutoMappingJob.this.getTargetNode(var1);
            if (var2 != null && var3 != null) {
               Future var4 = var1.doStraightMappings(var2, var3);
               AbstractAutoMappingJob.this.savePageAfterMappingIsDone(var4);
            } else {
               AbstractAutoMappingJob.this.countDownLatch();
            }
         }
      });
   }

   private void countDownLatch() {
      if (this.completionLatch != null) {
         this.completionLatch.countDown();
      }
   }

   protected abstract Node getSourceNode(TreeMapper var1);

   protected abstract Node getTargetNode(TreeMapper var1);

   private void savePageAfterMappingIsDone(Future<?> var1) {
      AbstractAutoMappingJob.SaveExecutor var2 = new AbstractAutoMappingJob.SaveExecutor(var1);
      var2.execute();
   }

   private class SaveExecutor extends SwingWorker<Void, Void> {
      private final Future<?> mapJob;

      public SaveExecutor(Future<?> var2) {
         this.mapJob = var2;
      }

      protected Void doInBackground() throws Exception {
         this.mapJob.get();
         return null;
      }

      @Override
      protected void done() {
         try {
            AbstractAutoMappingJob.this.page.save();
         } catch (SaveEditorException var2) {
            var2.printStackTrace();
         }

         AbstractAutoMappingJob.this.countDownLatch();
      }
   }
}
