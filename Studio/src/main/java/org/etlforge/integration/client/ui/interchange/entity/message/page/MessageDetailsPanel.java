package org.etlforge.integration.client.ui.interchange.entity.message.page;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Scrollable;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.binding.ui.BoundPropertyFactory;
import org.etlforge.integration.data.entity.Message;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.DropDownPanel;

public final class MessageDetailsPanel extends InputPanel {
   private final TextInputField serversField;
   private final TextInputField2 sendToField;
   private final TextInputField ccField;
   private final TextInputField bccField;
   private final TextInputField sentFromField;
   private final TextInputField replyToField;
   private final TextInputField subjectField;
   private final TextInputField textField;
   private final AuthenticationPanel authenticationPanel = this.createAuthenticationPanel();
   private final CheckBoxInputField useSslOption;

   public MessageDetailsPanel(Message var1) {
      this.serversField = createServerField();
      this.sendToField = createSendToField();
      this.ccField = createCcField();
      this.bccField = createBccField();
      this.sentFromField = createSentFromField();
      this.replyToField = createReplyToField();
      this.subjectField = createSubjectField();
      this.textField = createMessageTextField();
      this.useSslOption = createUseSslOption();
      this.setMessage(var1);
      this.addInputFields();
      this.layoutComponents();
   }

   private AuthenticationPanel createAuthenticationPanel() {
      AuthenticationPanel var1 = new AuthenticationPanel();
      var1.registerWithParent(this);
      return var1;
   }

   private static TextInputField createServerField() {
      String var0 = Strings.get("MessagePage.Label.Server");
      return InputFieldFactory.getTextInputField(var0, var0, 50, true);
   }

   private static TextInputField2 createSendToField() {
      String var0 = Strings.get("MessagePage.Label.To");
      return InputFieldFactory.getNonEmptyTextAreaInputField(var0, var0, 2, 50);
   }

   private static TextInputField createCcField() {
      String var0 = Strings.get("MessagePage.Label.CC");
      return InputFieldFactory.getTextInputArea(var0, var0, 2, 50);
   }

   private static TextInputField createBccField() {
      String var0 = Strings.get("MessagePage.Label.BCC");
      return InputFieldFactory.getTextInputArea(var0, var0, 2, 50);
   }

   private static TextInputField createSentFromField() {
      String var0 = Strings.get("MessagePage.Label.From");
      return InputFieldFactory.getTextInputField(var0, var0, 20, true);
   }

   private static TextInputField createReplyToField() {
      String var0 = Strings.get("MessagePage.Label.ReplyTo");
      return InputFieldFactory.getTextInputField(var0, var0, 20, true);
   }

   private static TextInputField createSubjectField() {
      String var0 = Strings.get("MessagePage.Label.Subject");
      return InputFieldFactory.getTextInputField(var0, var0, 30, true);
   }

   private static TextInputField createMessageTextField() {
      String var0 = Strings.get("MessagePage.Label.Text");
      return InputFieldFactory.getTextInputArea(var0, var0, 10, 50);
   }

   private static CheckBoxInputField createUseSslOption() {
      String var0 = Strings.get("MessagePage.Label.UseSSL");
      return InputFieldFactory.getCheckBoxInputField(var0, var0);
   }

   private void addInputFields() {
      this.addInputField(this.sendToField, this.sendToField, this.sendToField);
      this.addInputField(this.serversField);
      this.addInputField(this.sentFromField);
      this.addInputField(this.replyToField);
      this.addInputField(this.subjectField);
      this.addInputField(this.ccField);
      this.addInputField(this.bccField);
      this.addInputField(this.textField);
      this.addInputField(this.useSslOption);
   }

   private void layoutComponents() {
      OneColumnPanel var1 = new OneColumnPanel().nonOpaque().withInsets(5, 0, 5, 5);
      var1.noFill(this.getTopFields());
      var1.withInsets(20, 0, 0, 5);
      var1.add(this.authenticationPanel);
      var1.add(this.getOptionalDetailsDropDown());
      var1.withInsets(20, 0, 5, 5);
      var1.add(getInputFieldForLayout(this.subjectField));
      var1.withInsets(5, 0, 5, 5);
      var1.fillOut(getInputFieldForLayout(this.textField));
      BorderLayoutBuilder.from(this.getUi()).center(var1);
   }

