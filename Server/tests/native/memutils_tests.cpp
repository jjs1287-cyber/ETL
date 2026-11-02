#include <konga/Utils/MemUtils.h>
#include <sstream>
int main() {
  ULONG_PTR low = 0, high = 0;
  GetCurrentThreadStackLimits(&low, &high);
  if (reinterpret_cast<ULONG_PTR>(konga::GetStackBottom()) != high || low >= high) return 1;
  if (konga::GetCallsLeft() != 999) return 2;
  if (konga::FormatBytes(512) != "512 B" || konga::FormatBytes(2048) != "2 kB" || konga::FormatBytes(2097152) != "2 MB") return 3;
  std::ostringstream captured;
  auto* previous = std::cout.rdbuf(captured.rdbuf());
  konga::PrintStackFrames();
  std::cout.rdbuf(previous);
  if (captured.str().find("Frame 0:") == std::string::npos) return 4;
  CalculateStackFrames();
  std::cout << "PASS: Windows x64 stack bounds, unwinding, byte formatting, legacy call limit\n";
}
