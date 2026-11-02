package org.etlforge.integration.client.ui.interchange.component.launcher;

import java.awt.EventQueue;
import java.io.File;
import javax.swing.JLabel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.jitterpack.ImporterFactory;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.jitterpack.UnpackCallback;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.jitterpack.DefaultFileExaminer;
import org.etlforge.integration.client.ui.jitterpack.DoImportAction;
import org.etlforge.integration.client.ui.jitterpack.ImportJitterPackUi;
import org.etlforge.integration.client.ui.jitterpack.JitterPackSelectionListener;
import org.etlforge.integration.client.ui.jitterpack.JitterPackerUi;
import org.etlforge.integration.client.ui.project.loading.LoadingIssuesPresenter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.util.file.FileSelectionListener;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class ImportJitterPackOption extends AbstractLaunchOption {
   private final ApplicationWindow appWin;
   private final ImporterFactory importerFactory;
   private volatile IntegrationProject project;
   private boolean hasBeenActivated;
   private ImportJitterPackUi importUi;
   private DoImportAction importAction;
   private final KongaLayeredPane layeredPane;

   public ImportJitterPackOption(final ApplicationWindow var1, final ProjectManager var2, final ImporterFactory var3) {
      super(Strings.format("Launcher.Import.Name", "Backup"), ClientIcons.JITTERPAK_32);
      this.appWin = var1;
      this.importerFactory = var3;
      this.layeredPane = new KongaLayeredPane();
      EventQueue.invokeLater(
         new Runnable() {
            @Override
            public void run() {
               ImportJitterPackOption.this.importUi = new ImportJitterPackUi(var2, var1);
               ImportJitterPackOption.this.importAction = new DoImportAction(
                  ImportJitterPackOption.this.importUi, var3, ImportJitterPackOption.this.new UnpackCallbackImpl()
               );
               ImportJitterPackOption.this.importAction.setWaitService(ImportJitterPackOption.this.layeredPane);
            }
         }
      );
   }

   @Override
   public void activated() {
      if (!this.hasBeenActivated) {
         this.customizeImportUiBeforeUse();
         this.hasBeenActivated = true;
      }

      this.importUi.requestFocus();
   }

   private void customizeImportUiBeforeUse() {
      JitterPackerUi var1 = new JitterPackerUi();
      var1.setLoadingIssuesHandler(new LoadingIssuesPresenter());
      DefaultFileExaminer var2 = new DefaultFileExaminer(this.appWin, var1, this.importerFactory);
      this.importUi.setFileExaminer(var2);
      this.bindEnabledStateOfImportAction();
   }

   private void bindEnabledStateOfImportAction() {
      this.importUi.setDoImportAction(this.importAction);
      this.importUi.addJitterPackSelectionListener(new JitterPackSelectionListener() {
         @Override
         public void selectionChanged(JitterPack var1) {
            ImportJitterPackOption.this.updateImportActionEnabledState();
         }
      });
      this.importUi.addDestinationFolderListener(new FileSelectionListener() {
         @Override
         public void selectionChanged(File var1) {
            ImportJitterPackOption.this.updateImportActionEnabledState();
         }
      });
      this.updateImportActionEnabledState();
   }

   private void updateImportActionEnabledState() {
      this.importAction.setEnabled(this.importUi.isInputValid());
   }

   @Override
   public IntegrationProject getProject() {
      return this.project;
   }

   @Override
   public void wakeUp() {
      if (this.importUi != null) {
         this.importUi.reset();
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      JLabel var2 = this.createCaption();
      KongaButton var3 = this.createImportButton();
      OneColumnPanel var4 = new OneColumnPanel().setWeightX(1.0).of(var2, this.importUi, InvisiblePanel.of(var3));
      this.layeredPane.setBaseComponent(Vertical.Anchor.north(var4).container());
      var1.display(this.layeredPane);
   }

   private KongaButton createImportButton() {
      KongaButton var1 = new KongaButton(this.importAction);
      var1.setUseMediumActionIcon(true);
      WizardStyleSheet.SMALL_CAPTION_STYLE.makeOver(var1);
      return var1;
   }

   @Override
   public void requestFocus() {
      this.importUi.requestFocus();
   }

   private final class UnpackCallbackImpl implements UnpackCallback {
      private UnpackCallbackImpl() {
      }

      @Override
      public void unpackingCompleted(IntegrationProject var1) {
         ImportJitterPackOption.this.project = var1;
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ImportJitterPackOption.this.launchProject();
            }
         });
      }

      @Override
      public void unpackingCancelled(String var1) {
      }

      @Override
      public void unpackingFailed(String var1, Throwable var2) {
      }
   }
}

