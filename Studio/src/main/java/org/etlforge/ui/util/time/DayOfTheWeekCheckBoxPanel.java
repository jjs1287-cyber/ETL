package org.etlforge.ui.util.time;

import com.google.common.base.Preconditions;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import org.etlforge.ui.UiResources;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.WidgetUtils;
import org.etlforge.util.time.DayOfTheWeek;

public final class DayOfTheWeekCheckBoxPanel extends JPanel implements ActionListener {
   public static final String FONT_RESOURCE_KEY = DayOfTheWeekCheckBoxPanel.class.getName() + "#font";
   public static final String SELECTION_CHANGED = "SelectionChanged";
   public static final int HORIZONTAL = 0;
   public static final int VERTICAL = 1;
   private final JCheckBox[] boxes = new JCheckBox[7];
   private int selected;
   private DayOfTheWeek[] oldSelection;
   private boolean blockEvents;

   public DayOfTheWeekCheckBoxPanel() {
      this(0, null);
   }

   public DayOfTheWeekCheckBoxPanel(int var1) {
      this(var1, null);
   }

   public DayOfTheWeekCheckBoxPanel(DayOfTheWeek[] var1) {
      this(0, var1);
   }

   public DayOfTheWeekCheckBoxPanel(int var1, DayOfTheWeek[] var2) {
      validateOrientation(var1);
      DayOfTheWeek[] var3 = var2 != null ? var2 : new DayOfTheWeek[0];
      this.createBoxes(var3);
      this.oldSelection = var3;
      this.selected = var3.length;
      this.layoutComponents(var1);
   }

   private static void validateOrientation(int var0) {
      Preconditions.checkArgument(var0 == 0 || var0 == 1, "orientation must be one of HORIZONTAL or VERTICAL");
   }

   private void createBoxes(DayOfTheWeek[] var1) {
      Font var2 = UiResources.getDefault().getFont(FONT_RESOURCE_KEY);

      for (DayOfTheWeek var6 : DayOfTheWeek.values()) {
         JCheckBox var7 = new JCheckBox(var6.getShortName());
         var7.setOpaque(false);
         if (var2 != null) {
            var7.setFont(var2);
         }

         for (DayOfTheWeek var11 : var1) {
            if (var6 == var11) {
               var7.setSelected(true);
               break;
            }
         }

         var7.addActionListener(this);
         this.boxes[var6.ordinal()] = var7;
      }
   }

   private void layoutComponents(int var1) {
      BoxBuilder var2 = var1 == 1 ? BoxBuilder.vertical(this) : BoxBuilder.horizontal(this);
      var2.addAllWithSpace(10, this.boxes);
   }

   public boolean isDaySelected(DayOfTheWeek var1) {
      return this.boxes[var1.ordinal()].isSelected();
   }

   public void clearSelection() {
      if (this.selected != 0) {
         this.blockEvents = true;

         for (JCheckBox var4 : this.boxes) {
            var4.setSelected(false);
         }

         this.selected = 0;
         this.fireSelectionChanged();
         this.blockEvents = false;
      }
   }

   public void select(DayOfTheWeek var1) {
      this.boxes[var1.ordinal()].setSelected(true);
   }

   public void setSelection(DayOfTheWeek[] var1) {
      this.blockEvents = true;

      for (JCheckBox var5 : this.boxes) {
         var5.setSelected(false);
      }

      this.selected = 0;
      if (var1 != null) {
         for (DayOfTheWeek var9 : var1) {
            this.boxes[var9.ordinal()].setSelected(true);
            this.selected++;
         }
      }

      this.fireSelectionChanged();
      this.blockEvents = false;
   }

   public DayOfTheWeek[] getSelectedDays() {
      DayOfTheWeek[] var1 = new DayOfTheWeek[this.selected];
      if (this.selected > 0) {
         int var2 = 0;

         for (DayOfTheWeek var6 : DayOfTheWeek.values()) {
            if (this.boxes[var6.ordinal()].isSelected()) {
               var1[var2++] = var6;
            }
         }
      }

      return var1;
   }

   @Override
   public void setEnabled(boolean var1) {
      super.setEnabled(var1);
      UiUtils.setEnabled(var1, this.boxes);
   }

   public void makeReadOnly() {
      WidgetUtils.makeReadOnly(this.boxes);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      if (((JCheckBox)var1.getSource()).isSelected()) {
         this.selected++;
      } else {
         this.selected--;
      }

      if (!this.blockEvents) {
         this.fireSelectionChanged();
      }
   }

   private void fireSelectionChanged() {
      DayOfTheWeek[] var1 = this.getSelectedDays();
      this.firePropertyChange("SelectionChanged", this.oldSelection, var1);
      this.oldSelection = var1;
   }
}
