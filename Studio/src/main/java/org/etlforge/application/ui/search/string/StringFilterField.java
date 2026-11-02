package org.etlforge.application.ui.search.string;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.string.StringMatcher;
import org.etlforge.string.StringMatcherPredicate;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.DelayedDocumentContentChangeReaction;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.label.UiProviderLabel;
import org.etlforge.ui.widget.menu.KongaCheckBoxMenuItem;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.util.preference.KongaPreferences;

public final class StringFilterField implements UiProvider {
   private static final String INDEX_PREF_KEY = "RuleIndex";
   private final KongaTextField field;
   private JLabel label;
   private final List<StringSearchRule> options = Lists.newArrayList();
   private final JButton optionsButton;
   private final JLabel iconLabel;
   private StringSearchRule selectedOption;
   private boolean trim;
   private boolean emptyStringMatchesAll;
   private StringFilterField.LabelLayout labelLayout;
   private Receiver<Predicate<String>> callback;
   private DelayedDocumentContentChangeReaction textChangeHandler;
   private int textChangeDelay;
   private KongaPreferences prefs;
   private JComponent ui;
   private Border border;

   public StringFilterField() {
      this(StringSearchRule.CONTAINS_IGNORE_CASE);
   }

   public StringFilterField(Collection<StringSearchRule> var1) {
      this(var1.toArray(new StringSearchRule[var1.size()]));
   }

   public StringFilterField(StringSearchRule... var1) {
      for (StringSearchRule var5 : var1) {
         this.options.add(var5);
      }

      this.field = new KongaTextField(10);
      if (var1.length > 1) {
         this.optionsButton = this.installOptionsButton();
         this.iconLabel = null;
      } else {
         this.iconLabel = this.installFilterIcon();
         this.optionsButton = null;
      }

      this.emptyStringMatchesAll = true;
      this.labelLayout = StringFilterField.LabelLayout.HORIZONTAL;
      this.textChangeDelay = 250;
      this.field.setSelectAllWhenFocused(true);
      this.setCallback(this.callback);
      this.setSelectedOption(var1[0]);
   }

   private JButton installOptionsButton() {
      JButton var1 = this.createButton();
      this.installBorder(var1);
      this.installOptionButtonShortcut();
      return var1;
   }

