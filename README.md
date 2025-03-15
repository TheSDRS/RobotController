# RobotController

![GitHub last commit](https://img.shields.io/github/last-commit/TheSDRS/RobotController)
![GitHub issues](https://img.shields.io/github/issues/TheSDRS/RobotController)
![GitHub pull requests](https://img.shields.io/github/issues-pr/TheSDRS/RobotController)
![GitHub contributors](https://img.shields.io/github/contributors/TheSDRS/RobotController)

## Description
RobotController is a public repository for managing and controlling LegoMindstorms robots.

## Contents
### Java Backend
The Backend is handled by a Java Http Server that handles RESTful API requests and the webinterface for the user.<br><br>
- The root URL is by default `http://localhost:8080/`
- The API URL is by default `http://localhost:8080/api/`
- The Webinterface URL is by default `http://localhost:8080/control/`
- Tests are located at `http://localhost:8080/test/`

### Python Project
For connection with a robot use the Python project. It is a simple project that sends RESTful API requests to the Java backend.
It contains a simple simulation of a robot for when you dont have access to one in real life.

## Language Composition
![Java](https://img.shields.io/badge/dynamic/json?color=blue&label=Java&query=Java&url=https://api.github.com/repos/TheSDRS/RobotController/languages)
![JavaScript](https://img.shields.io/badge/dynamic/json?color=yellow&label=JavaScript&query=JavaScript&url=https://api.github.com/repos/TheSDRS/RobotController/languages)
![Python](https://img.shields.io/badge/dynamic/json?color=green&label=Python&query=Python&url=https://api.github.com/repos/TheSDRS/RobotController/languages)
![CSS](https://img.shields.io/badge/dynamic/json?color=red&label=CSS&query=CSS&url=https://api.github.com/repos/TheSDRS/RobotController/languages)
![HTML](https://img.shields.io/badge/dynamic/json?color=orange&label=HTML&query=HTML&url=https://api.github.com/repos/TheSDRS/RobotController/languages)

## Features
- RESTful API
- Simple simulation of a robot
- Robot control
- Web-based user interface

## Getting Started
### Prerequisites
- Java Development Kit (JDK)
- Python 3.x

### Installation
1. Clone the repository:
    ```bash
    git clone https://github.com/TheSDRS/RobotController.git
    ```
2. Navigate to the project directory:
    ```bash
    cd RobotController
    ```
3. Install the necessary dependencies:
    ```bash
   pip install -r requirements.txt
    ```

## Usage
To run the Java backend, use the following command:
```bash
mvn clean package
```
To test the Java backend, use the following command:
```bash
mvn clean test
```

To run the Python project, use the following command:
```bash
.venv\Scripts\activate
python main.py
```

## Contributing
Contributions are welcome! Please fork the repository and create a pull request.

## License
This project is licensed under the MIT License.

## Contributors
Thanks goes to these wonderful people:
![GitHub contributors](https://img.shields.io/github/contributors/TheSDRS/RobotController)

## Open Issues
![GitHub issues](https://img.shields.io/github/issues-raw/TheSDRS/RobotController)

## Pull Requests
![GitHub pull requests](https://img.shields.io/github/issues-pr-raw/TheSDRS/RobotController)

## Repository Stats
![GitHub repo size](https://img.shields.io/github/repo-size/TheSDRS/RobotController)
![GitHub code size](https://img.shields.io/github/languages/code-size/TheSDRS/RobotController)

## Latest Release
![GitHub release (latest by date)](https://img.shields.io/github/v/release/TheSDRS/RobotController)