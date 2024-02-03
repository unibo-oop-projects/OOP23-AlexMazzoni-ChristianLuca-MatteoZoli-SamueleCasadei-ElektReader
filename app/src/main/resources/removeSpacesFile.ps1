# Define the root directory path
$rootDirectory = "./icons"

# Get all files in the directory tree
$files = Get-ChildItem -Path $rootDirectory -Recurse

# Loop through each file and rename it by replacing spaces with underscores
foreach ($file in $files) {
    $newFileName = $file.Name -replace '_', ''
    $newFilePath = Join-Path -Path $file.Directory.FullName -ChildPath $newFileName

    # Check if the file name has changed and the new file name doesn't already exist
    if ($file.Name -ne $newFileName -and -not (Test-Path -Path $newFilePath)) {
        Rename-Item -Path $file.FullName -NewName $newFileName
        Write-Host "Renamed: $($file.FullName) -> $($newFilePath)"
    }
}

Write-Host "Spaces removed from file names in $rootDirectory and its subdirectories."
