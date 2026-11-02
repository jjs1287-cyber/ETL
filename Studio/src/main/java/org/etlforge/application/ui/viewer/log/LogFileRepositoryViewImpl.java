package org.etlforge.application.ui.viewer.log;

import com.google.common.base.Charsets;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.etlforge.application.log.LogFileRepository;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.log.ApplicationLogLevelConsoleDialog;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.text.editor.ChunkedTextFileEditor;
import org.etlforge.application.ui.text.editor.TextEditorFactory;
import org.etlforge.application.ui.viewer.log.tree.LogFileRepositoryTree;
import org.etlforge.application.ui.window.AbstractWindowElement;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.DisposableWindowElementResource;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorCategory;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.ui.Empty;
import org.etlforge.ui.event.ApplicationMessageEvent;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.PopupMenuRequestHandlers;
import org.etlforge.util.file.FileDescriber;

final class LogFileRepositoryViewImpl extends AbstractWindowElement implements LogFileRepositoryView {
   private static final int FILE_CHUNK_SIZE = 250000;
   private final LogFileRepository repository;
   private final WindowSection windowSection;
   private final LogFileRepositoryTree tree;
   private final LogFileRepositoryViewImpl.OpenFileAction openFileAction;
   private final FileStatusMonitor fileMonitor;

   public LogFileRepositoryViewImpl(LogFileRepository var1, WindowSection var2) {
      super(CONTENT_ID);
      this.repository = var1;
      this.windowSection = var2;
      this.openFileAction = new LogFileRepositoryViewImpl.OpenFileAction();
      this.tree = this.createTree();
      this.tree.asSelectionSource().setDefaultAction(this.openFileAction);
      this.fileMonitor = new FileStatusMonitor(this.tree, var1.getSessionLogs()[0]);
      this.addResource(new DisposableWindowElementResource(this.fileMonitor));
   }

   private LogFileRepositoryTree createTree() {
      LogFileRepositoryTree var1 = new LogFileRepositoryTree(this.repository, this.getLeftTreeActions(), this.getRightTreeActions());
      var1.setPopupMenuRequestHandler(PopupMenuRequestHandlers.with(this.openFileAction));
      var1.asSelectionSource().addSelectionListener(new LogFileRepositoryViewImpl.TreeSelectionHandler());
      return var1;
   }

   private List<? extends Action> getLeftTreeActions() {
      return Collections.singletonList(this.openFileAction);
   }

   private List<? extends Action> getRightTreeActions() {
      return Collections.singletonList(new LogFileRepositoryViewImpl.CloseAction());
   }

