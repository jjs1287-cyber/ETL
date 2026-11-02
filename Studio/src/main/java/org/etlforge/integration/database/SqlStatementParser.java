package org.etlforge.integration.database;

import java.util.ArrayList;
import java.util.List;
import org.etlforge.util.string.KongaStringTokenizer;

public class SqlStatementParser {
   private String m_Sql;
   private static final int t_select = 1;
   private static final int t_from = 2;
   private static final int t_where = 3;
   private static final int t_order = 5;
   private static final int t_group = 6;
   private static final int t_ws = 7;
   private static final int t_comma = 8;
   private static final int t_phrase = 9;
   private static final int t_dot = 10;
   private static final int t_name = 11;
   private static final int t_value = 12;
   private static final int t_end = 13;
   private static final int t_union = 14;
   private static final int t_all = 15;
   private SqlStatementParser.SqlToken select = null;
   private SqlStatementParser.SqlToken from = null;
   private SqlStatementParser.SqlToken where = null;
   private SqlStatementParser.SqlToken order = null;
   private SqlStatementParser.SqlToken group = null;
   private SqlStatementParser.SqlToken end = null;
   private SqlStatementParser.SqlToken union = null;

   public SqlStatementParser(String var1) throws Exception {
      this.m_Sql = var1;
      this.parse(var1, 0);
   }

   private List<SqlStatementParser.SqlToken> parse(String var1, int var2) throws Exception {
      ArrayList var3 = new ArrayList();
      KongaStringTokenizer var4 = new KongaStringTokenizer(var1, " \"][()`',.\t\r\n;", true);
      int var5 = 0;
      int var6 = 0;

      while (var4.hasMoreTokens()) {
         String var8 = var4.nextToken();
         var5 = var6;
         var6 += var8.length();
         char var7;
         switch (var7 = var8.charAt(0)) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
               while (var4.hasMoreTokens() && ((var7 = var4.peek().charAt(0)) == ' ' || var7 == '\r' || var7 == '\n' || var7 == '\t')) {
                  var6 += var4.nextToken().length();
               }

               var3.add(new SqlStatementParser.SqlToken(var5, var6, 7));
               break;
            case '(':
               var6 += this.findMatchingParenthesis(var4);
               var3.add(new SqlStatementParser.SqlToken(var5, var6, 9));
               break;
            case ')':
            case ']':
               throw new Exception("Misplaced token " + var8);
            case ',':
               var3.add(new SqlStatementParser.SqlToken(var5, var6, 8));
               break;
            case '.':
               var3.add(new SqlStatementParser.SqlToken(var5, var6, 10));
               break;
            case ';':
               var3.add(this.end = new SqlStatementParser.SqlToken(var5, var6, 13));
               break;
            case '[':
               var7 = ']';
            case '"':
            case '\'':
            case '`':
               var6 += this.findMatchingToken(var4, var7);
               var3.add(new SqlStatementParser.SqlToken(var5, var6, var7 == '\'' ? 12 : 11));
               break;
            default:
               if (var8.equalsIgnoreCase("SELECT")) {
                  if (this.select != null && this.union != null) {
                     int var9 = var3.size();

                     for (int var10 = var9 - 1; var10 >= 0; var10--) {
                        SqlStatementParser.SqlToken var11 = (SqlStatementParser.SqlToken)var3.get(var10);
                        if (var11.tokenType != 7 && var11.tokenType != 15) {
                           if (var11 != this.union) {
                              throw new Exception("UNION operator should be followed by SELECT operator.");
                           }
                           break;
                        }
                     }

                     this.m_Sql = this.m_Sql.substring(0, this.union.beginPos);
                     var3.add(this.end = new SqlStatementParser.SqlToken(0, 0, 13));
                  } else {
                     if (this.select != null && var2 == 0) {
                        throw new Exception("More than one SELECT key words");
                     }

                     var3.add(this.select = new SqlStatementParser.SqlToken(var5, var6, 1));
                  }
               } else if (var8.equalsIgnoreCase("FROM")) {
                  if (this.select == null && var2 == 0) {
                     throw new Exception("FROM key word does not follow SELECT key word.");
                  }

                  if (this.from != null && var2 == 0) {
                     throw new Exception("More than one FROM key words");
                  }

                  var3.add(this.from = new SqlStatementParser.SqlToken(var5, var6, 2));
               } else if (var8.equalsIgnoreCase("WHERE")) {
                  if (var2 == 0 && (this.select == null || this.from == null)) {
                     throw new Exception("This is not a valid SELECT statement.");
                  }

                  if (var2 == 0 && this.where != null) {
                     throw new Exception("More than one WHERE key words");
                  }

                  if (var2 == 0 && (this.order != null || this.group != null)) {
                     throw new Exception("WHERE key word should be placed before ORDER OR GROUP.");
                  }

                  var3.add(this.where = new SqlStatementParser.SqlToken(var5, var6, 3));
               } else if (var8.equalsIgnoreCase("ORDER")) {
                  var6 += this.findToken(var4, "BY");
                  if (var2 == 0 && (this.select == null || this.from == null)) {
                     throw new Exception("This is not a valid SELECT statement.");
                  }

                  if (var2 == 0 && this.order != null) {
                     throw new Exception("More than one order key words");
                  }

                  var3.add(this.order = new SqlStatementParser.SqlToken(var5, var6, 5));
               } else if (var8.equalsIgnoreCase("GROUP")) {
                  var6 += this.findToken(var4, "BY");
                  if (var2 == 0 && (this.select == null || this.from == null)) {
                     throw new Exception("This is not a valid SELECT statement.");
                  }

                  var3.add(this.group = new SqlStatementParser.SqlToken(var5, var6, 6));
               } else if (var2 == 0 && var8.equalsIgnoreCase("UNION")) {
                  var3.add(this.union = new SqlStatementParser.SqlToken(var5, var6, 14));
               } else if (var2 != 0 || !var8.equalsIgnoreCase("ALL")) {
                  if (Character.isDigit(var7)) {
                     var3.add(new SqlStatementParser.SqlToken(var5, var6, 12));
                  } else {
                     var3.add(new SqlStatementParser.SqlToken(var5, var6, 11));
                  }
               }
         }

         if (this.end != null) {
            break;
         }
      }

