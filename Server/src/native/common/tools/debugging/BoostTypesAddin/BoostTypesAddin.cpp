                          

#include <windows.h>
#include <stdio.h>
#include <string.h>
#include <tchar.h>

#include <string>

#ifdef max
#undef max
#endif

#ifdef min
#undef min
#endif

#include <boost/date_time/posix_time/posix_time.hpp>
#include <boost/date_time/gregorian/gregorian.hpp>
#include <boost/date_time/local_time_adjustor.hpp>
#include <boost/date_time/c_local_time_adjustor.hpp>

#include "../../include/custview.h"
#include "../../include/AddinHelper.h"	                           

#include "BoostTypesAddin.h"

using namespace std;

namespace {
HRESULT FormatGregorianDateTime(boost::gregorian::date& date, char *pResult, size_t max )
{
	string sDate(to_simple_string(date));

	if( sDate.length() > max )
		return E_FAIL;                                

	strcpy(pResult, sDate.c_str());
	return S_OK;
}

HRESULT FormatPosixTime(boost::posix_time::ptime& t, char *pResult, size_t max )
{
	string sDate(to_simple_string(t));

	if( sDate.length() > max )
		return E_FAIL;                                

	strcpy(pResult, sDate.c_str());
	return S_OK;
}

HRESULT FormatPosixTimeDuration(boost::posix_time::time_duration& x, char *pResult, size_t max )
{
	string sDate(to_simple_string(x));

	if( sDate.length() > max )
		return E_FAIL;                                

	strcpy(pResult, sDate.c_str());
	return S_OK;
}

}                       

ADDIN_API HRESULT WINAPI AddIn_GregorianTime( DWORD dwAddress, DEBUGHELPER *pHelper, int nBase, BOOL bUniStrings, char *pResult, size_t max, DWORD reserved )
{
	boost::gregorian::date date;
	if( FAILED(GetDataFromDebugger<boost::gregorian::date>(date, pHelper)) )
		return E_FAIL;
	return FormatGregorianDateTime(date, pResult, max);
}

ADDIN_API HRESULT WINAPI AddIn_PosixTime( DWORD dwAddress, DEBUGHELPER *pHelper, int nBase, BOOL bUniStrings, char *pResult, size_t max, DWORD reserved )
{
	boost::posix_time::ptime t;

	if( FAILED(GetDataFromDebugger<boost::posix_time::ptime>(t, pHelper)) )
		return E_FAIL;

	return FormatPosixTime(t, pResult, max);
}

ADDIN_API HRESULT WINAPI AddIn_PosixTimeDuration( DWORD dwAddress, DEBUGHELPER *pHelper, int nBase, BOOL bUniStrings, char *pResult, size_t max, DWORD reserved )
{
	boost::posix_time::time_duration x;

	if( FAILED(GetDataFromDebugger<boost::posix_time::time_duration>(x, pHelper)) )
		return E_FAIL;

	return FormatPosixTimeDuration( x, pResult, max );
}
