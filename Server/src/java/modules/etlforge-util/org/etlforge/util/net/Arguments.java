package org.etlforge.util.net;

public interface Arguments extends Iterable<String> {
   void add(String var1);

   String get(int var1);

   int size();

   String[] toArray();
}
