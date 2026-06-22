# MiniNetflix Android App

A mini Netflix clone application for Android that uses the TMDB API to display movies, search for titles, and manage a personal "My List".

## 🚀 Features
- **Home Screen**: Browse trending, popular, and top-rated movies.
- **Movie Details**: View detailed information, including ratings, release dates, and overviews.
- **Search**: Search for your favorite movies by title.
- **My List**: Save movies to your local database to watch later.
- **Similar Movies**: Get recommendations based on the movie you are viewing.

## 🛠️ Built With
- **Kotlin**: Primary programming language.
- **Retrofit & GSON**: For networking and API data parsing.
- **Room Database**: Local storage for "My List" functionality.
- **Navigation Component**: For seamless fragment transitions.
- **ViewModel & LiveData**: Following MVVM architecture.
- **Glide**: For efficient image loading.
- **KSP**: Kotlin Symbol Processing for Room and Glide.

---

## 📋 How to Set Up (Step-by-Step)

### 1. Clone the Repository
Open your terminal or use Git Bash and run:
```bash
git clone https://github.com/kimpheng369/MiniNetflix_Android.git
```

### 2. Get a TMDB API Key
1. Go to [The Movie Database (TMDB)](https://www.themoviedb.org/).
2. Log in or create an account.
3. Navigate to **Settings** > **API**.
4. Request an API Key (v3 auth).

### 3. Add Your API Key
1. Open the project in **Android Studio**.
2. Navigate to `app/src/main/java/com/example/mininetflix/util/Constants.kt`.
3. Replace the `API_KEY` value with your own key:
   ```kotlin
   const val API_KEY = "YOUR_API_KEY_HERE"
   ```

### 4. Build and Run
1. Let Gradle sync finish.
2. Select an emulator or connect a physical device.
3. Click the **Run** button in Android Studio.

---

## 📖 How it Works

### 1. Data Fetching (Retrofit)
When the app starts, the `HomeViewModel` triggers API calls through `MovieRepository`. Retrofit fetches JSON data from TMDB and converts it into Kotlin data classes using GSON.

### 2. UI Updates (MVVM)
The data is held in `LiveData` objects within the `ViewModel`. The `HomeFragment` observes these changes and updates the `RecyclerView` adapters to display the movies on the screen.

### 3. Navigation
The app uses a `BottomNavigationView` with the Jetpack Navigation Component. Switching between Home, Search, and My List is handled by the `nav_graph`.

### 4. Search Functionality
In the `SearchFragment`, every time you type in the search bar, a query is sent to the TMDB search endpoint. The results are displayed in real-time.

### 5. Local Storage (Room)
When you click "Add to My List" on a movie detail screen, the movie's data is saved into a local SQLite database using Room. The `MyListFragment` then pulls this data to show your saved movies even when offline.

---

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
