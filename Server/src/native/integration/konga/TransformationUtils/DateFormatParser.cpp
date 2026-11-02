/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/PortDefs.h>

#include "DateFormatParser.h"

const char* DateFormatParser::monthsAbr[]=
	{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec",0};
const char* DateFormatParser::months[]=
	{"January","February","March","April","May","June","July","August","September",
		"October","November","December",0};
const char* DateFormatParser::daysAbr[]=
	{"Sun","Mon","Tues","Wed","Thur","Fri","Sat",0};	
const char* DateFormatParser::days[]=
	{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday",0};

DateFormatParser::DateFormatParser(const char* szValue, const char* szFormat, int& iErr)
{
	iErr = 0;
	if((szValue==0||szFormat==0) || (strlen(szValue)==0||strlen(szFormat)==0)){
		iErr = -1;
		m_csLastErr = "The value and/or format string is empty";
	}
	if(iErr == 0){
		m_csValue = szValue;
		m_csFormat = szFormat;
		m_csValue.TrimLeft(); m_csValue.TrimRight();
		m_csFormat.TrimLeft(); m_csFormat.TrimRight();
		KString csValueToUse = m_csValue;
		KString csFormatToUse = m_csFormat;
		if(m_csFormat.Find("%") == -1){
			             
			if(stricmp(m_csFormat, "LongDate")==0){
				iErr = ParseLongDateValue(csFormatToUse, csValueToUse);
			}
			if(iErr == 0){
				iErr = CreateCVTTokenList(csFormatToUse);
				if(iErr == 0){
					iErr = ConformCVTFormat(csValueToUse);
				}
			}
		} else {
			                           

		}
	}

	SetErrorMessage(iErr);
}

DateFormatParser::~DateFormatParser() { }

int DateFormatParser::ParseLongDateValue(KString& csFmtRest, KString& csValRest)
{
	                                                                 
	csFmtRest = csValRest = "";
	konga::StringTokenizer strTok(m_csValue, ", ");
	int n = 0, pos = 0;
	while(strTok.NextToken()){
		KString csToken = strTok.token();
		switch(csToken[0]){
		case ',':
		case ' ':
			++pos;
			break;
		default:
			++n;
			if(n>2){
				csFmtRest = "?d, yyyy";
				csValRest = m_csValue.Mid(pos);
				return 0;
			}
			pos += (int)csToken.GetLength();
			if(n==1){
				if(konga::inArray(DateFormatParser::days, csToken)<0){
					return 1;
				}
			} else if(n==2){
				if(konga::inArray(DateFormatParser::months, csToken)<0){
					return 2;
				}
			}
		}
	}
	return 3;
}


int DateFormatParser::CreateCVTTokenList(KString csFmt)
{
	if(stricmp(csFmt, "GeneralDate") == 0){
		csFmt = "?m/?d/yyyy ?H:MM:SS AP";
	} else if(stricmp(csFmt, "MediumDate") == 0){
		csFmt = "dd-mmm-yyyy";
	} else if(stricmp(csFmt, "ShortDate") == 0){
		csFmt = "?m/?d/yy";
	}

	int pos, len = (int)csFmt.GetLength(), n_q = 0;
	bool bYear = false, bMonth = false, bDay = false, bHour = false, bAP = false;
	KString csToken;
	for(pos=0; pos<len; ++pos){
		char c = csFmt[pos];
		switch(c){
		case '?': 
			++n_q;
			if(n_q==2){
				if(pos>(len-3) || bYear)return 4;
				if(!(csFmt[pos+1]=='y' && csFmt[pos+2]=='y'))return 4;
				pos += 2;
				n_q = 0;
				AddAmbiguity(Ambiguity((int)GetNumberOfTokens()));
				AddToken("??yy");
				bYear = true;
			}
			break;
		case 'A':
			if(n_q!=0 || bAP || (pos==(len-1)))return 5;
			if(csFmt[pos+1] != 'P')return 5;
			++pos;
			AddToken("AP");
			bAP = true;
			break;
		case 'H':
			if(bHour)return 6;
			if(n_q==1){
				AddAmbiguity((int)GetNumberOfTokens());
				AddToken("?H");
				n_q = 0;
			} else {
				if(pos<(len-1) && csFmt[pos+1]=='H'){
					AddToken("HH");
					++pos;
				} else return 6;
			}
			bHour = true;
			break;
		case 'M':
		case 'S':
			if(n_q!=0 || pos==(len-1))return 7;
			if(csFmt[pos+1] != c)return 7;
			AddToken(csFmt.Mid(pos, 2));
			++pos;
			break;
		case 'y':
			if(n_q!=0 || bYear || pos==(len-1))return 8;
			if(csFmt[pos+1]=='y'){
				if(pos<(len-2) && csFmt[pos+2]=='y'){
					if(pos>(len-4))return 9;			                                         
					if(csFmt[pos+3]!='y')return 9;		                
					AddToken("yyyy");
					pos += 3;
					bYear = true;
					break;
				}
				AddToken("yy");
				++pos;
				bYear = true;
				break;
			}
			return 8;		             
		case 'm':
		case 'd':
			if((c=='m' && bMonth) || (c=='d' && bDay))return 10;
			if(n_q==1){
				AddAmbiguity(Ambiguity((int)GetNumberOfTokens()));
				csToken.Format("?%c", c);
				AddToken(csToken);
				n_q = 0;
				if(c=='m')bMonth = true;
				else bDay = true;
				break;
			}
			if(pos==(len-1) || csFmt[pos+1]!=c)return 10;
			if(pos<(len-2) && csFmt[pos+2]==c)
				csToken.Format("%c%c%c", c, c, c);
			else
				csToken.Format("%c%c", c, c);
			AddToken(csToken);
			pos += (int)csToken.GetLength() - 1;
			if(c=='m')bMonth = true;
			else bDay = true;
			break;
		default:
			csToken.Format("%c", c);
			AddToken(csToken);
			break;
		}
	}

	return 0;
}


int DateFormatParser::ConformCVTFormat(const KString& csVal)
{
	int pos = 0, len = (int)csVal.GetLength(), i, j, iDigits;
	bool bValid = true;
	KString csTmp;
	for(int n = 0; n < (int)GetNumberOfTokens() && pos<len; ++n)
	{
		KString csToken = m_vTokens[n];
		DateFormatToken fToken = GetFormatToken(csToken);
		switch(fToken){
		case t_error: 
			m_csLastErr = csToken;
			return 11;
		case t_AP:
			csTmp = csVal.Mid(pos, 2);
			if(stricmp(csTmp, "AM")==0 || stricmp(csTmp, "PM")==0){
				SavePosition(pos);
				pos += 2;
			} else {
				if(ResolveAmbiguity(n, pos) != 0)return 12;
			}
			break;
		case t_HH:
		case t_MM:
		case t_SS:
		case t_yy:
		case t_mm:
		case t_dd:
			if(pos==(len-1) || !(isdigit((unsigned char)csVal[pos]) && isdigit((unsigned char)csVal[pos+1]))){
				if(ResolveAmbiguity(n, pos) != 0)return 12;
				break;
			}
			SavePosition(pos);
			pos += 2;
			break;
		case t_mmm:
			csTmp = csVal.Mid(pos, 3);
			if(konga::inArray(DateFormatParser::monthsAbr, csTmp)>1){
				SavePosition(pos);
				pos += 3;
			} else {
				if(ResolveAmbiguity(n, pos)!=0)return 12;
			}
			break;
		case t_ddd:
		case t_yyyy:
			iDigits = (fToken == t_ddd) ? 3 : 4;
			if(pos>(len-iDigits)){
				if(ResolveAmbiguity(n, pos)!=0)return 12;
				break;
			}
			bValid = true;
			for(i=0; i<iDigits && bValid; ++i)
				bValid = isdigit((unsigned char)csVal[pos+i])!=0;
			if(bValid){
				SavePosition(pos);
				pos += iDigits;
			} else {
				if(ResolveAmbiguity(n, pos)!=0)return 12;
			}
			break;
		case t_qqyy:
			                                        
			for(i=0; i < (int)m_vAmbiguities.size() && n!=m_vAmbiguities[i].iIndex; ++i)
				;
			if(i==m_vAmbiguities.size())return 8;
			if(m_vAmbiguities[i].iTried == 0x00){
				                                   
				if(pos<(len-1) && (isdigit((unsigned char)csVal[pos]) && isdigit((unsigned char)csVal[pos+1]))){
					SavePosition(pos);
					pos += 2;
					m_vAmbiguities[i].iTried = 0x01;
				} else {
					if(ResolveAmbiguity(n, pos)!=0)return 12;
				}
				break;
			} else if(m_vAmbiguities[i].iTried == 0x01){
				                                     
				for(j=0; j<4 && bValid; ++j)
					bValid = (pos+j)<len && isdigit((unsigned char)csVal[pos+j]);
				if(bValid){
					SavePosition(pos);
					pos += 4;
					m_vAmbiguities[i].iTried = 0x11;
				} else {
					if(ResolveAmbiguity(n, pos)!=0)return 12;
				}
				break;
			} else return 11;
		case t_qm:
		case t_qd:
		case t_qH:
			                                        
			for(i=0; i < (int)m_vAmbiguities.size() && n!=m_vAmbiguities[i].iIndex; ++i)
				;
			if(i==m_vAmbiguities.size())return 12;
			if(m_vAmbiguities[i].iTried == 0x00){
				                                  
				if(isdigit((unsigned char)csVal[pos])){
					SavePosition(pos);
					++pos;
					m_vAmbiguities[i].iTried = 0x01;
				} else {
					if(ResolveAmbiguity(n, pos)!=0)return 12;
				}
				break;
			} else if(m_vAmbiguities[i].iTried == 0x01){
				                                    
				if(pos<(len-1) && (isdigit((unsigned char)csVal[pos]) && isdigit((unsigned char)csVal[pos+1]))){
					SavePosition(pos);
					pos += 2;
					m_vAmbiguities[i].iTried = 0x11;
				} else {
					if(ResolveAmbiguity(n, pos)!=0)return 12;
				}
				break;
			} else return 12;
		case t_char:
			if(strcmp(csToken, csVal.Mid(pos,1))==0){
				SavePosition(pos);
				++pos;
			} else {
				if(ResolveAmbiguity(n, pos)!=0)return 12;
			}
			break;
		}
		if(n==(GetNumberOfTokens()-1) && (pos<len)){
			if(ResolveAmbiguity(n, pos, true)!=0)return 13;
		}
	}

	if(m_vTokens.size() != m_PositionStack.size())return 12;
	if(pos < len) return 13;

	return 0;
}


int DateFormatParser::ResolveAmbiguity(int& n, int& val_pos, bool bIncludeLast              )
{
	if(GetNumberOfAmbiguities()==0)return 1;

	int index0 = -1;
	if(!bIncludeLast)
		--n;
	for(int k = (int)GetNumberOfAmbiguities()-1; k>=0; --k){
		if(m_vAmbiguities[k].iIndex <= n){
			if(m_vAmbiguities[k].iTried == 0x01){
				index0 = k;
				break;
			} else if(m_vAmbiguities[k].iTried == 0x11){
				if(k == 0)return 2;
				m_vAmbiguities[k].iTried = 0x00;
			} else return 3;
		}
	}
	if(index0 == -1)return 4;
	int iAmb = m_vAmbiguities[index0].iIndex;
	for(int i=n; i>=iAmb; --i){
		if(i == iAmb)
			val_pos = GetCurrentPostion();
		RemovePosition();
	}
	n = iAmb - 1;
	if(n<-1 || val_pos<0)
		return 5;

	return 0;
}


DateFormatToken DateFormatParser::GetFormatToken(const KString& csToken)
{
	if(strcmp(csToken, "yyyy")==0)return t_yyyy;
	else if(strcmp(csToken, "yy")==0)return t_yy;
	else if(strcmp(csToken, "??yy")==0)return t_qqyy;
	else if(strcmp(csToken, "mmm")==0)return t_mmm;
	else if(strcmp(csToken, "mm")==0)return t_mm;
	else if(strcmp(csToken, "?m")==0)return t_qm;
	else if(strcmp(csToken, "ddd")==0)return t_ddd;
	else if(strcmp(csToken, "dd")==0)return t_dd;
	else if(strcmp(csToken, "?d")==0)return t_qd;
	else if(strcmp(csToken, "HH")==0)return t_HH;
	else if(strcmp(csToken, "?H")==0)return t_qH;
	else if(strcmp(csToken, "MM")==0)return t_MM;
	else if(strcmp(csToken, "SS")==0)return t_SS;
	else if(strcmp(csToken, "AP")==0)return t_AP;
	else if(csToken.GetLength()>1)return t_error;
	return t_char;
}


void DateFormatParser::RemovePosition()
{
	if(!m_PositionStack.empty())
		m_PositionStack.pop();
}

int DateFormatParser::GetCurrentPostion()
{
	int i = -1;
	if(!m_PositionStack.empty())
		i = m_PositionStack.top();
	return i;
}


void DateFormatParser::SetErrorMessage(int i)
{
	KString csTmp;
	switch(i){
	case 0:
		m_csLastErr.Empty();
		break;
	case 1:
		m_csLastErr = "The LongDate value contained a non-valid weekday, or the weekday was at the wrong position.";
		break;
	case 2:
		m_csLastErr = "The LongDate value contained a non-valid month, or the month was at the wrong position.";
		break;
	case 3:
		m_csLastErr = "The format is LongDate, but the date string has another format.";
		break;
	case 4:
		m_csLastErr = "'\?\?' was found at an illegal position in the format string, or it wasn't followed by 'yy'.";
		break;
	case 5:
		m_csLastErr = "'A' was found at an illegal position in the format string, or it wasn't followed by 'P'.";
		break;
	case 6:
		m_csLastErr = "'H' was found at an illegal position in the format string.";
		break;
	case 7:
		m_csLastErr = "'M' or 'S'  was found at an illegal position in the format string, or it wasn't followed by another 'M' or 'S'.";
		break;
	case 8:
		m_csLastErr = "'y' was found at an illegal position in the format string, or it wasn't followed by another 'y'.";
		break;
	case 9:
		m_csLastErr = "'yyy' is not a legal format code.";
		break;
	case 10:
		m_csLastErr = "'m' or 'd' was found at an illegal position in the format string, or it wasn't followed by another 'm' or 'd'.";
		break;
	case 11:
		csTmp = m_csLastErr;
		m_csLastErr.Format("Unknown pattern, '%s', was found in the format string.", (const char*)csTmp);
		break;
	case 12:
                                                         
		m_csLastErr.Empty();
		break;
	case 13:
		m_csLastErr = "The value is longer than expected from the format.";
		break;

	default:
		m_csLastErr = "Unknown error occurred when parsing the date string.";
	}

}
