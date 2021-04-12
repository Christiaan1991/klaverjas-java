# Klaverjas

## The project

In this application, you will be able to play the typical Dutch game klaverjassen on a server with three other players! Rules of the game can be found [here](RULES.md).
In the readme, you will find install instructions and how to run the application.

## Two servers

The project consists of two servers. The front-end uses a Node.js server. It is mainly used to compile your React code into Javascript files during development. This will shorten the feedback loop between changing your code and seeing the results in the browser. The second server is the back-end, which uses a Jetty server. The back-end server allows your Java API to be accessible for other programs, including the front-end server. To prevent [cross-origin request shenanigans (CORS)](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS), all requests from the browser will be sent to the front-end server. That server will then forward to the back-end server if needed.

The front-end assumes that the back-end will run on port 8080. If that is not the case, edit the snowpack.config.js file.

To run the application you need to have both servers running at the same time. This probably means you'll need to open two different terminals/command prompts to do so.

## Installating front-end dependencies

To run the React application you'll first need to install the required dependencies. These dependencies are defined in the package.json file. Run the command `npm install` from the `/client` directory.

## Running the front-end

The package.json specifies which commands can be run using npm (e.g. npm run start). In this sample repository, two commands have been defined. You should also run these in the `/client` directory.

```bash
# Start a development server
npm run start
# Check code for common mistakes and style conventions
npm run lint
```

## Using Gradle

You can either install Gradle on your machine and use the installation or use the Gradle wrapper files found next to this README. Replace the `./gradlew` command with `gradle` if using the globally installed Gradle or `.\gradlew.bat` if you're running the Windows batch script.

```bash
# Building
./gradlew build
# Testing (will fail with the initial code)
./gradlew test
# Running (only relevant for the MVC case)
./gradlew run
```

If you run the program, you will notice the build "progress" is stuck on 87% or so. That means your application is running and Gradle is waiting for it to succeed. You can ignore the progress bar when running the application; it should print some lines when it's ready.
