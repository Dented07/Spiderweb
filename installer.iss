[Setup]
AppName=SpiderWeb
AppVersion=1.0
WizardStyle=modern
DefaultDirName={autopf}\SpiderWeb
DefaultGroupName=SpiderWeb
OutputBaseFilename=SpiderWebSetup
Compression=lzma2
SolidCompression=yes
OutputDir=output

[Files]
Source: "SpiderWeb.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "SpiderWeb.jar"; DestDir: "{app}"; Flags: ignoreversion

[Icons]
Name: "{group}\SpiderWeb"; Filename: "{app}\SpiderWeb.exe"
Name: "{commondesktop}\SpiderWeb"; Filename: "{app}\SpiderWeb.exe"

[Run]
Filename: "{app}\SpiderWeb.exe"; Description: "Launch SpiderWeb"; Flags: postinstall nowait

[Code]
function InitializeSetup(): Boolean;
begin
  Result := True;
  if not RegKeyExists(HKEY_LOCAL_MACHINE, 'SOFTWARE\JavaSoft\Java Runtime Environment') then
  begin
    MsgBox('Java Runtime Environment (JRE) is required but not installed.' + #13#10 +
           'Please install Java and run this setup again.', mbInformation, MB_OK);
    Result := False;
  end;
end; 