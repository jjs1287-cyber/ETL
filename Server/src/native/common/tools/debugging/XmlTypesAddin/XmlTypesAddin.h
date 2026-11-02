
#define ADDIN_API __declspec(dllexport)

extern "C" ADDIN_API HRESULT WINAPI AddIn_XmlNode( DWORD dwAddress, DEBUGHELPER *pHelper, int nBase, BOOL bUniStrings, char *pResult, size_t max, DWORD reserved );
