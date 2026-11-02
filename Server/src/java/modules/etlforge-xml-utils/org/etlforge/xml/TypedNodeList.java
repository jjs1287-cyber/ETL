package org.etlforge.xml;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TypedNodeList<T extends Node> implements Iterable<T>, NodeList {
   private final List<T> nodes = Lists.newArrayList();

   public static <E extends Node> TypedNodeList<E> children(Node var0, Class<E> var1) {
      return new TypedNodeList<>(var0.getChildNodes(), var1);
   }

   public static <E extends Node> TypedNodeList<E> of(NodeList var0, Class<E> var1) {
      return new TypedNodeList<>(var0, var1);
   }

   public TypedNodeList(NodeList var1, Class<T> var2) {
      int var3 = 0;

      for (int var4 = var1.getLength(); var3 < var4; var3++) {
         Node var5 = var1.item(var3);
         if (var2.isInstance(var5)) {
            this.nodes.add((T)var2.cast(var5));
         }
      }
   }

   @Override
   public Iterator<T> iterator() {
      return this.nodes.iterator();
   }

   @Override
   public int getLength() {
      return this.nodes.size();
   }

   @Override
   public T item(int var1) {
      return this.nodes.get(var1);
   }
}
