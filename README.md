# Project 2 - Popular Movies Stage Two - Trailers, Reviews and Favorites

Android app to display a list of popular or highest-rated movies via [the MovieDB API](https://www.themoviedb.org/settings/api) and additional details of the movie. 

## User Stories
* [x] User can view and play trailers via their favorite browser.
* [x] User can read reviews of a selected movie.
* [x] User can mark a movie as a favorite in the details view by tapping a button (star).
* [x] User can sort movies by their favorites collection as well.

## Required Features
* [x] Create a database and content provider to store the names and ids of the user's favorite movies.
* [x] App should work either in portrait or landscape mode.
* [x] Use onSaveInstanceState/onRestoreInstanceState to recreate Activities.
* [x] When a trailer is selected, the app uses an intent to launch the trailer.
* [ ] Save scroll position of detail activity scrollview on rotation.

## Optional Features
* [ ] Store movie poster, synopsis, user rating and release date, and display them even when offline.
* [ ] Implement sharing functionality to allow user to share the first trailer's YouTube URL from the movie details screen.

## Open-source libraries used
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- [Parceler](https://github.com/johncarl81/parceler) - Serialization library
- [Butterknife](http://jakewharton.github.io/butterknife/i) - Popular view injection library.
- [RxJava 2]
- [Retrofit 2]
- [OkHttp3]
- [Android Architecture Components - Room/LiveData/ViewModel]

## References
These are the websites/sources that were used as references.
- [Jose Mateo](http://mateoj.com/2015/10/07/creating-movies-app-retrofit-picass-android-part2/)
- [CodePath Consuming APIs with Retrofit](https://guides.codepath.com/android/consuming-apis-with-retrofit)
- [Tutorial](https://github.com/arriolac/GitHubRxJava/wiki/Tutorial)
- [How to add interceptor in Retrofit 2.0](https://mobikul.com/use-interceptor-add-headers-body-retrofit-2-0/)
- [Retrofit 2 - How to Add Query Parameters to Every Requst](https://futurestud.io/tutorials/retrofit-2-how-to-add-query-parameters-to-every-request)
- [Leveraging the Gson Library](https://guides.codepath.com/android/Leveraging-the-Gson-Library)
- [Toggle Button Example](https://www.dev2qa.com/android-togglebutton-example/)
- [Custom Toggle Button](http://mohsenoid.blog/how-to-create-a-toggle-button-with-custom-image-and-no-text-in-android/)

## Notes
An API key is required to fetch data from themovieDB API. The key is specified in the file `NetworkUtils.java`.
