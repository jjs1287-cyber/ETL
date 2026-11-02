package org.etlforge.ui.event;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableColumnMetaData;

public class DoubleClick<T extends Component> {
   private final List<T> components = Lists.newArrayList();
   private Predicate<MouseEvent> condition = Predicates.alwaysTrue();

   private DoubleClick(T var1) {
      this.components.add((T)var1);
   }

   public static <T extends Component> DoubleClick<T> on(T var0) {
      return new DoubleClick<>(Preconditions.checkNotNull((T)var0));
   }

   public static DoubleClick.KongaTableDoubleClick on(KongaTable var0) {
      return new DoubleClick.KongaTableDoubleClick(Preconditions.checkNotNull(var0));
   }

   public DoubleClick or(T var1) {
      this.components.add(Preconditions.checkNotNull((T)var1));
      return this;
   }

   public DoubleClick when(Predicate<MouseEvent> var1) {
      this.condition = Preconditions.checkNotNull(var1);
      return this;
   }

   public final void toRun(final Runnable var1) {
      Preconditions.checkNotNull(var1);
      this.registerListener(new MouseActionListener() {
         @Override
         protected void onDoubleClick(MouseEvent var1x) {
            if (DoubleClick.this.condition.apply(var1x)) {
               var1.run();
            }
         }
      });
   }

   private void registerListener(MouseActionListener var1) {
      for (Component var3 : this.components) {
         var3.addMouseListener(var1);
      }
   }

   public final void toInvoke(final Action var1) {
      Preconditions.checkNotNull(var1);
      this.registerListener(new MouseActionListener() {
         @Override
         protected void onDoubleClick(MouseEvent var1x) {
            if (DoubleClick.this.condition.apply(var1x) && var1.isEnabled()) {
               ActionUtils.run(var1, var1x.getSource(), "actionPerformed");
            }
         }
      });
   }

   public final void toInvoke(final ActionListener var1) {
      Preconditions.checkNotNull(var1);
      this.registerListener(new MouseActionListener() {
         @Override
         protected void onDoubleClick(MouseEvent var1x) {
            if (DoubleClick.this.condition.apply(var1x)) {
               var1.actionPerformed(new ActionEvent(var1x.getSource(), 1001, "actionPerformed"));
            }
         }
      });
   }

   protected final Iterable<T> components() {
      return this.components;
   }

   public static final class KongaTableDoubleClick extends DoubleClick<KongaTable> {
      private KongaTableDoubleClick(KongaTable var1) {
         super(var1);
      }

      public DoubleClick.KongaTableDoubleClick or(KongaTable var1) {
         super.or(var1);
         return this;
      }

      public DoubleClick.KongaTableDoubleClick when(Predicate<MouseEvent> var1) {
         super.when(var1);
         return this;
      }

      public DoubleClick.KongaTableDoubleClick inColumn(TableColumnMetaData var1) {
         ArrayList var2 = Lists.newArrayList();

         for (KongaTable var4 : this.components()) {
            var2.add(Mouse.inTableColumn(var4, var1));
         }

         Predicate var5 = Predicates.or(var2);
         return this.when(var5);
      }
   }
}
