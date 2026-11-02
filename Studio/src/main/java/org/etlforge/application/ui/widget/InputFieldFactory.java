package org.etlforge.application.ui.widget;

import com.google.common.base.Preconditions;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.DefaultNonEmptyTextInputField;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.input.IntegerInputField;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.text.IntegerDocument;
import org.etlforge.ui.widget.text.KongaTextArea;
import org.etlforge.ui.widget.text.KongaTextField;

public final class InputFieldFactory {
   public static final int HORIZONTAL_LABEL_LEFT = 0;
   public static final int HORIZONTAL_LABEL_RIGHT = 1;
   public static final int VERTICAL = 2;
   private static final Insets DEFAULT_GBC_LABEL_INSETS_HORIZ = new Insets(0, 4, 2, 4);
   private static final Insets DEFAULT_GBC_LABEL_INSETS_VERTICAL = new Insets(0, 0, 0, 2);

   private InputFieldFactory() {
   }

   public static TextInputField getDescriptionField() {
      String var0 = ApplicationResources.STRINGS.getString("Description");
      JLabel var1 = new JLabel(var0);
      KongaTextArea var2 = new KongaTextArea(4, 30).disableTabs();
      return new TextInputField(var0, var2, var1);
   }

   public static TextInputField2 getNonEmptyTextInputField(String var0, String var1, int var2) {
      return getNonEmptyTextInputField(var0, var1, var2, true);
   }

   public static TextInputField2 getNonEmptyTextInputField(String var0, JLabel var1, int var2) {
      return getNonEmptyTextInputField(var0, var1, var2, true);
   }

   public static TextInputField2 getNonEmptyTextInputField(String var0, String var1, int var2, boolean var3) {
      JLabel var4 = var1 != null && var1.length() > 0 ? new JLabel(var1) : null;
      return getNonEmptyTextInputField(var0, var4, var2, var3);
   }

   public static TextInputField2 getNonEmptyTextInputField(String var0, JLabel var1, int var2, boolean var3) {
      return new DefaultNonEmptyTextInputField(var0, getTextField(var2, var3), var1);
   }

   public static TextInputField2 getNonEmptyTextAreaInputField(String var0, String var1, int var2, int var3) {
      JLabel var4 = var1 != null && var1.length() > 0 ? new JLabel(var1) : null;
      KongaTextArea var5 = (new KongaTextArea(var2, var3) {
         @Override
         public Dimension getMaximumSize() {
            return this.getPreferredSize();
         }
      }).disableTabs();
      return new DefaultNonEmptyTextInputField(var0, var5, var4);
   }

   public static TextInputField2 getTextInputField2(String var0, String var1, int var2, boolean var3) {
      JLabel var4 = var1 != null && var1.length() > 0 ? new JLabel(var1) : null;
      return getTextInputField2(var0, var4, var2, var3);
   }

   public static TextInputField2 getTextInputField2(String var0, JLabel var1, int var2, boolean var3) {
      return new TextInputField2(var0, getTextField(var2, var3), var1) {
         @Override
         public boolean isInputValid(String var1) {
            return true;
         }
      };
   }

   public static TextInputField getTextInputField(String var0, String var1, int var2, boolean var3) {
      JLabel var4 = var1 != null && var1.length() > 0 ? new JLabel(var1) : null;
      return getTextInputField(var0, var4, var2, var3);
   }

   public static TextInputField getTextInputField(String var0, JLabel var1, int var2, boolean var3) {
      return new TextInputField(var0, getTextField(var2, var3), var1);
   }

   public static TextInputField getTextInputArea(String var0, String var1, int var2, int var3) {
      JLabel var4 = var1 != null && var1.length() > 0 ? new JLabel(var1) : null;
      return new TextInputField(var0, new KongaTextArea(var2, var3).disableTabs(), var4);
   }

   public static IntegerInputField getIntegerInputField(String var0, String var1, int var2, int var3, int var4) {
      return getIntegerInputField(var0, var1, var2, var3, var4, true);
   }

   public static IntegerInputField getIntegerInputField(String var0, String var1, int var2, int var3, int var4, boolean var5) {
      JLabel var6 = var1 != null && var1.length() > 0 ? new JLabel(var1) : null;
      return getIntegerInputField(var0, var6, var2, var3, var4, var5);
   }

   public static IntegerInputField getIntegerInputField(String var0, JLabel var1, int var2, int var3, int var4, boolean var5) {
      KongaTextField var6 = getTextField(var4, var5);
      var6.setDocument(new IntegerDocument());
      return new IntegerInputField(var0, var6, var2, var3, var1);
   }

   public static ComboBoxInputField getComboBoxInputField(String var0, JComboBox var1, JLabel var2) {
      return new ComboBoxInputField(var0, var1, var2);
   }

   public static CheckBoxInputField getCheckBoxInputField(String var0, String var1) {
      return getCheckBoxInputField(var0, var1, false);
   }

   public static CheckBoxInputField getCheckBoxInputField(String var0, String var1, boolean var2) {
      KongaCheckBox var3 = new KongaCheckBox(var1, var2);
      return new CheckBoxInputField(var0, var3);
   }

   public static JComponent getCapsule(InputField var0) {
      return getCapsule(var0, 2, false);
   }

   public static JComponent getCapsule(InputField var0, int var1) {
      return getCapsule(var0, var1, false);
   }

   public static JComponent getCapsule(InputField var0, boolean var1) {
      return getCapsule(var0, 2, var1);
   }

