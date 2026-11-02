package org.etlforge.integration.client.ui.interchange.entity.transformation;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import org.etlforge.application.ui.job.DefaultUiJobProgressMonitor;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobProgressMonitor;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.client.ui.interchange.entity.MappingStructureRefresher;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;
import org.etlforge.system.callback.CallbackResultAdapter;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.canvas.ContainerCanvas;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.widget.label.KongaLabel;

public class TransformationsRefresher {
   private final ApplicationWindow appWin;
   private final MappingStructureFactory structureFactory;
   private InputAndOutputMappingTreeStructureCache cache = InputAndOutputMappingTreeStructureCache.EMPTY;
   private TransformationsRefresher.Ui ui;
   private volatile boolean cancelled;

   public TransformationsRefresher(ApplicationWindow var1, MappingStructureFactory var2) {
      this.appWin = Preconditions.checkNotNull(var1, "appWin");
      this.structureFactory = Preconditions.checkNotNull(var2, "structureFactory");
   }

   public void setCache(InputAndOutputMappingTreeStructureCache var1) {
      this.cache = Preconditions.checkNotNull(var1);
   }

   public void refreshInputAndOutput(List<Transformation> var1) {
      this.refresh(KongaListUtils.transform(var1, new Function<Transformation, TransformationsRefresher.TransformationToRefresh>() {
         public TransformationsRefresher.TransformationToRefresh apply(Transformation var1) {
            return new TransformationsRefresher.TransformationToRefresh(var1);
         }
      }));
   }

   public void refresh(List<TransformationsRefresher.TransformationToRefresh> var1) {
      this.ui = this.openUi(var1.size());

      for (TransformationsRefresher.TransformationToRefresh var3 : var1) {
         this.refresh(var3);
         if (this.cancelled) {
            this.ui.hide();
            return;
         }

         this.ui.transformationRefreshed();
      }

      this.ui.done();
   }

   private TransformationsRefresher.Ui openUi(final int var1) {
      return UiThreadSupplier.call(new Supplier<TransformationsRefresher.Ui>() {
         public TransformationsRefresher.Ui get() {
            TransformationsRefresher.Ui var1x = TransformationsRefresher.this.new Ui(var1);
            var1x.show();
            return var1x;
         }
      });
   }

   private void refresh(TransformationsRefresher.TransformationToRefresh var1) {
      MappingStructureRefresher var2 = new MappingStructureRefresher(this.structureFactory);
      var2.setCache(this.cache);
      var2.addProgressMonitor(this.ui);

      try {
         var1.refresh(var2, new CallbackResultAdapter() {
            @Override
            public void cancelled(Object var1) {
               TransformationsRefresher.this.cancelled = true;
            }
         });
      } catch (MappingTreeStructureException var4) {
         var4.printStackTrace();
         this.cancelled = true;
      }
   }

   public static final class TransformationToRefresh {
      private final Transformation tx;
      private final boolean refreshInput;
      private final boolean refreshOutput;

      public static Function<TransformationsRefresher.TransformationToRefresh, Transformation> toTransformation() {
         return new Function<TransformationsRefresher.TransformationToRefresh, Transformation>() {
            public Transformation apply(TransformationsRefresher.TransformationToRefresh var1) {
               return var1.tx;
            }
         };
      }

      private TransformationToRefresh(Transformation var1) {
         this(var1, true, true);
      }

      public TransformationToRefresh(Transformation var1, boolean var2, boolean var3) {
         this.tx = Preconditions.checkNotNull(var1);
         this.refreshInput = var2;
         this.refreshOutput = var3;
      }

      private void refresh(MappingStructureRefresher var1, CallbackResultAdapter var2) throws MappingTreeStructureException {
         boolean var3 = false;
         if (this.refreshInput && this.refreshOutput) {
            MappingTreeStructure[] var6 = var1.refreshInputAndOuputStructures(this.tx);
            var3 = var6 == null;
         } else if (this.refreshInput) {
            MappingTreeStructure var4 = var1.refreshInputStructure(this.tx);
            var3 = var4 == null;
         } else if (this.refreshOutput) {
            MappingTreeStructure var5 = var1.refreshOutputStructure(this.tx);
            var3 = var5 == null;
         }

         if (var3) {
            var2.cancelled(null);
         }
      }

      @Override
      public String toString() {
         return Objects.toStringHelper(this).addValue(this.tx).add("refreshInput", this.refreshInput).add("refreshOutput", this.refreshOutput).toString();
      }
   }

