# TowerDefense_Java

A simple Tower Defense game in Java.

## Project structure
- `src/` — Java source code
- `bin/` — compiled classes (ignored by git)
- `res/` — resources (images, sounds, etc.)

## How to run
Use your Java 21 JDK. From the project root:

```powershell
# Run with Java modules (example based on your current run config)
$env:JAVA_HOME = "C:\\Users\\hubla\\AppData\\Local\\Programs\\Eclipse Adoptium\\jdk-21.0.6.7-hotspot"
$env:PATH = "$env:JAVA_HOME\\bin;$env:PATH"

# Compile (if needed) and run
# Assuming classes are built into bin and module name is TD_Java
java --module-path bin -m TD_Java/main.Game
```

## License
Add a license if you want to share this publicly.
