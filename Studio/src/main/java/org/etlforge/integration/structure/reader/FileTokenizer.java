package org.etlforge.integration.structure.reader;

import java.io.IOException;
import org.etlforge.util.csv.FileContents;
import org.etlforge.util.csv.ParseQuoteFlag;

public class FileTokenizer {
   private final FileContents m_fc;
   private final String m_terminators;
   private long m_k;
   private long m_k_saved;
   private final long m_end_pos;

   FileTokenizer(FileContents var1, String var2) {
      this.m_fc = var1;
      this.m_terminators = var2;
      this.m_k = 0L;
      this.m_k_saved = 0L;
      this.m_end_pos = var1.size();
   }

   FileTokenizer(FileContents var1, long var2, long var4, String var6) {
      this.m_fc = var1;
      this.m_terminators = var6;
      this.m_k = var2;
      this.m_k_saved = var2;
      this.m_end_pos = var4;
   }

   public boolean hasMoreTokens() {
      return this.m_k < this.m_end_pos;
   }

   int count_line(String var1) {
      int var2 = 0;

      for (int var4 = 0; (var4 = var1.indexOf(10, var4)) >= 0; var4++) {
         var2++;
      }

      return var2;
   }

   int count_line(long var1, long var3) throws IOException {
      int var5 = 0;

      for (long var6 = var1; var6 < var3; var6++) {
         if (this.m_fc.at(var6) == 10) {
            var5++;
         }
      }

      return var5;
   }

   public String nextToken() {
      if (this.m_k >= this.m_end_pos) {
         return null;
      }

      int var1 = this.m_fc.findNextTokenLength(this.m_k, this.m_end_pos, this.m_terminators);
      if (var1 == 0) {
         var1 = 1;
      }

      String var2 = this.m_fc.getStr(this.m_k, var1);
      this.m_k_saved = this.m_k;
      this.m_k += var1;
      return var2;
   }

   public long getTokenBeginPos() {
      return this.m_k_saved;
   }

   public long getTokenEndPos() {
      return this.m_k;
   }

   public void setPos(long var1) {
      this.m_k = var1;
      this.m_k_saved = -1L;
   }

   public String peek_next_token() throws IOException {
      if (!this.hasMoreTokens()) {
         return null;
      }

      String var1 = this.nextToken();
      this.m_k = this.m_k_saved;
      return var1;
   }

   public boolean stepBack() {
      if (this.m_k_saved < 0L) {
         return false;
      }

      this.m_k = this.m_k_saved;
      this.m_k_saved = -1L;
      return true;
   }

   public void skip(char var1, SkipPosition var2, ParseQuoteFlag var3) throws IOException {
      if (this.m_k < this.m_end_pos) {
         int var4 = this.m_fc.findNextTokenLength(this.m_k, this.m_end_pos, var1, var3);
         this.m_k_saved = this.m_k;
         if (var2 == SkipPosition.To) {
            if (var4 == 0) {
               boolean var5 = true;
               this.m_k++;
            } else {
               this.m_k += var4;
               if (this.m_k < this.m_end_pos && this.m_fc.at(this.m_k) == var1) {
                  this.m_k++;
               }
            }
         } else {
            this.m_k += var4;
         }
      }
   }

   public String getNextTokenSpanToEndOfCsvString() throws IOException {
      if (this.m_k >= this.m_end_pos) {
         return null;
      }

      this.m_k_saved = this.m_k;
      int var1 = this.m_fc.findNextTokenLength(this.m_k, this.m_end_pos, '"', ParseQuoteFlag.FALSE);

      for (this.m_k += var1; var1 != 0 || this.m_fc.at(this.m_k + 1L) == 34; this.m_k += var1) {
         if (var1 == 0) {
            this.m_k += 2L;
         }

         var1 = this.m_fc.findNextTokenLength(this.m_k, this.m_end_pos, '"', ParseQuoteFlag.FALSE);
      }

      this.m_k++;
      return this.m_fc.getStr(this.m_k_saved, (int)(this.m_k - this.m_k_saved));
   }

   public String getLocation() throws IOException {
      return this.m_fc.getLocation(this.m_k).toString();
   }
}
