package org.etlforge.integration.data.script;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import org.etlforge.integration.structure.mapping.PathSeparator;
import org.etlforge.util.string.Case;
import org.etlforge.util.string.KongaStringTokenizer;

public class Transform {
   private static final boolean BEFORE = true;
   private static final boolean AFTER = false;
   private static final String TERMINATOR = "+-*/^=&|\"'\\<>!(),;{}[] \n\r\t";
   private int current_index;
   private KongaStringTokenizer m_tk;
   public Transform.ErrObject m_err;
   private String m_savedTokenStr;
   private String m_expr;
   private Transform.Token currentToken;
   private int m_pos;
   private int m_nData;
   private Transform.Token m_savedToken;
   private Transform.Level m_levelObj;
   private final List<Transform.Token> m_stackVector = Lists.newArrayList();
   private final List<Transform.Token> m_tokenVector = Lists.newArrayList();
   private int m_lastId;
   private SourceDataElementContainer sourceDataElementContainer = SourceDataElementContainer.EMPTY;
   private final Map<String, Integer> lde_status_map = new HashMap<>();
   public static final int t_Unknown = 400;
   public static final int t_DE = 405;
   public static final int t_IntC = 402;
   public static final int t_FloatC = 403;
   public static final int t_StrC = 401;
   public static final int t_GlobalDE = 406;
   public static final int t_LocalDE = 408;
   public static final int t_Set = 320;
   public static final int t_PSet = 321;
   public static final int t_MSet = 322;
   public static final int t_Add = 700;
   public static final int t_Sub = 701;
   public static final int t_Mul = 800;
   public static final int t_Div = 801;
   public static final int t_Not = 901;
   public static final int t_NE = 600;
   public static final int t_EQ = 601;
   public static final int t_GT = 602;
   public static final int t_GE = 603;
   public static final int t_LT = 604;
   public static final int t_LE = 605;
   public static final int t_LP = 100;
   public static final int t_RP = 1000;
   public static final int t_LBrace = 0;
   public static final int t_RBrace = 1003;
   public static final int t_LBracket = 1004;
   public static final int t_RBracket = 1005;
   public static final int t_StartArray = 1006;
   public static final int t_EndArray = 1007;
   public static final int t_Negative = 1001;
   public static final int t_PostPP = 1010;
   public static final int t_PostMM = 1011;
   public static final int t_PrePP = 1012;
   public static final int t_PreMM = 1013;
   public static final int t_Pow = 900;
   public static final int t_And = 500;
   public static final int t_Or = 407;
   public static final int t_Fcn = 101;
   public static final int t_Comma = 200;
   public static final int t_BoolC = 404;
   public static final int t_Marker = 1002;
   public static final int t_Semicolon = 310;
   public static final int t_Space = 1100;
   public static final int t_Comment = 1101;
   public static final int t_Trans = -1;
   public static final int t_End = -2;
   private Map<String, FCN> m_fcnList;

   public Transform() {
      this.createFcnList();
   }

   public void setSourceDataElementContainer(SourceDataElementContainer var1) {
      this.sourceDataElementContainer = Preconditions.checkNotNull(var1);
   }

   private void createFcnList() {
      FunctionRegistry var1 = FunctionRegistry.getRegistry();
      Iterable var2 = var1.getCategories();
      this.m_fcnList = Maps.newHashMapWithExpectedSize(200);

      for (Object __etlItem96 : var2) {
         FunctionCategory var4 = (FunctionCategory) __etlItem96;
         for (Function var6 : var4.getFunctions()) {
            String var7 = var6.getName() + "(";
            FCN var8 = var6.getGrammar();
            this.m_fcnList.put(var7.toUpperCase(), var8);
         }
      }
   }

   public static int find_END_TRANS_position(String var0, int var1) {
      int var2 = var0.length() - ScriptConstants.BEGIN_TRANS_LENGTH;
      if (var1 >= var2) {
         return -1;
      }

      boolean var3 = false;

      for (int var4 = var1; var4 < var2; var4++) {
         char var5 = var0.charAt(var4);
         if (var5 != '"' && var5 != '\'') {
            if (var3) {
               var3 = false;
               if (var5 == '/') {
                  var4++;

                  while (var4 < var2) {
                     char var8 = var0.charAt(var4);
                     if (var8 == '\n' || var8 == '\r') {
                        break;
                     }

                     var4++;
                  }
               } else if (var5 == '*') {
                  var4++;

                  while (var4 < var2) {
                     char var7 = var0.charAt(var4);
                     if (var7 == '/' && var0.charAt(var4 - 1) == '*') {
                        break;
                     }

                     var4++;
                  }
               }
            } else if (var5 == '/') {
               var3 = true;
            } else if (var5 == '<' && var0.substring(var4).startsWith("</trans>")) {
               return var4;
            }
         } else {
            var4++;

            for (; var4 < var2; var4++) {
               char var6 = var0.charAt(var4);
               if (var6 == '\\') {
                  var4++;
               } else if (var6 == var5) {
                  break;
               }
            }
         }
      }

      return -1;
   }

   private boolean isFunctionTakesNodeArgument(Transform.Token var1) {
      if (var1 == null) {
         return false;
      }

      String var2 = var1.getStr().toLowerCase();
      return var2.startsWith("getxmlstring(")
         || var2.startsWith("getsourceinstancemap")
         || var2.startsWith("getsourceinstancearray")
         || var2.startsWith("getsourceattrnames")
         || var2.startsWith("getsourceelementnames")
         || var2.startsWith("getsourceinstanceelementmap")
         || var2.startsWith("getsourceinstanceelementarray");
   }

   private static Transform.Token getLastLastToken(List<Transform.Token> var0) {
      Transform.Token var1 = null;
      int var2 = 2;

      for (int var3 = var0.size() - 2; var3 >= 0; var3--) {
         var1 = (Transform.Token)var0.get(var3);
         if (var1.isNoOp()) {
            var1 = null;
         } else if (--var2 == 0) {
            return var1;
         }
      }

      return null;
   }

