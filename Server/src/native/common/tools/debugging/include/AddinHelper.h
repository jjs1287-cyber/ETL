#ifndef ADDINHELPER_H
#define ADDINHELPER_H

#include <windows.h>
#include "custview.h"	              

template<typename T>
HRESULT GetDataFromDebugger(T& t, DEBUGHELPER *pHelper) {
	DWORD nGot;

	if (pHelper->ReadDebuggeeMemoryEx(pHelper, pHelper->GetRealAddress(pHelper), sizeof(T), &t, &nGot)!=S_OK)
		return E_FAIL;

	if (nGot!=sizeof(t))
		return E_FAIL;

	return S_OK;
}

#endif                 
