package org.etlforge.application.ui.widget;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.util.text.ThreadSafeDateFormat;

public class BasicTwoColumnLabelDisplayer implements UiProvider {
   private static final ThreadSafeDateFormat DATE_FORMAT = new ThreadSafeDateFormat(DateFormat.getDateTimeInstance());
   private final TwoColumnPanel layout;
   private TextStyle captionStyle = TextStyles.DefaultBold;
   private TextStyle valueStyle = TextStyles.DefaultText;
   private BasicTwoColumnLabelDisplayer.Mode valueMode;
   private int fieldWidth;

   public BasicTwoColumnLabelDisplayer() {
      this(BasicTwoColumnLabelDisplayer.Mode.LABEL);
   }

   public BasicTwoColumnLabelDisplayer(BasicTwoColumnLabelDisplayer.Mode var1) {
      Preconditions.checkNotNull(var1, "valueMode");
      this.valueMode = var1;
      this.layout = new TwoColumnPanel();
      this.layout.withLeftInsets(0, 0, 5, 10);
      this.fieldWidth = -1;
      this.setDefaultRightFillBehavior();
   }

   public void setCaptionStyle(TextStyle var1) {
      Preconditions.checkNotNull(var1, "style");
      this.captionStyle = var1;
   }

   public void setValueStyle(TextStyle var1) {
      Preconditions.checkNotNull(var1, "style");
      this.valueStyle = var1;
   }

   public void addRow(String var1, Object var2) {
      this.addRow(var1, var2 != null ? var2.toString() : "null");
   }

   public void addRow(String var1, String var2) {
      JComponent var3 = this.valueMode == BasicTwoColumnLabelDisplayer.Mode.LABEL ? this.makeValueLabel(var2) : this.makeValueTextField(var2);
      this.layout.addRow(this.makeCaptionLabel(var1), var3);
   }

   public void addRow(String var1, String var2, Icon var3) {
      JLabel var4 = this.makeValueLabel(var2);
      var4.setIcon(var3);
      this.addRow(var1, var4);
   }

   public void addRow(String var1, JLabel var2) {
      this.layout.addRow(this.makeCaptionLabel(var1), var2);
   }

   public void addRow(String var1, Date var2) {
      this.addRow(var1, var2 != null ? DATE_FORMAT.format(var2) : "?");
   }

   public void addRow(String var1, JComponent var2, boolean var3) {
      if (var3) {
         this.layout.withLeftAnchor(TwoColumnPanel.NORTHWEST);
         this.layout.withRightWeights(1.0, 1.0);
         this.layout.withRightFill(TwoColumnPanel.BOTH);
      }

      this.layout.addRow(this.makeCaptionLabel(var1), var2);
      if (var3) {
         this.setDefaultRightFillBehavior();
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.layout.decorate(var1);
   }

   @Override
   public void requestFocus() {
   }

   public JLabel makeCaptionLabel(String var1) {
      return this.captionStyle.makeLabel(var1);
   }

   public JLabel makeValueLabel(String var1) {
      KongaLabel var2 = this.valueStyle.makeLabel(var1);
      var2.setToolTipText(var1);
      return var2;
   }

   public JTextField makeValueTextField(String var1) {
      KongaTextField var2 = new KongaTextField(var1);
      if (this.fieldWidth >= 0) {
         var2.setColumns(this.fieldWidth);
      }

      var2.setEditable(false);
      var2.setToolTipText(var1);
      var2.setCaretPosition(0);
      var2.setBackground(javax.swing.UIManager.getColor("Panel.background"));
      return var2;
   }

   public void setFieldWidth(int var1) {
      this.fieldWidth = var1;
   }

   private void setDefaultRightFillBehavior() {
      this.layout.withRightFill(TwoColumnPanel.HORIZONTAL);
      this.layout.withRightWeights(1.0, 0.0);
   }

   public enum Mode {
      LABEL,
      TEXTFIELD;
   }
}

