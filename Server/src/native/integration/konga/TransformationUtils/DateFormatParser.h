/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#if !defined(AFX_DATEFORMATPARSER_H__667E84CB_E1A4_4F87_913A_B7B04C3495FE__INCLUDED_)
#define AFX_DATEFORMATPARSER_H__667E84CB_E1A4_4F87_913A_B7B04C3495FE__INCLUDED_

#include <vector>
#include <stack>

#include <konga/Utils/KString.h>

                                                                           

enum DateFormatToken{t_error=-1,t_yyyy,t_yy,t_qqyy,t_mmm,t_mm,t_qm,t_ddd,t_dd,t_qd,t_HH,t_qH,t_MM,t_SS,t_AP,t_char};

class DateFormatParser {
public:
	DateFormatParser(const char* szValue, const char* szFormat, int& iErr);
	virtual ~DateFormatParser();
	KString GetValue() {return m_csValue;}
	KString GetFormat() {return m_csFormat;}
	KString GetLastError() {return m_csLastErr;}

	static const char* monthsAbr[];
	static const char* months[];
	static const char* daysAbr[];
	static const char* days[];

private:
	DateFormatParser(){};
	class Ambiguity{
	public:
		Ambiguity(int i=0) : iIndex(i), iTried(0x00) {};
		int iIndex;
		int iTried;
	};
	KString m_csFormat;
	KString m_csValue;
	KString m_csLastErr;
	std::vector<KString>	m_vTokens;
	std::vector<Ambiguity>	m_vAmbiguities;
	std::stack<int>			m_PositionStack;

	void AddToken(KString cs) {m_vTokens.push_back(cs);}
	void AddAmbiguity(Ambiguity amb) {m_vAmbiguities.push_back(amb);}
	void SavePosition(int i) {m_PositionStack.push(i);}
	void RemovePosition();
	int GetCurrentPostion();
	size_t GetNumberOfTokens() {return m_vTokens.size();}
	size_t GetNumberOfAmbiguities() {return m_vAmbiguities.size();}
	void SetErrorMessage(int i);
	DateFormatToken GetFormatToken(const KString& csToken);
	int ResolveAmbiguity(int& n, int& val_pos, bool bIncludeLast = false);
	int CreateCVTTokenList(KString csFmt);
	int ConformCVTFormat(const KString& csVal);
	int ParseLongDateValue(KString& csFmtRest, KString& csValRest);
};

#endif                                                                                     
