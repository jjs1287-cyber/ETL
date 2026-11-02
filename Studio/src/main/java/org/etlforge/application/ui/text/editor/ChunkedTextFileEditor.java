package org.etlforge.application.ui.text.editor;

import com.google.common.collect.Lists;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.EditorIdGenerator;
import org.etlforge.application.ui.window.editor.GenericEditor;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.popup.KongaPopupMenu;
import org.etlforge.ui.widget.text.TextDecorator;
import org.etlforge.util.preference.KongaPreferences;

public final class ChunkedTextFileEditor extends GenericEditor implements TextEditor {
   private final File file;
   private final Charset charset;
   private final int chunkSize;
   private final ChunkedTextFileViewer ui;
   private final SaveCopyAsAction saveCopyAction;

   public ChunkedTextFileEditor(String var1, File var2, Charset var3, int var4) {
      super(EditorIdGenerator.get(DefaultTextEditor.class), var1, null);
      this.file = var2;
      this.charset = var3;
      this.chunkSize = var4;
      this.ui = new ChunkedTextFileViewer();
      this.ui.setWaitService(this.getWaitService());
      this.saveCopyAction = this.createSaveCopyAction();
      this.ui.addAction(this.saveCopyAction);
      this.setUseScroll(false);
   }

   @Override
   public Object getObject() {
      return this.file;
   }

   @Override
   public void decorateText(TextDecorator var1) {
      this.ui.decorateText(var1);
   }

   @Override
   public void requestFocus() {
      this.ui.requestFocus();
   }

   @Override
   public void setEditable(boolean var1) {
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      this.createToolBarButtons();
      super.addToWindow(var1);
      this.prepareForDisplay();
   }

   private SaveCopyAsAction createSaveCopyAction() {
      SaveCopyAsAction var1 = new SaveCopyAsAction(this.file);
      var1.setWaitService(this.getWaitService());
      KongaPreferences var2 = KongaPreferences.getPreferences(this.getClass());
      var1.setDirectoryPreference(var2);
      return var1;
   }

   private void createToolBarButtons() {
      ArrayList var1 = Lists.newArrayList();
      var1.addAll(this.ui.getNavigationButtons());
      var1.add(null);
      var1.add(this.createPopupMenuToolBarButton());
      var1.add(null);
      this.setToolbarButtons(var1);
   }

   private void prepareForDisplay() {
      this.ui.openFile(this.file, this.charset, this.chunkSize);
      this.setMainPanel(this.createMainPanel());
      this.createTitlePanel();
      this.setToolBar(this.createToolBar());
   }

   private JComponent createMainPanel() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(this.ui).nonOpaque();
      return var1.container();
   }

   @Override
   public boolean removeFromWindow(boolean var1) {
      boolean var2 = super.removeFromWindow(var1);
      if (var2) {
         this.ui.closeFile();
      }

      return var2;
   }

   @Override
   protected JPopupMenu getToolbarPopupMenu() {
      KongaPopupMenu var1 = new KongaPopupMenu();
      this.ui.populateIconPopupMenu(var1);
      return var1;
   }

   public void setSuggestedTargetNameForCopy(String var1) {
      this.saveCopyAction.setSuggestedTargetName(var1);
   }

   public void refresh() {
      this.ui.closeFile();
      this.ui.openFile(this.file, this.charset, this.chunkSize);
   }
}
