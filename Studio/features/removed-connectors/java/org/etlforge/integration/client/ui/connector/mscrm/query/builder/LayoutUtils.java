package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.property.MoveListPropertyItemAction;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.util.ListProperty;

final class LayoutUtils {
   public static KongaListFactory newListFactory() {
      KongaListFactory var0 = ComponentFactories.newKongaListFactory();
      var0.setVisibleRowCount(3);
      var0.setPrototypeValue("XXXXXXXXXXXXXX");
      return var0;
   }

   public static UiProvider layoutListWithButtons(KongaList var0, Object var1, Object var2, Object var3) {
      BoxBuilder var4 = Horizontal.box().addAllWithSpace(10, var1, var2).rigid(20).glue().add(var3);
      return new BorderLayoutBuilder(0, 10).center(var0).south(var4);
   }

   public static UiProvider layoutListWithButtons(KongaList var0, ListProperty<?> var1, Object var2, Object var3, Object var4) {
      MoveListPropertyItemAction var5 = MoveListPropertyItemAction.up(var1);
      var5.setSelectionSource(var0);
      MoveListPropertyItemAction var6 = MoveListPropertyItemAction.down(var1);
      var6.setSelectionSource(var0);
      BoxBuilder var7 = Horizontal.box()
         .addAllWithSpace(5, var2, var3, var4)
         .rigid(10)
         .glue()
         .addAllWithSpace(5, KongaButton.forToolbar(var6), KongaButton.forToolbar(var5));
      return new BorderLayoutBuilder(0, 2).center(var0).south(var7);
   }

   private LayoutUtils() {
   }
}
