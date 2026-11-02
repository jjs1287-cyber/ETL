package org.etlforge.xml;

import java.util.Iterator;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeListIterator implements Iterator<Node>, Iterable<Node> {
   private final NodeList nodes;
   private int index;

   public static NodeListIterator children(Node var0) {
      return new NodeListIterator(var0.getChildNodes());
   }

   public NodeListIterator(NodeList var1) {
      this.nodes = var1;
   }

   @Override
   public Iterator<Node> iterator() {
      return this;
   }

   @Override
   public boolean hasNext() {
      return this.index < this.nodes.getLength();
   }

   public Node next() {
      return this.nodes.item(this.index++);
   }

   @Override
   public void remove() {
      throw new UnsupportedOperationException();
   }
}
