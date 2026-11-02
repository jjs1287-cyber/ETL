import java.lang.reflect.*;
public class LocalValidatorBaseline {
 public static void main(String[] args)throws Exception{
  String[] cases={"x=1;x", "x", "x=x+1", "x=1+x", "x=1;X", "x=0;x+=2;x", "If(false,x=1);x", "x={1,2};x[0]", "ArgumentList(x);x", "_1", "x=y=1;x+y", "x=Null();x", "x++;x=1"};
  Class<?> type=Class.forName(args[0]+".integration.data.script.Transform");
  for(String s:cases){Object t=type.newInstance();Object r=type.getMethod("getRpnTokenVector",String.class).invoke(t,"<trans>"+s+"</trans>");Object e=type.getField("m_err").get(t);System.out.println(s+" => "+(e==null&&r!=null?"VALID":e==null?"INVALID":e.getClass().getMethod("getFormattedErrorMessage").invoke(e)));}
 }
}
