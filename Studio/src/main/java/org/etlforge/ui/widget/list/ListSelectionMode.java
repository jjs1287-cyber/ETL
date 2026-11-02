package org.etlforge.ui.widget.list;

import javax.swing.JList;

public enum ListSelectionMode {
   NONE {
      @Override
      public void applyTo(JList var1) {
         var1.setSelectionModel(new NoSelectionModel());
      }
   },
   SINGLE_SELECTION {
      @Override
      public void applyTo(JList var1) {
         var1.setSelectionMode(0);
      }
   },
   SINGLE_INTERVAL_SELECTION {
      @Override
      public void applyTo(JList var1) {
         var1.setSelectionMode(1);
      }
   },
   MULTIPLE_INTERVAL_SELECTION {
      @Override
      public void applyTo(JList var1) {
         var1.setSelectionMode(2);
      }
   };

   ListSelectionMode() {
   }

   public abstract void applyTo(JList var1);
}
