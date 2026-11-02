package com.etlforge.integration.client.ui.admin.file;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.etlforge.integration.client.admin.server.ServerFileLocator;
import java.io.File;
import java.util.ArrayList;
import javax.swing.Action;
import javax.swing.JLabel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.text.editor.ChunkedTextFileViewer;
import org.etlforge.application.ui.text.editor.SaveCopyAsAction;
import org.etlforge.system.Disposable;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;
import org.etlforge.util.preference.KongaPreferences;

public final class ServerFileContentViewer implements UiProvider, Disposable {
   private static final int CHUNK_SIZE = 250000;
   private final KongaLayeredPane ui = new KongaLayeredPane();
   private final JLabel fileName = TextStyles.DefaultBold.makeLabel(" ");
   private final ChunkedTextFileViewer textViewer;
   private final SaveCopyAsAction saveCopyAction = this.createSaveCopyAsAction();
   private final Action searchAction;
   private Action refreshAction;

   public ServerFileContentViewer() {
      this.textViewer = this.createTextViewer();
      this.searchAction = this.textViewer.createSearchAction("Search");
   }

   private SaveCopyAsAction createSaveCopyAsAction() {
      SaveCopyAsAction var1 = new SaveCopyAsAction();
      var1.setWaitService(this.ui);
      KongaPreferences var2 = KongaPreferences.getPreferences(this.getClass());
      var1.setDirectoryPreference(var2);
      return var1;
   }

   private ChunkedTextFileViewer createTextViewer() {
      ChunkedTextFileViewer var1 = new ChunkedTextFileViewer();
      var1.decorateText(TextStyles.DefaultMonoSpaced.asTextDecorator());
      var1.setWaitService(this.ui);
      var1.addAction(this.saveCopyAction);
      return var1;
   }

   @Override
   public void dispose() {
      this.textViewer.closeFile();
   }

   public WaitService getWaitService() {
      return this.ui;
   }

   public void setRefreshAction(Action var1) {
      this.refreshAction = var1;
   }

   public void setSelectedFile(final ServerFileLocator var1, final File var2) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (var1 != null && var2 != null) {
               String var1x = new File(var1.getPath()).getName();
               ServerFileContentViewer.this.fileName.setText(var1x);
               ServerFileContentViewer.this.textViewer.openFile(var2, Charsets.UTF_8, 250000);
               ServerFileContentViewer.this.saveCopyAction.setSourceFile(var2);
               ServerFileContentViewer.this.saveCopyAction.setSuggestedTargetName(var1x);
            } else {
               ServerFileContentViewer.this.fileName.setText(" ");
               ServerFileContentViewer.this.textViewer.closeFile();
               ServerFileContentViewer.this.saveCopyAction.setSourceFile(null);
               ServerFileContentViewer.this.saveCopyAction.setSuggestedTargetName(null);
            }
         }
      });
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 3);
      var2.north(this.createToolBar()).center(this.textViewer);
      this.ui.setBaseComponent(var2.container());
      var1.display(this.ui);
   }

   @Override
   public void requestFocus() {
      this.textViewer.requestFocus();
   }

   private UiProvider createToolBar() {
      ArrayList var1 = Lists.newArrayList();
      var1.add(this.refreshAction);
      var1.addAll(this.textViewer.getNavigationButtons());
      var1.add(this.searchAction);
      return Horizontal.box().add(this.fileName).glue().addAllWithSpace(10, var1).withBorder(Empty.border(2, 5, 2, 5));
   }
}