   @Override
   public WindowSection getWindowSection() {
      return this.windowSection;
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.fileMonitor.start();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            LogFileRepositoryViewImpl.this.tree.requestFocus();
         }
      });
   }

   @Override
   public String getTitle() {
      return PackageResources.View.TITLE;
   }

   @Override
   public Icon getIcon() {
      return PackageResources.View.ICON;
   }

   @Override
   public void decorate(JComponent var1) {
      JComponent var2 = this.createDisplayer();
      BorderLayoutBuilder.from(var1).center(var2);
   }

   private JComponent createDisplayer() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder();
      JComponent var2 = this.layoutControlPanel();
      var1.center(this.tree).south(var2);
      return var1.container();
   }

   private JComponent layoutControlPanel() {
      LogFileRepositoryViewImpl.ConfigureLogLevelsAction var1 = new LogFileRepositoryViewImpl.ConfigureLogLevelsAction(this.getWindow());
      return BoxBuilder.horizontal().withBorder(Empty.border(10, 0, 10, 0)).add(var1).glue().invisible();
   }

   @Override
   public boolean isDetachable() {
      return true;
   }

   private final class CloseAction extends AbstractAction {
      public CloseAction() {
         super("", CommonIcons.CLOSE_16);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         BusyWorker var2 = new BusyWorker(LogFileRepositoryViewImpl.this.getWindow()) {
            @Override
            protected void doWork() {
               LogFileRepositoryViewImpl.this.windowSection.removeContent(LogFileRepositoryViewImpl.this);
            }
         };
         var2.setDelay(75);
         var2.run();
      }
   }

   private static final class ConfigureLogLevelsAction extends AbstractAction {
      private final ApplicationWindow appWin;

      public ConfigureLogLevelsAction(ApplicationWindow var1) {
         super("Configure log levels...");
         this.appWin = var1;
         this.putValue("ShortDescription", "Configure the application log levels");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ConfigureLogLevelsAction.this.openConsole();
            }
         });
      }

      private void openConsole() {
         JFrame var1 = this.appWin.getFrame();
         ApplicationLogLevelConsoleDialog.openWithExistingApplicationLogs(var1);
      }
   }

   private final class OpenFileAction extends AbstractSingleTypeSelectionAction<File> {
      public OpenFileAction() {
         super(File.class, "", PackageResources.OpenFile.ICON);
         ActionDecorators.decorateWithNameAndMnemonic(this, PackageResources.OpenFile.NAME);
         this.putValue("ShortDescription", PackageResources.OpenFile.TOOLTIP);
         this.setEnabled(false);
      }

      @Override
      protected void run(List<File> var1) {
         this.openFiles(var1);
      }

      private void openFiles(List<File> var1) {
         for (File var3 : var1) {
            this.openFile(var3);
         }
      }

      private void openFile(File var1) {
         ChunkedTextFileEditor var2 = this.findOpenEditor(var1);
         if (var2 != null) {
            var2.refresh();
         } else {
            LogFileRepositoryViewImpl.this.fileMonitor.fileOpened(var1);
            var2 = this.createEditor(var1);
         }

         LogFileRepositoryViewImpl.this.getWindow().getEditorService().openEditor(var2);
      }

      private ChunkedTextFileEditor findOpenEditor(File var1) {
         EditorService var2 = LogFileRepositoryViewImpl.this.getWindow().getEditorService();

         for (Editor var6 : var2.getOpenEditors()) {
            if (var6.getObject() == var1 && var6 instanceof ChunkedTextFileEditor) {
               return (ChunkedTextFileEditor)var6;
            }
         }

         return null;
      }

      private ChunkedTextFileEditor createEditor(File var1) {
         String var2 = LogFileUtils.getDisplayName(var1);
         ChunkedTextFileEditor var3 = TextEditorFactory.openFileInChunks(var2, var1, Charsets.UTF_8, 250000);
         if (LogFileRepositoryViewImpl.this.repository.isSystemOut(var1)) {
            var3.decorateText(TextStyles.SystemOutText.asTextDecorator());
         } else if (LogFileRepositoryViewImpl.this.repository.isSystemErr(var1)) {
            var3.decorateText(TextStyles.SystemErrText.asTextDecorator());
         } else {
            var3.decorateText(TextStyles.DefaultMonoSpaced.asTextDecorator());
         }

         EditorCategory var4 = new EditorCategory("Client Log Files", PackageResources.View.ICON);
         var3.setCategory(var4);
         var3.setSuggestedTargetNameForCopy(var1.getName());
         var3.setMementoPath(new String[]{"Application", "LogFile"});
         return var3;
      }
   }

   private final class TreeSelectionHandler implements SelectionListener {
      private final FileDescriber fileDescriber = new FileDescriber(
         EnumSet.of(FileDescriber.Element.PATH, FileDescriber.Element.SIZE, FileDescriber.Element.LAST_MODIFIED)
      );

      private TreeSelectionHandler() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         OrderedSelection var2 = (OrderedSelection)var1.getSelection();
         Object[] var3 = var2.toArray();
         if (var3.length == 0) {
            this.onNothingSelected();
         } else if (var3.length == 1) {
            this.onOneFileSelected((File)var3[0]);
         }
      }

      private void onNothingSelected() {
         this.publishMessage(" ");
      }

      private void onOneFileSelected(File var1) {
         this.publishMessage(this.fileDescriber.describe(var1));
      }

      private void publishMessage(String var1) {
         ApplicationWindow var2 = LogFileRepositoryViewImpl.this.windowSection.getWindow();
         if (var2 != null) {
            var2.postApplicationMessageEvent(new ApplicationMessageEvent(this, var1));
         }
      }
   }
}
