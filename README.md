# Group Project in TDT4240 - Software Architecture

## Description

Tank Maze Mayhem is a multiplayer game centered around a real-time combat system where players control tanks navigating through a maze. Up to four players can play at once. 
As players traverse the maze, they'll encounter scattered elements that grant various power-ups, adding strategic elements to the gameplay. These power-ups offer 
unique abilities, allowing players to gain an advantage over their opponents.The projectiles from their firearms will be able to bounce off the walls of the maze, 
so that a player can be shot by its own projectiles. Once a player is shot, they respawn. Tank movements and shots are updated in real-time across all participants' 
screens, ensuring a synchronized and fair gaming experience.

The goal of the game is to achieve the highest score possible before the countdown timer hits 0. A point is deducted from the user’s score if they get hit by a projectile,
while a point is added if they manage to hit another player. The player with the highest score at the end of the game is declared the winner.

## Installing and Setup

### Prerequisites
- **Git**
  - Download and install Git from the official [Git website](https://git-scm.com/downloads).
- **Java JDK 17**
  - To be able to run our project, you need a Java Development Kit (JDK) with a version higher or equal to 17.
- **Gradle 8.2.2**
  - Gradle is used as the build tool for this project. It automates the process of building, testing, and deploying the application.
    - You can download Gradle from the [Gradle Download Page](https://gradle.org/install/).
    - Alternatively, if you are using a Gradle Wrapper script (gradlew or gradlew.bat), you do not need to manually install Gradle, 
      as the wrapper script will handle the installation for you.
- **Android SDK**
  - This is needed in order to run the game on either an emulator or a physical Android device. If you are using Android Studio, you should already have an Android SDK installed.
- **Android API Level**
  - In order to play Tank Maze Mayhem, you need an Android device with an API level ≥ 19, preferably 34.

### Installing

Clone our project into a folder of your choice with the following command:
```cmd
git clone https://github.com/hei98/Tank-Maze-Mayhem.git
```
### Running the game
- #### Running the game in Android Studio
1. Download and open the latest version of Android Studio
2. Retrieve our source code and open it in Android Studio
3. If you don’t have one already, create a new Android Device. Remember the requirement of API Level ≥ 19, preferably 34. We recommend a Pixel 3a device, as this is what was
   mainly used by the group during development.
4. Click the green play-button

- #### Building with APK
1. Download our APK file. This can for instance be done in Android Studio, by navigating to Build → Build Bundle(s) / APK(s) → Build APK(s)
2. After downloading it, transfer the APK file to the Android device of your choice, either a physical one or an emulator.
3. Tank Maze Mayhem should now be downloaded to the device, and can be played by simply opening the application

### Multiplayer

In order to play multiplayer, you can either create a party of your own or join one created by another player. To join another player’s party, 
you have to type in the IP-address of the other player’s device, and port you want to connect to. Because of this, you have to ensure that all of the devices 
that want to play together are connected to the same network. **NTNU'S NETWORK DOES NOT SUFFICE**, as it has set up firewalls making communication by IP-addresses impossible. 
After creating a party, you will be shown the IP-address and port that others have to connect to.

The following section is **VERY** important if you want to play multiplayer with 2 or more emulators <ins>on the same device</ins>. However, if you want to play on two or more
physical Android devices, you can safely skip this section.

#### Playing multiplayer with 2 or more emulators on same device

Playing multiplayer between two or more emulators does not work out of the box. The issue arises because each emulator is connected to its own virtual router 
instead of the host-machine's router, meaning they are <ins>not</ins> connected to the same internet. Follow these steps if you want to play between two or more emulators:

- ##### Windows

1. Turn on the emulator that will be the party leader. Ensure no other emulators are active.
2. Open Control Panel → Programs → Turn Windows features on or off
3. Navigate to Telnet Client and ensure this feature is activated
4. In a terminal of your choice, type “telnet localhost 5554”. 5554 is a reference to the emulator you just turned on.
5. You will be prompted to authenticate yourself. Navigate to C:\Users\<your_user>\.emulator_console_auth_token and copy the auth token in the file. Go back to the terminal and type “auth <auth_token>.
6. We now need to set up a redirection between localhost and your emulator. Type “redir add tcp:5000:54555” in the terminal. This will redirect all TCP messages sent to port 5000 on localhost (127.0.0.1) to port 54555 on the emulator.
7. To exit the shell, type “quit”.

- ##### MacOS

1. Turn on the emulator that will be the party leader. Ensure no other emulators are active.
2. If you already have Homebrew installed, skip to step 3.
    - Open a zsh terminal and type /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)". Homebrew is used to download packages that didn’t come with MacOS.
    - Open a zsh terminal and type “brew install telnet”.
3. After the installation has finished, type “telnet localhost 5554”. 5554 is a reference to the emulator you just turned on.
4. You will be prompted to authenticate yourself. Open the file the shell tells you to, and copy the auth token. Then, in the terminal, type “auth <auth_token>”.
5. We now need to set up a redirection between localhost and your emulator. Type “redir add tcp:5000:54555” in the terminal. This will redirect all TCP messages sent to port 5000 on localhost (127.0.0.1) to port 54555 on the emulator.
6. To exit the shell, type “quit”.

Now other emulators on your device are able to communicate with the emulator we just configured! It is important that the emulator we just configured is the party leader for the game,
or else you won't be able to connect to the lobby. Now go ahead, test the game with 2 or more emulators!







