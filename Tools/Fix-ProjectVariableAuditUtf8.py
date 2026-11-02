from pathlib import Path
import shutil
p=Path('C:/ETL/Server/src/native/audit/AuditDatabase.cpp')
b=Path('C:/ETL/Archives/project-variables-20261029/Server/src/native/audit/AuditDatabase.cpp');b.parent.mkdir(parents=True,exist_ok=True);shutil.copy2(p,b)
s=p.read_text()
s=s.replace('void check(SQLRETURN result) {','''std::wstring wide(const std::string& value) {
    if(value.empty())return {};
    if(value.size()>static_cast<std::size_t>(INT_MAX))throw std::runtime_error("Audit text is too large");
    int length=MultiByteToWideChar(CP_UTF8,MB_ERR_INVALID_CHARS,value.data(),static_cast<int>(value.size()),nullptr,0);
    if(!length)throw std::runtime_error("Invalid UTF-8 audit text");
    std::wstring result(length,L'\\0');
    if(MultiByteToWideChar(CP_UTF8,MB_ERR_INVALID_CHARS,value.data(),static_cast<int>(value.size()),&result[0],length)!=length)throw std::runtime_error("Cannot decode audit UTF-8 text");
    return result;
}
std::string utf8(const std::wstring& value) {
    if(value.empty())return {};
    if(value.size()>static_cast<std::size_t>(INT_MAX))throw std::runtime_error("Audit text is too large");
    int length=WideCharToMultiByte(CP_UTF8,WC_ERR_INVALID_CHARS,value.data(),static_cast<int>(value.size()),nullptr,0,nullptr,nullptr);
    if(!length)throw std::runtime_error("Invalid Unicode from audit database");
    std::string result(length,'\\0');
    if(WideCharToMultiByte(CP_UTF8,WC_ERR_INVALID_CHARS,value.data(),static_cast<int>(value.size()),&result[0],length,nullptr,nullptr)!=length)throw std::runtime_error("Cannot encode audit UTF-8 text");
    return result;
}
void check(SQLRETURN result) {''')
a=s.index('    check(SQLPrepareA(');z=s.index('    SQLRETURN executed =',a)
s=s[:a]+'''    std::wstring sqlWide=wide(statementText);
    check(SQLPrepareW(statement,reinterpret_cast<SQLWCHAR*>(&sqlWide[0]),SQL_NTS));
    std::vector<SQLLEN> lengths(parameters.size());
    std::vector<std::wstring> values(parameters.size());
    for(std::size_t i=0;i<parameters.size();++i) {
        if(parameters[i] && parameters[i]->find('\\0')!=std::string::npos)throw std::runtime_error("Audit database text contains NUL");
        if(parameters[i])values[i]=wide(*parameters[i]);
    }
    for(std::size_t i=0;i<parameters.size();++i) {
        lengths[i]=parameters[i]?static_cast<SQLLEN>(values[i].size()*sizeof(wchar_t)):SQL_NULL_DATA;
        check(SQLBindParameter(statement,static_cast<SQLUSMALLINT>(i+1),SQL_PARAM_INPUT,SQL_C_WCHAR,SQL_WLONGVARCHAR,
            std::max<std::size_t>(values[i].size(),1),0,
            parameters[i]?const_cast<wchar_t*>(values[i].c_str()):nullptr,
            parameters[i]?static_cast<SQLLEN>((values[i].size()+1)*sizeof(wchar_t)):0,&lengths[i]));
    }
'''+s[z:]
s=s.replace('            std::string value;','            std::wstring value;').replace('char buffer[8192] = {};','wchar_t buffer[4096] = {};').replace('SQLGetData(statement,col,SQL_C_CHAR,buffer','SQLGetData(statement,col,SQL_C_WCHAR,buffer').replace('Cell(std::move(value))','Cell(utf8(value))')
p.write_text(s,encoding='utf-8')
print('Audit ODBC boundary now uses strict UTF-8 / UTF-16 conversion with wide bindings.')
