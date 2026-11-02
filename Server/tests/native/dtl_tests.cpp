#include <dtl/DTL.h>
#include <dtl/date_util.h>
#include <dtl/nullable.h>
#include <cstring>
#include <iostream>
int main(){
 static_assert(sizeof(void*)==8,"x64 required");
 static_assert(sizeof(SQLLEN)==8,"x64 ODBC indicator width required");
 try {
  dtl::DBConnection connection;
  if(connection.IsConnected())return 1;
  TIMESTAMP_STRUCT earlier={2024,2,29,23,59,59,0};
  TIMESTAMP_STRUCT later={2024,3,1,0,0,0,0};
  if(dtl::TSCmp(earlier,later)>=0 || dtl::TSCmp(earlier,earlier)!=0)return 2;
  connection.EnableLog();if(!connection.IsLogEnabled())return 3;
  connection.DisableLog();if(connection.IsLogEnabled())return 4;
  struct Parameters { dtl::nullable<long> count; dtl::nullable<std::string> label; } parameters;
  parameters.count=17;parameters.label.SetNull();
  struct ResultRow { unsigned char guard[128]; } row;
  std::memset(&row,0x5a,sizeof(row));
  dtl::BoundIOs bindings;
  bindings.BindAsBase(parameters);
  bindings[0] >> parameters.count;
  bindings[1] << parameters.label;
  bindings.BindAsBase(row);
  bindings.InsUserValidate();
  if(*bindings[1].GetBytesFetchedPtr()!=SQL_NULL_DATA)return 6;
  bindings[0].SetNull();
  bindings.SelUserValidate();
  if(!parameters.count.IsNull())return 7;
  for(unsigned char value:row.guard)if(value!=0x5a)return 8;
  std::cout<<"PASS: nullable parameter validation uses its bound owner independently of the result row\n";
  std::cout<<"PASS: DTL x64 connection lifecycle, timestamp comparison, statement log state and ODBC indicator ABI\n";
 }catch(const std::exception& error){std::cerr<<error.what()<<"\n";return 5;}
}
