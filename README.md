Granite
=============

Granite is the SpongeAPI implementation for Vanilla Minecraft.

* IRC: #sponge or #spongedev on irc.esper.net

## Prerequisites
* [Java] 8
* [Gradle] 2.3+

## Cloning
The following steps will ensure your project is cloned properly.

1. `git clone git@github.com:SpongePowered/Granite.git`
2. `cd Granite`  
3. `git submodule update --init --recursive`  
4. `cp scripts/pre-commit .git/hooks`

## Setup
__Note:__ If you do not have [Gradle] installed then use ./gradlew for Unix systems or Git Bash and gradlew.bat for Windows systems in place of any 'gradle' command.

__For [Eclipse]__
  1. Run `gradle setupDecompWorkspace --refresh-dependencies`
  2. Make sure you have the Gradle plugin installed (Help > Eclipse Marketplace > Gradle Integration Plugin)
  3. Import Granite as a Gradle project (File > Import)
  4. Select the root folder for Granite  and click **Build Model**
  5. Check Granite when it finishes building and click **Finish**

__For [IntelliJ]__
  1. Run `gradle setupDecompWorkspace --refresh-dependencies`
  2. Make sure you have the Gradle plugin enabled (File > Settings > Plugins).
  3. Click File > Import Module and select the **build.gradle** file for Granite.

## Running
__Note:__ The following is aimed to help you setup run configurations for Eclipse and IntelliJ, if you do not want to be able to run Granite directly from your IDE then you can skip this.

__For [Eclipse]__
  1. Go to **Run > Run Configurations**.
  2. Right-click **Java Application** and select **New**.
  3. Set the current project.
  4. Set the name as `Granite (Client)` and apply the information for Client below.
  5. Repeat step 1 through 4, then set the name as `Granite (Server)` and apply the information for Server below.
  6. When launching the server for the first time, it will shutdown by itself. You will need to modify the server.properties to set onlinemode=false and modify the eula.txt to set eula=true (this means you agree to the Mojang EULA, if you do not wish to do this then you cannot run the server).


__For [IntelliJ]__
  1. Go to **Run > Edit Configurations**.
  2. Click the green + button and select **Application**.
  3. Set the name as `Granite (Client)` and apply the information for Client below.
  4. Repeat step 2 and set the name as `Granite (Server)` and apply the information for Server below.
  5. When launching the server for the first time, it will shutdown by itself. You will need to modify the server.properties to set onlinemode=false and modify the eula.txt to set eula=true (this means you agree to the Mojang EULA, if you do not wish to do this then you cannot run the server).

__Client__

|     Property      | Value                       |
|:-----------------:|:----------------------------|
|    Main class     | GradleStart                 |
| Program arguments | --noCoreSearch              |
| Working directory | ./run (Included in project) |
| Module classpath  | Granite (IntelliJ Only)     |

__Server__

|     Property      | Value                       |
|:-----------------:|:----------------------------|
|    Main class     | GradleStartServer           |
| Program arguments | --noCoreSearch              |
| Working directory | ./run (Included in project) |
| Module classpath  | Granite (IntelliJ Only)     |


## Building
__Note:__ If you do not have [Gradle] installed then use ./gradlew for Unix systems or Git Bash and gradlew.bat for Windows systems in place of any 'gradle' command.

In order to build Granite you simply need to run the `gradle` command. You can find the compiled JAR files in `./build/libs` but in most cases
you'll only need 'granite-x.x.x-x.x.x.jar'.

## FAQ
__A dependency was added, but my IDE is missing it! How do I add it?__
>If a new dependency was added, you can just restart your IDE and the Gradle plugin for that IDE should pull in the new dependencies.

__Help! Things are not working!__
>Some issues can be resolved by deleting the '.gradle' folder in your user directory and running through the setup steps again, or even running `gradle cleanCache` and running through the setup again. Otherwise if you are having trouble with something that the README does not cover, feel free to join our IRC channel and ask for assistance.

[Eclipse]: http://www.eclipse.org/
[Gradle]: http://www.gradle.org/
[IntelliJ]: http://www.jetbrains.com/idea/
[Java]: http://java.oracle.com/
[MIT License]: http://www.tldrlegal.com/license/mit-license
