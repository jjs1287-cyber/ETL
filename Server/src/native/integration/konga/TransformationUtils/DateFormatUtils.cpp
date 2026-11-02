/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#ifdef WIN32
	#include <windows.h>
#endif
#include <sqlext.h>		           

#include <cassert>

#include <konga/Utils/date.h>
#include <konga/Utils/KString.h>

#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/DataElement/DataElement.h>

#include "DateFormatUtils.h"

using namespace konga;
using std::string;


string konga::FormatDate(const konga::CrDateTime& t, const char* sFormat0){
	assert(sFormat0);
	if(stricmp(sFormat0,"GeneralDate")==0){
		return t.toString("%m/%d/%Y %I:%M:%S %p");
	} else if(stricmp(sFormat0,"LongDate")==0){
		return t.toString("%A, %B %d, %Y");
	} else if(stricmp(sFormat0,"MediumDate")==0){
		return t.toString("%d-%b-%y");
	} else if(stricmp(sFormat0,"ShortDate")==0){
		return t.toString("%m/%d/%y");
	} else if(konga::valid_strftime_format(sFormat0)){
		return t.toString(sFormat0);
	} else {
		int y=0,m=0,d=0, h=0, mi=0,s=0;
		y=t.GetYear();
		m=t.GetMonth();
		d=t.GetDay();
		h=t.GetHour();
		mi=t.GetMinute();
		s=t.GetSecond();

		KString flags, sFormat=parseDateFormat(sFormat0, flags), output_data_str(sFormat);
		int l=(int)strlen(sFormat);
		char*str=output_data_str.GetBuffer(l+1);

		bool AP=strstrWithDateFormatFlags(sFormat, "AP", flags)!=0, PM=h > 11;
		if(AP && PM)h -= 12;
		if(h == 0 && AP) h = 12;
		
		if(strstrWithDateFormatFlags(sFormat, "ddd", flags)){
			d=t.GetDayOfYear();
		}
		
		SetNumb(sFormat, 'y', str, flags, y);
		SetNumb(sFormat, 'm', str, flags, m);
		SetNumb(sFormat, 'd', str, flags, d);
		SetNumb(sFormat, 'H', str, flags, h);
		SetNumb(sFormat, 'M', str, flags, mi);
		SetNumb(sFormat, 'S', str, flags, s);

		str[l]=0;
		output_data_str.ReleaseBuffer();
		                                                
		if (AP)       
		{
			output_data_str.Replace("AP", PM? "PM" : "AM");
		}
		return (const char*)fixup_output_string(output_data_str, sFormat, flags);
	}
}

string konga::GetDateString(const char* driverNameStr, const char* inputDateStr, int iSQLColType)
{
	string stDriverName( driverNameStr!=0 ? driverNameStr : "" );
	ToUpper(stDriverName);

	std::string errMsg;
	DataElement de;
	konga::CrDateTime oleDate=GetDate(inputDateStr, errMsg);
	bool bDateOnly=oleDate.GetHour()==0 && oleDate.GetMinute()==0 && oleDate.GetSecond()==0;

	KString sOutputDate;
	if ( konga::DriverIsOracle(stDriverName) )
	{
		std::string sFormat(bDateOnly?"%Y-%m-%d":"%Y-%m-%d %H:%M:%S");
		std::string sDateString=oleDate.toString(sFormat.c_str());
		sOutputDate = KString("to_date('")+sDateString.c_str()+
			(bDateOnly? "', 'YYYY-MM-DD')":"', 'YYYY-MM-DD HH24:MI:SS')");
	}
    else
	{
        if (SQL_DATE == iSQLColType)
            sOutputDate.Format("'%04d-%02d-%02d'", oleDate.GetYear(), oleDate.GetMonth(), oleDate.GetDay());
        else if (SQL_TIME == iSQLColType)
			sOutputDate.Format("'%02d:%02d:%02d'", oleDate.GetHour(), oleDate.GetMinute(), oleDate.GetSecond());
        else if (SQL_TIMESTAMP == iSQLColType)
			sOutputDate.Format("'%04d-%02d-%02d %02d:%02d:%02d'", oleDate.GetYear(), oleDate.GetMonth(), oleDate.GetDay(), oleDate.GetHour(), oleDate.GetMinute(), oleDate.GetSecond());
	}
	return string((const char*)sOutputDate);
}
