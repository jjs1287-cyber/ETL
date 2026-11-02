package org.etlforge.ui.util.find;

import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.property.BooleanBinding;
import org.etlforge.ui.property.TextBinding;
import org.etlforge.ui.text.ConditionalTextEnabler;
import org.etlforge.ui.text.NonEmptyTextEnablerCondition;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.util.Property;

public final class TextSearchUi implements UiProvider, Disposable {
   private final TextSearchModel model;
   private final JTextField searchTextField;
   private final JTextField replaceField;
   private final JCheckBox caseSensitiveSelector;
   private final JCheckBox wholeWordsSelector;
   private final JCheckBox wrapSelector;
   private final SearchDirectionPanel directionPanel;
   private boolean showOptions;
   private final Bindings bindings;

   private TextSearchUi(TextSearchModel var1, boolean var2) {
      this.model = var1;
      this.searchTextField = createField();
      if (var2) {
         this.replaceField = createField();
      } else {
         this.replaceField = null;
      }

      this.caseSensitiveSelector = new KongaCheckBox(PackageResources.TextSearch.MATCH_CASE);
      this.wholeWordsSelector = new KongaCheckBox(PackageResources.TextSearch.WHOLE_WORDS);
      this.wrapSelector = new KongaCheckBox(PackageResources.TextSearch.WRAP);
      this.directionPanel = SearchDirectionPanel.newVerticalPanel();
      this.showOptions = true;
      this.bindings = this.installBindings();
      this.bindings.syncUi();
   }

   private static KongaTextField createField() {
      KongaTextField var0 = new KongaTextField();
      var0.setSelectAllWhenFocused(true);
      return var0;
   }

   private Bindings installBindings() {
      Bindings var1 = new Bindings();
      Property var2 = this.model.getProperty("searchString");
      var1.add(new TextBinding(var2, this.searchTextField));
      if (this.replaceField != null) {
         Property var3 = this.model.getProperty("replaceString");
         var1.add(new TextBinding(var3, this.replaceField));
      }

      Property var7 = this.model.getProperty("caseSensitive");
      var1.add(new BooleanBinding(var7, this.caseSensitiveSelector));
      Property var4 = this.model.getProperty("wholeWords");
      var1.add(new BooleanBinding(var4, this.wholeWordsSelector));
      Property var5 = this.model.getProperty("wrap");
      var1.add(new BooleanBinding(var5, this.wrapSelector));
      Property var6 = this.model.getProperty("direction");
      var1.add(new TextSearchUi.SearchDirectionBinding(var6, this.directionPanel));
      return var1;
   }

   public TextSearchModel getModel() {
      return this.model;
   }

   @Override
   public void dispose() {
      this.bindings.dispose();
   }

   public void setShowOptions(boolean var1) {
      this.showOptions = var1;
   }

   public static TextSearchUi searchOnly() {
      return searchOnly(new TextSearchModel());
   }

   public static TextSearchUi searchOnly(TextSearchModel var0) {
      return new TextSearchUi(var0, false);
   }

   public static TextSearchUi searchAndReplace() {
      return searchAndReplace(new TextSearchModel());
   }

   public static TextSearchUi searchAndReplace(TextSearchModel var0) {
      return new TextSearchUi(var0, true);
   }

   @Override
   public void decorate(UiCanvas var1) {
      TextSearchUi.Layout var2 = new TextSearchUi.Layout();
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.searchTextField.requestFocusInWindow();
   }

   public void setDefaultAction(Action var1) {
      this.searchTextField.addActionListener(var1);
      if (this.replaceField != null) {
         this.replaceField.addActionListener(var1);
      }
   }

   public void prepareTextFieldForDisplay() {
      int var1 = this.searchTextField.getDocument().getLength();
      if (var1 > 0) {
         this.searchTextField.setCaretPosition(0);
         this.searchTextField.moveCaretPosition(var1);
      }
   }

   public TextSearchParameters getDefinedSearchParameters() {
      return this.model.getSearchParameters();
   }

   public String getReplaceString() {
      return this.replaceField != null ? this.replaceField.getText() : null;
   }

   public void setSearchParameters(TextSearchParameters var1) {
      this.bindings.setUiToModelEnabled(false);
      if (var1 != null) {
         this.setSearchParametersImpl(var1);
      } else {
         this.reset();
      }

      this.bindings.setUiToModelEnabled(true);
      this.bindings.syncModel();
   }

