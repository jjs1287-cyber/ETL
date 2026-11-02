package org.etlforge.application.ui.window.editor;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.swing.JPanel;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.window.WindowCornerPosition;

public final class QuickSwitchEditorWindow extends AbstractEditorSelectorWindow {
   private QuickSwitchEditorUi ui;
   private Collection<Editor> notifications;

   public static QuickSwitchEditorWindow open(ApplicationWindow var0) {
      return openAt(var0, (WindowCornerPosition)null);
   }

   public static QuickSwitchEditorWindow openAt(ApplicationWindow var0, WindowCornerPosition var1) {
      EditorService var2 = var0.getEditorService();
      if (var2.getNumberOfEditors() > 0) {
         QuickSwitchEditorWindow var3 = new QuickSwitchEditorWindow(var2, Arrays.asList(var2.getOpenEditors()));
         var3.setSelectedEditor(var2.getActiveEditor());
         var3.open(var0.getFrame(), var1);
         return var3;
      } else {
         return null;
      }
   }

   public QuickSwitchEditorWindow(EditorService var1, List<Editor> var2) {
      super(var1, var2, false);
   }

   @Override
   protected UiProvider createUi(List<Editor> var1, boolean var2) {
      this.ui = new QuickSwitchEditorUi(var1);
      this.ui.addSelectorListener(new AbstractEditorSelectorWindow.SelectionHandler());
      if (this.notifications != null) {
         this.ui.setNotifications(this.notifications);
      }

      return this.ui;
   }

   @Override
   public Dimension getSize() {
      JPanel var1 = BorderLayoutBuilder.fillWith(this.ui);
      return var1.getPreferredSize();
   }

   @Override
   public void setSelectedEditor(Editor var1) {
      this.ui.setSelectedEditor(var1);
   }

   public void setIncludeSearchField(boolean var1) {
      this.ui.setIncludeSearchField(var1);
   }

   @Override
   public void setNotifications(Collection<Editor> var1) {
      if (this.ui != null) {
         this.ui.setNotifications(var1);
      } else {
         this.notifications = var1;
      }
   }
}
