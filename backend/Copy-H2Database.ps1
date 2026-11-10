#Requires -Version 7.0
[CmdletBinding()]
param(
    [Parameter(Mandatory)][string]$Source,
    [Parameter(Mandatory)][string]$Destination
)

$sourcePath = $ExecutionContext.SessionState.Path.GetUnresolvedProviderPathFromPSPath($Source)
$destinationPath = $ExecutionContext.SessionState.Path.GetUnresolvedProviderPathFromPSPath($Destination)
if (!$sourcePath.EndsWith('.mv.db', [StringComparison]::OrdinalIgnoreCase) -or
    !$destinationPath.EndsWith('.mv.db', [StringComparison]::OrdinalIgnoreCase)) {
    throw '源文件和目标文件都必须使用 .mv.db 后缀。'
}
if ([IO.File]::Exists($destinationPath)) {
    throw "目标文件已存在，拒绝覆盖：$destinationPath"
}

try {
    $inputStream = [IO.File]::Open($sourcePath, 'Open', 'Read', 'None')
} catch [IO.IOException] {
    throw "无法独占打开数据库文件。请确认源文件存在，且使用它的后端已停止。$($_.Exception.Message)"
}

$temporaryPath = $null
try {
    [void][IO.Directory]::CreateDirectory([IO.Path]::GetDirectoryName($destinationPath))
    $temporaryPath = "$destinationPath.$([Guid]::NewGuid().ToString('N')).partial"
    $outputStream = [IO.File]::Open($temporaryPath, 'CreateNew', 'ReadWrite', 'None')
    try {
        $inputStream.CopyTo($outputStream)
        $outputStream.Flush($true)
        $inputStream.Position = 0
        $outputStream.Position = 0
        $sourceHash = (Get-FileHash -InputStream $inputStream -Algorithm SHA256 -ErrorAction Stop).Hash
        $copyHash = (Get-FileHash -InputStream $outputStream -Algorithm SHA256 -ErrorAction Stop).Hash
        if ($sourceHash -ne $copyHash) { throw '副本校验失败。' }
    } finally {
        $outputStream.Dispose()
    }
    [IO.File]::Move($temporaryPath, $destinationPath)
    [pscustomobject]@{ Path = $destinationPath; SHA256 = $copyHash }
} finally {
    $inputStream.Dispose()
    if ($temporaryPath) { [IO.File]::Delete($temporaryPath) }
}
