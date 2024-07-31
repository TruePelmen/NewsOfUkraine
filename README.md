# News of Ukraine App

## 1. Project Description
Test Task for job interview in TetaLab

## 2. Features Implemented
- **News Fetching**: Retrieve latest news from an API.
- **Search Functionality**: Search news articles by keyword.
- **Save Articles**: Bookmark news articles for later reading(doesnt work actually =) ).
- **Error Handling**: Handle network errors with retry options.
- **Custom Tabs**: Open articles in custom tabs.

## 3. Clean Architecture
The project follows the Clean Architecture principles, ensuring a clear separation of concerns and making the codebase more maintainable and testable.

### Layers:
- **Data Layer**: Handles data retrieval from network and local storage.
- **Domain Layer**: Contains business logic with use cases.
- **Presentation Layer**: Manages UI components and ViewModels.

## 4. Room
Room is used as the local database for storing bookmarked news articles. It provides an abstraction layer over SQLite, ensuring compile-time verification of SQL queries.

### Setup:
- Define entities for database tables.
- Create DAO interfaces for data access.
- Use Room database class to manage data operations.

## 5. Retrofit
Retrofit is used for network operations, facilitating seamless communication with the news API.

### Setup:
- Define API endpoints in service interfaces.
- Use Retrofit Builder to create instances.
- Handle API responses efficiently.

## 6. MVI
The Model-View-Intent (MVI) pattern is implemented to manage state and handle user interactions in a predictable way.

### Components:
- **Model**: Represents the state of the UI.
- **View**: Displays the current state and reacts to state changes.
- **Intent**: Captures user interactions and translates them into actions.

## 7. Single Activity App
The application is designed as a single activity app, using Jetpack Compose for UI components and navigation.

### Benefits:
- Simplifies navigation.
- Reduces boilerplate code.
- Enhances performance by minimizing activity transitions.

## 8. Koin
Koin is used for dependency injection, providing a lightweight and easy-to-use framework for managing dependencies.
