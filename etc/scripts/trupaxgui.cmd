@echo off
IF EXIST %SystemRoot%\SysWOW64\javaw.exe (
    %SystemRoot%\SysWOW64\javaw -cp "%~d0%~p0\trupax.jar" coderslagoon.trupax.exe.GUI %*
) ELSE (
    javaw -cp "%~d0%~p0\trupax.jar" coderslagoon.trupax.exe.GUI %*
)
