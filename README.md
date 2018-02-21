# MyCart

A Simple e-commerce application to demonstrate my coding abilities

## Facts about the application
- Supports both mobiles, tablets and both orientations

- Used **Android ViewModel** concepts to avoid View/data loss in Configuration changes like Orientation etc. This application followed activity lifecycle. 
- Used **Android LiveData** concepts to update View automatically on ViewModel data change. 
- Used **ROOM** , **SQlite** (to store cart items) for database and DAO wrapper classes 
- Used **Android Architecture Components** accordingly.

- Used **Kotlin** data class concept to store Model data. **Kotlin Synthetic plugin** for **Data Binding**
- Used **Dagger 2 (Dependency Injection)** to ease development and for clean code
- Used **Reactivex (Rx)** way to fetch json data from given URLs Used Rx schedulers to switch between appropriate Threads
- Used RetroFit and Okhttp Libraries for constructing and fetching Url data
- Having Both **Instrumentation Test** ( insert, delete, fetch database tables) & **Unit Test** (App Util functions) using in build JUnit in Android Studio
- Followed **MVVM Architectural pattern**
- Followed **Material Design guidelines** and chose colors and application user interface.
- Added Animations between fragment transactions and Ripple effects across application
- Used Third party Libraries for loader, Image Sliders, Gson, Image Loading (picasso) etc.
- More focused on Coding Standard, DataBase Handling and Reactive and less focused on Trivial things like Network handling etc.
