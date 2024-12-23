; SpiderWeb Installer Script
!include "MUI2.nsh"

; General
Name "SpiderWeb"
OutFile "SpiderWebSetup.exe"
InstallDir "$PROGRAMFILES\SpiderWeb"
InstallDirRegKey HKCU "Software\SpiderWeb" ""

; Interface Settings
!define MUI_ABORTWARNING
!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\modern-install.ico"
!define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\modern-uninstall.ico"

; Pages
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH

!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

; Languages
!insertmacro MUI_LANGUAGE "English"

Section "SpiderWeb" SecSpiderWeb
    SetOutPath "$INSTDIR"
    
    ; Add files
    File "SpiderWeb.exe"
    File "SpiderWeb.jar"
    
    ; Create shortcuts
    CreateDirectory "$SMPROGRAMS\SpiderWeb"
    CreateShortcut "$SMPROGRAMS\SpiderWeb\SpiderWeb.lnk" "$INSTDIR\SpiderWeb.exe"
    CreateShortcut "$DESKTOP\SpiderWeb.lnk" "$INSTDIR\SpiderWeb.exe"
    
    ; Write uninstaller
    WriteUninstaller "$INSTDIR\Uninstall.exe"
    
    ; Registry information for add/remove programs
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\SpiderWeb" "DisplayName" "SpiderWeb"
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\SpiderWeb" "UninstallString" "$\"$INSTDIR\Uninstall.exe$\""
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\SpiderWeb" "DisplayIcon" "$INSTDIR\SpiderWeb.exe"
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\SpiderWeb" "Publisher" "Your Name"
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\SpiderWeb" "DisplayVersion" "1.0"
SectionEnd

Section "Uninstall"
    ; Remove files
    Delete "$INSTDIR\SpiderWeb.exe"
    Delete "$INSTDIR\SpiderWeb.jar"
    Delete "$INSTDIR\Uninstall.exe"
    
    ; Remove shortcuts
    Delete "$SMPROGRAMS\SpiderWeb\SpiderWeb.lnk"
    Delete "$DESKTOP\SpiderWeb.lnk"
    RMDir "$SMPROGRAMS\SpiderWeb"
    
    ; Remove registry keys
    DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\SpiderWeb"
    DeleteRegKey HKCU "Software\SpiderWeb"
    
    ; Remove install directory
    RMDir "$INSTDIR"
SectionEnd

Function .onInit
    ; Check for Java
    ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
    StrCmp $R0 "" NoJava HasJava
    
    NoJava:
        MessageBox MB_OK|MB_ICONEXCLAMATION "Java Runtime Environment is required but not installed. Please install Java and run this setup again."
        Abort
    HasJava:
FunctionEnd 