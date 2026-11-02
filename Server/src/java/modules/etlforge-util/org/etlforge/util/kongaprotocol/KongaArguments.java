package org.etlforge.util.kongaprotocol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.etlforge.util.net.Arguments;

public class KongaArguments implements Arguments {
   private final List<String> arguments = new ArrayList<>();

   @Override
   public Iterator<String> iterator() {
      return this.arguments.iterator();
   }

   @Override
   public String get(int var1) {
      return this.arguments.get(var1);
   }

   @Override
   public void add(String var1) {
      this.arguments.add(var1);
   }

   @Override
   public int size() {
      return this.arguments.size();
   }

   @Override
   public String[] toArray() {
      return this.arguments.toArray(new String[this.arguments.size()]);
   }
}
