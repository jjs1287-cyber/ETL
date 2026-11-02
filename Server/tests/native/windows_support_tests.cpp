#include <konga/WinUtils/WinStringUtils.h>
#include <iostream>
#include <string>
int main() {
 static_assert(sizeof(void*)==8,"ETLForge requires x64");
 for(const std::string input : {std::string("ETLForge x64"),std::string("")}) {
  BSTR converted=nullptr;
  if(konga::AnsiToBstr(input,converted)!=0 || converted==nullptr)return 1;
  char* restored=nullptr;
  const long status=konga::BstrToAnsi(converted,restored);
  const bool matches=status==0 && restored && input==restored;
  delete[] restored;SysFreeString(converted);
  if(!matches)return 2;
 }
 std::cout << "PASS: Windows x64 BSTR conversion and allocation cleanup\n";
}
