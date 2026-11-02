package org.etlforge.integration.activity.ui.component.page;

import com.google.common.collect.Sets;
import java.util.WeakHashMap;
import javax.swing.Icon;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorDisplayer;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.ActivitySubjectType;
import org.etlforge.integration.activity.ui.ActivityUtils;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;

public abstract class ActivityPageManager {
   private final EditorDisplayer pageDisplayer;
   private final boolean isRecyclingPages;
   private final WeakHashMap<ActivityPage, Boolean> ownedPages;

   public ActivityPageManager(EditorDisplayer var1, boolean var2) {
      this.pageDisplayer = var1;
      this.isRecyclingPages = var2;
      if (var2) {
         this.ownedPages = new WeakHashMap<>();
      } else {
         this.ownedPages = null;
      }
   }

   public final ActivityPage display(ActivitySubject var1) {
      ActivityPage var2 = this.getPageToDisplay(var1);
      this.displayPage(var2);
      return var2;
   }

   private ActivityPage getPageToDisplay(ActivitySubject var1) {
      ActivityPage var2 = this.isRecyclingPages ? this.findOpenPage(var1) : null;
      if (var2 == null) {
         var2 = this.createNewPage(var1);
         var2.setSubjectIcon(getSubjectIcon(var1));
         if (this.ownedPages != null) {
            this.ownedPages.put(var2, Boolean.TRUE);
         }
      }

      return var2;
   }

   private ActivityPage findOpenPage(ActivitySubject var1) {
      this.resetOwnedPages();
      ActivityPage var2 = null;

      for (Editor var6 : this.pageDisplayer.getOpenEditors()) {
         if (this.ownedPages.containsKey(var6)) {
            this.ownedPages.put((ActivityPage)var6, Boolean.TRUE);
            if (var1.equals(var6.getObject())) {
               var2 = (ActivityPage)var6;
            }
         }
      }

      this.removeClosedPages();
      return var2;
   }

   protected abstract ActivityPage createNewPage(ActivitySubject var1);

   private static Icon getSubjectIcon(ActivitySubject var0) {
      ActivitySubjectType var1 = var0.getId().getType();
      if (var1 == ActivitySubjectType.SERVER) {
         return ClientIcons.SERVER_16;
      }

      EntityType var2 = ActivityUtils.toEntityType(var1);
      return var2 != null ? EntityIcons.forType(var2).getDefaultIcon(EntityIcons.Size.SMALL) : null;
   }

   private void displayPage(ActivityPage var1) {
      this.pageDisplayer.openEditor(var1);
      var1.retrieveRecords();
   }

   private void resetOwnedPages() {
      for (ActivityPage var2 : this.ownedPages.keySet()) {
         this.ownedPages.put(var2, Boolean.FALSE);
      }
   }

   private void removeClosedPages() {
      for (ActivityPage var3 : Sets.newHashSet(this.ownedPages.keySet())) {
         if (!this.ownedPages.get(var3)) {
            this.ownedPages.remove(var3);
         }
      }
   }
}
