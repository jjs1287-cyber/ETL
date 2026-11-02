package org.etlforge.ui.util.file;

import com.google.common.base.Preconditions;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.io.File;
import javax.annotation.Nullable;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.JTextComponent;
import org.apache.commons.lang.StringUtils;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.event.ActionListeners;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputFieldVisitor;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.InputPanelException;
import org.etlforge.ui.input.InputPanelSiteImplementor;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneRowPanel;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.text.contentassist.DropDownAutoCompleter;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.ui.widget.LabelAdapter;
import org.etlforge.ui.widget.LabelDecorator;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.FileDropStrategy;
import org.etlforge.ui.widget.text.FileDropSupport;
import org.etlforge.util.Property;
import org.etlforge.util.event.ListenerManager;
import org.etlforge.util.event.ListenerManagerFactory;

public abstract class AbstractFileSelectorPanel implements FileSelectorPanel {
   private final FileChooser fileChooser;
   private final FileSelectorPanel.Mode mode;
   private final String title;
   private FileFilter fileFilter;
   private PathValidator pathValidator = PathValidators.ALWAYS_VALID;
   private final RecentFileStore recentFiles;
   private final InputPanel panel;
   private boolean useTitleLabel;
   private final JLabel titleLabel;
   private final KongaButton browseButton;
   private final TextInputField2 textField;
   private InputPanelSiteImplementor siteImplementor;
   private final ListenerManager<FileSelectionListener> selectionListeners;
   private final ActionListeners actionListeners;
   private DropDownAutoCompleter completer;

   protected AbstractFileSelectorPanel(FileChooser var1, FileSelectorPanel.Mode var2, String var3, RecentFileStore var4) {
      Preconditions.checkNotNull(var2, "mode");
      this.fileChooser = var1 == null ? new DefaultFileChooser() : var1;
      this.mode = var2;
      this.title = var3;
      this.recentFiles = var4;
      this.selectionListeners = ListenerManagerFactory.create(FileSelectionListener.class);
      this.actionListeners = new ActionListeners();
      this.useTitleLabel = true;
      this.panel = this.createInputPanel();
      this.textField = this.createTextInputField();
      this.titleLabel = this.createTitleLabel(var3);
      this.browseButton = new KongaButton(new AbstractFileSelectorPanel.BrowseAction());
      this.panel.setSendInputChangeEvents(true);
      this.panel.setSendUndoEvents(true);
   }

