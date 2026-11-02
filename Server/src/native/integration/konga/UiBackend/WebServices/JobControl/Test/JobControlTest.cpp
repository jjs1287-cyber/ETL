#include <iostream>

#include <konga/UiBackend/WebServices/JobControl/Impl/JobControlImpl.h>

using namespace konga;
using namespace std;

int main (int argc, char *argv[])
{
	JobControlImpl impl;
	impl.RunOperation("admin", "****", "put guid here");

	return(0);
}

