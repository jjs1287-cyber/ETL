/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#ifdef WIN32
	                      
	                    
	#include <windows.h>
#endif         

#include <string>

#include <string.h>
#include <ctype.h>	            

#include <sqlext.h>		           
#include <sqltypes.h>	          

#include <boost/scoped_array.hpp>

#include <konga/Utils/CrException.h>

#include "date.h"
#include "PortDefs.h"
#include "StringUtils.h"

using namespace std;

namespace konga
{
const char* monthsAbr[]=
	{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec",0};
const char* months[]=
	{"January","February","March","April","May","June","July","August","September",
		"October","November","December",0};
const char* daysAbr[]=
	{"Sun","Mon","Tues","Wed","Thur","Fri","Sat",0};	
const char* days[]=
	{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday",0};


int findYear(const int y)
{
	if(y<100)return y+1900;
	return y;
}

int findMonth(const char* month) {
	int i = inArray(monthsAbr, month);
	if ( i != -1 )
		return i+1;

	i = inArray(months, month);
	if ( i == -1 )
		return 0;
	return i + 1;
}

KString parseDateFormat(const char*format0, KString& sFlags){
	sFlags="";
	bool bInsideQuote=false;
	int j=0,n=strlen(format0);
	boost::scoped_array<char> format(new char[n+1]), flags(new char[n+1]);
	for(int i=0;i<n;i++){
		char c=format0[i];
		if(c=='"'){
			bInsideQuote=true;
			for(i++;i<n;i++){
				c=format0[i];
				if(c=='"'){
					if(format0[i+1]!='"'){
						bInsideQuote=false;
						break;
					}
					else {
						i++;
					}
				}
				flags[j]='"';
				format[j++]=c;
			}
		}
		else {
			flags[j]=' ';
			format[j++]=c;
		}
	}
	flags[j]=0;
	format[j]=0;
	sFlags=flags.get();
	return KString(format.get());
}

const char* strstrWithDateFormatFlags(const char*form_str, const char*pattern, const char*flags)
{
	size_t i,l=strlen(pattern);
	const char* s;

	for(size_t k=0; (s=strstr(&form_str[k], pattern))!=0; k++){
		k=s-form_str;
		for(i=0; i<l; i++){
			if(flags[i+k]!=' ')break;
		}
		if(i==l)return s;
	}
	return 0;
}

int findLastCharWithDateFormatFlags(const char*form_str, char c0, const char*flags)
{
	char c;
	int i0=-1;
	for(int i=0; (c=form_str[i])!=0; i++){
		if(c==c0 && flags[i]==' ')i0=i;
	}
	return i0;
}

int GetNumb(const char*input_data_str,const char c,const char*input_form_str, const char*flags)
{
	if ( input_data_str == 0 || input_form_str == 0 )
		return 0;

	const size_t inputLen = strlen(input_data_str);

	int i = findLastCharWithDateFormatFlags(input_form_str,c,flags);
	if( i < 0 ) return 0;

	unsigned int n = 0;
	if( c=='m'){
		const char* s = strstrWithDateFormatFlags(input_form_str,"mmm", flags);
		if(s!=0){
			i=s-input_form_str;
			if(i<inputLen){
				for(n=0;n<12;n++){
					if(strnicmp(monthsAbr[n],&input_data_str[i],3)==0)break;
				}
				return n+1;
			}
			return 0;
		}
	}

	char ch=input_form_str[i];
	while(i>0 && input_form_str[i-1]==c&&flags[i-1]==' ')i--;
	if(i>0&&(ch=='m'||ch=='d'||ch=='H'||ch=='M'||ch=='S')&&input_form_str[i-1]=='?')i--;
	if(i>=2&&strnicmp(&input_form_str[i-2],"??yy",4)==0)i-=2;
	for(n=0;i<inputLen && (input_form_str[i]==c||input_form_str[i]=='?');i++){
		if( input_data_str[i]==' '){
		} else {
			if(!isdigit(input_data_str[i]))break;
			n=n*10+(input_data_str[i]-'0');
		}
	}
	return n;
}

void SetNumb(const char*output_form_str, const char c, char*output_data_str, const char*flags, int n)
{
	int i = findLastCharWithDateFormatFlags(output_form_str, c, flags);
	if( i < 0 ) return;
	if( c=='m'){
		const char* s = strstrWithDateFormatFlags(output_form_str,"mmm", flags);
		if(s!=0){
			i = s-output_form_str;
			if(i < strlen(output_form_str)){
				strncpy(&output_data_str[i],monthsAbr[n-1],3);
			}
			return;
		}
	}
	for(;i>=0&&(output_form_str[i]==c||output_form_str[i]=='?');i--){
		output_data_str[i]='0'+n-(n/10)*10;
		n=n/10;
	}
}

KString fixup_input_string(const char*data_str,const char*form_str, const char*flags)
{
	KString str;
	int i,j,k=0;
	char c,c1,c2;
	for(i=0,j=0;(c=form_str[i]);i++){
		if(flags[i]!=' '){
			c2=0;
			continue;
		}
		c1=form_str[i+1];
		if(c1!=0){
			c2=form_str[i+2];
		} else {
			c2=0;
		}
		if(c=='?'&&(c1=='d'||c1=='m'||c1=='H'||c1=='S'||c1=='M')&&c2!=c1){
			if(isdigit(data_str[i-j])&&isdigit(data_str[i-j+1])==false){
				if(i>k)str+=KString(&data_str[k-j],i-k);
				str+="0";
				j++;
				k=i+1;
			}
		}
		else if(c=='?'&&c1=='?'&&c2=='y'&&form_str[i+3]=='y'){
			char d1,d2;
			if(isdigit(d1=data_str[i-j])&&isdigit(d2=data_str[i-j+1])
				&&isdigit(data_str[i-j+2])==false){
				if(i>k)str+=KString(&data_str[k-j],i-k);
				int y=(d1-'0')*10+d2-'0';
				if(y>70)str+="19";
				else str+="20";
				j+=2;
				k=i+2;
			}
		}
	}
	str+=KString(&data_str[k-j],i-k);
	return str;
}

KString fixup_output_string(const char*data_str, const char*form_str, const char*flags)
{
	KString str;
	int i,k=0;
	char c,c1;
	for(i=0;(c=form_str[i]);i++){
		if(flags[i]!=' ')continue;
		if(c=='?'&&((c1=form_str[i+1])=='d'||c1=='m')&&form_str[i+2]!=c1){
			if(data_str[i]=='0'&&isdigit(data_str[i+1])){
				if(i>k)str+=KString(&data_str[k],i-k);
				k=i+1;
			}
		}
	}
	str+=KString(&data_str[k],i-k);
	return str;
}



int CheckDateError(int year,int month,int day, bool bCheckValidYear, std::string&stErrMsg)
{
	if(year!=-1||month!=-1||day!=-1){
		if(bCheckValidYear){
			if(year<100||year>9999){
				stErrMsg="YEAR_OUT_OF_RANGE";
				return 220;
			}
		}
		if(month<1||month>12){
			stErrMsg="MONTH_OUT_OF_RANGE";
			return 221;
		}
		stErrMsg="";
		switch(month){
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			if(day<1||day>31){
				stErrMsg="DAY_OUT_OF_RANGE";
				return 222;
			}
			break;
		case 2:
			if((year%4)==0&&((year%100)!=0||(year%400)==0)){
				if(day<1||day>29){
					stErrMsg="DAY_OUT_OF_RANGE";
					return 222;
				}
			} else {
				if(day<1||day>28){
					stErrMsg="DAY_OUT_OF_RANGE";
					return 222;
				}
			}
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			if(day<1||day>30){
				stErrMsg="DAY_OUT_OF_RANGE";
				return 222;
			}
			break;
		default:
			stErrMsg="MONTH_OUT_OF_RANGE";
			return 221;
		}
	}
	return 0;
}

std::string CVTDate(const CrDateTime&t,const char*output_form_str0)
{
	KString output_data_str;
	if(stricmp(output_form_str0,"GeneralDate")==0){
		if(t.GetHour() || t.GetMinute() || t.GetSecond())
			output_data_str=t.toString("%m/%d/%Y %I:%M:%S %p").c_str();
		else
			output_data_str=t.toString("%m/%d/%y").c_str();
	} else if(stricmp(output_form_str0,"LongDate")==0){
		output_data_str=t.toString("%A, %B %d, %Y").c_str();
	} else if(stricmp(output_form_str0,"MediumDate")==0){
		output_data_str=t.toString("%d-%b-%y").c_str();
	} else if(stricmp(output_form_str0,"ShortDate")==0){
		output_data_str=t.toString("%m/%d/%y").c_str();
	} else if(valid_strftime_format(output_form_str0)){
		output_data_str=t.toString(output_form_str0).c_str();
	} else {
		int y=0,m=0,d=0, h=0, mi=0,s=0;

		y=t.GetYear();
		m=t.GetMonth();
		d=t.GetDay();
		h=t.GetHour();
		mi=t.GetMinute();
		s=t.GetSecond();

		KString flags, output_form_str=parseDateFormat(output_form_str0, flags);
		if(strstrWithDateFormatFlags(output_form_str, "ddd", flags)){
			d=MonthDay2Day(d,m,y);
			m=0;
		}
		output_data_str=output_form_str;
		size_t l=strlen(output_form_str);
		char*str=output_data_str.GetBuffer(static_cast<int>(l)+1);

		                               
		bool AP=strstrWithDateFormatFlags(output_form_str, "AP", flags)!=0,PM=h>11;
		if(AP&&PM)h-=12;
		if(h==0&&AP)h=12;
		                               
		
		SetNumb(output_form_str, 'y', str,flags, y);
		SetNumb(output_form_str, 'm', str,flags, m);
		SetNumb(output_form_str, 'd', str,flags, d);
		SetNumb(output_form_str, 'H', str,flags, h);
		SetNumb(output_form_str, 'M', str,flags, mi);
		SetNumb(output_form_str, 'S', str,flags, s);

		str[l]=0;
		output_data_str.ReleaseBuffer();
		                                                
		if (AP)       
		{
			output_data_str.Replace("AP", PM? "PM" : "AM");
		}
		return std::string((const char*)fixup_output_string(output_data_str, output_form_str, flags));
	}
	return std::string((const char*)output_data_str);	
}

std::string CVTDate(const KString&input_data_str0,const KString&input_form_str0,const KString&output_form_str0, std::string&errMsg)
{
	int y=0,m=0,d=0, h=0, mi=0, s=0;
	if(valid_strftime_format((const char*)input_form_str0)){
		string tz;
		ReadDate_From_strftime_format((const char*)input_data_str0,(const char*)input_form_str0,y,m,d,h,mi,s,tz);
	}
	else {
		KString flags, input_form_str=parseDateFormat(input_form_str0, flags);
		if (input_data_str0.IsEmpty())return "";

		KString input_data_str=fixup_input_string(input_data_str0, input_form_str, flags);
		
		int bAPFlag = AP_NOTPRESENT;

		y=GetNumb(input_data_str,'y', input_form_str, flags);
		d=GetNumb(input_data_str,'d', input_form_str, flags);
		h=GetNumb(input_data_str,'H', input_form_str, flags);
		s=GetNumb(input_data_str,'S', input_form_str, flags);
		mi=GetNumb(input_data_str,'M', input_form_str, flags);


		                                                             
		if (strstrWithDateFormatFlags(input_form_str, "AP", flags))
		{
			                                                    
			if (strstrWithDateFormatFlags(input_data_str0, "AM", flags) || strstrWithDateFormatFlags(input_data_str0, "am", flags))
				bAPFlag = AM_PRESENT;
			else if (strstrWithDateFormatFlags(input_data_str0, "PM", flags) || strstrWithDateFormatFlags(input_data_str0, "pm", flags))
				bAPFlag = PM_PRESENT;
			                                       
			if(h==12)h=0;
			if(bAPFlag)h+=12;
			h%=24;
			                                 
		}

		if(strstrWithDateFormatFlags(input_form_str, "ddd", flags)){
			Date2MonthDay(d,m,y);
		} else {
			m=GetNumb(input_data_str,'m', input_form_str, flags);
		}
		if(y<100){
			if(y>70)y+=1900;
			else y+=2000;
		}
		CheckDateError(y,m,d,strstrWithDateFormatFlags(input_form_str,"yyyy", flags)==0,errMsg);
		if(errMsg.empty()){
			if(h<0||h>=24){
				errMsg="HOUR_OUT_OF_RANGE";
			}
			if(mi<0||mi>=60){
				errMsg="MINUTE_OUT_OF_RANGE";
			}
			if(s<0||s>=60){
				errMsg="SECOND_OUT_OF_RANGE";
			}
		}
		if(!errMsg.empty())return "";
	}

	CrDateTime t=CrDateTime(y,m,d,h,mi,s);        

	KString output_data_str;
	if(stricmp(output_form_str0,"GeneralDate")==0){
		output_data_str=t.toString("%m/%d/%Y %I:%M:%S %p").c_str();
	} else if(stricmp(output_form_str0,"LongDate")==0){
		output_data_str=t.toString("%A, %B %d, %Y").c_str();
	} else if(stricmp(output_form_str0,"MediumDate")==0){
		output_data_str=t.toString("%d-%b-%y").c_str();
	} else if(stricmp(output_form_str0,"ShortDate")==0){
		output_data_str=t.toString("%m/%d/%y").c_str();
	} else if(valid_strftime_format(output_form_str0)){
		output_data_str=t.toString((const char*)output_form_str0).c_str();
	} else {
		KString flags, output_form_str=parseDateFormat(output_form_str0, flags);
		output_data_str=output_form_str;
		size_t l=output_form_str.GetLength();
		char*str=output_data_str.GetBuffer(l+1);

		if(strstrWithDateFormatFlags(output_form_str,"ddd",flags)){
			d=MonthDay2Day(d,m,y);
			m=0;
		}

		bool AP=strstrWithDateFormatFlags(output_form_str, "AP", flags)!=0, PM=h>11;
		if(AP&&PM)h-=12;
		if(h==0&&AP)h=12;
		
		SetNumb(output_form_str, 'y', str, flags, y);
		SetNumb(output_form_str, 'm', str, flags, m);
		SetNumb(output_form_str, 'd', str, flags, d);
		SetNumb(output_form_str, 'H', str, flags, h);
		SetNumb(output_form_str, 'M', str, flags, mi);
		SetNumb(output_form_str, 'S', str, flags, s);

		str[l]=0;
		output_data_str.ReleaseBuffer();
		                                                
		if (AP)       
		{
			output_data_str.Replace("AP", PM? "PM" : "AM");
		}
		output_data_str=fixup_output_string(output_data_str, output_form_str, flags);
	}
	return std::string((const char*)output_data_str);
}

std::string ToOracleDate(const char* date_str, bool& bHasSeconds, std::string&stErrMsg)
{
	if(strlen(date_str) > 10)
	{
		                                     
		bHasSeconds = true;
		return CVTDate(date_str,"??yy-?m-?d HH:MM:SS","?d-mmm-??yy HH:MM:SS", stErrMsg);
	}
	else
	{
		bHasSeconds = false;
		return CVTDate(date_str,"??yy-?m-?d","?d-mmm-??yy",stErrMsg);
	}
}


void DMY(KString&s,int&year,int&month,int&day,int&hour,
		int&min,int&sec,int&day_of_week,int&AMPM,bool col_case, bool& bCheckValidYear, std::string&stErrMsg)
{
	stErrMsg="";
	int i;
	if(isdigit(s.GetAt(0))){
		i=atoi((const char*)s);
		if(i>=0){
			if(col_case||(month!=-1&&day!=-1&&year!=-1)){
				if(hour==-1){
					hour=i;
				} else if(min==-1){
					min=i;
				} else if (sec==-1) {
					sec=i;
				}
				else {
					                                                            
				}
			} else {
				if(strlen(s)==4){
					year=i;
					bCheckValidYear=false;
				} else {
					if(month==-1)
						month=i;
					else if(day==-1)
						day=i;
					else 
						year=findYear(i);
					if (month <= 0 || month > 12)
					{
						year=findYear(i);
						month = -1;
					}
				}
			}
		}
	} else {
		if((i=inArray(months,s))>=0||
			(i=inArray(monthsAbr,s))>=0){
			month=i+1;
		} else if((i=inArray(days,s))>=0||
			(i=inArray(daysAbr,s))>=0){
			day_of_week=i+1;
		} else if(stricmp(s,"AM")==0||stricmp(s,"a.m.")==0){
			AMPM=1;
		} else if(stricmp(s,"PM")==0||stricmp(s,"p.m.")==0){
			AMPM=2;
		} else {
			stErrMsg="TRANSFORM_ILLEGAL_DATE_INPUT";
		}
	}
}

bool isOracleDateStr(const char*str, int len){
	return (len>8) && isdigit(str[0]) && isdigit(str[1]) && str[2]=='-' &&
		isalpha(str[3]) && isalpha(str[4]) && isalpha(str[5]) && str[6]=='-' &&
		isdigit(str[7]) && isdigit(str[8]);
}

bool isTimeStr(const char*str, int len){
	return (len>7) && isdigit(str[0]) && isdigit(str[1]) && str[2]==':' &&
		isdigit(str[3]) && isdigit(str[4]) && str[5]==':' &&
		isdigit(str[6]) && isdigit(str[7]);
}

int ReadDate(const char*str, int&year, int&month, int&day, int&hour,
			  int&min,int&sec, int&day_of_week, int& tz_shift_minutes, bool& bCheckValidYear, std::string&stErrMsg)
{
	if(str==0){
		stErrMsg="Date string is null!";
		return -1;
	}
	stErrMsg="";
	year=month=day=hour=min=sec=day_of_week=-1;
	tz_shift_minutes=0;
	int ampm=0;
	int i=0,j=-1,k=0,l=static_cast<int>(strlen(str));
	if(l>=18 && isOracleDateStr(str, l) && isTimeStr(&str[10],l-10)){
		char sMonth[4];
		if(sscanf(str, "%02d-%3s-%02d %02d:%02d:%02d", &day, sMonth, &year, &hour, &min, &sec)==6){
			for(int mon=0; mon<12; ++mon){
				if(strnicmp(monthsAbr[mon], sMonth,3)==0){
					month=mon+1;
				}
			}
			if(year>=70)year+=1900;
			else year+=2000;
		}
		else {
			throw konga::CrException(std::string("Read DateTime failed \"")+str+"\"");
		}
	}
	else if(isTimeStr(str,l)){
		if(sscanf(str, "%02d:%02d:%02d", &hour, &min, &sec)<3){
			throw konga::CrException(std::string("Read Time failed \"")+str+"\"");
		}
	}
	else if(isOracleDateStr(str, l)){
		char sMonth[4];
		if(sscanf(str, "%02d-%3s-%02d", &day, sMonth, &year)==3){
			for(int mon=0; mon<12; ++mon){
				if(strnicmp(monthsAbr[mon], sMonth,3)==0){
					month=mon+1;
				}
			}
			if(year>=70)year+=1900;
			else year+=2000;
		}
		else {
			throw konga::CrException(std::string("Read Date failed \"")+str+"\"");
		}
	}
	else {
		bool col_case=false;
		int n_minus_sign=0;
		KString tmp;
		while(i<l) {
			if(isalpha(str[i])){
				for(j=i+1;j<l;j++){
					if(!isalpha(str[j]))break;
				}
			}
			else if(isdigit(str[i])){
				for(j=i+1;j<l;j++){
					if(!isdigit(str[j]))break;
				}
			}
			if(j<0){
				stErrMsg="Invalid date string \""+std::string(str)+"\"";
				return -1;
			}

			if(j>i){                                                      
				if(j<l){
					char c=str[j];
					if(c==':'||c=='/')col_case=(c==':');
					if(c=='-')n_minus_sign++;
	                             
				}
				tmp=KString(&str[i],j-i);
				                                                      
				                             
				                                    
				                       
				if(k==2&&year==-1){
					if(strlen(tmp)==2&&col_case==false){
						int ii=atoi((const char*)tmp);
						if(ii>38)tmp="19"+tmp;
						else tmp="20"+tmp;
					}
				}

				DMY(tmp,year,month,day,hour,min,sec,day_of_week,ampm,col_case,bCheckValidYear,stErrMsg);
				if(!stErrMsg.empty())return 220;
			}
			else {                 
				j=i;
			}
			i=j+1;
			k=k+1;
			if((str[j]=='-'&&(n_minus_sign==3||col_case))||str[j]=='+'){                           
				tmp=KString(&str[i],5);
				int dh,dm;
				sscanf((const char*)tmp,"%2d:%2d",&dh,&dm);
				tz_shift_minutes=dh*60+dm;
				if(str[j]=='-')tz_shift_minutes=-tz_shift_minutes;
				i+=5;
				n_minus_sign=0;
			}
		}
	}
	if(ampm!=0){
		if(ampm==2&&hour<12&&hour>=0)hour+=12;
		if(ampm==1&&hour==12)hour=0;
	}
	return CheckDateError(year,month,day,bCheckValidYear,stErrMsg);
}

int ReadDate_From_strftime_format(const char*str,const char*format,int&year,int&month,int&day,int&hour,int&min,int&sec,std::string&tz)
{
	year=0;
	month=0;
	day=0;
	hour=0;
	min=0;
	sec=0;
	tz="";
	const char* strftime_format_char_set="aAbBcdHIjmMpSUwWxXyYzZ%#";
	int i=0, m=strlen(str), k=0, n=strlen(format), c, ddd=0;
	bool am=true;

	                                          
	char*field=new char[m];
	boost::scoped_array<char> adopt_field(field);	                                            

	while(k<n&&i<m){
		int n1=findk(&format[k],'%');
		k+=n1;
		if(format[k++]=='%'&&k<n){
			i+=n1;
			int n2=findk(&str[i],format[k+1]);
			strncpy(field,&str[i],n2);
			field[n2]=0;
			if(strftime_format_char_set[findk(strftime_format_char_set,(c=format[k]))]!=0){
	once_more:	switch(c){
					case 'a': break;                            
					case 'A': break;                     
					case 'b':                          
					case 'B': month=findMonth(field); break;                   
					case 'c': break;                                                       
					case 'd': sscanf(field,"%d",&day); break;
					case 'H': sscanf(field,"%d",&hour); break;
					case 'I': sscanf(field,"%d",&hour); break;
					case 'j': sscanf(field,"%d",&ddd); break;
					case 'm': sscanf(field,"%d",&month); break;
					case 'M': sscanf(field,"%d",&min); break;
					case 'p': am=field[0]=='A'||field[0]=='a'; break;
					case 'S': sscanf(field,"%d",&sec); break;
					case 'U': break;                                                                              
					case 'w': break;                                                  
					case 'W': break;                                                                              
					case 'x': break;                                          
					case 'y': sscanf(field,"%d",&year); year+=2000; break;
					case 'Y': sscanf(field,"%d",&year); break;
					case 'z':
					case 'Z': 
						{
							tz=field;
							break;
						}
					case '%': break;
					case '#':
						k++;
						c=str[k];
						goto once_more;
					default:
						break;
				}
			}
			if(!am&&hour<12)hour+=12;
			i+=n2;
		}
		k++;
	}
	return 0;
}



CrDateTime GetDate(const char*str,std::string&stErrMsg){
	stErrMsg="";
	if(str==0||str[0]==0){
		stErrMsg="TRANSFORM_INPUT_EMPTY";
		return CrDateTime::GetCurrentTime();
	}
	int year,month,day,hour,min,sec,day_of_week,tz_shift_minutes;
	bool bCheckValidYear=true;
	ReadDate(str,year,month,day,hour,min,sec,day_of_week,tz_shift_minutes,bCheckValidYear,stErrMsg);
	if(!stErrMsg.empty())return CrDateTime::GetCurrentTime();	
	CrDateTime t=CrDateTime::GetCurrentTime();
	if(year==-1&&hour!=-1&&min!=-1)year=t.GetYear();
	if(month==-1&&hour!=-1&&min!=-1)month=t.GetMonth();
	if(day==-1&&hour!=-1&&min!=-1)day=t.GetDay();
	if(hour==-1)hour=0;
	if(min==-1)min=0;
	if(sec==-1)sec=0;
	CheckDateError(year,month,day,bCheckValidYear,stErrMsg);
	if(stErrMsg.empty()){
		if(min<0||min>=60)stErrMsg="MINUTES_OUT_OF_RANGE";
		if(hour<0||hour>=24)stErrMsg="HOUR_OUT_OF_RANGE";
		if(sec<0||sec>=60)stErrMsg="SECOND_OUT_OF_RANGE";
	}
	if(stErrMsg.empty()){
		CrDateTime t=CrDateTime(year,month,day,hour,min,sec,0);
		if(tz_shift_minutes!=0){
			char c=tz_shift_minutes>0?'+':'-';
			tz_shift_minutes=abs(tz_shift_minutes);
			int dh=tz_shift_minutes/60,dm=tz_shift_minutes%60;
			char buf[7];
			sprintf(buf,"%c%02d:%02d",c,dh,dm);
			t.setTZ(buf);
		}
		return t;
	}
	return CrDateTime::GetCurrentTime();
}

#ifdef DS_USE_CTIME
CTime CrDateTimeToCTime(const CrDateTime& oleDate, std::string&stErrMsg)
{
	if (oleDate.GetStatus() != CrDateTime::valid) {
		stErrMsg = "TRANSFORM_ILLEGAL_DATE_INPUT";
		return CrDateTime::GetCurrentTime();
	}
	return CTime(oleDate.GetYear(), oleDate.GetMonth(), 
		oleDate.GetDay(), oleDate.GetHour(), 
		oleDate.GetMinute(), oleDate.GetSecond());
}

CrDateTime CTimeToCrDateTime(const CTime& objTime, std::string&stErrMsg)
{
	CrDateTime retTime;
	if (retTime.SetDateTime(objTime.GetYear(), 
		objTime.GetMonth(), 
		objTime.GetDay(), 
		objTime.GetHour(), 
		objTime.GetMinute(), 
		objTime.GetSecond()) != 0)
	{
		stErrMsg = "TRANFORM_ILLEGAL_DATE_INPUT";
		return CrDateTime::GetCurrentTime();
	}
	return retTime;
}
#endif
}
