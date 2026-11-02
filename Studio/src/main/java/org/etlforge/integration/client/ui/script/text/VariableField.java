package org.etlforge.integration.client.ui.script.text;

import com.google.common.base.Predicate;
import java.awt.Font;
import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.Document;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.AutoCompleterFactory;
import org.etlforge.integration.client.ui.script.partition.ScriptPartitioner;
import org.etlforge.integration.data.script.ExpressionParser;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.integration.data.script.ScriptStatus;
import org.etlforge.integration.dataelement.ui.DeTemplateTransferSupport;
import org.etlforge.integration.structure.mapping.PathSeparator;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.text.contentassist.CompletionToken;
import org.etlforge.ui.text.contentassist.DropDownAutoCompleter;
import org.etlforge.ui.text.highlight.SyntaxHighlightDocument;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextComponentWithIconSupport;
import org.etlforge.ui.widget.text.KongaTextPane;

public final class VariableField implements UiProvider {
   private final KongaTextPane field = new VariableField.Field();
   private ExpressionParser parser;

   public VariableField() {
      this(new CompletionContext() {
         @Override
         public CompletionToken[] getAvailableTokens(Document var1, int var2) {
            return new CompletionToken[0];
         }

         @Override
         public boolean delete(CompletionToken var1) {
            return false;
         }
      });
   }

   public VariableField(CompletionContext var1) {
      this.configureField(var1);
   }

   private void configureField(CompletionContext var1) {
      this.installFont();
      this.installSyntaxHighlighting();
      this.installAutoCompletion(var1);
      this.installBorder();
      this.unbindEnter();
      this.field.disableTabs();
      DeTemplateTransferSupport.installDropHandler(this.field, "$%s");
   }

   private void installFont() {
      Font var1 = TextStyles.LargeMonoSpaced.getFont();
      this.field.setFont(var1);
   }

   private void installSyntaxHighlighting() {
      ScriptPartitioner var1 = new ScriptPartitioner(false);
      SyntaxHighlightDocument var2 = (SyntaxHighlightDocument)this.field.getDocument();
      var1.setDocument(var2);
   }

   private void installAutoCompletion(CompletionContext var1) {
      AutoCompleterFactory var2 = AutoCompleterFactory.dropDownFactory();
      DropDownAutoCompleter var3 = (DropDownAutoCompleter)var2.createAutoCompleter(var1);
      var3.setTypingFilter(new VariableField.TypingFilter());
      var3.install(this.field);
   }

   private void installBorder() {
      Border var1 = new JTextField().getBorder();
      this.field.setBorder(var1);
   }

   private void unbindEnter() {
      InputMap var1 = this.field.getInputMap();
      var1.put(KeyUtils.ENTER, "none");
   }

   public void setParser(ExpressionParser var1) {
      this.parser = var1;
   }

   public String getText() {
      return this.field.getText().trim();
   }

   public void setText(String var1) {
      this.field.setText(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.field);
   }

   @Override
   public void requestFocus() {
      this.field.requestFocusInWindow();
   }

   public void bindAction(Action var1) {
      ActionKeyBinding.install(var1, this.field);
   }

   public TextInputField2 asInputField(String var1, boolean var2) {
      MnemonicString var3 = UiResourceBundle.MNEMONIC_FACTORY.fromString(var1);
      String var4 = var3.getText();
      KongaLabel var5 = new KongaLabel(var3);
      return new VariableField.InputFieldImpl(var4, var5, var2);
   }

   private static class Field extends KongaTextPane implements KongaTextComponentWithIconSupport {
      private final KongaTextComponentWithIconSupport.Wrapper iconWrapper = new KongaTextComponentWithIconSupport.Wrapper(this);

      public Field() {
         super(new ScriptDocument());
      }

      @Override
      public void replaceSelection(String var1) {
         String var2 = var1.replaceAll("\\n", "");
         super.replaceSelection(var2);
      }

      @Override
      public void setIcon(Icon var1) {
         Icon var2 = this.iconWrapper.getIcon();
         if (this.iconWrapper.setIcon(var1)) {
            this.firePropertyChange("icon", var2, var1);
         }
      }

      @Override
      public Icon getIcon() {
         return this.iconWrapper.getIcon();
      }
   }

   private class InputFieldImpl extends TextInputField2 {
      private final boolean required;
      private String errorMessage;

      public InputFieldImpl(String var2, JLabel var3, boolean var4) {
         super(var2, VariableField.this.field, var3);
         this.required = var4;
      }

      @Override
      public boolean isInputValid(String var1) {
         if (var1.isEmpty()) {
            this.errorMessage = "This is a required field. You must provide a value.";
            return !this.required;
         }

         if (VariableField.this.parser != null) {
            String var2 = ScriptConstants.wrapInTags(var1, false);
            ExpressionParser.Result var3 = VariableField.this.parser.parse(var2);
            ScriptStatus var4 = var3.getStatus();
            if (var4.getState() == ScriptStatus.State.INVALID) {
               this.errorMessage = var4.getMessage();
               return false;
            }
         }

         this.errorMessage = null;
         return true;
      }

      @Override
      public String getInvalidMessage(String var1) {
         return this.errorMessage;
      }
   }

   private static final class TypingFilter implements Predicate<KeyEvent> {
      private TypingFilter() {
      }

      public boolean apply(KeyEvent var1) {
         char var2 = var1.getKeyChar();
         return PathSeparator.isSeparator(var2) || var2 == '_' || Character.isLetterOrDigit(var2);
      }
   }
}
