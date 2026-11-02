package com.etlforge.integration.client.ui.sfdc.soql;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeSupport;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.event.DocumentListener;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.widget.AutoCompleterFactory;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.property.TextBinding;
import org.etlforge.ui.text.contentassist.AutoCompleter;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextArea;
import org.etlforge.util.DefaultProperty;
import org.etlforge.util.Property;

public abstract class AbstractQueryArea implements UiProvider {
   protected static final String FIELD_LIST_MARKER = "...";
   private final KongaTextArea textArea;
   private final JButton helpButton;
   private final KongaCheckBox queryAllOption;
   private boolean includeQueryAllOption;
   private boolean includeCaption = true;
   private final Property<String> soqlProperty = new DefaultProperty<>("soql", new PropertyChangeSupport(this));

   public AbstractQueryArea() {
      this.textArea = this.createQueryArea();
      this.helpButton = this.createHelpButton();
      this.queryAllOption = new KongaCheckBox("Query &all");
      this.customize(this.textArea);
   }

   private KongaTextArea createQueryArea() {
      KongaTextArea var1 = new KongaTextArea(Size.rows(3), Size.columns(30));
      var1.setTabSize(4);
      ActionKeyBinding.install(new AbstractQueryArea.SelectFromAccountAction(), var1);
      TextBinding.bind(this.soqlProperty, var1);
      return var1;
   }

   private JButton createHelpButton() {
      KongaButton var1 = new KongaButton(new AbstractQueryArea.WhatIsThisAction());
      var1.setForeground(Color.BLUE);
      var1.setBorder(null);
      var1.setContentAreaFilled(false);
      ButtonUtils.enableRolloverForButton(var1);
      return var1;
   }

   public final Property<String> getSoqlProperty() {
      return this.soqlProperty;
   }

   public final void setIncludeQueryAllOption(boolean var1) {
      this.includeQueryAllOption = var1;
   }

   public final void setIncludeCaption(boolean var1) {
      this.includeCaption = var1;
   }

   protected void customize(KongaTextArea var1) {
   }

   protected final KongaTextArea getTextArea() {
      return this.textArea;
   }

   public final void setLabel(String var1) {
      this.textArea.setLabel(var1);
   }

   public final void setQueryString(String var1) {
      this.textArea.setText(var1);
      this.textArea.setCaretPosition(0);
   }

   public final String getQueryString() {
      String var1 = this.textArea.getText().trim();
      return var1.contains("...") ? null : var1;
   }

   public final void installAutoCompletion(CompletionContext var1) {
      AutoCompleterFactory var2 = AutoCompleterFactory.codeEditorFactory();
      var2.setGrabFocus(false);
      AutoCompleter var3 = var2.createAutoCompleter(var1);
      var3.install(this.textArea);
   }

   public final void addDocumentListener(DocumentListener var1) {
      this.textArea.getDocument().addDocumentListener(var1);
   }

   @Override
   public final void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.center(this.textArea.asUiProvider());
      if (this.includeCaption) {
         var2.south(this.buildCaptionPanel());
      }

      var2.decorate(var1);
   }

   private JComponent buildCaptionPanel() {
      KongaLabel var1 = KongaLabel.withMnemonic("Enter the salesforce &query string");
      var1.setLabelFor(this.textArea);
      BoxBuilder var2 = BoxBuilder.horizontal();
      var2.add(var1).add("  (").add(this.helpButton).add(")");
      if (this.includeQueryAllOption) {
         var2.glue().add(this.queryAllOption);
      }

      return var2.container();
   }

   @Override
   public final void requestFocus() {
      this.textArea.requestFocusInWindow();
   }

   public final boolean isQueryAll() {
      return this.includeQueryAllOption && this.queryAllOption.isSelected();
   }

   public final void installUndoRedo(UndoRedoInstaller var1) {
      var1.install(this.textArea);
   }

   private class SelectFromAccountAction extends AbstractAction {
      public SelectFromAccountAction() {
         this.putValue("AcceleratorKey", KeyUtils.getCtrlDown(48));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         AbstractQueryArea.this.textArea.setText("SELECT Id, Name FROM Account");
      }
   }

   private class WhatIsThisAction extends AbstractAction {
      public WhatIsThisAction() {
         super("&What Is This?", CommonIcons.HELP_16);
         this.putValue("AcceleratorKey", KeyUtils.F1);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               SoqlHelpDisplayer.open(AbstractQueryArea.this.textArea);
            }
         });
      }
   }
}
