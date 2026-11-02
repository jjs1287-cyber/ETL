package org.etlforge.ui.widget.tree;

import com.google.common.base.Function;
import java.util.List;
import org.etlforge.collections.KongaListUtils;

public final class NodeFunctions {
   public static <F extends KongaTreeNode> Function<F, Object> toUserObject() {
      return new NodeFunctions.ToUserObjectImpl<>(Object.class);
   }

   public static <F extends KongaTreeNode, T> Function<F, T> toUserObject(Class<? extends T> var0) {
      return new NodeFunctions.ToUserObjectImpl<>(var0);
   }

   public static <F extends KongaTreeNode, T> List<T> toUserObjects(List<F> var0, Class<? extends T> var1) {
      return KongaListUtils.transform(var0, toUserObject(var1));
   }

   private NodeFunctions() {
   }

   private static final class ToUserObjectImpl<F extends KongaTreeNode, T> implements Function<F, T> {
      private final Class<? extends T> clazz;

      public ToUserObjectImpl(Class<? extends T> var1) {
         this.clazz = var1;
      }

      public T apply(F var1) {
         return (T)this.clazz.cast(var1.getUserObject());
      }
   }
}
