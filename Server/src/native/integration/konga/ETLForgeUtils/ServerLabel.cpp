/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#include <iostream>
#include <string>

#include <konga/Utils/StringUtils.h>
#include <konga/Server/Server.h>

#include "ServerLabel.h"

using namespace konga;
using std::string;
using std::cout;
using std::endl;

void konga::ETLForgeUtils::SetServerLabel(const std::string& label, bool bQuiet) {
	konga::SetServerLabel(label);
	if ( !bQuiet ) {
		if ( label.empty() )
			cout << "This server now has no label" << endl;
		else
			cout << "Server label is now: " << label << endl;
	}
}

void konga::ETLForgeUtils::PrintServerLabel(bool bQuiet) {
	const string label(konga::GetServerLabel());
	if ( label.empty() && !bQuiet )
		cout << "No label defined" << endl;
	else
		cout << label << endl;
}

