package org.etlforge.integration.structure.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.util.csv.FileContents;

public class FixFieldParser extends TextParser {
   private final int id_pos;
   private final int id_len;

   public FixFieldParser(FileContents var1, int var2, int var3, boolean var4) {
      super(var1, '\u0000', '\u0000', "", var4);
      this.id_pos = var2;
      this.id_len = var3;
   }

   @Override
   public String getIdentifier() throws IOException {
      if (this.isFlat) {
         return "";
      }

      String record = this.fc.getStr(this.pos0, (int)(this.pos2 - this.pos0));
      String var1 = slice(record, this.id_pos, this.id_len);
      return var1.trim();
   }

   @Override
   public List<String> splitValues(JbNode var1, boolean var2) throws IOException {
      CROM var3 = var1.getCrom();
      long var4 = var1.getPos();
      int var6 = var1.getLength();
      if (var3 != null && var6 != 0) {
         ArrayList var7 = new ArrayList();
         Iterator var8 = var3.getChildren().iterator();
         long var9 = var4;
         String record = this.fc.getStr(var9, var6);

         for (CROM var13 = CROM.getNextValueAttribute(var8); var13 != null; var13 = CROM.getNextValueAttribute(var8)) {

            int start = var13.getFieldPos() - 1;
            String var16 = start < record.codePointCount(0, record.length()) ? slice(record, start, var13.getFieldLength()) : "NULL";
            if (var2) {
               var16 = var13 + "=" + var16;
            }

            var7.add(var16);
         }

         return var7;
      } else {
         return null;
      }
   }
   private static String slice(String value, int start, int length) {
      int count = value.codePointCount(0, value.length());
      if (start < 0 || length <= 0 || start >= count) return "";
      int begin = value.offsetByCodePoints(0, start);
      int end = value.offsetByCodePoints(begin, Math.min(length, count - start));
      return value.substring(begin, end);
   }}