   public List<Transform.Token> tokenize(String var1) {
      this.reset();
      this.m_expr = var1;
      java.util.List<int[]> mixedBlocks = null;
      if (var1.contains("<javascript>")) {
         try { mixedBlocks = NativeJavaScript.inspect(var1); }
         catch (RuntimeException failure) {
            this.m_err = new Transform.ErrObject(this.m_expr, null, failure.getMessage(), false);
            return null;
         }
      }
      int var2 = 0;

      int var3;
      for (var3 = 0; (var2 = NativeJavaScript.nextBlock(var1, var2)) >= 0; var2 = var3) {
         if (var2 > var3) {
            this.m_tokenVector.add(new Transform.Token(401, var1.substring(var3, var2), this, false));
         }

         if (var1.startsWith("<javascript>", var2)) {
            int[] selected = null;
            if (mixedBlocks != null) for (int[] block : mixedBlocks) if (block[1] == var2) { selected = block; break; }
            if (selected == null) {
               this.m_err = new Transform.ErrObject(this.m_expr, null, "Invalid JavaScript block", false);
               return null;
            }
            this.m_pos = var2;
            this.m_tokenVector.add(new Transform.Token(401, var1.substring(var2, selected[4]), this, false));
            var3 = selected[4];
            continue;
         }

         var2 += ScriptConstants.BEGIN_TRANS_LENGTH;
         var3 = find_END_TRANS_position(var1, var2);
         if (var3 < 0) {
            this.m_err = new Transform.ErrObject(this.m_expr, null, "The expression:" + var1 + "\nMissing closing tag " + "</trans>", false);
            return null;
         }

         this.m_tokenVector.add(new Transform.Token(-1, "<trans>", this, false));
         String var4 = var1.substring(var2, var3);
         this.m_pos = var2;
         this.m_err = null;
         this.m_savedTokenStr = null;
         this.m_savedToken = null;
         this.m_tk = new KongaStringTokenizer(var4, "+-*/^=&|\"'\\<>!(),;{}[] \n\r\t", true);
         Transform.Token var6 = null;
         Transform.Token var7 = null;
         boolean var8 = true;
         boolean var9 = false;
         byte var10 = 0;
         Stack var11 = new Stack();

         Transform.Token var5;
         while ((var5 = this.nextToken(var8, var9)) != null) {
            if (!var5.isArgumentList() && var10 <= 0) {
               if (var5.m_id == 1012) {
                  if (var6 != null && var6.isLHSDataElement()) {
                     var5.m_id = 1010;
                  }
               } else if (var5.m_id == 1013 && var6 != null && var6.isLHSDataElement()) {
                  var5.m_id = 1011;
               }

               if (var5.m_id == 1004 && this.isFunctionTakesNodeArgument(var6)) {
                  var7 = var5;
               } else if (var5.m_id == 1005 && var6 != null && var6.m_id == 1004) {
                  int var12 = this.m_tokenVector.size();
                  this.m_tokenVector.remove(var12 - 1);
                  var5.m_id = 1006;
                  var5.m_str = "[]";
                  var5.m_pos1 = var6.m_pos1;
                  var5.setIndex(var12 - 1);
                  var7 = null;
                  this.m_tokenVector.add(var5);
                  continue;
               }

               if (var7 != null && var7.m_id == 1004) {
                  if (var5.m_id == 405) {
                     var7 = var5;
                  } else if (var5.m_id == 1005) {
                     int var20 = this.m_tokenVector.size();
                     this.m_tokenVector.remove(var20 - 1);
                     var5.m_id = 405;
                     var5.m_str = "[]";
                     var5.m_pos1 = var7.m_pos1;
                     var5.setIndex(var20 - 1);
                     var7 = null;
                     var9 = false;
                  }
               } else if (var7 != null && var7.m_id == 405 && var5.m_id == 1005) {
                  int var19 = this.m_tokenVector.size();
                  Transform.Token var13 = this.m_tokenVector.get(var19 - 2);
                  this.m_tokenVector.remove(var19 - 1);
                  this.m_tokenVector.remove(var19 - 2);
                  var5.m_id = 405;
                  var5.m_str = "[" + var7.m_str + "]";
                  var5.m_pos1 = var13.m_pos1;
                  var5.setIndex(var19 - 2);
                  var7 = null;
                  var9 = false;
               } else {
                  var7 = null;
               }

               this.m_tokenVector.add(var5);
               int var21 = var5.getId();
               switch (var21) {
                  case -2:
                     break;
                  case 400:
                     this.m_err = new Transform.ErrObject(this.m_expr, var5, "Unknown token " + var5.m_str, false);
                     return null;
                  case 1000:
                     if (var6 != null && var6.m_id == 100) {
                        this.m_err = new Transform.ErrObject(this.m_expr, var5, "Nothing between parentheses", true);
                     }
                  default:
                     var9 = var21 == 406 || var21 == 320 || var21 == 408;
                     var8 = var21 == 320
                        || var21 == 321
                        || var21 == 322
                        || var21 == 100
                        || var21 == 101
                        || var21 == 500
                        || var21 == 407
                        || var21 == 600
                        || var21 == 601
                        || var21 == 602
                        || var21 == 603
                        || var21 == 604
                        || var21 == 605
                        || var21 == 0
                        || var21 == 200
                        || var21 == 310;
                     break;
                  case 1004:
                     var11.add(new Transform.Bracket(var9));
                     var9 = false;
                     var8 = false;
                     break;
                  case 1005:
                     if (var11.size() <= 0) {
                        this.m_err = new Transform.ErrObject(this.m_expr, var5, "The expression:" + var1 + "\nextra closing bracket ']'", false);
                        return null;
                     }

                     var9 = ((Transform.Bracket)var11.pop()).isValidLHS();
                     var8 = false;
                     break;
                  case 1100:
                  case 1101:
                     continue;
               }

               boolean var22 = var6 != null && var6.isPreOp() && !var5.isLHSDataElement();
               Transform.Token var14;
               boolean var15 = var5.isPostOp()
                  && var6 != null
                  && var6.isLHSDataElement()
                  && (var14 = getLastLastToken(this.m_tokenVector)) != null
                  && var14.isPreOp();
               if (var22 || var15) {
                  this.m_err = new Transform.ErrObject(this.m_expr, var6, "Invalid argument to operator ++/--.", false);
                  return null;
               }

               if (var21 == -2) {
                  break;
               }

               var6 = var5;
            } else {
               this.m_tokenVector.add(var5);
               label143:
               switch (var10) {
                  case 0:
                     var10 = 1;
                     break;
                  case 1:
                     switch (var5.m_id) {
                        case 405:
                           this.m_err = new Transform.ErrObject(
                              this.m_expr, var5, "ArgumentList argument name is invalid, it is already used as source DE.", false
                           );
                           return null;
                        case 408:
                           this.lde_status_map.put(var5.m_str.toUpperCase(), 1);
                           var10 = 2;
                           break label143;
                        case 1100:
                        case 1101:
                           continue;
                        default:
                           this.m_err = new Transform.ErrObject(this.m_expr, var5, "ArgumentList argument must be a local variable.", false);
                           return null;
                     }
                  case 2:
                     switch (var5.m_id) {
                        case 200:
                           var10 = 1;
                           break;
                        case 1000:
                           var10 = 0;
                           break;
                        case 1100:
                        case 1101:
                           continue;
                        default:
                           this.m_err = new Transform.ErrObject(this.m_expr, var5, "ArgumentList argument must be a local variable.", false);
                           return null;
                     }
               }

               var6 = var5;
            }
         }

         if (this.m_err != null) {
            return null;
         }

         var3 += ScriptConstants.END_TRANS_LENGTH;
      }

      if (var2 < 0 && var3 < var1.length()) {
         this.m_tokenVector.add(new Transform.Token(401, var1.substring(var3), this, false));
      }

      return this.m_tokenVector;
   }

   private void reset() {
      this.m_err = null;
      this.m_lastId = -1;
      this.currentToken = null;
      this.current_index = 0;
      this.m_savedToken = null;
      this.m_tokenVector.clear();
      this.lde_status_map.clear();
   }

   public List<Transform.Token> getRpnTokenVector(String var1) {
      if (this.tokenize(var1) == null) {
         return null;
      }

      if (!this.validateLocalAssignments()) return null;
      this.m_stackVector.clear();
      return this.rpnOrdering(this.m_tokenVector, this.m_stackVector, false);
   }

   public List<Transform.Token> rpnOrder() {
      if (!this.validateLocalAssignments()) return null;
      this.m_stackVector.clear();
      return this.rpnOrdering(this.m_tokenVector, this.m_stackVector, false);
   }

   private boolean validateLocalAssignments() {
      java.util.Set<String> defined = new java.util.HashSet<>();
      List<Token> tokens = new ArrayList<>();
      for (Token token : this.m_tokenVector) {
         if (token.m_id != t_Space && token.m_id != t_Comment) tokens.add(token);
      }
      class Pending {
         String name; int depth;
         Pending(String name, int depth) { this.name = name; this.depth = depth; }
      }
      List<Pending> pending = new ArrayList<>();
      List<Boolean> arguments = new ArrayList<>();
      int depth = 0;
      for (int i = 0; i < tokens.size(); i++) {
         Token token = tokens.get(i);
         int type = token.m_id;
         if (type == t_Fcn || type == t_LP || type == t_LBrace || type == t_LBracket) {
            arguments.add(type == t_Fcn && token.m_str.equalsIgnoreCase("ArgumentList("));
            depth++;
            continue;
         }
         boolean closing = type == t_RP || type == t_RBrace || type == t_RBracket;
         if (closing || type == t_Comma || type == t_Semicolon || type == t_End) {
            for (int j = 0; j < pending.size();) {
               Pending assignment = pending.get(j);
               if (assignment.depth >= depth) { defined.add(assignment.name); pending.remove(j); }
               else j++;
            }
            if (closing && depth > 0) { depth--; arguments.remove(arguments.size()-1); }
         }
         if (type != t_LocalDE) continue;
         boolean argumentName = !arguments.isEmpty() && arguments.get(arguments.size()-1);
         boolean target = argumentName || (i+1 < tokens.size() && tokens.get(i+1).m_id == t_Set);
         if (target) pending.add(new Pending(token.m_str, depth));
         else if (!defined.contains(token.m_str) && !this.isArgument(token.m_str)) {
            this.m_err = new ErrObject(this.m_expr, token,
               "Local variable " + token.m_str + " has not been assigned before use", true);
            return false;
         }
      }
      return true;
   }

