package org.etlforge.integration.client.ui.connector.mscrm.query.wizard;

import com.google.common.base.Optional;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.ComboBoxBinding;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.combobox.KongaComboBox;
import org.etlforge.ui.widget.list.NameableRenderer;
import org.etlforge.ui.widget.tabbedpane.KongaTabbedPane;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

final class QuerySelectionUi implements UiProvider {
   private final QuerySelectionModel model;
   private final KongaComboBox querySelector;
   private final TextQueryEditor textEditor;
   private final TreeQueryEditor treeEditor;
   private QueryEditor currentEditor;
   private final KongaTabbedPane editorTabPane = new KongaTabbedPane();
   private Optional<JComponent> downloadButton = Optional.absent();

   public QuerySelectionUi(QuerySelectionModel var1) {
      this.model = var1;
      this.querySelector = this.createQuerySelector();
      this.textEditor = new TextQueryEditor(var1);
      this.treeEditor = new TreeQueryEditor(var1);
      this.editorTabPane.addTab("Text Editor", wrapEditor(this.textEditor));
      this.editorTabPane.addTab("Tree Editor", wrapEditor(this.treeEditor));
      this.editorTabPane.setSelectedIndex(0);
      this.editorTabPane.addChangeListener(new QuerySelectionUi.CurrentEditorListener());
      this.currentEditor = this.textEditor;
   }

   private KongaComboBox createQuerySelector() {
      KongaComboBox var1 = new KongaComboBox();
      var1.setRenderer(new NameableRenderer());
      Property var2 = this.model.getProperty("queries");
      ComboBoxBinding var3 = new ComboBoxBinding<>(var1, var2, this.model.getSelectedQueryProperty());
      var3.setSelectionPolicyOnContentChange(ComboBoxBinding.SelectionPolicy.NULL_OUT);
      var3.syncUi();
      QuerySelectionUi.SelectorNullMessageBinding var4 = new QuerySelectionUi.SelectorNullMessageBinding(this.model, "state", var3);
      var4.syncUi();
      return var1;
   }

   private static JComponent wrapEditor(QueryEditor var0) {
      return new BorderLayoutBuilder().withEmptyBorder(12).center(var0).nonOpaque().container();
   }

   public void setDownloadAction(Action var1) {
      KongaButton var2 = new KongaButton(var1);
      WizardStyleSheet.CAPTION_STYLE.makeOver(var2);
      this.downloadButton = Optional.of(var2);
   }

   public String getDefinedQueryXml() {
      return this.currentEditor.getFetchXml();
   }

   public boolean canWizardForward() {
      return this.currentEditor.canDoForwardNavigation();
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = BoxBuilder.horizontal().addAllWithSpace(10, this.querySelector, this.downloadButton.orNull());
      JComponent var3 = new BorderLayoutBuilder().withEmptyBorder(12).center(this.editorTabPane).nonOpaque().container();
      new BorderLayoutBuilder(0, 10).north(var2).center(var3).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.downloadButton.or(this.querySelector).requestFocusInWindow();
   }

   private class CurrentEditorListener implements ChangeListener {
      private CurrentEditorListener() {
      }

      @Override
      public void stateChanged(ChangeEvent var1) {
         int var2 = QuerySelectionUi.this.editorTabPane.getSelectedIndex();
         QuerySelectionUi.this.currentEditor = this.getEditor(var2);
      }

      private QueryEditor getEditor(int var1) {
         switch (var1) {
            case 0:
               return QuerySelectionUi.this.textEditor;
            case 1:
               return QuerySelectionUi.this.treeEditor;
            default:
               throw new RuntimeException("Unexpected tab index: " + var1);
         }
      }
   }

   private static class SelectorNullMessageBinding extends AbstractBinding<QuerySelectionModel.State> {
      private final ComboBoxBinding target;

      public SelectorNullMessageBinding(AbstractPropertyBasedModel var1, String var2, ComboBoxBinding var3) {
         super(var1, var2);
         this.target = var3;
      }

      protected void updateUiWithValue(QuerySelectionModel.State var1) {
         String var2 = this.getNullMessage(var1);
         this.target.setNullSelectionMessage(var2);
      }

      private String getNullMessage(QuerySelectionModel.State var1) {
         switch (var1) {
            case UNKNOWN:
               return "Click Download to see a list of saved queries";
            case HAS_RESULT:
               return "No saved queries are available";
            default:
               throw new RuntimeException();
         }
      }
   }
}
