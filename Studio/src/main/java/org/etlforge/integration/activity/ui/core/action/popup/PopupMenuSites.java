package org.etlforge.integration.activity.ui.core.action.popup;

import org.etlforge.application.ui.action.popup.PopupMenuSite;

public final class PopupMenuSites {
   public static final PopupMenuSite TREE = new PopupMenuSite() {
      @Override
      public boolean isTree() {
         return true;
      }
   };
   public static final PopupMenuSite PAGE = new PopupMenuSite() {
      @Override
      public boolean isTree() {
         return false;
      }
   };
   public static final PopupMenuSite OTHER = new PopupMenuSite() {
      @Override
      public boolean isTree() {
         return false;
      }
   };

   private PopupMenuSites() {
   }
}
