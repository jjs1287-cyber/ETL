package org.etlforge.integration.client.ui.interchange.location.panel;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.application.UnsupportedPlatformException;
import org.etlforge.application.ui.browser.WebBrowserFactory;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.application.ui.widget.LoginNameField;
import org.etlforge.application.ui.widget.password.AbstractPasswordAction;
import org.etlforge.integration.client.datalocation.cache.LoginNameCache;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelUtils;
import org.etlforge.integration.client.ui.interchange.util.InterchangeViewUtils;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.FileLocation;
import org.etlforge.integration.data.location.LoginProtectedResource;
import org.etlforge.integration.data.location.TemporaryStorageLocation;
import org.etlforge.integration.data.location.file.FileFilter;
import org.etlforge.ui.EmptyUiProvider;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.event.MouseActionListener;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.OneRowPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.DropDownPanel;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextField;

public class FileLocationPanel {
   private static WebBrowserFactory BROWSER_FACTORY;
   private final FileLocation fileLocation;
   private final InputPanel inputPanel;
   private final LoginNameField loginField;
   private final JButton passwordButton;
   private Optional<Password> password = Optional.absent();
   private final FileFilterField fileFilterField;
   private final FileFilterOptionsEditor filterOptionsEditor;
   private final AfterProcessingPanel afterProcessingPanel;
   private final IgnoreLinesPanel ignoreLinesPanel;
   public final KongaCheckBox skipEmptyTargets;
   public final KongaCheckBox autoCreateDirectories;
   public final TextInputField characterEncoding;
   private final SourceTarget sourceTarget;
   private final EndpointCompressionPanel compressionPanel;
   private final EndpointPgpPanel pgpPanel;

   public FileLocationPanel(FileLocation var1, SourceTarget var2, boolean var3, InputPanel var4, int var5) {
      this.fileLocation = var1;
      this.inputPanel = new InputPanel();
      this.sourceTarget = var2;
      this.compressionPanel = new EndpointCompressionPanel(var2);
      this.pgpPanel = new EndpointPgpPanel(var2);
      this.inputPanel.addInputPanel(this.pgpPanel);
      this.inputPanel.addInputPanel(this.compressionPanel);
      this.filterOptionsEditor = this.createFilterOptionsEditor(var1);
      this.loginField = new LoginNameField(var3);
      this.loginField.registerAsChangeSource(this.inputPanel);
      this.passwordButton = this.createPasswordButton();
      this.fileFilterField = this.createFileFilterField(var5);
      this.afterProcessingPanel = new AfterProcessingPanel(this.inputPanel, var5);
      this.ignoreLinesPanel = new IgnoreLinesPanel();
      this.skipEmptyTargets = this.createSkipEmptyTargetsBox();
      this.autoCreateDirectories = this.createAutoCreateDirectoriesBox();
      this.characterEncoding = this.createCharacterEncoding();
      this.populateFields(var1);
      this.addInputComponents();
      if (var4 != null) {
         var4.addInputPanel(this.inputPanel);
      }
   }

   private KongaButton createPasswordButton() {
      FileLocationPanel.PasswordAction var1 = new FileLocationPanel.PasswordAction();
      KongaButton var2 = new KongaButton(var1);
      var1.setTrigger(var2);
      return var2;
   }

   private FileFilterField createFileFilterField(int var1) {
      String var2 = this.sourceTarget == SourceTarget.Source ? Strings.get("File.Filter.Label") : Strings.get("File.Name.Label");
      FileFilterField var3 = new FileFilterField(this.inputPanel, var2, this.sourceTarget, var1);
      String var4 = this.sourceTarget == SourceTarget.Source ? Strings.get("File.Filter.Example") : Strings.get("File.Name.Example");
      var3.setTextWhenEmpty(var4);
      return var3;
   }

   private KongaCheckBox createSkipEmptyTargetsBox() {
      KongaCheckBox var1 = new KongaCheckBox("&Don't create 0K files");
      var1.setToolTipText("Select this option if you prefer to skip creating the target file if there is no target data.");
      return var1;
   }

