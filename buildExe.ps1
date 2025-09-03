Write-Output "Clear executable output directory..."
Remove-Item -Path "out\*" -Recurse -Force
Write-Output "Clear target directory..."
mvn clean
Write-Output "build shaded jar..."
mvn package
Write-Output "Run jpackage..."
jpackage --input target --main-jar rockPaperScissors-1.0.jar --name rps --main-class org.maz.rps.RPSApp --type app-image --dest out
