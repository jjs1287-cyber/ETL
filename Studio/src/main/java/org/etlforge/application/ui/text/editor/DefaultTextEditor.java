package org.etlforge.application.ui.text.editor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.event.UndoableEditEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.EditorIdGenerator;
import org.etlforge.application.ui.window.editor.GenericEditor;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.file.DefaultFileChooser;
import org.etlforge.ui.util.file.FileChooser;
import org.etlforge.ui.widget.DefaultFileChooserFactory;
import org.etlforge.ui.widget.popup.KongaPopupMenu;
import org.etlforge.ui.widget.text.TextDecorator;

class DefaultTextEditor extends GenericEditor implements TextEditor {
   private Object object;
   private final DefaultTextEditorUi ui;
   private FileChooser fileChooser;
   private final Charset charset;

   public DefaultTextEditor(String var1, String var2, String var3) {
      super(EditorIdGenerator.get(DefaultTextEditor.class), var1, var2);
      this.charset = Charset.defaultCharset();
      this.ui = new DefaultTextEditorUi(this);
      this.object = var3;
      this.setUseScroll(false);
   }

   public DefaultTextEditor(String var1, File var2, Charset var3) {
      super(EditorIdGenerator.get(DefaultTextEditor.class), var1, var2.getName());
      this.ui = new DefaultTextEditorUi(this);
      this.object = var2;
      this.charset = var3 == null ? Charset.defaultCharset() : var3;
      this.setUseScroll(false);
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.prepareForDisplay();
   }

   @Override
   public Object getObject() {
      return this.object;
   }

   @Override
   public void setEditable(boolean var1) {
      this.ui.setEditable(var1);
   }

   public Charset getCharset() {
      return this.charset;
   }

   @Override
   public void requestFocus() {
      this.ui.setInitialFocus();
   }

   @Override
   public String getTitle() {
      if (this.object instanceof File) {
         String var1 = ((File)this.object).getName();
         return this.isDirty() ? var1 + " *" : var1;
      } else {
         return super.getTitle();
      }
   }

   @Override
   public void decorateText(TextDecorator var1) {
      this.ui.decorateText(var1);
   }

   @Override
   public void undoableEditHappened(UndoableEditEvent var1) {
      this.setDirty();
      super.undoableEditHappened(var1);
   }

   @Override
   public void save() throws SaveEditorException {
      if (this.object instanceof File) {
         this.saveToFile((File)this.object);
      }
   }

   private void saveToFile(File var1) throws SaveEditorException {
      try {
         this.ui.saveToFile(var1);
         this.setClean();
      } catch (IOException var3) {
         throw new SaveEditorException(this, "Failed to save the file " + ((File)this.object).getAbsolutePath() + ": " + var3.getMessage(), var3);
      }
   }

   @Override
   public boolean isSaveAsAllowed() {
      return this.isDirty();
   }

   @Override
   public void saveAs() throws SaveEditorException {
      new DefaultTextEditor.SaveAs().execute();
   }

   @Override
   protected JPopupMenu getToolbarPopupMenu() {
      KongaPopupMenu var1 = new KongaPopupMenu();
      this.ui.populateIconPopupMenu(var1);
      return var1;
   }

   private void prepareForDisplay() {
      this.ui.populateTextArea();
      this.setMainPanel(this.createMainPanel());
      this.createToolBarButtons();
      this.createTitlePanel();
      this.setToolBar(this.createToolBar());
   }

   private JComponent createMainPanel() {
      return new BorderLayoutBuilder(this.ui.getDisplayer()).nonOpaque().container();
   }

   private void createToolBarButtons() {
      ArrayList var1 = new ArrayList();
      var1.add(this.createPopupMenuToolBarButton());
      var1.add(null);
      this.setToolbarButtons(var1);
   }

   private synchronized FileChooser getFileChooser() {
      if (this.fileChooser == null) {
         this.fileChooser = new DefaultFileChooser(new DefaultFileChooserFactory());
      }

      return this.fileChooser;
   }

   private class SaveAs {
      private File originalFile;
      private File newFile;

      private SaveAs() {
      }

      public void execute() throws SaveEditorException {
         this.originalFile = DefaultTextEditor.this.object instanceof File ? (File)DefaultTextEditor.this.object : null;
         this.newFile = DefaultTextEditor.this.getFileChooser().getFileToSave(DefaultTextEditor.this.getMainPanel(), this.originalFile, null, null);
         if (this.newFile != null) {
            this.saveToNewFile();
         }
      }

      private void saveToNewFile() throws SaveEditorException {
         try {
            DefaultTextEditor.this.ui.saveToFile(this.newFile);
            this.fileWasSaved();
         } catch (IOException var2) {
            throw new SaveEditorException(
               DefaultTextEditor.this,
               "Failed to save the file " + this.originalFile.getAbsolutePath() + " to " + this.newFile.getAbsolutePath() + ": " + var2.getMessage(),
               var2
            );
         }
      }

      private void fileWasSaved() {
         DefaultTextEditor.this.setClean();
         if (this.newFile != DefaultTextEditor.this.object) {
            File var1 = DefaultTextEditor.this.object instanceof File ? (File)DefaultTextEditor.this.object : null;
            DefaultTextEditor.this.object = this.newFile;
            DefaultTextEditor.this.setSubTitle(this.newFile.getName());
            DefaultTextEditor.this.firePropertyChange("file", var1, this.newFile);
         }
      }
   }
}
