package org.etlforge.util.kongaprotocol;

public enum PacketDirection {
   Request(0),
   Response(1);

   private final int id;

   PacketDirection(int var3) {
      this.id = var3;
   }

   public int getId() {
      return this.id;
   }
}
