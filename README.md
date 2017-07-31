# NewsAppGuardianAPI

An Android app that alows the user to read the news and customize the search.
An app is using Guardian News API.
## Features:

* Discover the most wanted news articles, set tags, query and sort result
* Added refresh content on the screen swipe
* Used SharedPreferences along with GuardianNews API to allow user save search preferences in persistent memory.
* Developed multi-thread application (AsyncTaskLoader) for better user experience.
* UI optimized for phone and tablet
* Managed to significantly increase application performance and efficient memory management by used of ListView and Loader Class

## Screenshots:
![ListView](https://github.com/Wkurylo/NewsAppGuardianAPI/blob/master/app/src/main/res/images/newsapp_list.png "ListView")

![SharedPreferences](https://github.com/Wkurylo/NewsAppGuardianAPI/blob/master/app/src/main/res/images/newsapp_sharedpreferences.png "SharedPreferences")

![QuerySearch](https://github.com/Wkurylo/NewsAppGuardianAPI/blob/master/app/src/main/res/images/newsapp_query.png "QuerySearch")

## Developer setup
### Requirements
Java 8
Latest version of Android SDK and Android Build Tools
### API Key
The app uses Guardian News API `api-key=test` to get news article.

Tu use other API change in:
uriConstructor(String urlString) method in MainActivity.java

## License

Copyright 2017 Wojciech Kurylo

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
