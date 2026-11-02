package org.etlforge.ui.widget.text;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.text.JTextComponent;

public final class BeanStringPropertyBindings {
   private final ImmutableList<BeanStringPropertyBinding> bindings;

   public BeanStringPropertyBindings(Class<?> var1, Collection<String> var2, Collection<? extends JTextComponent> var3) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      Preconditions.checkNotNull(var3);
      Preconditions.checkArgument(var2.size() == var3.size(), "propertyNames and textComponents must contain the same number of elements.");
      Builder var4 = ImmutableList.builder();
      Iterator var5 = var2.iterator();
      Iterator var6 = var3.iterator();

      while (var5.hasNext()) {
         String var7 = (String)var5.next();
         JTextComponent var8 = (JTextComponent)var6.next();
         var4.add(new BeanStringPropertyBinding(var1, var7, var8));
      }

      this.bindings = var4.build();
   }

   public void updateUi(Object var1) {
      for (BeanStringPropertyBinding var3 : this.bindings) {
         var3.updateUi(var1);
      }
   }

   public void updateBean(Object var1) throws IllegalArgumentException {
      for (BeanStringPropertyBinding var3 : this.bindings) {
         var3.updateBean(var1);
      }
   }
}
