
#define ADDIN_API __declspec(dllexport)

extern "C" ADDIN_API HRESULT WINAPI AddIn_SystemTime( DWORD dwAddress, DEBUGHELPER *pHelper, int nBase, BOOL bUniStrings, char *pResult, size_t max, DWORD reserved );
extern "C" ADDIN_API HRESULT WINAPI AddIn_FileTime( DWORD dwAddress, DEBUGHELPER *pHelper, int nBase, BOOL bUniStrings, char *pResult, size_t max, DWORD reserved );
extern "C" ADDIN_API HRESULT WINAPI AddIn_GregorianTime( DWORD dwAddress, DEBUGHELPER *pHelper, int nBase, BOOL bUniStrings, char *pResult, size_t max, DWORD reserved );
extern "C" ADDIN_API HRESULT WINAPI AddIn_PosixTime( DWORD dwAddress, DEBUGHELPER *pHelper, int nBase, BOOL bUniStrings, char *pResult, size_t max, DWORD reserved );
extern "C" ADDIN_API HRESULT WINAPI AddIn_PosixTimeDuration( DWORD dwAddress, DEBUGHELPER *pHelper, int nBase, BOOL bUniStrings, char *pResult, size_t max, DWORD reserved );

