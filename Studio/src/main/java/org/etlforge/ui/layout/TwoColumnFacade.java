package org.etlforge.ui.layout;

import com.google.common.base.Preconditions;
import java.awt.Component;
import java.util.Arrays;
import javax.swing.JPanel;

public final class TwoColumnFacade implements TwoColumnLayout {
   private final NColumnPanel grid;
   private final Component[] row;
   private int currentColumn;

   public TwoColumnFacade(NColumnPanel var1) {
      Preconditions.checkArgument(var1.getColumnCount() % 2 == 0, "Expected an even number of columns, got " + var1.getColumnCount());
      this.grid = var1;
      this.row = new Component[var1.getColumnCount()];
   }

   public NColumnPanel getFourColumnGrid() {
      return this.grid;
   }

   @Override
   public TwoColumnLayout addRow(Component var1) {
      this.row[this.currentColumn] = var1;
      this.newComponentsAdded();
      return this;
   }

   private void newComponentsAdded() {
      this.currentColumn += 2;
      if (this.currentColumn == this.grid.getColumnCount()) {
         this.layoutRow();
      }
   }

   private void layoutRow() {
      for (byte var1 = 0; var1 < this.grid.getColumnCount(); var1 += 2) {
         this.layoutGroup(var1);
      }

      this.currentColumn = 0;
      Arrays.fill(this.row, null);
   }

   private void layoutGroup(int var1) {
      if (this.row[var1 + 1] == null) {
         this.grid.add(this.row[var1], 2);
      } else {
         this.grid.add(this.row[var1]);
         this.grid.add(this.row[var1 + 1]);
      }
   }

   @Override
   public TwoColumnLayout addRow(Component var1, Component var2) {
      this.row[this.currentColumn] = var1;
      this.row[this.currentColumn + 1] = var2;
      this.newComponentsAdded();
      return this;
   }

   public void addLastRow() {
      if (this.currentColumn > 0) {
         assert this.currentColumn < this.grid.getColumnCount();
         this.layoutRow();
      }
   }

   public JPanel container() {
      return this.grid.container();
   }
}