      return var3;
   }

   public String insertNoneFilter() {
      String var1;
      if (this.where != null) {
         int var2 = this.where.endPos;
         var1 = this.m_Sql.substring(0, var2) + " (1=2) AND" + this.m_Sql.substring(var2);
      } else {
         int var5 = this.getInsrtFilterPosition();
         char var3 = this.m_Sql.charAt(var5 - 1);
         var1 = this.m_Sql.substring(0, var5);
         if (!Character.isWhitespace(var3)) {
            var1 = var1 + " ";
         }

         var1 = var1 + "WHERE (1=2)";
         if (var5 < this.m_Sql.length()) {
            if (this.end != null && this.end.beginPos != var5) {
               var1 = var1 + " ";
            }

            var1 = var1 + this.m_Sql.substring(var5);
         }
      }

      return var1;
   }

   private int getInsrtFilterPosition() {
      int var1 = this.m_Sql.length();
      if (this.group != null && this.group.beginPos < var1) {
         var1 = this.group.beginPos;
      }

      if (this.order != null && this.order.beginPos < var1) {
         var1 = this.order.beginPos;
      }

      if (this.end != null && this.end.beginPos < var1) {
         var1 = this.end.beginPos;
      }

      return var1;
   }

   public String getFilter() {
      if (this.where == null) {
         return "";
      }

      String var1 = this.m_Sql.substring(this.where.endPos, this.getInsrtFilterPosition());
      return var1.trim();
   }

   public String replaceFilter(String var1) {
      var1 = var1.trim();
      if (!var1.isEmpty() && !var1.toUpperCase().startsWith("WHERE")) {
         var1 = "WHERE " + var1;
      }

      int var2 = this.getInsrtFilterPosition();
      String var3;
      if (this.where != null) {
         var3 = this.m_Sql.substring(0, this.where.beginPos) + var1;
      } else {
         var3 = this.m_Sql.substring(0, var2);
         if (!var3.endsWith(" ") && !var1.isEmpty()) {
            var3 = var3 + " ";
         }

         var3 = var3 + var1;
      }

      if (var2 == this.m_Sql.length()) {
         return var3;
      }

      if (this.end != null && this.end.beginPos != var2) {
         var3 = var3 + " ";
      }

      return var3 + this.m_Sql.substring(var2);
   }

   private int findToken(KongaStringTokenizer var1, String var2) {
      int var3 = 0;

      while (var1.hasMoreTokens()) {
         String var4 = var1.nextToken();
         var3 += var4.length();
         if (var4.equalsIgnoreCase(var2)) {
            return var3;
         }
      }

      return var3;
   }

   private int findMatchingToken(KongaStringTokenizer var1, char var2) {
      int var3 = 0;

      while (var1.hasMoreTokens()) {
         String var4 = var1.nextToken();
         var3 += var4.length();
         if (var4.charAt(0) == var2) {
            String var5 = var1.peek();
            if (var5 == null || var5.charAt(0) != var2) {
               break;
            }

            var3 += var1.nextToken().length();
         }
      }

      return var3;
   }

   private int findMatchingParenthesis(KongaStringTokenizer var1) {
      int var2 = 0;

      while (var1.hasMoreTokens()) {
         String var3 = var1.nextToken();
         var2 += var3.length();
         char var4 = var3.charAt(0);
         if (var4 == ')') {
            break;
         }

         if (var4 != '\'' && var4 != '"' && var4 != '`' && var4 != '[') {
            if (var4 == '(') {
               var2 += this.findMatchingParenthesis(var1);
            }
         } else {
            if (var4 == '[') {
               var4 = ']';
            }

            var2 += this.findMatchingToken(var1, var4);
         }
      }

      return var2;
   }

   private static boolean isQName(String var0) {
      String var1 = var0.trim();
      if (var1.isEmpty()) {
         return false;
      }

      char var2 = var1.charAt(0);
      if (!Character.isLetter(var2) && var2 != '_' && var2 != '.') {
         return false;
      }

      for (char var6 : var1.toCharArray()) {
         if (var6 != '_' && var6 != '.' && !Character.isLetterOrDigit(var6)) {
            return false;
         }
      }

      return true;
   }

   private static boolean isWhiteSpace(String var0) {
      for (char var4 : var0.toCharArray()) {
         if (!Character.isWhitespace(var4)) {
            return false;
         }
      }

      return true;
   }

   public static String GetDESubstitudedWithDefaultString(String var0) {
      int var1 = 0;
      int var2 = var0.indexOf(91, var1);
      int var3 = var0.indexOf(93, var1);

      while (var2 >= 0 && var3 >= 0 && var3 > var2) {
         int var4 = var0.indexOf(123, var2);
         int var5 = var0.indexOf(125, var2);
         if (var4 > var2 && var5 < var3 && isQName(var0.substring(var2 + 1, var4)) && isWhiteSpace(var0.substring(var5 + 1, var3))) {
            var0 = var0.substring(0, var2) + var0.substring(var4 + 1, var5) + var0.substring(var3 + 1);
            var1 = var2 + (var5 - var4 - 1);
         } else {
            var1 = var3 + 1;
         }

         var2 = var0.indexOf(91, var1);
         var3 = var0.indexOf(93, var1);
      }

      return var0;
   }

   public String subSqlWithoutUnion() {
      if (this.m_Sql == null || this.m_Sql.isEmpty()) {
         return "";
      } else {
         return this.union == null ? this.m_Sql : this.m_Sql.substring(0, this.union.beginPos);
      }
   }

   class SqlToken {
      final int beginPos;
      final int endPos;
      final int tokenType;

      public SqlToken(int var2, int var3, int var4) {
         this.beginPos = var2;
         this.endPos = var3;
         this.tokenType = var4;
      }

      @Override
      public String toString() {
         return SqlStatementParser.this.m_Sql.substring(this.beginPos, this.endPos);
      }
   }
}