   public static JComponent getCapsule(InputField var0, int var1, boolean var2) {
      return new InputFieldFactory.InputFieldCapsule(var0, var1, var2);
   }

   public static KongaTextField getTextField(int var0, boolean var1) {
      KongaTextField var2 = new KongaTextField(var0);
      var2.setFixedSize(var1);
      return var2;
   }

   public static JComponent getLabeledComponent(JComponent var0, String var1) {
      return getLabeledComponent(var0, var1, 2);
   }

   public static JComponent getLabeledComponent(JComponent var0, JLabel var1) {
      return getLabeledComponent(var0, var1, 2);
   }

   public static JComponent getLabeledComponent(JComponent var0, String var1, int var2) {
      return getLabeledComponent(var0, var1, var2, TextStyles.InputLabelText);
   }

   public static JComponent getLabeledComponent(JComponent var0, String var1, int var2, TextStyle var3) {
      return getLabeledComponent(var0, var3.makeLabel(var1), var2);
   }

   public static JComponent getLabeledComponent(JComponent var0, JLabel var1, int var2) {
      Preconditions.checkNotNull(var0, "comp");
      return new InputFieldFactory.LabeledComponentCapsule(var0, var1, var2);
   }

   private static GridBagConstraints getInputFieldLabelConstraintsHorizontal(int var0, int var1) {
      GridBagConstraints var2 = new GridBagConstraints();
      var2.gridx = var0;
      var2.gridy = var1;
      var2.anchor = 17;
      var2.insets = DEFAULT_GBC_LABEL_INSETS_HORIZ;
      return var2;
   }

   private static GridBagConstraints getInputFieldLabelConstraintsVertical(int var0, int var1) {
      GridBagConstraints var2 = new GridBagConstraints();
      var2.gridx = var0;
      var2.gridy = var1;
      var2.anchor = 17;
      var2.insets = DEFAULT_GBC_LABEL_INSETS_VERTICAL;
      return var2;
   }

   private static final class InputFieldCapsule extends JPanel {
      public InputFieldCapsule(InputField var1, int var2, boolean var3) {
         Preconditions.checkNotNull(var1, "input");
         Preconditions.checkArgument(var2 == 2 || var2 == 0 || var2 == 1, "invalid orientation");
         this.setOpaque(false);
         this.setLayout(new GridBagLayout());
         GridBagConstraints var4 = new GridBagConstraints();
         var4.anchor = 17;
         var4.fill = 2;
         var4.weightx = 1.0;
         if (var1.getLabel() != null) {
            int[] var5 = this.getLabelXY(var2);
            int[] var6 = this.getComponentXY(var2);
            GridBagConstraints var7 = var2 == 2
               ? InputFieldFactory.getInputFieldLabelConstraintsVertical(var5[0], var5[1])
               : InputFieldFactory.getInputFieldLabelConstraintsHorizontal(var5[0], var5[1]);
            this.add(var1.getLabel(), var7);
            var4.gridx = var6[0];
            var4.gridy = var6[1];
            if (var3) {
               var4.weighty = 1.0;
               var4.fill = 1;
               this.add(new KongaScrollPane(var1.getInputComponent()), var4);
            } else {
               this.add(var1.getInputComponent(), var4);
            }
         } else {
            this.add(var1.getInputComponent(), var4);
         }
      }

      private int[] getLabelXY(int var1) {
         switch (var1) {
            case 0:
            case 2:
               return new int[]{0, 0};
            case 1:
               return new int[]{1, 0};
            default:
               return null;
         }
      }

      private int[] getComponentXY(int var1) {
         switch (var1) {
            case 0:
               return new int[]{1, 0};
            case 1:
               return new int[]{0, 0};
            case 2:
               return new int[]{0, 1};
            default:
               return null;
         }
      }

      @Override
      public Dimension getMinimumSize() {
         return this.getPreferredSize();
      }
   }

   private static final class LabeledComponentCapsule extends JPanel {
      public LabeledComponentCapsule(JComponent var1, JLabel var2, int var3) {
         super(new GridBagLayout());
         Preconditions.checkNotNull(var1, "component");
         Preconditions.checkArgument(var3 == 2 || var3 == 0 || var3 == 1, "invalid orientation");
         this.setOpaque(false);
         GridBagConstraints var4 = new GridBagConstraints();
         var4.anchor = 17;
         if (var2 != null) {
            int[] var5 = this.getLabelXY(var3);
            int[] var6 = this.getComponentXY(var3);
            GridBagConstraints var7 = var3 == 2
               ? InputFieldFactory.getInputFieldLabelConstraintsVertical(var5[0], var5[1])
               : InputFieldFactory.getInputFieldLabelConstraintsHorizontal(var5[0], var5[1]);
            this.add(var2, var7);
            var4.gridx = var6[0];
            var4.gridy = var6[1];
            this.add(var1, var4);
         } else {
            this.add(var1, var4);
         }
      }

      private int[] getLabelXY(int var1) {
         switch (var1) {
            case 0:
            case 2:
               return new int[]{0, 0};
            case 1:
               return new int[]{1, 0};
            default:
               return null;
         }
      }

      private int[] getComponentXY(int var1) {
         switch (var1) {
            case 0:
               return new int[]{1, 0};
            case 1:
               return new int[]{0, 0};
            case 2:
               return new int[]{0, 1};
            default:
               return null;
         }
      }

      @Override
      public Dimension getMinimumSize() {
         return this.getPreferredSize();
      }

      @Override
      public Dimension getMaximumSize() {
         return this.getPreferredSize();
      }
   }
}
