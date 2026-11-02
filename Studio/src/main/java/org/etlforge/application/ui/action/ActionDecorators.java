package org.etlforge.application.ui.action;

import javax.swing.Action;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.action.ActionDecorator;
import org.etlforge.ui.util.KeyUtils;

public final class ActionDecorators {
   public static final ActionDecorator SAVE = new ActionDecorator() {
      @Override
      public void decorate(Action var1) {
         var1.putValue("Name", ApplicationResources.STRINGS.getString("Save"));
         var1.putValue("SmallIcon", ApplicationResources.getIcon("Save.16"));
         var1.putValue("KongaMediumIconKey", ApplicationResources.getIcon("Save.24"));
         var1.putValue("AcceleratorKey", KeyUtils.SAVE);
      }
   };
   public static final ActionDecorator REFRESH = new ActionDecorator() {
      @Override
      public void decorate(Action var1) {
         var1.putValue("Name", ApplicationResources.STRINGS.getString("Refresh"));
         var1.putValue("SmallIcon", CommonIcons.REFRESH_16);
         var1.putValue("KongaMediumIconKey", CommonIcons.REFRESH_24);
         var1.putValue("AcceleratorKey", KeyUtils.REFRESH);
      }
   };
   public static final ActionDecorator DELETE = new ActionDecorator() {
      @Override
      public void decorate(Action var1) {
         var1.putValue("Name", ApplicationResources.STRINGS.getString("Delete"));
         var1.putValue("SmallIcon", CommonIcons.DELETE_16);
         var1.putValue("KongaMediumIconKey", CommonIcons.DELETE_24);
         var1.putValue("AcceleratorKey", KeyUtils.DELETE);
      }
   };
   public static final ActionDecorator REMOVE = new ActionDecorator() {
      @Override
      public void decorate(Action var1) {
         var1.putValue("Name", ApplicationResources.STRINGS.getString("Remove"));
         var1.putValue("SmallIcon", CommonIcons.DELETE_16);
         var1.putValue("KongaMediumIconKey", CommonIcons.DELETE_24);
      }
   };
   public static final ActionDecorator EDIT = new ActionDecorator() {
      @Override
      public void decorate(Action var1) {
         var1.putValue("Name", ApplicationResources.STRINGS.getString("Edit"));
      }
   };
   public static final ActionDecorator VIEW_DETAILS = new ActionDecorator() {
      @Override
      public void decorate(Action var1) {
         var1.putValue("Name", ApplicationResources.STRINGS.getString("ViewDetails"));
         var1.putValue("SmallIcon", CommonIcons.VIEW_DETAILS_16);
         var1.putValue("KongaMediumIconKey", ApplicationResources.getIcon("ViewDetails.24"));
      }
   };
   public static final ActionDecorator FIND = new ActionDecorator() {
      @Override
      public void decorate(Action var1) {
         var1.putValue("Name", ApplicationResources.STRINGS.getString("Find"));
         var1.putValue("SmallIcon", CommonIcons.FIND_16);
         var1.putValue("KongaMediumIconKey", ApplicationResources.ICONS.getIcon("Find.24"));
      }
   };
   public static final ActionDecorator UNDO = new ActionDecorator() {
      @Override
      public void decorate(Action var1) {
         var1.putValue("Name", ApplicationResources.STRINGS.getString("Undo"));
         var1.putValue("SmallIcon", ApplicationResources.ICONS.getIcon("Undo.16"));
         var1.putValue("AcceleratorKey", KeyUtils.UNDO);
      }
   };
   public static final ActionDecorator REDO = new ActionDecorator() {
      @Override
      public void decorate(Action var1) {
         var1.putValue("Name", ApplicationResources.STRINGS.getString("Redo"));
         var1.putValue("SmallIcon", ApplicationResources.ICONS.getIcon("Redo.16"));
         var1.putValue("AcceleratorKey", KeyUtils.REDO);
      }
   };
   public static final ActionDecorator LOGIN = new ActionDecorator() {
      @Override
      public void decorate(Action var1) {
         MnemonicString var2 = ApplicationResources.getMnemonicString("Login.Action");
         ActionDecorators.decorateWithNameAndMnemonic(var1, var2);
         var1.putValue("SmallIcon", ApplicationResources.ICONS.getIcon("Login.16"));
      }
   };
   public static final ActionDecorator CLOSE = new ActionDecorator() {
      @Override
      public void decorate(Action var1) {
         var1.putValue("Name", ApplicationResources.STRINGS.getString("Close"));
         var1.putValue("SmallIcon", CommonIcons.CLOSE_16);
      }
   };

   public static void decorateFromDictionary(Action var0, ActionDictionary var1, String var2) {
      MnemonicString var3 = var1.getName(var2);
      if (var3 != null) {
         var0.putValue("Name", var3.getRaw());
      }

      var0.putValue("ShortDescription", var1.getToolTip(var2));
      var0.putValue("SmallIcon", var1.getSmallIcon(var2));
      var0.putValue("KongaMediumIconKey", var1.getMediumIcon(var2));
      var0.putValue("SwingLargeIconKey", var1.getLargeIcon(var2));
      var0.putValue("AcceleratorKey", var1.getAccelerator(var2));
   }

   public static void decorateWithNameAndMnemonic(Action var0, String var1) {
      ActionDecorators.NameAndMnemonicDecorator var2 = new ActionDecorators.NameAndMnemonicDecorator(var1);
      var2.decorate(var0);
   }

   public static void decorateWithNameAndMnemonic(Action var0, MnemonicString var1) {
      ActionDecorators.NameAndMnemonicDecorator var2 = new ActionDecorators.NameAndMnemonicDecorator(var1);
      var2.decorate(var0);
   }

   private ActionDecorators() {
   }

   private static final class NameAndMnemonicDecorator implements ActionDecorator {
      private final MnemonicString name;

      public NameAndMnemonicDecorator(String var1) {
         this.name = ApplicationResources.MNEMONIC_FACTORY.fromString(var1);
      }

      public NameAndMnemonicDecorator(MnemonicString var1) {
         this.name = var1;
      }

      @Override
      public void decorate(Action var1) {
         var1.putValue("Name", this.name.getText());
         char var2 = this.name.getMnemonic();
         if (var2 != 0) {
            var1.putValue("MnemonicKey", Integer.valueOf(var2));
         }

         int var3 = this.name.getDisplayedMnemonicIndex();
         if (var3 >= 0) {
            var1.putValue("SwingDisplayedMnemonicIndexKey", var3);
         }
      }
   }
}
