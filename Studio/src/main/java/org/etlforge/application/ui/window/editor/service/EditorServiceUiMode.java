package org.etlforge.application.ui.window.editor.service;

import java.util.EnumSet;

public enum EditorServiceUiMode {
   MULTI_TABBED {
      @Override
      public String toString() {
         return "Multi-Tabs";
      }

      @Override
      public EditorServiceUi createUi(DefaultEditorService var1) {
         return MultiTabEditorServiceUi2.SUPPORTED ? new MultiTabEditorServiceUi2(var1) : new MultiTabEditorServiceUi(var1);
      }

      @Override
      public boolean isSupported() {
         return true;
      }
   },
   TABBED {
      @Override
      public EditorServiceUi createUi(DefaultEditorService var1) {
         return new TabbedEditorServiceUi(var1);
      }

      @Override
      public String toString() {
         return "Tabs";
      }
   },
   SINGLE_CARD {
      @Override
      public String toString() {
         return "Single card";
      }

      @Override
      public EditorServiceUi createUi(DefaultEditorService var1) {
         return new CardEditorServiceUi(var1);
      }
   },
   FLOATING {
      @Override
      public EditorServiceUi createUi(DefaultEditorService var1) {
         return new MDIEditorServiceUi(var1);
      }

      @Override
      public String toString() {
         return "Floating";
      }
   };

   EditorServiceUiMode() {
   }

   public boolean isSupported() {
      return true;
   }

   public abstract EditorServiceUi createUi(DefaultEditorService var1);

   public static Iterable<EditorServiceUiMode> getSupportedModes() {
      EnumSet var0 = EnumSet.noneOf(EditorServiceUiMode.class);

      for (EditorServiceUiMode var4 : values()) {
         if (var4.isSupported()) {
            var0.add(var4);
         }
      }

      return var0;
   }
}
