                                                                            
#include <windows.h>
#include <dwmapi.h>
#include <jawt.h>
#include <jawt_md.h>
extern "C" JNIEXPORT void JNICALL
Java_org_etlforge_integration_client_main_LookAndFeelInstaller_applyEtlForgeWindowAppearance(
    JNIEnv* env, jclass, jobject window) {
    JAWT awt = {};
    awt.version = JAWT_VERSION_1_4;
    if (!JAWT_GetAWT(env, &awt)) return;
    JAWT_DrawingSurface* surface = awt.GetDrawingSurface(env, window);
    if (!surface) return;
    jint lock = surface->Lock(surface);
    if (!(lock & JAWT_LOCK_ERROR)) {
        JAWT_DrawingSurfaceInfo* info = surface->GetDrawingSurfaceInfo(surface);
        if (info) {
            HWND hwnd = static_cast<JAWT_Win32DrawingSurfaceInfo*>(info->platformInfo)->hwnd;
            BOOL dark = TRUE;
            COLORREF black = RGB(0, 0, 0), white = RGB(255, 255, 255);
            COLORREF border = RGB(176, 176, 176);
            DwmSetWindowAttribute(hwnd, 20, &dark, sizeof(dark));
                                                                                 
            DwmSetWindowAttribute(hwnd, 34, &border, sizeof(border));
            DwmSetWindowAttribute(hwnd, 35, &black, sizeof(black));
            DwmSetWindowAttribute(hwnd, 36, &white, sizeof(white));
            surface->FreeDrawingSurfaceInfo(info);
        }
        surface->Unlock(surface);
    }
    awt.FreeDrawingSurface(surface);
}