   private List<Transform.Token> rpnOrdering(List<Transform.Token> var1, List<Transform.Token> var2, boolean var3) {
      var2.clear();
      int var5 = 0;

      for (int var6 = var1.size(); var5 < var6; var5++) {
         Transform.Token var7 = (Transform.Token)var1.get(var5);
         if (var3 || var7.m_id == -1) {
            if (var7.m_id == -1) {
               var5++;
            }

            this.m_err = null;
            Stack var10 = new Stack();
            this.m_nData = 0;
            this.m_levelObj = new Transform.Level(null, 0, this.m_nData);
            Stack var11 = new Stack();
            this.m_lastId = -1;
            boolean var12 = true;
            boolean var13 = false;
            boolean var14 = false;
            boolean var15 = false;
            boolean var16 = false;
            Transform.Token var17 = null;
            Transform.Token var18 = null;

            while (var5 < var6) {
               var7 = (Transform.Token)var1.get(var5);
               int var19 = var7.getId();
               if (var19 == -2) {
                  break;
               }

               if (var19 == 400) {
                  return null;
               }

               if (var19 != 1100 && var19 != 1101) {
                  var13 = false;
                  var14 = false;
                  int var20 = var7.getPriority();
                  var12 = false;
                  switch (var19) {
                     case 0:
                     case 101:
                        if (var15) {
                           if (var17 == null) {
                              throw new RuntimeException("lastOperandToken should have been set");
                           }

                           this.m_err = new Transform.ErrObject(
                              this.m_expr, var7, "Missing operator between two operands: " + var17.m_str + " and " + var7.m_str, true
                           );
                           return null;
                        }

                        var2.add(new Transform.Token(1002, "[", null, false));
                     case 100:
                        if (var15) {
                           if (var17 == null) {
                              throw new RuntimeException("lastOperandToken should have been set");
                           }

                           this.m_err = new Transform.ErrObject(
                              this.m_expr, var7, "Missing operator between two operands: " + var17.m_str + " and " + var7.m_str, true
                           );
                           return null;
                        }

                        var10.push(var7);
                        var11.push(this.m_levelObj);
                        this.m_levelObj = new Transform.Level(var7, 0, this.m_nData);
                        break;
                     case 200:
                        Transform.Token var37 = null;

                        while (var10.size() > 0) {
                           var37 = (Transform.Token)var10.peek();
                           if (var37.getPriority() <= var20) {
                              break;
                           }

                           var2.add(var37);
                           this.m_nData = this.m_nData + var37.numberOfDataToBeAdd();
                           if (this.m_nData < this.m_levelObj.get_nData_expected()) {
                              this.m_err = new Transform.ErrObject(this.m_expr, var7, "Not enough operands for the operation: \"" + var37.m_str + "\"", true);
                              return null;
                           }

                           var10.pop();
                        }

                        String var32 = this.m_levelObj.checkSemicolon(var7, var2);
                        if (var32.length() > 0) {
                           this.m_err = new Transform.ErrObject(this.m_expr, var7, var32, true);
                           return null;
                        }

                        this.m_levelObj.add_comma();
                        var2.add(var7);
                        break;
                     case 310:
                        while (var10.size() > 0) {
                           Transform.Token var36 = (Transform.Token)var10.peek();
                           if (!var36.isSetOp() && var36.getPriority() <= var20) {
                              break;
                           }

                           var2.add(var36);
                           int var45 = 1 - var36.numberOfDataToBeAdd();
                           if (this.m_nData < var45) {
                              this.m_err = new Transform.ErrObject(this.m_expr, var7, "Not enough operand for the operation: \"" + var7.m_str + "\"", true);
                              return null;
                           }

                           if (var36.m_id == 320) {
                              int var48 = 0;

                              while (var48 < var5 && var1.get(var48) != var36) {
                                 var48++;
                              }

                              var48--;

                              while (var48 >= 0) {
                                 Transform.Token var51 = (Transform.Token)var1.get(var48);
                                 int var53 = var51.m_id;
                                 if (var53 != 1100 && var53 != 1101) {
                                    if (var53 == 408) {
                                       String var54 = var51.m_str.toUpperCase();
                                       Integer var55 = this.lde_status_map.get(var54);
                                       if (var55 != null && var55 == 0) {
                                          this.lde_status_map.put(var54, 1);
                                       }
                                    }
                                    break;
                                 }

                                 var48--;
                              }
                           }

                           this.m_nData = this.m_nData + var36.numberOfDataToBeAdd();
                           var10.pop();
                        }

                        var2.add(var7);
                        this.m_nData--;
                        break;
                     case 320:
                     case 321:
                     case 322:
                     case 407:
                     case 500:
                     case 600:
                     case 601:
                     case 602:
                     case 603:
                     case 604:
                     case 605:
                     case 700:
                     case 701:
                     case 800:
                     case 801:
                     case 900:
                        if (var16) {
                           if (var18 == null) {
                              throw new RuntimeException("lastOperatorToken should have been set");
                           }

                           this.m_err = new Transform.ErrObject(
                              this.m_expr, var7, "Operator: " + var7.m_str + " cannot proceeded by another operator: " + var18.m_str, true
                           );
                           return null;
                        }

                        var14 = true;
                        var18 = var7;
                        if (this.m_nData <= 0) {
                           this.m_err = new Transform.ErrObject(this.m_expr, var7, "Not enough operand for the operation: \"" + var7.m_str + "\"", true);
                           return null;
                        }

                        while (var10.size() > 0) {
                           Transform.Token var35 = (Transform.Token)var10.peek();
                           if (var35.getPriority() < var20) {
                              break;
                           }

                           int var41 = var35.getId();
                           if (var41 != 200) {
                              var2.add(var35);
                              this.m_nData = this.m_nData + var35.numberOfDataToBeAdd();
                              if (this.m_nData <= this.m_levelObj.get_nData_expected()) {
                                 this.m_err = new Transform.ErrObject(this.m_expr, var7, "Not enough operand for the operation: \"" + var35.m_str + "\"", true);
                                 return null;
                              }

                              var10.pop();
                           }
                        }

                        var10.push(var7);
                        break;
                     case 408:
                        String var44 = var7.m_str.toUpperCase();
                        if (!this.isLocalVariableDefined(var44) && !this.isArgument(var44)) {
                           boolean var47 = false;

                           for (int var50 = var5 + 1; var50 < var6; var50++) {
                              int var52 = ((Transform.Token)var1.get(var50)).m_id;
                              if (var52 != 1100 && var52 != 1101) {
                                 if (var52 == 320) {
                                    this.lde_status_map.put(var44, 1);
                                    var47 = true;
                                 }
                                 break;
                              }
                           }

                           if (!var47) {
                              this.m_err = new Transform.ErrObject(this.m_expr, var7, "Local variable " + var44 + " hasn't been initialized", true);
                              return null;
                           }
                        }
                     case 401:
                     case 402:
                     case 403:
                     case 404:
                     case 405:
                     case 406:
                     case 1006:
                        var13 = true;
                        if (var15 && var17 != null) {
                           this.m_err = new Transform.ErrObject(
                              this.m_expr, var7, "Missing operator between two operands: " + var17.m_str + " and " + var7.m_str, true
                           );
                           return null;
                        }

                        var2.add(var7);
                        this.m_nData = this.m_nData + var7.numberOfDataToBeAdd();
                        if (var10.size() > 0) {
                           Transform.Token var34 = (Transform.Token)var10.peek();
                           switch (var34.m_id) {
                              case 1012:
                              case 1013:
                                 if (var19 != 406 && var19 != 408) {
                                    this.m_err = new Transform.ErrObject(
                                       this.m_expr, var7, "The operand for " + var34.m_str + " operator must be local or global data element", true
                                    );
                                    return null;
                                 }

                                 if (var19 == 408 && !this.isLocalVariableDefined(var7.m_str.toUpperCase())) {
                                    this.m_err = new Transform.ErrObject(this.m_expr, var7, "Local variable " + var7.m_str + " hasn't bean initialized", true);
                                    return null;
                                 }
                              case 901:
                              case 1001:
                                 var2.add(var34);
                                 this.m_nData = this.m_nData + var34.numberOfDataToBeAdd();
                                 var10.pop();
                           }
                        }

                        var17 = var7;
                        break;
                     case 901:
                        if (var15) {
                           if (var17 == null) {
                              throw new RuntimeException("lastOperandToken should have been defined");
                           }

                           this.m_err = new Transform.ErrObject(this.m_expr, var7, "Operator ! cannot proceeded with an operand: " + var17.m_str, true);
                           return null;
                        }

                        var10.push(var7);
                        break;
                     case 1000:
                        if (var16) {
                           if (var18 == null) {
                              throw new RuntimeException("lastOperatorToken should have been set");
                           }

                           this.m_err = new Transform.ErrObject(this.m_expr, var7, "Missing operand between " + var18.m_str + " and " + var7.m_str, true);
                           return null;
                        }

                        var13 = true;
                        var17 = var7;
                        int var40 = -1;
                        Transform.Token var33 = null;

                        while (var10.size() > 0) {
                           var33 = (Transform.Token)var10.peek();
                           var10.pop();
                           var40 = var33.getId();
                           if (var40 == 100 || var40 == 101) {
                              break;
                           }

                           if (var40 != 200) {
                              var2.add(var33);
                              this.m_nData = this.m_nData + var33.numberOfDataToBeAdd();
                           }
                        }

                        String var31 = this.m_levelObj.checkSemicolon(var7, var2);
                        if (var31.length() > 0) {
                           this.m_err = new Transform.ErrObject(this.m_expr, var7, var31, true);
                           return null;
                        }

                        if (var40 == 101) {
                           if (var33 == null) {
                              throw new RuntimeException("op token should have been set at this point");
                           }

                           var2.add(var33);
                           int var43 = 1 - var33.numberOfDataToBeAdd();
                           FCN var46 = this.getFcn(var33.m_str);
                           if (var46.checkFcnArguments(var43) == FCN.ArgumentCheck.fail) {
                              this.m_err = new Transform.ErrObject(
                                 this.m_expr,
                                 var7,
                                 "Wrong number of arguments in " + var33.m_str + "): number of arguments = " + var43 + ", " + var46.msg(),
                                 true
                              );
                              return null;
                           }
                        }

                        if (var33 != null) {
                           this.m_nData = this.m_nData + var33.numberOfDataToBeAdd();
                        }

                        if (var11.size() == 0) {
                           this.m_err = new Transform.ErrObject(this.m_expr, var7, "Extra right parenthesis ')' in the expression", true);
                           return null;
                        }

                        this.m_levelObj = (Transform.Level)var11.pop();
                        break;
                     case 1001:
                     case 1012:
                     case 1013:
                        var10.push(var7);
                        break;
                     case 1003:
                        int var9 = -1;
                        Transform.Token var8 = null;

                        while (var10.size() > 0) {
                           var8 = (Transform.Token)var10.peek();
                           var10.pop();
                           var9 = var8.getId();
                           if (var9 == 0) {
                              break;
                           }

                           if (var9 != 200) {
                              var2.add(var8);
                              this.m_nData = this.m_nData + var8.numberOfDataToBeAdd();
                           }
                        }

                        String var4 = this.m_levelObj.checkSemicolon(var7, var2);
                        if (var4.length() > 0) {
                           this.m_err = new Transform.ErrObject(this.m_expr, var7, var4, true);
                           return null;
                        }

                        if (var9 == 0) {
                           var2.add(var8);
                        }

                        if (var8 != null) {
                           this.m_nData = this.m_nData + var8.numberOfDataToBeAdd();
                        }

                        if (var11.size() == 0) {
                           this.m_err = new Transform.ErrObject(this.m_expr, var7, "Extra right BRACE '}' in the expression.", true);
                           return null;
                        }

                        this.m_levelObj = (Transform.Level)var11.pop();
                        break;
                     case 1004:
                        ArrayList var21 = Lists.newArrayList();
                        ArrayList var22 = Lists.newArrayList();
                        int var23 = var5 + 1;
                        int var25 = 0;
                        Transform.Token var26 = null;

                        int var24;
                        for (var24 = var23; var24 < var6; var24++) {
                           var26 = (Transform.Token)var1.get(var24);
                           int var27 = var26.m_id;
                           switch (var27) {
                              case 1004:
                                 var25++;
                                 break;
                              case 1005:
                                 var25--;
                           }

                           if (var25 < 0) {
                              break;
                           }

                           if (var27 != 1100 && var27 != 1101) {
                              var21.add(var26);
                           }
                        }

                        if (var25 >= 0) {
                           this.m_err = new Transform.ErrObject(this.m_expr, var21.isEmpty() ? var7 : var26, "The closing bracket is missing.", false);
                           return null;
                        }

                        ArrayList var56 = Lists.newArrayList();
                        if (this.m_lastId == 1004 && !var2.isEmpty()) {
                           int var28 = var2.size();

                           for (int var29 = var28 - 1; var29 > 0; var29--) {
                              Transform.Token var30 = (Transform.Token)var2.get(var29);
                              if (var30.m_id != 1007) {
                                 break;
                              }

                              var2.remove(var29);
                              var56.add(var30);
                           }
                        }

                        if (var21.isEmpty()) {
                           var7.m_id = 1006;
                           var2.add(var7);
                           this.m_nData++;
                        } else {
                           var11.push(this.m_levelObj);
                           int var57 = this.m_nData;
                           if (this.rpnOrdering(var21, var22, true) == null) {
                              return null;
                           }

                           Transform.Token var59 = var22.isEmpty() ? null : (Transform.Token)var22.get(var22.size() - 1);
                           this.m_nData += var57;
                           if (this.m_nData == var57 && var59 != null && var59.m_id == 310) {
                              var22.remove(var22.size() - 1);
                              this.m_nData++;
                           }

                           this.m_levelObj = (Transform.Level)var11.pop();
                           if (this.m_nData != var57 + 1) {
                              this.m_err = new Transform.ErrObject(
                                 this.m_expr,
                                 var59 == null ? var7 : var59,
                                 "Syntax error inside []. The expression could not be evaluate to a single value.",
                                 false
                              );
                              return null;
                           }
                        }

                        var2.addAll(var22);
                        var5 = var24;
                        Transform.Token var58 = (Transform.Token)var1.get(var24);
                        if (var22.isEmpty()) {
                           var58.m_id = 1007;
                        }

                        var2.add(var58);
                        var2.addAll(var56);
                        this.m_nData--;
                        var13 = true;
                        var17 = var7;
                        break;
                     case 1010:
                     case 1011:
                        var2.add(var7);
                  }

                  this.m_lastId = var19;
               }

               var5++;
               var15 = var13;
               var16 = var14;
            }

            if (this.m_levelObj.get_nComma() != 0) {
               this.m_err = new Transform.ErrObject(this.m_expr, var7, "Extra comma in the expression.", true);
               return null;
            }

            while (var10.size() > 0) {
               Transform.Token var39 = (Transform.Token)var10.peek();
               if (var39.m_id == 200) {
                  this.m_err = new Transform.ErrObject(this.m_expr, var39, "Misplaced comma in the expression.", true);
                  return null;
               }

               if (var39.m_id == 101) {
                  this.m_err = new Transform.ErrObject(this.m_expr, var39, "Function does not have closing parenthesis.", true);
                  return null;
               }

               if (var39.m_id == 100) {
                  this.m_err = new Transform.ErrObject(this.m_expr, var39, "Unbalanced parenthesis.", true);
                  return null;
               }

               var2.add(var39);
               this.m_nData = this.m_nData + var39.numberOfDataToBeAdd();
               if (this.m_nData <= 0) {
                  this.m_err = new Transform.ErrObject(this.m_expr, var39, "Not enough operand for the operation: \"" + var39.m_str + "\"", true);
                  return null;
               }

               var10.pop();
            }

            if (!var12) {
               if (this.m_nData == 0) {
                  int var42 = var2.size();
                  if (var42 > 0 && ((Transform.Token)var2.get(var42 - 1)).m_id == 310) {
                     var2.remove(var42 - 1);
                     this.m_nData = 1;
                     if (var42 > 1) {
                        return var2;
                     }
                  }
               }

               if (this.m_nData < 1) {
                  this.m_err = new Transform.ErrObject(this.m_expr, var7, "This expression returns no value.", true);
                  return null;
               }

               if (this.m_nData > 1) {
                  this.m_err = new Transform.ErrObject(
                     this.m_expr, var7, "This expression returns more than one value. Maybe some operators are missing.", true
                  );
                  return null;
               }
            }
         }
      }

      return var2;
   }