   private KongaCheckBox createAutoCreateDirectoriesBox() {
      KongaCheckBox var1 = new KongaCheckBox("&Auto Create Directories");
      var1.setToolTipText("Select this option if you prefer to automatically create any directories that do not exist in the target path.");
      return var1;
   }

   private TextInputField createCharacterEncoding() {
      String var1 = "CharacterEncoding";
      KongaTextField var2 = new KongaTextField(10);
      JLabel var3 = new JLabel("Character Encoding:");
      return new TextInputField(var1, var2, var3);
   }

   private void populateFields(FileLocation var1) {
      this.compressionPanel.reset(var1);
      this.pgpPanel.reset(var1);
      if (var1 instanceof LoginProtectedResource) {
         this.loginField.setLoginName(((LoginProtectedResource)var1).getLogin());
         this.password = ((LoginProtectedResource)var1).getPassword();
      }

      this.fileFilterField.reset(var1);
      this.filterOptionsEditor.setOptions(var1.getFileFilter().getOptions());
      this.ignoreLinesPanel.reset(var1);
      this.afterProcessingPanel.reset(var1);
      this.skipEmptyTargets.setSelected(var1.getSkipEmptyFiles());
      this.autoCreateDirectories.setSelected(var1.getFileShareCreateDirectories());
      this.characterEncoding.setValue(var1.getCharacterEncoding());
   }

   private void addInputComponents() {
      switch (this.sourceTarget) {
         case Source:
            this.inputPanel.addInputPanel(this.ignoreLinesPanel);
            break;
         case Target:
            this.inputPanel.addInputField(new CheckBoxInputField("Skip Empty Files", this.skipEmptyTargets, (JLabel)null));
            this.inputPanel.addInputField(new CheckBoxInputField("Auto Create Directories", this.autoCreateDirectories, (JLabel)null));
            this.inputPanel.addInputField(this.characterEncoding);
            break;
         default:
            throw new AssertionError();
      }
   }

   public void apply(FileLocation var1) {
      this.compressionPanel.apply(var1);
      this.pgpPanel.apply(var1);
      if (var1 instanceof LoginProtectedResource) {
         var1.setLogin(this.loginField.getLoginName());
         var1.setPassword(this.password);
      }

      FileFilter var2 = new FileFilter(this.fileFilterField.getValue(), this.filterOptionsEditor.getOptions());
      var1.setFileFilter(var2);
      switch (this.sourceTarget) {
         case Source:
            this.ignoreLinesPanel.apply(var1);
            this.afterProcessingPanel.apply(var1);
            break;
         case Target:
            boolean var3 = this.skipEmptyTargets.isSelected();
            var1.setSkipEmptyFiles(var3);
            var1.setFileShareCreateDirectories(this.autoCreateDirectories.isSelected());
            var1.setCharacterEncoding(this.characterEncoding.getValue());
            break;
         default:
            throw new AssertionError();
      }
   }

   protected boolean needsLoginControls() {
      return true;
   }

   public UiProvider getLoginFieldsForLayout() {
      OneRowPanel var1 = new OneRowPanel();
      var1.setAnchor(OneRowPanel.SOUTHWEST);
      var1.add(this.loginField);
      var1.add(Box.createHorizontalStrut(10));
      var1.add(this.passwordButton);
      var1.setWeightX(1.0);
      var1.add(Box.createHorizontalGlue());
      return var1;
   }

   public void layoutLoginControl(DataLocationPanelLayout var1) {
      TextInputField2 var2 = this.loginField.asInputField();
      var1.add(var2.getLabel(), var2.getInputComponent(), this.passwordButton);
   }

   public UiProvider getFileFilterFieldForLayout() {
      return this.fileFilterField.asUiProvider();
   }

   public InputField getFileFilterFieldAsInputFieldForLayout() {
      return this.fileFilterField.asInputFieldForLayout();
   }

   public UiProvider getFileFilterOptionsTrigger() {
      this.verifySourceOnlyFunctionality();
      KongaButton var1 = this.createEditFilterOptionsButton();
      return var1.asUiProvider();
   }

   public final String getFileFilterString() {
      return this.fileFilterField.getValue();
   }

   public final void setFileFilterString(String var1) {
      this.fileFilterField.setValue(var1);
   }

