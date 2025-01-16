## Getting Started

This project requires Docker and Java to run. 

**To build and run the application, follow these steps:**

1. **Build the application:**
   ```bash
   ./gradlew clean build -x test 
   ```
   This command will clean, build the application, and skip the tests.

2. **Run the application using Docker Compose:**
   ```bash
   docker-compose up --build
   ```
   This command will build the Docker image and start the application. The `--build` flag ensures that the image is rebuilt if there have been any changes to the Dockerfile.

**Note:** 

* Make sure you have Docker and Docker Compose installed on your system.
* The application will be accessible at the port specified in your `docker-compose.yml` file. 