   public void setReplaceString(String var1) {
      if (this.replaceField == null) {
         throw new UnsupportedOperationException("This TextSearchUi was created in search-only mode");
      }

      this.replaceField.setText(var1);
   }

   public void reset() {
      this.searchTextField.setText("");
      if (this.replaceField != null) {
         this.replaceField.setText("");
      }

      this.directionPanel.setSearchDirection(SearchDirection.DOWN);
      this.wholeWordsSelector.setSelected(false);
      this.caseSensitiveSelector.setSelected(false);
      this.wrapSelector.setSelected(false);
   }

   public void registerInputDependency(Component var1) {
      NonEmptyTextEnablerCondition var2 = new NonEmptyTextEnablerCondition();
      ConditionalTextEnabler var3 = ConditionalTextEnabler.install(this.searchTextField);
      var3.addDependency(var1, var2);
   }

   public void registerInputDependency(Action var1) {
      NonEmptyTextEnablerCondition var2 = new NonEmptyTextEnablerCondition();
      ConditionalTextEnabler var3 = ConditionalTextEnabler.install(this.searchTextField);
      var3.addDependency(var1, var2);
   }

   private void setSearchParametersImpl(TextSearchParameters var1) {
      this.searchTextField.setText(var1.getSearchString());
      if (this.replaceField != null) {
         this.replaceField.setText(var1.getReplaceString());
      }

      this.directionPanel.setSearchDirection(var1.getDirection());
      this.wholeWordsSelector.setSelected(var1.isWholeWordsOnly());
      this.caseSensitiveSelector.setSelected(var1.isCaseSensitive());
      this.wrapSelector.setSelected(var1.isWrap());
   }

   private class Layout {
      private final OneColumnPanel layout = new OneColumnPanel();

      public Layout() {
         this.layout.withInsets(0, 0, 5, 0);
         this.layout.setWeightX(1.0);
         this.layoutSearchFields();
         this.layoutControlRow();
      }

      public void decorate(UiCanvas var1) {
         BorderLayoutBuilder var2 = new BorderLayoutBuilder();
         var2.north(this.layout);
         var2.decorate(var1);
      }

      private void layoutSearchFields() {
         TwoColumnPanel var1 = new TwoColumnPanel()
            .withLeftInsets(0, 0, 5, 5)
            .withRightInsets(0, 0, 5, 0)
            .withRightFill(TwoColumnPanel.HORIZONTAL)
            .withRightWeights(1.0, 0.0);
         this.layoutField(var1, TextSearchUi.this.searchTextField, PackageResources.TextSearch.WHAT);
         if (TextSearchUi.this.replaceField != null) {
            this.layoutField(var1, TextSearchUi.this.replaceField, PackageResources.TextSearch.REPLACE);
         }

         this.layout.add(var1);
      }

      private void layoutField(TwoColumnPanel var1, JTextField var2, String var3) {
         KongaLabel var4 = KongaLabel.withMnemonic(var3);
         var4.setLabelFor(var2);
         var1.addRow(var4, var2);
      }

      private void layoutControlRow() {
         Grid var1 = Grid.oneRow(5);
         var1.add(TextSearchUi.this.directionPanel);
         if (TextSearchUi.this.showOptions) {
            this.layoutOptionsCheckBoxes(var1);
         }

         this.layout.add(var1);
      }

      private void layoutOptionsCheckBoxes(Grid var1) {
         TitledBorder var2 = new TitledBorder(PackageResources.TextSearch.OPTIONS);
         OneColumnPanel var3 = new OneColumnPanel()
            .withInsets(0, 0, 5, 0)
            .withBorder(var2)
            .setWeightX(1.0)
            .of(TextSearchUi.this.caseSensitiveSelector, TextSearchUi.this.wholeWordsSelector, TextSearchUi.this.wrapSelector);
         var1.add(var3);
      }
   }

   private static class SearchDirectionBinding extends AbstractBinding<SearchDirection> {
      private final SearchDirectionPanel ui;
      private final ActionListener uiListener;

      public SearchDirectionBinding(Property<SearchDirection> var1, SearchDirectionPanel var2) {
         super(var1);
         this.ui = var2;
         this.uiListener = this.defaultActionListener();
         var2.addActionListener(this.uiListener);
      }

      protected void updateUiWithValue(SearchDirection var1) {
         this.ui.setSearchDirection(var1);
      }

      protected SearchDirection getValueFromUi() {
         return this.ui.getSearchDirection();
      }

      @Override
      protected void disposeUiListener() {
         this.ui.removeActionListener(this.uiListener);
      }
   }
}