   private boolean isLocalVariableDefined(String var1) {
      Integer var2 = this.lde_status_map.get(var1);
      return var2 == null ? false : var2 > 0;
   }

   private boolean isArgument(String var1) {
      assert var1 != null;
      if (var1.length() < 2) {
         return false;
      }

      if (!var1.startsWith("_")) {
         return false;
      }

      int var2 = 1;

      for (int var3 = var1.length(); var2 < var3; var2++) {
         char var4 = var1.charAt(var2);
         if (!Character.isDigit(var4)) {
            return false;
         }
      }

      return true;
   }

   private boolean isLocalVariable(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         char var2 = var1.charAt(0);
         if (var2 != '_' && !Character.isLetter(var2)) {
            return false;
         }

         int var3 = 1;

         for (int var4 = var1.length(); var3 < var4; var3++) {
            var2 = var1.charAt(var3);
            if (var2 != '_' && !Character.isLetterOrDigit(var2)) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public void initTokenizer(String var1, int var2, int var3) {
      this.reset();
      this.m_expr = var1;
      this.m_pos = var2;
      String var4 = var1.substring(var2, var3);
      this.m_tk = new KongaStringTokenizer(var4, "+-*/^=&|\"'\\<>!(),;{}[] \n\r\t", true);
   }

   public void setLastId(int var1) {
      this.m_lastId = var1;
   }

   public Transform.Token nextToken(boolean var1, boolean var2) {
      if (this.m_savedToken != null) {
         this.currentToken = this.m_savedToken;
         this.m_savedToken = null;
         return this.currentToken;
      }

      int var4 = 400;
      String var3;
      if (this.m_savedTokenStr != null) {
         var3 = this.m_savedTokenStr;
         this.m_savedTokenStr = null;
      } else {
         if (!this.m_tk.hasMoreTokens()) {
            this.currentToken = new Transform.Token(-2, "</trans>", this, false);
            return this.currentToken;
         }

         var3 = this.m_tk.nextToken();
         this.m_pos = this.m_pos + var3.length();
      }

      char var5;
      label206:
      switch (var5 = var3.charAt(0)) {
         case '\t':
         case '\n':
         case '\r':
         case ' ':
            var4 = 1100;
            break;
         case '\u000b':
         case '\f':
         case '\u000e':
         case '\u000f':
         case '\u0010':
         case '\u0011':
         case '\u0012':
         case '\u0013':
         case '\u0014':
         case '\u0015':
         case '\u0016':
         case '\u0017':
         case '\u0018':
         case '\u0019':
         case '\u001a':
         case '\u001b':
         case '\u001c':
         case '\u001d':
         case '\u001e':
         case '\u001f':
         case '#':
         case '%':
         case '.':
         case '0':
         case '1':
         case '2':
         case '3':
         case '4':
         case '5':
         case '6':
         case '7':
         case '8':
         case '9':
         case ':':
         case '?':
         case '@':
         case 'A':
         case 'B':
         case 'C':
         case 'D':
         case 'E':
         case 'F':
         case 'G':
         case 'H':
         case 'I':
         case 'J':
         case 'K':
         case 'L':
         case 'M':
         case 'N':
         case 'O':
         case 'P':
         case 'Q':
         case 'R':
         case 'S':
         case 'T':
         case 'U':
         case 'V':
         case 'W':
         case 'X':
         case 'Y':
         case 'Z':
         case '\\':
         case '_':
         case '`':
         case 'a':
         case 'b':
         case 'c':
         case 'd':
         case 'e':
         case 'f':
         case 'g':
         case 'h':
         case 'i':
         case 'j':
         case 'k':
         case 'l':
         case 'm':
         case 'n':
         case 'o':
         case 'p':
         case 'q':
         case 'r':
         case 's':
         case 't':
         case 'u':
         case 'v':
         case 'w':
         case 'x':
         case 'y':
         case 'z':
         default:
            String var13 = this.checkNextToken("(");
            var3 = var3 + var13;
            if (var13.equals("(")) {
               if (this.getFcn(var3) != null) {
                  var4 = 101;
               } else {
                  this.m_err = new Transform.ErrObject(this.m_expr, this.currentToken, "unrecognized token: " + var3 + " ", false);
                  var4 = 400;
               }
            } else if (isNum(var3)) {
               var4 = 402;
            } else if (isFloat(var3)) {
               var4 = 403;
            } else if (isTerminatedWithE(var3)) {
               var13 = this.checkNextToken("+");
               if (var13.equals("")) {
                  var13 = this.checkNextToken("-");
               }

               var3 = var3 + var13;
               if (!var13.equals("") && this.m_tk.hasMoreTokens()) {
                  var13 = this.m_tk.nextToken();
                  this.m_pos = this.m_pos + var13.length();
                  var3 = var3 + var13;
                  if (!isNum(var13)) {
                     this.m_err = new Transform.ErrObject(this.m_expr, this.currentToken, "expecting scientific notation ", false);
                     var4 = 400;
                  } else {
                     var4 = 403;
                  }
               }
            } else if (var3.equalsIgnoreCase("TRUE") || var3.equalsIgnoreCase("FALSE")) {
               var4 = 404;
            } else if (this.isSourceDE(var3)) {
               var4 = 405;
            } else if (this.isLocalVariable(var3)) {
               var4 = 408;
            }
            break;
         case '!':
            var3 = var3 + this.checkNextToken("=");
            var4 = var3.equals("!=") ? 600 : 901;
            break;
         case '"':
         case '\'':
            boolean var12 = false;
            StringBuilder var17 = new StringBuilder(var3);

            while (this.m_tk.hasMoreTokens()) {
               String var18 = this.m_tk.nextToken();
               this.m_pos = this.m_pos + var18.length();
               var17.append(var18);
               char var9 = var18.charAt(0);
               if (var9 == var5 && !var12) {
                  var4 = 401;
                  break;
               }

               if (var9 == '\\' || var12) {
                  var12 = !var12;
               }
            }

            var3 = var17.toString();
            if (var4 == 400) {
               this.m_err = new Transform.ErrObject(this.m_expr, this.currentToken, "missing closing quote.", false);
            }
            break;
         case '$':
            var4 = 406;
            break;
         case '&':
            var3 = var3 + this.checkNextToken("&");
            var4 = 500;
            break;
         case '(':
            var4 = 100;
            break;
         case ')':
            var4 = 1000;
            break;
         case '*':
            if (this.checkNextToken("/").equals("/")) {
               var3 = var3 + '/';
            } else {
               var4 = 800;
            }
            break;
         case '+':
         case '-':
            if (this.checkNextToken(var3).equals(var3)) {
               var3 = var3 + var3;
               var4 = var5 == '+' ? 1012 : 1013;
            } else if (this.checkNextToken("=").equals("=")) {
               var3 = var3 + "=";
               var4 = var5 == '+' ? 321 : 322;
               if (!var2) {
                  this.m_err = new Transform.ErrObject(
                     this.m_expr,
                     this.currentToken,
                     "The left hand side of the assignment operator '+=' or '-=' must be a local or global data element, such as x=... or $x=...  error occured ",
                     false
                  );
                  return null;
               }
            } else if (var1) {
               Transform.Token var11 = this.nextToken(false, false);
               switch (var11.m_id) {
                  case 402:
                  case 403:
                     if (var5 == '+') {
                        var3 = var11.m_str;
                     } else {
                        var3 = var3 + var11.m_str;
                     }

                     var4 = var11.m_id;
                     break label206;
                  default:
                     this.m_savedToken = var11;
                     if (var5 == '+') {
                        var4 = 700;
                     } else if (var5 == '-') {
                        var4 = 1001;
                     }
               }
            } else {
               var4 = var5 == '+' ? 700 : 701;
            }
            break;
         case ',':
            var4 = 200;
            break;
         case '/':
            if (this.checkNextToken("/").equals("/")) {
               StringBuilder var6 = new StringBuilder(var3);
               var6.append('/');
               this.m_savedToken = null;

               while (this.m_tk.hasMoreTokens()) {
                  String var7 = this.m_tk.nextToken();
                  this.m_pos = this.m_pos + var7.length();
                  var6.append(var7);
                  if (var7.equals("\n")) {
                     break;
                  }
               }

               var3 = var6.toString();
               var4 = 1101;
            } else if (this.checkNextToken("*").equals("*")) {
               StringBuilder var10 = new StringBuilder(var3);
               var10.append('*');
               this.m_savedToken = null;

               while (this.m_tk.hasMoreTokens()) {
                  String var16 = this.m_tk.nextToken();
                  this.m_pos = this.m_pos + var16.length();
                  var10.append(var16);
                  int var8 = var10.length();
                  if (var10.charAt(var8 - 2) == '*' && var10.charAt(var8 - 1) == '/') {
                     break;
                  }
               }

               var3 = var10.toString();
               var4 = 1101;
            } else {
               var4 = 801;
            }
            break;
         case ';':
            var4 = 310;
            break;
         case '<':
            var3 = var3 + this.checkNextToken("=");
            var4 = var3.equals("<=") ? 605 : 604;
            break;
         case '=':
            var3 = var3 + this.checkNextToken("=");
            var4 = var3.equals("==") ? 601 : 320;
            if (var4 == 320 && !var2) {
               this.m_err = new Transform.ErrObject(
                  this.m_expr,
                  this.currentToken,
                  "The left hand side of the assignment operator '=' must be a local or global data element, such as x=... or $x=...  error occured ",
                  false
               );
               return null;
            }
            break;
         case '>':
            var3 = var3 + this.checkNextToken("=");
            var4 = var3.equals(">=") ? 603 : 602;
            break;
         case '[':
            var4 = 1004;
            break;
         case ']':
            var4 = 1005;
            break;
         case '^':
            var4 = 900;
            break;
         case '{':
            var4 = 0;
            break;
         case '|':
            var3 = var3 + this.checkNextToken("|");
            var4 = 407;
            break;
         case '}':
            var4 = 1003;
      }

      this.currentToken = new Transform.Token(var4, var3, this, var1);
      return this.currentToken;
   }

   private boolean isSourceDE(String var1) {
      if (this.sourceDataElementContainer.isFlat()) {
         String var2 = ScriptUtils.stripInstanceTag(var1);
         return this.sourceDataElementContainer.contains(var2);
      } else {
         return var1.indexOf(36) > 0 || var1.indexOf(46) >= 0;
      }
   }

   public static List<String> getDEVector(List<Transform.Token> var0) {
      return getDEVector(var0, 405);
   }

   public static List<String> getDEVector(List<Transform.Token> var0, int var1) {
      ArrayList var2 = Lists.newArrayList();

      for (Transform.Token var4 : var0) {
         if (var4.getId() == 401 && var4.m_str.startsWith("<javascript>") && (var1 == 405 || var1 == 406)) {
            for (String reference : NativeJavaScript.references(var4.m_str)) {
               if (var1 == 405 && reference.startsWith("S:") && !var2.contains(reference.substring(2))) var2.add(reference.substring(2));
               if (var1 == 406 && reference.startsWith("G:") && !var2.contains(reference.substring(2))) var2.add(reference.substring(2));
            }
         }
         if (var4.getId() == var1) {
            boolean var5 = false;

            for (Object __etlItem1407 : var2) {
               String var7 = (String) __etlItem1407;
               var5 = var4.m_str.equals(var7);
               if (var5) {
                  break;
               }
            }

            if (!var5) {
               var2.add(var4.m_str);
            }
         }
      }

      return var2;
   }

   private String checkNextToken(String var1) {
      String var2;
      if (this.m_savedTokenStr != null) {
         var2 = this.m_savedTokenStr;
         this.m_savedTokenStr = null;
      } else {
         if (!this.m_tk.hasMoreTokens()) {
            return "";
         }

         var2 = this.m_tk.nextToken();
         this.m_pos = this.m_pos + var2.length();
      }

      if (var2.equals(var1)) {
         return var2;
      }

      this.m_savedTokenStr = var2;
      return "";
   }

   private FCN getFcn(String var1) {
      return this.m_fcnList.get(var1.toUpperCase());
   }

   private static boolean isNum(String var0) {
      int var1 = var0.length();

      for (int var2 = 0; var2 < var1; var2++) {
         if (!Character.isDigit(var0.charAt(var2))) {
            return false;
         }
      }

      return true;
   }

   private static boolean isFloat(String var0) {
      int var1 = var0.length();
      boolean var3 = false;
      boolean var4 = false;
      boolean var5 = false;
      boolean var6 = false;

      for (int var7 = 0; var7 < var1; var7++) {
         char var2;
         if (!Character.isDigit(var2 = var0.charAt(var7))) {
            if (var2 == '.') {
               if (var4 || var5) {
                  return false;
               }

               var4 = true;
            } else {
               if (var2 != 'E' && var2 != 'e' && var2 != 'd' && var2 != 'D') {
                  return false;
               }

               if (var5) {
                  return false;
               }

               var5 = true;
            }
         } else if (var5) {
            var6 = true;
         } else {
            var3 = true;
         }
      }

      return !var3 ? false : !var5 || var6;
   }

   private static boolean isTerminatedWithE(String var0) {
      int var1 = var0.length();
      if (var1 == 1) {
         return false;
      }

      boolean var3 = false;

      for (int var4 = 0; var4 < var1; var4++) {
         char var2;
         if (!Character.isDigit(var2 = var0.charAt(var4))) {
            if (var2 == '.') {
               if (var3) {
                  return false;
               }

               var3 = true;
            } else {
               if (var2 != 'E' && var2 != 'e' && var2 != 'd' && var2 != 'D') {
                  return false;
               }

               if (var4 < var1 - 1) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   public boolean hasCResolve() {
      for (Transform.Token var2 : this.m_tokenVector) {
         if (var2.m_id == 101 && this.hasCResolve(FunctionRegistry.getRegistry().getFunctionByName(var2.m_str))) {
            return true;
         }
      }

      return false;
   }

   private boolean hasCResolve(Function var1) {
      return var1 != null && var1.getFormat() != null ? var1.getFormat().indexOf(35) >= 0 : false;
   }

   private int getNumberOfTags(Stack<Transform.InstanceTagging> var1) {
      int var2 = 0;

      for (Transform.InstanceTagging var4 : var1) {
         if (var4.tagShouldBeAdded()) {
            var2++;
         }

         if (var4.bReturnArray) {
            var2--;
         }
      }

      return var2;
   }

   public String addInstanceTag(String var1, String var2) throws Exception {
      this.m_expr = var1;
      int var3 = this.m_tokenVector.size();
      Stack var4 = new Stack();

      for (int var5 = 0; var5 < var3; var5++) {
         Transform.Token var6 = this.m_tokenVector.get(var5);
         switch (var6.m_id) {
            case 100:
               var4.push(new Transform.InstanceTagging());
               break;
            case 101:
               this.addInstanceTagToFunction(var1, var4, var6);
               break;
            case 200:
               if (!var4.isEmpty()) {
                  ((Transform.InstanceTagging)var4.peek()).nextArg();
               }
               break;
            case 405:
               this.addInstanceTagToDataElement(var2, var4, var6);
               break;
            case 1000:
               var4.pop();
         }
      }

      return this.m_expr;
   }

   private void addInstanceTagToDataElement(String var1, Stack<Transform.InstanceTagging> var2, Transform.Token var3) throws Exception {
      int var4 = this.getNumberOfTags(var2);
      if (var4 > 0) {
         var3.addInstanceTags(var4);
      } else if (var1 != null) {
         var3.addInstanceTags(var1);
      }
   }

   private void addInstanceTagToFunction(String var1, Stack<Transform.InstanceTagging> var2, Transform.Token var3) throws Exception {
      Function var4 = FunctionRegistry.getRegistry().getFunctionByName(var3.m_str);
      if (var4 == null) {
         throw new Exception("The expression contains undefined functions " + var1);
      }

      var2.push(new Transform.InstanceTagging(var4));
   }

   public String rmInstanceTag(String var1) {
      this.m_expr = var1;

      for (Transform.Token var3 : this.m_stackVector) {
         if (var3.m_id == 405 && var3.m_str.indexOf(35) >= 0) {
            var3.rmInstanceTag();
         }
      }

      return this.m_expr;
   }

   public static String switchCaseForDbSourceDataElement(String var0, Case var1) throws Exception {
      Transform var2 = new Transform();
      List var3 = var2.tokenize(var0);
      if (var3 == null) {
         throw new Exception("Invalid script: " + var0);
      }

      for (Object __etlItem1627 : var3) {
         Transform.Token var5 = (Transform.Token) __etlItem1627;
         if (var5.m_id == 405) {
            var5.m_str = var1.convert(var5.m_str);
         }
      }

      StringBuilder var7 = new StringBuilder();

      for (Object __etlItem1635 : var3) {
         Transform.Token var6 = (Transform.Token) __etlItem1635;
         var7.append(var6.m_str);
      }

      return var7.toString();
   }

   private static String changeOwner(String var0, String var1) throws Exception {
      assert var1 != null;
      boolean var2 = var0.startsWith("[") && var0.endsWith("]");
      if (var2) {
         var0 = var0.substring(1, var0.length() - 1);
      }

      StringTokenizer var3 = PathSeparator.tokenize(var0, true);
      ArrayList var4 = new ArrayList();

      while (var3.hasMoreTokens()) {
         var4.add(var3.nextToken());
      }

      int var5 = var4.size() - (var2 ? 0 : 1);
      StringBuilder var6 = new StringBuilder();
      if (var2) {
         var6.append('[');
      }

      for (int var7 = 0; var7 < var5; var7++) {
         String var8 = (String)var4.get(var7);
         char var9 = var8.charAt(0);
         if (PathSeparator.isSeparator(var9)) {
            var6.append(var9);
         } else {
            int var10 = var8.indexOf(58);
            if (var10 >= 0) {
               var8 = var8.substring(var10 + 1);
            }

            if (!var1.isEmpty()) {
               var8 = var1 + ":" + var8;
            }

            var6.append(var8);
         }
      }

      if (var5 < var4.size()) {
         var6.append((String)var4.get(var5));
      }

      if (var2) {
         var6.append(']');
      }

      return var6.toString();
   }

   public static String switchCaseOwnerForDbDataElement(String var0, Case var1, String var2, String var3) throws Exception {
      if (var0 == null) {
         return null;
      }

      int var4 = var0.indexOf(9);
      if (var4 < 0) {
         throw new Exception("Can't find separater character in the input string.");
      }

      String var5 = var0.substring(0, var4);
      if (var3 != null) {
         var5 = var1.convert(var5);
         var5 = changeOwner(var5, var3);
      }

      String var6 = var0.substring(var4 + 1);
      if (var2 != null) {
         boolean var7 = var5.startsWith("[") && var5.endsWith("]");
         if (var7) {
            var6 = var1.convert(var6);
            var6 = changeOwner(var6, var2);
         } else {
            Transform var8 = new Transform();
            List var9 = var8.tokenize(var6);
            if (var9 == null) {
               throw new Exception("Invalid script: " + var6);
            }

            for (Object __etlItem1721 : var9) {
               Transform.Token var11 = (Transform.Token) __etlItem1721;
               if (var11.m_id == 405) {
                  var11.m_str = var1.convert(var11.m_str);
                  var11.m_str = changeOwner(var11.m_str, var2);
               }
            }

            StringBuilder var15 = new StringBuilder();

            for (Object __etlItem1730 : var9) {
               Transform.Token var12 = (Transform.Token) __etlItem1730;
               var15.append(var12.m_str);
            }

            var6 = var15.toString();
         }
      }

      return var5 + "\t" + var6;
   }

   static class Bracket {
      boolean m_bValidLHS;

      Bracket(boolean var1) {
         this.m_bValidLHS = var1;
      }

      boolean isValidLHS() {
         return this.m_bValidLHS;
      }
   }

   public static final class ErrObject {
      private final int lineNumber;
      private final String line;
      private final String err_msg;
      private final Transform.Token token;
      private final boolean beforeOrAfter;

      private ErrObject(String var1, Transform.Token var2, String var3, boolean var4) {
         this.token = var2;
         this.err_msg = var3;
         this.beforeOrAfter = var4;
         if (var2 != null) {
            StringTokenizer var5 = new StringTokenizer(var1.substring(0, var2.m_pos1 + var2.m_str.length()), "\n", true);
            int var6 = 1;
            String var7 = null;

            while (var5.hasMoreTokens()) {
               var7 = var5.nextToken();
               if (var7.charAt(0) == '\n') {
                  var6++;
               }
            }

            this.line = var7;
            this.lineNumber = var6;
         } else {
            this.line = "";
            this.lineNumber = 1;
         }
      }

      public int getLineNumber() {
         return this.lineNumber;
      }

      public String getErrorMessage() {
         return this.err_msg;
      }

      public String getErrorLine() {
         return this.line;
      }

      public Transform.Token getErrorToken() {
         return this.token;
      }

      public boolean occursBeforeToken() {
         return this.beforeOrAfter;
      }

      public String getFormattedErrorMessage() {
         String var1 = this.getErrorMessage();
         if (this.getLineNumber() >= 0) {
            var1 = "Error on line " + this.getLineNumber() + ": " + var1;
         }

         return var1;
      }

      @Override
      public String toString() {
         return this.getFormattedErrorMessage();
      }
   }

   private static class InstanceTagging {
      private String argTypeChars = "";
      private int currentArgPos;
      protected boolean bReturnArray;

      public InstanceTagging() {
      }

      public InstanceTagging(Function var1) {
         if (var1 != null) {
            String var2 = var1.getFormat();
            int var3 = var2.lastIndexOf(59);
            if (var3 > 0 && var3 + 1 < var2.length()) {
               this.argTypeChars = var2.substring(var3 + 1);
            }

            this.bReturnArray = var1.isReturnArray();
         }
      }

      public void nextArg() {
         this.currentArgPos++;
      }

      public boolean tagShouldBeAdded() {
         return this.currentArgPos >= this.argTypeChars.length() ? false : this.argTypeChars.charAt(this.currentArgPos) == '#';
      }
   }

   private class Level {
      private int comma_n;
      private final int data_stack_size_when_start;
      private final Transform.Token token;

      Level(Transform.Token var2, int var3, int var4) {
         assert var2 == null || var2.m_id == 100 || var2.m_id == 101 || var2.m_id == 0;
         this.token = var2;
         this.comma_n = var3;
         this.data_stack_size_when_start = var4;
      }

      void add_comma() {
         this.comma_n++;
      }

      int get_nComma() {
         return this.comma_n;
      }

      int get_nData_expected() {
         return this.data_stack_size_when_start + this.comma_n;
      }

      String checkSemicolon(Transform.Token var1, List<Transform.Token> var2) {
         int var3 = var1.m_id;
         assert var3 == 200 || var3 == 1000 || var3 == 1003;
         if (this.token == null) {
            return "Syntax error, misplaced operator \"" + var1.getStr() + "\".";
         }

         int var4 = this.token.m_id;
         if (var3 == 200) {
            if (var4 != 101 && var4 != 0) {
               return "Syntax error, misplaced Comma operator.";
            }
         } else if (var3 == 1003 != (var4 == 0)) {
            return "Syntax error, misplaced brace operator.";
         }

         if (var3 == 1000 && var4 == 101) {
            int var5 = Transform.this.m_levelObj.get_nComma() + (Transform.this.m_lastId != 101 ? 1 : 0);
            int var6 = 1 - this.token.numberOfDataToBeAdd();
            if (var5 != var6) {
               return "Wrong number of arguments for the function " + this.token.getStr();
            }

            if (Transform.this.m_lastId == 101) {
               return "";
            }
         }

         int var8 = var2.size();
         Transform.Token var9 = var8 > 0 ? (Transform.Token)var2.get(var8 - 1) : null;
         int var7 = this.get_nData_expected();
         if (var9 != null && var9.m_id != 1002) {
            var7++;
         }

         if (Transform.this.m_nData == var7) {
            return "";
         }

         if (Transform.this.m_nData == var7 - 1) {
            if (var9 != null && var9.m_id == 310) {
               var2.remove(var8 - 1);
               Transform.this.m_nData++;
               return "";
            } else {
               return "No data created for the argument";
            }
         } else {
            return "The expression for the argument created more than one data element.";
         }
      }

      @Override
      public String toString() {
         return "NComma=" + this.comma_n + ", data_stack_size_when_start=" + this.data_stack_size_when_start;
      }
   }

   public class Token {
      public String m_str;
      public int m_id;
      public int m_index;
      int m_pos1 = -1;
      Transform.Token_status m_status;

      public Token(int var2, String var3, Transform var4, boolean var5) {
         this.m_id = var2;
         this.m_str = var3;
         if (this.m_id != 1002) {
            this.m_pos1 = Transform.this.m_expr.lastIndexOf(var3, Transform.this.m_pos);
         }

         this.m_index = Transform.this.current_index++;
         if (var5) {
            this.m_status = Transform.Token_status.start_token;
         }

         if (this.m_id == 320) {
            for (int var6 = Transform.this.m_tokenVector.size() - 1; var6 >= 0; var6--) {
               Transform.Token var7 = Transform.this.m_tokenVector.get(var6);
               if (var7.m_status == Transform.Token_status.start_token) {
                  break;
               }

               if (var7.m_id == 1005) {
                  var7.m_status = Transform.Token_status.lhs_token;
               }
            }
         }
      }

      public void setIndex(int var1) {
         Transform.this.current_index = var1;
         this.m_index = Transform.this.current_index++;
      }

      public String source_location() {
         if (this.m_str == null) {
            return "";
         }

         StringTokenizer var1 = new StringTokenizer(Transform.this.m_expr.substring(0, this.m_pos1 + this.m_str.length()), "\n", true);
         int var2 = 1;
         String var3 = null;

         while (var1.hasMoreTokens()) {
            var3 = var1.nextToken();
            if (var3.charAt(0) == '\n') {
               var2++;
            }
         }

         return var2 == 1 ? var3 : "at line " + var2 + "\n" + var3 + "\n";
      }

      public int getId() {
         return this.m_id;
      }

      public int getPriority() {
         return this.m_id / 100;
      }

      public String getStr() {
         return this.m_str;
      }

      public int numberOfDataToBeAdd() {
         switch (this.m_id) {
            case 0:
               return 1 - (Transform.this.m_levelObj.get_nComma() + (Transform.this.m_lastId != 101 && Transform.this.m_lastId != 0 ? 1 : 0));
            case 101:
               FCN var1 = Transform.this.getFcn(this.m_str);
               if (!var1.isFixedArgumentNumber()) {
                  return 1 - (Transform.this.m_levelObj.get_nComma() + (Transform.this.m_lastId != 101 ? 1 : 0));
               }

               return 1 - var1.getMinimumNumberOfArguments();
            case 320:
            case 321:
            case 322:
               return -1;
            case 401:
            case 402:
            case 403:
            case 404:
            case 405:
            case 406:
            case 408:
            case 1006:
               return 1;
            case 407:
            case 500:
            case 600:
            case 601:
            case 602:
            case 603:
            case 604:
            case 605:
            case 700:
            case 701:
            case 800:
            case 801:
            case 900:
               return -1;
            case 901:
            case 1001:
            case 1010:
            case 1011:
            case 1012:
            case 1013:
               return 0;
            default:
               return 0;
         }
      }

      private void insertChar(int var1, String var2) {
         Transform.this.m_expr = Transform.this.m_expr.substring(0, var1) + var2 + Transform.this.m_expr.substring(var1);
         int var3 = var2.length();
         this.m_str = Transform.this.m_expr.substring(this.m_pos1, this.m_pos1 + this.m_str.length() + var3);

         for (Transform.Token var5 : Transform.this.m_stackVector) {
            if (var5.m_pos1 > var1) {
               var5.m_pos1 += var3;
            }
         }
      }

      public boolean isLHSDataElement() {
         return this.m_id == 406 || this.m_id == 408;
      }

      boolean isPreOp() {
         return this.m_id == 1013 || this.m_id == 1012;
      }

      boolean isPostOp() {
         return this.m_id == 1011 || this.m_id == 1010;
      }

      boolean isNoOp() {
         return this.m_id == 1100 || this.m_id == 1101;
      }

      boolean isSetOp() {
         return this.m_id == 320 || this.m_id == 321 || this.m_id == 322;
      }

      boolean isArgumentList() {
         return this.m_id == 101 && this.m_str.equalsIgnoreCase("ArgumentList(");
      }

      private void removeChar(int var1) {
         Transform.this.m_expr = Transform.this.m_expr.substring(0, var1) + Transform.this.m_expr.substring(var1 + 1);
         if (this.m_str.length() > 0) {
            this.m_str = Transform.this.m_expr.substring(this.m_pos1, this.m_pos1 + this.m_str.length() - 1);
         }
      }

      private void addInstanceTags(int var1) throws Exception {
         if (this.m_id == 405 && var1 != 0) {
            int var2 = var1;
            int var3 = this.m_pos1;
            int var4 = this.m_pos1 + this.m_str.length();

            for (int var5 = Transform.this.m_expr.lastIndexOf(".", var4); var5 > var3; var5 = Transform.this.m_expr.lastIndexOf(".", var5 - 1)) {
               for (int var6 = var5 - 1; var6 >= var3; var6--) {
                  char var7 = Transform.this.m_expr.charAt(var6);
                  switch (var7) {
                     case '#':
                        if (var6 == var5 - 1) {
                           if (var2 > 0) {
                              var2--;
                           } else {
                              this.removeChar(var6);
                           }

                           var6 = -1;
                        }
                        break;
                     case '$':
                     case '.':
                        if (var2 > 0) {
                           this.insertChar(var5, "#");
                           var6 = -1;
                           var2--;
                        }
                        break;
                     default:
                        if (var6 == var5 - 1 && Character.isDigit(var7)) {
                           var6--;

                           for (; var6 >= var3; var6--) {
                              var7 = Transform.this.m_expr.charAt(var6);
                              if (!Character.isDigit(var7)) {
                                 if (var7 != '#' && (var7 != '_' || var6 <= 0 || Transform.this.m_expr.charAt(var6 - 1) != '#')) {
                                    break;
                                 }

                                 var6 = -1;
                              }
                           }
                        }
                  }
               }
            }

            if (var2 > 0) {
               throw new Exception(
                  "Add Instance Tag failed. The data element does not have enough layers of loop node to be resolved by instance functions. number of instance functions in the chain:"
                     + var1
                     + ", the DE name:"
                     + this.m_str
               );
            }
         }
      }

      private void addInstanceTags(String var1) {
         if (this.m_id == 405 && var1 != null && !var1.isEmpty()) {
            int var2 = this.m_pos1;
            int var3 = this.m_pos1;

            for (int var4 = this.m_pos1 + this.m_str.length(); var3 < var4; var3++) {
               var2 = var3;
               var3 = Transform.this.m_expr.indexOf(".", var2);
               if (var3 < 0 || !var1.startsWith(Transform.this.m_expr.substring(this.m_pos1, var3))) {
                  break;
               }
            }

            for (; var3 >= 0 && var3 < this.m_pos1 + this.m_str.length(); var3 = Transform.this.m_expr.indexOf(".", var3 + 1)) {
               int var5 = Transform.this.m_expr.indexOf(35, var2);
               if (var5 < 0 || var5 > var3) {
                  this.insertChar(var3, "#");
                  var3++;
               }
            }
         }
      }

      private void rmInstanceTag() {
         int var1 = this.m_pos1;
         int var2 = this.m_pos1 + this.m_str.length();
         if (Transform.this.m_expr.substring(var1, var2).equals(this.m_str)) {
            this.m_str = ScriptUtils.stripInstanceTag(this.m_str);
            Transform.this.m_expr = Transform.this.m_expr.substring(0, var1) + this.m_str + Transform.this.m_expr.substring(var2);
            int var3 = this.m_str.length() - (var2 - var1);

            for (Transform.Token var5 : Transform.this.m_stackVector) {
               if (var5.m_pos1 > var1) {
                  var5.m_pos1 += var3;
               }
            }
         }
      }

      @Override
      public String toString() {
         if (this.m_id == 1101) {
            return "t_Comment";
         } else if (this.m_id == 1100) {
            return "t_Space";
         } else if (this.m_id == 1006) {
            return "start";
         } else if (this.m_id == 1007) {
            return "t_end";
         } else if (this.m_id == 1002) {
            return "t_Marker";
         } else if (this.m_id == 200) {
            return "t_Comma";
         } else if (this.m_id == 405) {
            return this.m_str + "(DE)";
         } else if (this.m_id == 408) {
            return this.m_str + "(Var)";
         } else if (this.m_id == 406) {
            return this.m_str + "(GVar)";
         } else {
            return this.m_status == Transform.Token_status.lhs_token ? this.m_str + "(LHS)" : this.m_str;
         }
      }
   }

   enum Token_status {
      lhs_token,
      set_token,
      rhs_token,
      start_token;
   }
}
