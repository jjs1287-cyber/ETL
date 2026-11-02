
#include "StringUtils.h"
#include "OdbcConnectionStringUtils.h"

using namespace konga;
using std::string;

namespace {
	const string sPWD("PWD=");
}

string konga::ReplacePassword(const string& connectStr,const string& newPassword)
{
	string sConnectStrUpper(connectStr);
	ToUpper(sConnectStrUpper);
	const string::size_type iPWDPos = sConnectStrUpper.find(sPWD);	
	if ( iPWDPos != string::npos )
	{
		                                      
		                                                          
		string::size_type iSemiColon = connectStr.find(';', iPWDPos);
		if ( iSemiColon == string::npos )
			iSemiColon = connectStr.size();

		const string::size_type iStartPos = iPWDPos + sPWD.size();

		return connectStr.substr(0,iStartPos) + newPassword + connectStr.substr(iSemiColon);
	} else {
		                                                                                   
		if ( !newPassword.empty() ) {
			string tmp(connectStr);
			AppendConnectString(tmp, "Pwd=" + newPassword);
			return tmp;
		}
		                                                             
		return connectStr;
	}
}

string konga::ExtractAndReplacePassword(string& connectStr, const string& newPassword) {
	string sConnectStrUpper(connectStr);
	ToUpper(sConnectStrUpper);
	const string::size_type iPWDPos = sConnectStrUpper.find(sPWD);
	if ( iPWDPos != string::npos ) {
		string::size_type iSemiColon = connectStr.find(';', iPWDPos);
		if ( iSemiColon == string::npos )
			iSemiColon = connectStr.size();

		const string::size_type iStartPos = iPWDPos + sPWD.size();

		const string& oldPassword = connectStr.substr(iStartPos, iSemiColon-iStartPos);
		connectStr = connectStr.substr(0,iStartPos) + newPassword + connectStr.substr(iSemiColon);
		return oldPassword;
	}
	else {
		return string("");
	}
}

bool konga::HasPwd(const string& connectStr) {
	string sConnectStrUpper(connectStr);
	ToUpper(sConnectStrUpper);
	return sConnectStrUpper.find(sPWD) != string::npos;
}


void konga::AppendConnectString(string& sConnectStr, const string& sAdditionalConnectParameters_in) {
	if ( sAdditionalConnectParameters_in.find_first_not_of(" \t;") == string::npos )
		return;

	const string& sAdditionalConnectParameters( TrimString(TrimString(sAdditionalConnectParameters_in), ';') );

	sConnectStr = TrimString(TrimString(sConnectStr), ';');
	
	sConnectStr += ";";
	sConnectStr += sAdditionalConnectParameters;
	sConnectStr += ";";
}
