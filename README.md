# Hospital Management System (HMS)

A JavaFX-based Hospital Management System built with Java 21 LTS and Maven.

## Features

- User-friendly GUI built with JavaFX
- FXML-based UI layouts
- Module-based Java architecture
- Cross-platform compatibility

## Requirements

- **Java**: JDK 21 LTS or higher
- **Maven**: 3.9.0 or higher
- **JavaFX**: 21.0.6

## Installation

### Prerequisites
1. Install Java 21 JDK from [Eclipse Adoptium](https://adoptium.net/)
2. Install Maven from [Apache Maven](https://maven.apache.org/download.cgi)

### Build the Project

```bash
# Clone the repository
git clone https://github.com/yourusername/hospital-management-system.git
cd hospital-management-system

# Build the project
mvn clean compile

# Run the application
mvn exec:java
```

## Project Structure

```
hospital_managment_system/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── hms/
│       │           ├── App.java
│       │           ├── PrimaryController.java
│       │           ├── SecondaryController.java
│       │           └── module-info.java
│       └── resources/
│           └── com/
│               └── hms/
│                   ├── primary.fxml
│                   └── secondary.fxml
├── pom.xml
└── README.md
```

## Technologies Used

- **Java 21 LTS** - Programming Language
- **JavaFX 21** - UI Framework
- **Maven** - Build Tool
- **FXML** - UI Markup Language

## Contributing

Contributions are welcome! Here's how to contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues or questions, please open an issue on the GitHub repository.

## Author

Hasibul Islam

---

**Last Updated**: January 25, 2026
