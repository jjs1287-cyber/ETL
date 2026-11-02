package org.etlforge.application.ui.window.editor;

import com.google.common.collect.Lists;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;

public class EditorThumbnailSelectorWindow extends AbstractEditorSelectorWindow {
   private EditorThumbnailSelector ui;
   private Collection<Editor> notifications;
   private final EditorThumbnailSelectorWindow.ThumbnailRemover thumbnailRemover = new EditorThumbnailSelectorWindow.ThumbnailRemover();

   public EditorThumbnailSelectorWindow(EditorService var1, List<Editor> var2, boolean var3) {
      super(var1, var2, var3);
      var1.addEditorServiceListener(this.thumbnailRemover);
      this.setBorder(Empty.border(3));
   }

   @Override
   protected UiProvider createUi(List<Editor> var1, boolean var2) {
      List var3 = this.createThumbnails(var1);
      int var4 = this.decideNumberOfColumns(var1.size());
      this.ui = new EditorThumbnailSelector(var3, var4, var2);
      this.ui.addSelectorListener(new AbstractEditorSelectorWindow.SelectionHandler());
      if (this.notifications != null) {
         this.ui.setNotifications(this.notifications);
      }

      return this.ui;
   }

   private List<EditorThumbnail> createThumbnails(List<Editor> var1) {
      Collections.reverse(var1);
      EditorThumbnailFactory var2 = new EditorThumbnailFactory(250, 30);
      ArrayList var3 = Lists.newArrayList();

      for (Editor var5 : var1) {
         EditorThumbnail var6 = var2.newThumbnail(var5);
         var3.add(var6);
      }

      return var3;
   }

   private int decideNumberOfColumns(int var1) {
      if (var1 <= 3) {
         return var1;
      } else {
         return var1 == 4 ? 2 : 3;
      }
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

   @Override
   public void setNotifications(Collection<Editor> var1) {
      if (this.ui != null) {
         this.ui.setNotifications(var1);
      } else {
         this.notifications = var1;
      }
   }

   @Override
   protected void onWindowClosed() {
      this.getService().removeEditorServiceListener(this.thumbnailRemover);
   }

   private class ThumbnailRemover extends EditorServiceAdapter {
      private ThumbnailRemover() {
      }

      @Override
      public void editorClosed(EditorServiceEvent var1) {
         if (EditorThumbnailSelectorWindow.this.ui != null) {
            EditorThumbnailSelectorWindow.this.ui.removeEditor(var1.getEditor());
            if (EditorThumbnailSelectorWindow.this.ui.isEmpty()) {
               EditorThumbnailSelectorWindow.this.close();
            }
         }
      }
   }
}