   private class Ui implements UiJobProgressMonitor {
      private final JProgressBar progressBar;
      private DefaultUiJobProgressMonitor monitor;
      private final JPanel monitorCapsule;
      private KongaDialog dialog;
      private UiJob currentJob;
      private boolean packed;

      public Ui(int var2) {
         this.progressBar = new JProgressBar(0, var2) {
            @Override
            public String getString() {
               return this.getValue() + " of " + this.getMaximum();
            }
         };
         this.progressBar.setStringPainted(true);
         this.monitorCapsule = new JPanel(new BorderLayout());
         this.monitorCapsule.setBackground(javax.swing.UIManager.getColor("Panel.background"));
         this.monitorCapsule.setBorder(BorderFactory.createEtchedBorder());
      }

      public void transformationRefreshed() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Ui.this.progressBar.setValue(Ui.this.progressBar.getValue() + 1);
            }
         });
      }

      public void done() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               if (Ui.this.dialog != null) {
                  Ui.this.progressBar.setValue(Ui.this.progressBar.getMaximum());
                  Ui.this.dialog.getOKButton().setEnabled(true);
                  Ui.this.dialog.getCancelButton().setEnabled(false);
               }
            }
         });
      }

      public void show() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Ui.this.showImpl();
            }
         });
      }

      public void hide() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Ui.this.hideImpl();
            }
         });
      }

      private void showImpl() {
         if (this.dialog == null) {
            this.dialog = this.createDialog();
            this.dialog.setLocationRelativeTo(null);
            this.dialog.setVisible(true);
         }
      }

      private KongaDialog createDialog() {
         KongaDialog var1 = new KongaDialog(TransformationsRefresher.this.appWin.getFrame(), "Refresh Transformations", true) {
            @Override
            protected void onCancel() {
               Ui.this.cancelCurrentJob();
               super.onCancel();
            }

            @Override
            protected void onDialogClose() {
               Ui.this.cancelCurrentJob();
               super.onDialogClose();
            }
         };
         JPanel var2 = var1.getButtonPanel(KongaDialog.OK_CANCEL, 10);
         var2.setBorder(Empty.border(10, 0, 10, 0));
         BorderLayoutBuilder var3 = new BorderLayoutBuilder(0, 5);
         var3.center(this.createMainArea()).south(var2);
         var3.withEmptyBorder(10);
         var3.asContentPaneFor(var1);
         var1.setOkButtonAsDefault();
         var1.getOKButton().setEnabled(false);
         return var1;
      }

      private JComponent createMainArea() {
         BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 10);
         var1.center(this.monitorCapsule).south(this.getProgressPanel()).nonOpaque();
         return var1.container();
      }

      private JComponent getProgressPanel() {
         KongaLabel var1 = TextStyles.InfoText.makeLabel("Number of processed transformations");
         return CaptionLayout.of(var1, this.progressBar).nonOpaque().container();
      }

      private void cancelCurrentJob() {
         synchronized (this) {
            if (this.currentJob != null && this.currentJob.canBeCancelled()) {
               this.currentJob.cancel();
            }
         }

         TransformationsRefresher.this.cancelled = true;
      }

      private void hideImpl() {
         if (this.dialog != null) {
            this.dialog.dispose();
            this.dialog = null;
         }
      }

      @Override
      public void ended() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               if (Ui.this.currentJob.isCancelled()) {
                  TransformationsRefresher.this.cancelled = true;
               }

               Ui.this.monitor.ended();
            }
         });
      }

      @Override
      public void setCanBeCancelled(final boolean var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Ui.this.monitor.setCanBeCancelled(var1);
            }
         });
      }

      @Override
      public void setCompletionLevel(final int var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Ui.this.monitor.setCompletionLevel(var1);
            }
         });
      }

      @Override
      public void setMessage(final String var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Ui.this.monitor.setMessage(var1);
            }
         });
      }

      @Override
      public void started(final UiJob var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Ui.this.currentJob = var1;
               Ui.this.monitor = new DefaultUiJobProgressMonitor(var1, true, false);
               Ui.this.monitor.started(var1);
               Ui.this.monitorCapsule.removeAll();
               ContainerCanvas.decorate(Ui.this.monitor, Ui.this.monitorCapsule, "North");
               if (Ui.this.dialog != null && !Ui.this.packed) {
                  Ui.this.packDialog();
               }
            }
         });
      }

      private void packDialog() {
         assert EventQueue.isDispatchThread() && !this.packed;
         this.dialog.pack();
         this.dialog.manageLocation(TransformationsRefresher.class);
         this.packed = true;
      }
   }
}

