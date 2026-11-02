package org.etlforge.application.ui.window.editor.service;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorCategory;
import org.etlforge.application.ui.window.editor.EditorService;

final class CategoryControlModel {
   static final EditorCategory OTHER = new EditorCategory("Other", null);
   private final ApplicationWindow appWin;
   private final EditorService editorService;
   private final Multimap<EditorCategory, Editor> editors;
   private final List<CategoryControlModelListener> listeners;
   private final PropertyChangeListener editorListener;
   private boolean longNameMode;
   private final Set<Editor> notifications;
   private int activeIndex;

   public CategoryControlModel(ApplicationWindow var1, EditorService var2) {
      this.appWin = var1;
      this.editorService = var2;
      this.editors = LinkedHashMultimap.create();
      this.notifications = Sets.newHashSet();
      this.editorListener = new CategoryControlModel.EditorListener();
      this.listeners = new CopyOnWriteArrayList<>();
      this.longNameMode = true;
   }

   public List<EditorCategory> getCategories() {
      return Lists.newArrayList(this.editors.keySet());
   }

   public void setActiveIndex(int var1) {
      this.activeIndex = var1;
   }

   public EditorCategory getActiveCategory() {
      if (this.activeIndex >= 0 && this.activeIndex < this.getNumberOfCategories()) {
         List var1 = this.getCategories();
         return (EditorCategory)var1.get(this.activeIndex);
      } else {
         return null;
      }
   }

   public int getNumberOfCategories() {
      return this.editors.keySet().size();
   }

   public EditorCategory getCategoryAt(int var1) {
      return this.getCategories().get(var1);
   }

   public List<Editor> getEditors(EditorCategory var1) {
      return Lists.newArrayList(this.editors.get(var1));
   }

   public int getNumberOfEditorsInCategory(EditorCategory var1) {
      Collection var2 = this.editors.get(var1);
      return var2 == null ? 0 : var2.size();
   }

   public void setLongNameMode(boolean var1) {
      this.longNameMode = var1;
   }

   public String getCategoryDisplayName(EditorCategory var1) {
      String var2 = "";
      if (var1.icon == null || this.longNameMode) {
         var2 = var2 + var1.name + " ";
      }

      int var3 = this.editors.get(var1).size();
      if (var3 > 1) {
         var2 = var2 + "(" + var3 + ")";
      }

      if (this.isCategoryDirty(var1)) {
         var2 = var2 + " *";
      }

      return var2.trim();
   }

   private boolean isCategoryDirty(EditorCategory var1) {
      for (Editor var3 : this.editors.get(var1)) {
         if (var3.isDirty()) {
            return true;
         }
      }

      return false;
   }

   public ApplicationWindow getAppWin() {
      return this.appWin;
   }

   public EditorService getEditorService() {
      return this.editorService;
   }

   public void openEditor(Editor var1) {
      EditorCategory var2 = this.categorize(var1);
      boolean var3 = !this.contains(var2);
      this.editors.put(var2, var1);
      var1.addPropertyChangeListener("dirty", this.editorListener);
      if (var3) {
         for (CategoryControlModelListener var5 : this.listeners) {
            var5.categoryOpened(var2);
         }
      }

      for (CategoryControlModelListener var7 : this.listeners) {
         var7.editorOpened(var2, var1);
      }
   }

   public EditorCategory categorize(Editor var1) {
      EditorCategory var2 = var1.getCategory();
      if (var2 == null) {
         var2 = OTHER;
      }

      return var2;
   }

   private boolean contains(EditorCategory var1) {
      return !this.editors.get(var1).isEmpty();
   }

   public void closeEditor(Editor var1) {
      EditorCategory var2 = this.categorize(var1);
      if (this.contains(var2)) {
         this.editors.remove(var2, var1);
         var1.removePropertyChangeListener("dirty", this.editorListener);

         for (CategoryControlModelListener var4 : this.listeners) {
            var4.editorClosed(var2, var1);
         }

         if (!this.contains(var2)) {
            for (CategoryControlModelListener var6 : this.listeners) {
               var6.categoryClosed(var2);
            }
         }
      }
   }

   public void closeCategory(EditorCategory var1) {
      if (this.contains(var1)) {
         List var2 = this.getEditors(var1);
         this.editorService.closeEditors(var2);
      }
   }

   public void addNotification(Editor var1) {
      this.notifications.add(var1);
   }

   public void removeNotification(Editor var1) {
      this.notifications.remove(var1);
   }

   public Set<Editor> getNotifications() {
      return Sets.newHashSet(this.notifications);
   }

   public void addListener(CategoryControlModelListener var1) {
      this.listeners.add(var1);
   }

   public void removeListener(CategoryControlModelListener var1) {
      this.listeners.remove(var1);
   }

   private class EditorListener implements PropertyChangeListener {
      private EditorListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         assert "dirty".equals(var1.getPropertyName());
         Editor var2 = (Editor)var1.getSource();
         EditorCategory var3 = CategoryControlModel.this.categorize(var2);

         for (CategoryControlModelListener var5 : CategoryControlModel.this.listeners) {
            var5.editorPropertyChanged(var3, var2);
         }
      }
   }
}
