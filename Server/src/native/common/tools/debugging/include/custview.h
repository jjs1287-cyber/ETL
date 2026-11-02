#ifndef ADDIN_CUSTVIEW_H
#define ADDIN_CUSTVIEW_H

typedef struct tagDEBUGHELPER
{
	DWORD dwVersion;
	BOOL (WINAPI *ReadDebuggeeMemory)( struct tagDEBUGHELPER *pThis, DWORD dwAddr, DWORD nWant, VOID* pWhere, DWORD *nGot );
	                                           
	DWORDLONG (WINAPI *GetRealAddress)( struct tagDEBUGHELPER *pThis );
	BOOL (WINAPI *ReadDebuggeeMemoryEx)( struct tagDEBUGHELPER *pThis, DWORDLONG qwAddr, DWORD nWant, VOID* pWhere, DWORD *nGot );
	int (WINAPI *GetProcessorType)( struct tagDEBUGHELPER *pThis );
} DEBUGHELPER;

typedef HRESULT (WINAPI *CUSTOMVIEWER)( DWORD dwAddress, DEBUGHELPER *pHelper, int nBase, BOOL bUniStrings, char *pResult, size_t max, DWORD reserved );

#endif                    
