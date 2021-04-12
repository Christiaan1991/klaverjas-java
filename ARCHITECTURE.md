# Klaverjas Architecture

## Repository structure

- Main folder (this folder): contains the files relevant for the whole project. For example the Gradle-wrapper files, the .gitignore, the readme.md and this architecture file.
- api/: contains the files for the API or service layer of your application.
- api/src/main/java/klaverjas/api: contains the web endpoints.
- api/src/main/java/klaverjas/api/models: contains the web endpoints.
- domain/: contains the files that model the business domain (game rules).
- client/: contains the client (front-end)

## Java project structure

The domain is written in java, while a node.js client server is used to run the front-end server.

## React project structure

A React project is generally structured as follows:

```
package.json
public/
   index.html
src/
   Feature1/
      Feature1.css
      Feature1.tsx
      Feature1.tests.tsx
   Feature2/
      Feature2.css
      Feature2.tsx
      Feature2.tests.tsx
      Feature2B.tsx
      Feature2B.tests.tsx
```

The public directory contains static files, such as the relatively empty index.html file needed to run React. The src file contains the React code. The convention for TypeScript projects is to use the .tsx file extension for files that contain React components. Files are generally grouped together in directories by feature. These directories contain all files related to that feature, such as coponents, stylesheets, images and tests.