   private void verifySourceOnlyFunctionality() {
      if (this.sourceTarget == SourceTarget.Target) {
         throw new UnsupportedOperationException("This functionality is supported only for Source locations");
      }
   }

   public void layoutSourceFileFilterControls(DataLocationPanelLayout var1) {
      this.verifySourceOnlyFunctionality();
      InputField var2 = this.fileFilterField.asInputFieldForLayout();
      var1.add(var2.getLabel(), var2.getInputComponent(), this.createEditFilterOptionsButton());
   }

   public UiProvider getIgnoreLineFieldsForLayout() {
      if (ClientInfo.isDataLoader()) {
         return new EmptyUiProvider();
      }

      this.verifySourceOnlyFunctionality();
      return UiProviderAdapter.of(this.ignoreLinesPanel.getUi());
   }

   public JComponent getAfterProcessingLayout() {
      this.verifySourceOnlyFunctionality();
      return this.afterProcessingPanel.getUi();
   }

   private FileFilterOptionsEditor createFilterOptionsEditor(FileLocation var1) {
      FileFilterOptionsEditor var2 = new FileFilterOptionsEditor(var1.getFileFilter().getOptions());
      var2.addInputChangeListener(this.inputPanel);
      var2.addUndoRedoListener(this.inputPanel);
      return var2;
   }

   public void reset(FileLocation var1) {
      this.populateFields(var1);
      if (this.sourceTarget == SourceTarget.Source) {
         this.ignoreLinesPanel.reset(var1);
      }
   }

   public void setLoginNameCache(LoginNameCache var1) {
      Preconditions.checkNotNull(var1, "cache");
      this.loginField.installAutoCompletion(new CacheBasedLoginNameHistory(var1));
   }

   private KongaButton createEditFilterOptionsButton() {
      KongaButton var1 = new KongaButton(new FileLocationPanel.EditFilterOptionsAction());
      this.filterOptionsEditor.setTrigger(var1);
      return var1;
   }

   public void layoutComponents(Container var1, Object var2) {
      if (this.sourceTarget == SourceTarget.Source) {
         this.layoutForSource(var1, var2);
      } else {
         this.layoutForTarget(var1, var2);
      }
   }

   public UiProvider layoutComponents(DataLocationPanelLayout var1) {
      return this.sourceTarget == SourceTarget.Source ? this.layoutForSource(var1) : this.layoutForTarget(var1);
   }

   private void layoutForSource(Container var1, Object var2) {
      OneColumnPanel var3 = createControlCanvas();
      if (this.needsLoginControls()) {
         var3.add(this.getLoginFieldsForLayout());
      }

      var3.add(this.layoutFilterControls());
      var3.add(this.layoutOptionalControls());
      var3.addToParent(var1, var2);
   }

   private UiProvider layoutFilterControls() {
      BoxBuilder var1 = BoxBuilder.horizontal().bottomAligned();
      var1.addAllWithSpace(10, createInvisibleCapsule(this.fileFilterField.asInputFieldForLayout()), this.createEditFilterOptionsButton());
      return var1;
   }

   private JComponent layoutOptionalControls() {
      OneColumnPanel var1 = createControlCanvas();
      var1.add(this.compressionPanel.getUi());
      var1.add(this.pgpPanel.getUi());
      var1.add(this.afterProcessingPanel.getUi());
      var1.noFill(this.getIgnoreLineFieldsForLayout());
      JComponent var2 = new BorderLayoutBuilder().north(var1).container();
      InterchangeViewUtils.configurePropertyFields(var2);
      DropDownPanel var3 = DataLocationPanelUtils.createOptionsDropDown(var2, false);
      return var3.getDisplayer();
   }

   private JComponent layoutFileOptionsDropDown() {
      OneColumnPanel var1 = createControlCanvas();
      var1.add(this.compressionPanel.getUi());
      var1.add(this.pgpPanel.getUi());
      var1.add(this.skipEmptyTargets);
      if (!(this.fileLocation instanceof TemporaryStorageLocation)) {
         var1.add(this.autoCreateDirectories);
      }

      var1.add(this.getCharacterEncodingPanel());
      JComponent var2 = new BorderLayoutBuilder().north(var1).container();
      InterchangeViewUtils.configurePropertyFields(var2);
      DropDownPanel var3 = DataLocationPanelUtils.createOptionsDropDown(var2, false);
      return var3.getDisplayer();
   }

