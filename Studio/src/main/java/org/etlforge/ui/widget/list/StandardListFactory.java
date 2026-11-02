package org.etlforge.ui.widget.list;

import java.util.Collection;
import javax.swing.DropMode;
import javax.swing.ListModel;
import org.etlforge.ui.widget.ComponentCustomizer;
import org.etlforge.ui.widget.WidgetFactoryHelper;

public class StandardListFactory implements ListFactory {
   private final WidgetFactoryHelper<KongaListBase> helper;

   public StandardListFactory(ComponentCustomizer<KongaListBase> var1) {
      this.helper = new WidgetFactoryHelper<>(null, var1);
   }

   public KongaListBase newList() {
      KongaListBase var1 = new KongaListBase();
      return this.customizeList(var1);
   }

   public KongaListBase newList(ListModel var1) {
      KongaListBase var2 = new KongaListBase(var1);
      return this.customizeList(var2);
   }

   public KongaListBase newList(Object[] var1) {
      KongaListBase var2 = new KongaListBase(var1);
      return this.customizeList(var2);
   }

   public KongaListBase newList(Collection<?> var1) {
      KongaListBase var2 = new KongaListBase(var1.toArray());
      return this.customizeList(var2);
   }

   private KongaListBase customizeList(KongaListBase var1) {
      var1.setDropMode(DropMode.INSERT);
      return this.helper.customize(var1);
   }
}