   private OneColumnPanel getTopFields() {
      return Vertical.column()
         .stretchHorizontally()
         .withInsets(5, 0, 5, 0)
         .of(
            getInputFieldForLayout(this.serversField),
            getInputFieldForLayout(this.sendToField),
            getInputFieldForLayout(this.sentFromField),
            getInputFieldForLayout(this.useSslOption)
         );
   }

   private JComponent getOptionalDetailsDropDown() {
      JPanel var1 = new OneColumnPanel()
         .withInsets(5, 30, 5, 5)
         .of(getInputFieldForLayout(this.ccField), getInputFieldForLayout(this.bccField), getInputFieldForLayout(this.replyToField))
         .invisible();
      UiUtils.setOpaqueRecursivelySkipDefaults(var1, false);
      DropDownPanel var2 = new DropDownPanel(var1, Strings.get("MessagePage.Optional"), false);
      var2.setFocusWhenExpanded(this.ccField.getInputComponent());
      return var2.getDisplayer();
   }

   private static JComponent getInputFieldForLayout(InputField var0) {
      JComponent var1 = var0.getInputComponent();
      boolean var2 = var1 instanceof Scrollable && !(var1 instanceof JTextField);
      return InputFieldFactory.getCapsule(var0, var2);
   }

   BoundProperty[] getBoundPropertyControls() {
      return new BoundProperty[]{
         BoundPropertyFactory.textProperty("smtp_servers", this.serversField),
         BoundPropertyFactory.textProperty("send_to", this.sendToField),
         BoundPropertyFactory.textProperty("cc", this.ccField),
         BoundPropertyFactory.textProperty("bcc", this.bccField),
         BoundPropertyFactory.textProperty("sent_from", this.sentFromField),
         BoundPropertyFactory.textProperty("reply_to", this.replyToField),
         BoundPropertyFactory.textProperty("subject", this.subjectField),
         BoundPropertyFactory.textProperty("message_text", this.textField),
         BoundPropertyFactory.toggleProperty("use_ssl", this.useSslOption)
      };
   }

   public void setMessage(Message var1) {
      this.serversField.setValue(var1.getSmtpServers());
      this.serversField.getInputComponent().setCaretPosition(0);
      this.sendToField.setValue(var1.getSendTo());
      this.ccField.setValue(var1.getCC());
      this.bccField.setValue(var1.getBCC());
      this.sentFromField.setValue(var1.getSentFrom());
      this.replyToField.setValue(var1.getReplyTo());
      this.subjectField.setValue(var1.getSubject());
      this.textField.setValue(var1.getText());
      this.useSslOption.setSelected(var1.getUseSSL());
      this.authenticationPanel.setMessage(var1);
   }

   public void applyTo(Message var1) {
      var1.setSmtpServers(this.getSmtpServer());
      var1.setSentFrom(this.getSentFrom());
      var1.setReplyTo(this.getReplyTo());
      var1.setSendTo(this.getTo());
      var1.setCC(this.getCC());
      var1.setBCC(this.getBCC());
      var1.setSubject(this.getSubject());
      var1.setText(this.getMessageText());
      var1.setUseSSL(this.useSslOption.isSelected());
      this.authenticationPanel.applyTo(var1);
   }

   private String getTo() {
      return this.sendToField.getValue().trim();
   }

   private String getCC() {
      return this.ccField.getValue().trim();
   }

   private String getBCC() {
      return this.bccField.getValue().trim();
   }

   private String getSentFrom() {
      return this.sentFromField.getValue().trim();
   }

   private String getReplyTo() {
      return this.replyToField.getValue().trim();
   }

   private String getSubject() {
      return this.subjectField.getValue().trim();
   }

   private String getMessageText() {
      return this.textField.getValue().trim();
   }

   private String getSmtpServer() {
      return this.serversField.getValue().trim();
   }
}