   private JButton createButton() {
      Icon var1 = ApplicationResources.getIcon("Filter.Options.16");
      final KongaButton var2 = new KongaButton(var1);
      var2.addActionListener(new StringFilterField.ButtonListener());
      var2.setContentAreaFilled(false);
      var2.setFocusPainted(false);
      final Border var3 = Empty.border(2, 2, 3, 3);
      final BorderBuilder var4 = new BorderBuilder().etched().empty(0, 0, 1, 1);
      var2.setBorder(var3);
      var2.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseEntered(MouseEvent var1) {
            var2.setBorder(var4);
         }

         @Override
         public void mouseExited(MouseEvent var1) {
            var2.setBorder(var3);
         }
      });
      return var2;
   }

   private void installBorder(JComponent var1) {
      StringFilterField.OptionsButtonBorder var2 = new StringFilterField.OptionsButtonBorder(this.field, var1);
      BorderBuilder var3 = new BorderBuilder(this.field.getBorder(), var2);
      this.field.setBorder(var3);
   }

   private void installOptionButtonShortcut() {
      ActionKeyBinding.install(this.field, KeyUtils.CONTENT_ASSIST, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            StringFilterField.this.optionsButton.doClick();
         }
      });
   }

   private JLabel installFilterIcon() {
      JLabel var1 = new JLabel(CommonIcons.FILTER_16);
      var1.setBorder(Empty.border(2, 1, 0, 0));
      this.installBorder(var1);
      return var1;
   }

   public void setSelectedOption(StringSearchRule var1) {
      Preconditions.checkNotNull(var1, "option");
      Preconditions.checkArgument(this.options.contains(var1), "Unknown option: " + var1.getName());
      this.selectedOption = var1;
      if (this.optionsButton != null) {
         String var2 = UiResourceBundle.MNEMONIC_FACTORY.asText(var1.getName());
         this.optionsButton.setToolTipText(var2);
      }

      this.rememberSelectedOption(var1);
      this.notifyCallback();
   }

   private void rememberSelectedOption(StringSearchRule var1) {
      if (this.prefs != null) {
         int var2 = this.options.indexOf(var1);
         this.prefs.putInt("RuleIndex", var2);
      }
   }

   public StringSearchRule getSelectedOption() {
      return this.selectedOption;
   }

   public Predicate<String> getFilter() {
      String var1 = this.getFilterString();
      if (var1.isEmpty() && this.emptyStringMatchesAll) {
         return Predicates.alwaysTrue();
      }

      StringMatcher var2 = this.selectedOption.getMatcher();
      return new StringMatcherPredicate(var2, var1);
   }

   public String getFilterString() {
      String var1 = this.field.getText();
      if (this.trim) {
         var1 = var1.trim();
      }

      return var1;
   }

   public void clear() {
      this.field.setText("");
   }

   public void setPreferences(KongaPreferences var1) {
      this.prefs = var1;
      if (var1 != null) {
         StringSearchRule var2 = this.getPreferredOption();
         this.setSelectedOption(var2);
      }
   }

   private StringSearchRule getPreferredOption() {
      int var1 = this.prefs.getInt("RuleIndex", 0);
      return var1 >= 0 && var1 < this.options.size() ? this.options.get(var1) : this.options.get(0);
   }

   public void setEnabled(boolean var1) {
      UiUtils.setEnabled(var1, this.field, this.label, this.optionsButton, this.iconLabel);
   }

   @Override
   public void decorate(UiCanvas var1) {
      JComponent var2 = this.getLayoutContent();
      this.ui = new BorderLayoutBuilder().north(var2).withBorder(this.border).container();
      var1.display(this.ui);
   }

   private JComponent getLayoutContent() {
      if (this.label == null) {
         return this.field;
      } else {
         return this.labelLayout == StringFilterField.LabelLayout.HORIZONTAL ? this.layoutHorizontally() : this.layoutVertically();
      }
   }

   private JComponent layoutHorizontally() {
      BoxBuilder var1 = BoxBuilder.horizontal();
      if (this.label != null) {
         var1.add(this.label).rigid(5);
      }

      var1.add(this.field);
      return var1.container();
   }

   private JComponent layoutVertically() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.setWeightX(1.0);
      var1.setFill(GridBagLayoutPanel.Fill.HORIZONTAL);
      var1.withInsets(0, 0, 2, 0);
      var1.add(this.label);
      var1.withInsets(0, 0, 0, 0);
      var1.add(this.field);
      return var1.container();
   }

   @Override
   public void requestFocus() {
      this.field.requestFocusInWindow();
   }

   public void setTrim(boolean var1) {
      this.trim = var1;
   }

   public void setEmptyStringMatchesAll(boolean var1) {
      this.emptyStringMatchesAll = var1;
   }

   public void setSelectAllWhenFocused(boolean var1) {
      this.field.setSelectAllWhenFocused(var1);
   }

   public void setTextWhenEmpty(String var1) {
      this.field.setTextWhenEmpty(var1);
   }

   public void setLabel(String var1) {
      KongaLabel var2;
      if (StringUtils.isEmpty(var1)) {
         var2 = null;
      } else {
         var2 = KongaLabel.withMnemonic(var1);
      }

      this.setLabel(var2);
   }

   public void setLabel(JLabel var1) {
      this.label = var1;
      if (var1 != null) {
         new UiProviderLabel(this, var1);
      }
   }

   public void setLabelLayout(StringFilterField.LabelLayout var1) {
      Preconditions.checkNotNull(var1, "labelLayout");
      this.labelLayout = var1;
   }

   public void setColumns(int var1) {
      this.field.setColumns(var1);
   }

   public void setTextChangeDelay(int var1) {
      Preconditions.checkArgument(var1 >= 0, "textChangeDelay must be >= 0 (was " + var1 + ")");
      this.textChangeDelay = var1;
   }

   public void setCallback(Receiver<Predicate<String>> var1) {
      if (var1 == null) {
         this.callback = NoReceiver.create();
         if (this.textChangeHandler != null) {
            this.textChangeHandler.uninstall(this.field);
            this.textChangeHandler = null;
         }
      } else {
         this.callback = var1;
         if (this.textChangeHandler == null) {
            this.installTextChangeHandler();
         }

         this.notifyCallback();
      }
   }

   private void installTextChangeHandler() {
      this.textChangeHandler = new DelayedDocumentContentChangeReaction(this.textChangeDelay, new Runnable() {
         @Override
         public void run() {
            StringFilterField.this.notifyCallback();
         }
      });
      this.textChangeHandler.install(this.field);
   }

   private void notifyCallback() {
      Predicate var1 = this.getFilter();
      this.callback.handle(var1);
   }

   public void addActionListener(ActionListener var1) {
      this.field.addActionListener(var1);
   }

   public void removeActionListener(ActionListener var1) {
      this.field.removeActionListener(var1);
   }

   public void addFocusListener(FocusListener var1) {
      this.field.addFocusListener(var1);
   }

   public void removeFocusListener(FocusListener var1) {
      this.field.removeFocusListener(var1);
   }

   public void addKeyBinding(Action var1) {
      ActionKeyBinding.install(var1, this.field);
   }

   public void setBorder(Border var1) {
      this.border = var1;
      if (this.ui != null) {
         this.ui.setBorder(var1);
      }
   }

   private class ButtonListener implements ActionListener {
      private ButtonListener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         JPopupMenu var2 = this.createPopup();
         JComponent var3 = (JComponent)var1.getSource();
         var2.show(var3, 8, 8);
      }

      private JPopupMenu createPopup() {
         JPopupMenu var1 = new JPopupMenu();

         for (StringSearchRule var3 : StringFilterField.this.options) {
            StringFilterField.OptionAction var4 = StringFilterField.this.new OptionAction(var3);
            KongaCheckBoxMenuItem var5 = new KongaCheckBoxMenuItem(var4);
            var1.add(var5);
         }

         return var1;
      }
   }

   public enum LabelLayout {
      HORIZONTAL,
      VERTICAL;
   }

   private class OptionAction extends AbstractAction {
      private final StringSearchRule option;

      public OptionAction(StringSearchRule var2) {
         super(var2.getName());
         this.option = var2;
         boolean var3 = var2 == StringFilterField.this.selectedOption;
         this.putValue("SwingSelectedKey", var3);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         final int var2 = StringFilterField.this.field.getSelectionStart();
         final int var3 = StringFilterField.this.field.getSelectionEnd();
         StringFilterField.this.setSelectedOption(this.option);
         StringFilterField.this.field.requestFocusInWindow();
         UiUtils.nestedInvokeLater(2, new Runnable() {
            @Override
            public void run() {
               StringFilterField.this.field.setCaretPosition(var2);
               StringFilterField.this.field.moveCaretPosition(var3);
            }
         });
      }
   }

   private static class OptionsButtonBorder implements Border {
      private final JComponent button;
      private final Insets insets;
      private int deltaX;

      public OptionsButtonBorder(KongaTextField var1, JComponent var2) {
         this.button = var2;
         var2.setSize(var2.getPreferredSize());
         this.insets = this.createInsets(var1);
         var1.add(var2);
      }

      private Insets createInsets(KongaTextField var1) {
         Insets var2 = new Insets(0, this.button.getPreferredSize().width + 2, 0, 0);
         Border var3 = var1.getBorder();
         if (var3 != null) {
            Insets var4 = var3.getBorderInsets(var1);
            byte var5 = 5;
            if (var4.top < var5) {
               var2.top = var5 - var4.top;
            }

            if (var4.left < var5) {
               this.deltaX = var5 - var4.left;
               var2.left = var2.left + this.deltaX;
            }

            if (var4.right < var5) {
               var2.right = var5 - var4.right;
            }

            if (var4.bottom < var5) {
               var2.bottom = var5 - var4.bottom;
            }
         }

         return var2;
      }

      @Override
      public void paintBorder(Component var1, Graphics var2, int var3, int var4, int var5, int var6) {
         this.button.setLocation(var3 + this.deltaX - 1, var4 + this.insets.top - 2);
      }

      @Override
      public Insets getBorderInsets(Component var1) {
         return this.insets;
      }

      @Override
      public boolean isBorderOpaque() {
         return false;
      }
   }
}
