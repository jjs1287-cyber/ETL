package org.etlforge.application.ui.search.string;

import com.google.common.base.Preconditions;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import org.etlforge.application.ui.resources.ResourceLoader;
import org.etlforge.application.ui.undo.UndoRedoControllers;
import org.etlforge.lang.Arrays2;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.util.Orientation;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.option.ExclusiveOptionsPanel;
import org.etlforge.ui.util.option.Option;
import org.etlforge.ui.util.option.SelectedOptionListener;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.label.LabelUtils;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextField;

public final class StringSearchModelDefiner implements UiProvider {
   private final DefaultStringSearchModel model;
   private final StringSearchModelDefiner.SearchStringField searchField;
   private final ExclusiveOptionsPanel<SearchRuleOption> ruleSelector;

   public StringSearchModelDefiner() {
      this(StringSearchRule.STARTS_WITH);
   }

   public StringSearchModelDefiner(StringSearchRule... var1) {
      Preconditions.checkArgument(var1.length > 0, "rules may not be empty");
      this.model = new DefaultStringSearchModel();
      this.searchField = new StringSearchModelDefiner.SearchStringField(this.model);
      if (var1.length == 1) {
         this.model.setSearchRule(var1[0]);
         this.ruleSelector = null;
      } else {
         this.ruleSelector = this.createRuleSelector(var1);
      }

      this.updateTextFieldState();
   }

   public StringSearchModel getSearchModel() {
      return this.model;
   }

   public boolean isEmpty() {
      return this.model.isEmpty();
   }

   public void setSearchString(String var1) {
      this.searchField.setText(var1);
   }

   public void setSelectedRule(StringSearchRule var1) {
      if (this.ruleSelector != null) {
         SearchRuleOption var2 = new SearchRuleOption(var1);
         this.ruleSelector.setSelectedOption(var2);
      }
   }

   public void setEmptyStringMatchesEverything(boolean var1) {
      this.model.setEmptyStringMatchesEverything(var1);
   }

   public void setCaption(String var1) {
      String var2 = var1 != null ? var1 : "";
      MnemonicString var3 = ResourceLoader.MNEMONIC_FACTORY.fromString(var2);
      this.searchField.setCaption(var3);
   }

   public void setTextWhenEmpty(String var1) {
      this.searchField.setTextWhenEmpty(var1);
   }

   public void setExternalLabel(JLabel var1) {
      this.searchField.setExternalLabel(var1);
   }

   public void addActionListener(ActionListener var1) {
      this.searchField.addActionListener(var1);
   }

   public void removeActionListener(ActionListener var1) {
      this.searchField.removeActionListener(var1);
   }

   public void setToolTip(String var1) {
      this.searchField.setToolTip(var1);
   }

   public void setEnabled(boolean var1) {
      this.searchField.setEnabled(var1);
      this.ruleSelector.setEnabled(var1);
   }

   public void setFieldWidth(Size.Columns var1) {
      this.searchField.setWidth(var1);
   }

   public void layoutInGrid(TwoColumnLayout var1) {
      this.searchField.layoutIn(var1);
      if (this.ruleSelector != null) {
         var1.addRow(null, InvisiblePanel.of(this.ruleSelector));
      }
   }

   public void installKeyBinding(Action var1) {
      this.searchField.installKeyBinding(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      OneColumnPanel var2 = new OneColumnPanel();
      var2.setWeightX(1.0);
      var2.withInsets(2, 2, 2, 2);
      this.searchField.layoutIn(var2);
      if (this.ruleSelector != null) {
         var2.add(this.ruleSelector);
      }

      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.searchField.giveFocusTo();
   }

   private ExclusiveOptionsPanel<SearchRuleOption> createRuleSelector(StringSearchRule[] var1) {
      SearchRuleOption[] var2 = Arrays2.transform(var1, SearchRuleOption.class, SearchRuleOption.CONVERTER);
      ExclusiveOptionsPanel var3 = new ExclusiveOptionsPanel<>(var2, null, Orientation.HORIZONTAL);
      var3.addSelectionListener(new SelectedOptionListener() {
         @Override
         public void selectionChanged(Option[] var1) {
            StringSearchRule var2x = (StringSearchRule)var1[0];
            StringSearchModelDefiner.this.model.setSearchRule(var2x);
            StringSearchModelDefiner.this.updateTextFieldState();
         }
      });
      return var3;
   }

   private void updateTextFieldState() {
      this.searchField.setEnabled(this.model.getSearchRule() != null);
   }

   private static final class SearchStringField {
      private final DefaultStringSearchModel model;
      public final KongaTextField field;
      private final JLabel caption;

      public SearchStringField(DefaultStringSearchModel var1) {
         this.model = var1;
         this.caption = this.createCaption();
         this.field = this.createField();
         this.listenToField();
      }

      private JLabel createCaption() {
         KongaLabel var1 = new KongaLabel(Strings.mnemonic("StringSearch.Field.Caption"));
         var1.setAlignmentX(0.0F);
         return var1;
      }

      private KongaTextField createField() {
         KongaTextField var1 = new KongaTextField(20);
         var1.setAlignmentX(0.0F);
         this.caption.setLabelFor(var1);
         var1.setSelectAllWhenFocused(true);
         UndoRedoControllers.textFactory().newController(var1);
         return var1;
      }

      private void listenToField() {
         this.field.getDocument().addDocumentListener(new DocumentContentChangeListener() {
            @Override
            public void contentChanged(DocumentEvent var1) {
               SearchStringField.this.model.setSearchString(SearchStringField.this.field.getText());
            }
         });
      }

      public void addActionListener(ActionListener var1) {
         this.field.addActionListener(var1);
      }

      public void removeActionListener(ActionListener var1) {
         this.field.removeActionListener(var1);
      }

      public void setEnabled(boolean var1) {
         this.field.setEnabled(var1);
      }

      public void setWidth(Size.Columns var1) {
         this.field.setColumns(var1.get());
      }

      public void setExternalLabel(JLabel var1) {
         var1.setLabelFor(this.field);
      }

      public void setTextWhenEmpty(String var1) {
         this.field.setTextWhenEmpty(var1);
      }

      public void layoutIn(TwoColumnLayout var1) {
         var1.addRow(this.caption, this.field);
      }

      public void layoutIn(OneColumnPanel var1) {
         var1.add(this.caption);
         var1.add(this.field);
      }

      public void setText(String var1) {
         this.field.setText(var1);
      }

      public void setCaption(final MnemonicString var1) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               LabelUtils.applyMnemonics(SearchStringField.this.caption, var1);
            }
         });
      }

      public void setToolTip(final String var1) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               SearchStringField.this.caption.setToolTipText(var1);
               SearchStringField.this.field.setToolTipText(var1);
            }
         });
      }

      public void giveFocusTo() {
         this.field.requestFocusInWindow();
      }

      public void installKeyBinding(Action var1) {
         ActionKeyBinding.install(var1, this.field);
      }
   }
}
