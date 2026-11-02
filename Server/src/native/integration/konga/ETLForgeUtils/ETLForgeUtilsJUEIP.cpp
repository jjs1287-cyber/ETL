/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <konga/Server/Server.h>

#include "ETLForgeUtilsJUEIP.h"

using namespace konga;
using std::string;

namespace {
	const string stJUEIP_KEY_NAME("ParticipateInUsageStats");
}

void konga::EnableJUEIP() {
	konga::SetKeyValue(stJUEIP_KEY_NAME, "1");
}

void konga::DisableJUEIP() {
	konga::SetKeyValue(stJUEIP_KEY_NAME, "0");
}