   private InputPanel createInputPanel() {
      InputPanel var1 = new InputPanel(new BorderLayout());
      this.siteImplementor = new InputPanelSiteImplementor(var1);
      this.siteImplementor.addInputChangeListener(new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1) {
            AbstractFileSelectorPanel.this.fireFileSelectionChanged();
         }
      });
      return var1;
   }

   private JLabel createTitleLabel(String var1) {
      if (StringUtils.isEmpty(var1)) {
         return null;
      }

      KongaLabel var2 = KongaLabel.withMnemonic(var1);
      var2.setLabelFor(this.textField.getInputComponent());
      return var2;
   }

   private TextInputField2 createTextInputField() {
      JTextField var1 = this.createTextField();
      FileDropSupport.install(var1, FileDropStrategy.SET_FULL_PATH);
      var1.addActionListener(new AbstractFileSelectorPanel.TextActionSender());
      this.installToolTip(var1);
      TextInputField2 var2 = new TextInputField2("Filename", var1) {
         @Override
         public boolean isInputValid(String var1) {
            return AbstractFileSelectorPanel.this.pathValidator.isValid(var1);
         }
      };
      var2.setAutoTrim(true);
      var2.setChangeEventDelay(200);
      this.panel.addInputField(var2);
      this.installAutoCompletion(new AbstractFileSelectorPanel.CompletionFileFilter(this.getFileFilter(), this.getSelectionMode()), var2);
      return var2;
   }

   private void installAutoCompletion(java.io.FileFilter var1, TextInputField var2) {
      if (this.completer != null) {
         this.completer.uninstall();
      }

      FilePathCompletionContext var3 = new FilePathCompletionContext(var1);
      this.completer = new DropDownAutoCompleter(var3);
      this.completer.setDisplayMatchingTokensAfterCompletion(true);
      this.completer.install(var2.getInputComponent());
   }

   private void installToolTip(final JTextComponent var1) {
      var1.getDocument().addDocumentListener(new DocumentContentChangeListener() {
         @Override
         public void contentChanged(DocumentEvent var1x) {
            var1.setToolTipText(var1.getText());
         }
      });
   }

   protected abstract JTextField createTextField();

   protected JComponent getFileInputFieldForLayout() {
      return this.textField.getInputComponent();
   }

   protected final RecentFileStore getRecentFiles() {
      return this.recentFiles;
   }

   protected final InputPanel getInputPanel() {
      return this.panel;
   }

   @Override
   public final void setFileFilter(FileFilter var1) {
      this.fileFilter = var1;
      this.installAutoCompletion(new AbstractFileSelectorPanel.CompletionFileFilter(var1, this.getSelectionMode()), this.textField);
   }

   public final FileFilter getFileFilter() {
      return this.fileFilter;
   }

   @Override
   public void setPathValidator(PathValidator var1) {
      this.pathValidator = Preconditions.checkNotNull(var1);
   }

   @Override
   public final void setUseTitleLabel(boolean var1) {
      this.useTitleLabel = var1;
   }

   @Override
   public final void decorateTitle(LabelDecorator var1) {
      if (this.titleLabel != null) {
         LabelAdapter var2 = new LabelAdapter(this.titleLabel);
         var1.decorate(var2);
      }
   }

   public final void decorateBrowseButton(LabelDecorator var1) {
      LabelAdapter var2 = new LabelAdapter(this.browseButton);
      var1.decorate(var2);
   }

   @Nullable
   @Override
   public File getSelectedFile() {
      return this.getSelectedFile(true);
   }

   @Nullable
   private File getSelectedFile(boolean var1) {
      String var2 = this.textField.getValue().trim();
      File var3 = this.pathToFile(var2);
      if (var1 && var3 != null) {
         this.updateFileStore(var3);
      }

      return var3;
   }

   @Nullable
   private File pathToFile(String var1) {
      if (var1.isEmpty()) {
         return null;
      } else {
         return !this.pathValidator.isValid(var1) ? null : new File(var1);
      }
   }

   protected final void updateFileStore(File var1) {
      if (this.recentFiles != null) {
         this.recentFiles.update(var1);
      }
   }

   @Override
   public final boolean isInputValid() {
      String var1 = this.textField.getValue().trim();
      return var1.isEmpty() ? false : this.pathValidator.isValid(var1);
   }

   @Override
   public final void setEnabled(boolean var1) {
      UiUtils.setEnabledRecursively(this.panel.getUi(), var1);
   }

   @Override
   public void setSelectedFile(File var1) {
      this.updateTextField(var1);
   }

   public final FileChooser.SelectionMode getSelectionMode() {
      return this.fileChooser.getSelectionMode();
   }

   @Override
   public final void setSelectionMode(FileChooser.SelectionMode var1) {
      this.fileChooser.setSelectionMode(var1);
      this.installAutoCompletion(new AbstractFileSelectorPanel.CompletionFileFilter(this.getFileFilter(), var1), this.textField);
   }

   public void setAutoCompletionEnabled(boolean var1) {
      if (this.completer != null) {
         this.completer.setEnabled(var1);
      }
   }

   @Override
   public final void decorate(UiCanvas var1) {
      this.layoutDisplayPanel();
      var1.display(this.panel.getUi());
   }

   private void layoutDisplayPanel() {
      OneRowPanel var1 = new OneRowPanel();
      var1.setAnchor(OneRowPanel.SOUTHWEST);
      var1.setWeightX(1.0);
      var1.withInsets(0, 0, 0, 10);
      var1.add(this.getInputFieldLayout());
      var1.setWeightX(0.0);
      var1.withInsets(0, 0, 0, 0);
      var1.add(this.browseButton);
      var1.addToParent(this.panel.getUi(), "North");
   }

   private JComponent getInputFieldLayout() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 2);
      if (this.useTitleLabel) {
         var1.north(this.titleLabel);
      }

      var1.center(this.getFileInputFieldForLayout());
      return var1.container();
   }

   @Override
   public final void requestFocus() {
      this.textField.requestFocus();
   }

   @Override
   public final void addActionListener(ActionListener var1) {
      this.actionListeners.add(var1);
   }

   @Override
   public final void removeActionListener(ActionListener var1) {
      this.actionListeners.remove(var1);
   }

   @Override
   public final void addFileSelectionListener(FileSelectionListener var1) {
      this.selectionListeners.add(var1);
   }

   @Override
   public final void removeFileSelectionListener(FileSelectionListener var1) {
      this.selectionListeners.remove(var1);
   }

   @Override
   public final void addInputChangeListener(InputChangeListener var1) {
      this.siteImplementor.addInputChangeListener(var1);
   }

   @Override
   public final void removeInputChangeListener(InputChangeListener var1) {
      this.siteImplementor.removeInputChangeListener(var1);
   }

   @Override
   public final void addUndoableEditListener(UndoableEditListener var1) {
      this.siteImplementor.addUndoableEditListener(var1);
   }

   @Override
   public final void removeUndoableEditListener(UndoableEditListener var1) {
      this.siteImplementor.removeUndoableEditListener(var1);
   }

   @Override
   public final void validateInput() throws InputPanelException {
      this.siteImplementor.validateInput();
   }

   @Override
   public final void decorateFields() {
      this.siteImplementor.decorateFields();
   }

   @Override
   public final void dispose() {
      this.siteImplementor.dispose();
   }

   @Override
   public final void setActive(boolean var1) {
      this.siteImplementor.setActive(var1);
      if (var1) {
         this.textField.requestFocus();
      }
   }

   @Override
   public final void setSendInputChangeEvents(boolean var1) {
      this.siteImplementor.setSendInputChangeEvents(var1);
   }

   @Override
   public final boolean isSendingInputChangeEvents() {
      return this.siteImplementor.isSendingInputChangeEvents();
   }

   @Override
   public final void setSendUndoEvents(boolean var1) {
      this.siteImplementor.setSendUndoEvents(var1);
   }

   @Override
   public final boolean isSendingUndoEvents() {
      return this.siteImplementor.isSendingUndoEvents();
   }

   @Override
   public final void visitAllFields(InputFieldVisitor var1) {
      this.siteImplementor.visitAllFields(var1);
   }

   @Override
   public final void setBrowseButtonText(String var1) {
      this.browseButton.setText(var1);
   }

   private void selectFile() {
      File var1 = null;
      if (this.mode == FileSelectorPanel.Mode.OPEN) {
         var1 = this.getFileToOpen();
      } else {
         var1 = this.getFileToSave();
      }

      if (var1 != null) {
         this.setSelectedFile(var1);
      }
   }

   private File getFileToOpen() {
      return this.fileChooser.getFileToOpen(this.browseButton, this.fileFilter, this.recentFiles, this.title);
   }

   private File getFileToSave() {
      File var1 = this.getSelectedFile(false);
      return this.fileChooser.getFileToSave(this.browseButton, this.fileFilter, this.recentFiles, this.title, var1);
   }

   private void updateTextField(File var1) {
      String var2 = var1 == null ? "" : var1.getAbsolutePath();
      String var3 = this.textField.getValue().trim();
      if (!var2.equals(var3)) {
         this.textField.setValue(var2);
         this.textField.getInputComponent().setCaretPosition(0);
      }
   }

   private void fireFileSelectionChanged() {
      if (!this.selectionListeners.isEmpty()) {
         File var1 = this.getSelectedFile(false);
         this.selectionListeners.call().selectionChanged(var1);
      }
   }

   @Override
   public final void addFocusListener(FocusListener var1) {
      this.textField.getInputComponent().addFocusListener(var1);
      this.browseButton.addFocusListener(var1);
   }

   @Override
   public final void removeFocusListener(FocusListener var1) {
      this.textField.getInputComponent().removeFocusListener(var1);
      this.browseButton.removeFocusListener(var1);
   }

   @Override
   public final Binding bindWith(Property<File> var1) {
      return new FileSelectorBinding(this, var1);
   }

   @Override
   public void layoutIn(TwoColumnLayout var1) {
      var1.addRow(this.getFileInputFieldForLayout(), this.browseButton);
   }

   private final class BrowseAction extends AbstractAction {
      public BrowseAction() {
         super(UiResourceBundle.getString("Browse.Button"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         AbstractFileSelectorPanel.this.selectFile();
      }
   }

   private static final class CompletionFileFilter implements java.io.FileFilter {
      private final FileFilter fileChooserFilter;
      private final FileChooser.SelectionMode selectionMode;

      public CompletionFileFilter(FileFilter var1, FileChooser.SelectionMode var2) {
         this.fileChooserFilter = var1 != null ? var1 : new FileFilter() {
            @Override
            public boolean accept(File var1) {
               return true;
            }

            @Override
            public String getDescription() {
               return null;
            }
         };
         this.selectionMode = var2;
      }

      @Override
      public boolean accept(File var1) {
         switch (this.selectionMode) {
            case FILES_ONLY:
            case FILES_AND_DIRECTORIES:
               return var1.isDirectory() || this.fileChooserFilter.accept(var1);
            case DIRECTORIES_ONLY:
               return var1.isDirectory() && this.fileChooserFilter.accept(var1);
            default:
               throw new AssertionError();
         }
      }
   }

   private final class TextActionSender implements ActionListener {
      private TextActionSender() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         AbstractFileSelectorPanel.this.actionListeners.call().actionPerformed(var1);
      }
   }
}