   public TwoColumnPanel getCharacterEncodingPanel() {
      TwoColumnPanel var1 = new TwoColumnPanel();
      var1.withLeftAnchor(GridBagLayoutPanel.Anchor.EAST);
      var1.withRightAnchor(GridBagLayoutPanel.Anchor.WEST);
      var1.withRightWeights(1.0, 0.5);
      var1.withInsets(0, 0, 0, 0);
      var1.addRow(this.characterEncoding.getLabel(), this.characterEncoding.getInputComponent());
      var1.withInsets(0, 0, 14, 0);
      var1.addRow(null, this.createCharacterEncodingLink());
      return var1;
   }

   public JLabel createCharacterEncodingLink() {
      return this.createLinkLabel("Supported Encodings", "http://www.etlforge.invalid/Docs/WebHelp/Reference/Supported_Character_Encodings.htm");
   }

   private JLabel createLinkLabel(String var1, final String var2) {
      JLabel var3 = new JLabel("<html><u>" + var1 + "</u>");
      var3.setForeground(Color.BLUE);
      if (BROWSER_FACTORY != null) {
         var3.setCursor(Cursor.getPredefinedCursor(12));
         var3.addMouseListener(new MouseActionListener() {
            @Override
            protected void onSingleClick(MouseEvent var1) {
               FileLocationPanel.BROWSER_FACTORY.createBrowser().open(var2);
            }
         });
      }

      return var3;
   }

   private void layoutForTarget(Container var1, Object var2) {
      OneColumnPanel var3 = createControlCanvas();
      if (this.needsLoginControls()) {
         var3.add(this.getLoginFieldsForLayout());
      }

      var3.add(InputFieldFactory.getCapsule(this.fileFilterField.asInputFieldForLayout()));
      var3.add(this.layoutFileOptionsDropDown());
      var3.addToParent(var1, var2);
   }

   private UiProvider layoutForSource(DataLocationPanelLayout var1) {
      this.layoutLoginControl(var1);
      this.layoutSourceFileFilterControls(var1);
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
      var2.north(InvisiblePanel.of(var1));
      var2.center(this.layoutOptionalControls());
      return var2;
   }

   private UiProvider layoutForTarget(DataLocationPanelLayout var1) {
      this.layoutLoginControl(var1);
      var1.add(this.fileFilterField.asInputFieldForLayout());
      var1.add(this.layoutFileOptionsDropDown(), true);
      return var1;
   }

   private static OneColumnPanel createControlCanvas() {
      OneColumnPanel var0 = new OneColumnPanel();
      var0.withInsets(0, 0, 10, 0);
      var0.setWeightX(1.0);
      return var0;
   }

   private static JComponent createInvisibleCapsule(InputField var0) {
      JComponent var1 = InputFieldFactory.getCapsule(var0);
      return InvisiblePanel.of(var1);
   }

   static {
      try {
         BROWSER_FACTORY = WebBrowserFactory.newInstance();
      } catch (UnsupportedPlatformException var1) {
         var1.printStackTrace();
      }
   }

   private class EditFilterOptionsAction extends AbstractAction {
      public EditFilterOptionsAction() {
         super("Filter Options...");
         this.putValue("ShortDescription", "Set advanced options for how the file filter will be interpreted");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               FileLocationPanel.this.filterOptionsEditor.edit();
            }
         });
      }
   }

   private class PasswordAction extends AbstractPasswordAction {
      private PasswordAction() {
      }

      @Override
      protected void handlePassword(char[] var1) {
         if (var1 != null) {
            FileLocationPanel.this.password = Optional.of(Password.encrypt(new String(var1)));
            if (FileLocationPanel.this.inputPanel.isSendingInputChangeEvents()) {
               InputChangeEvent var2 = new InputChangeEvent(FileLocationPanel.this.inputPanel, FileLocationPanel.this.passwordButton, "Password");
               FileLocationPanel.this.inputPanel.inputChanged(var2);
            }
         }
      }
   }
}
