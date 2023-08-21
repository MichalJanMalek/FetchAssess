# FetchAssess
Fetch Assessment for Software Engineer (Mobile) Apprenticeship
This native Android app, developed in Java, retrieves data from the provided JSON URL and displays it to the user in an organized and sorted list based on certain criteria.

## Features

- Retrieves data from [hiring.json](https://fetch-hiring.s3.amazonaws.com/hiring.json).
- Displays items grouped by "listId" first then sorted by "name".
- Filters out items with blank or null "name" values.
- Reverse the sorted list data
- Search through the list by searching for name or listID

## Screenshots

The data from the required source filtered and sorted accordingly<br>
<img src="https://github.com/MichalJanMalek/FetchAssess/blob/main/Screenshots/Screenshot_Fetch1.png" width="300"><br>


The same list but reversed after clicking the reverse button on the bottom<br>
<img src="https://github.com/MichalJanMalek/FetchAssess/blob/main/Screenshots/Screenshot_FetchReversed.png" width="300"><br>

The list being searched through<br>
<img src="https://github.com/MichalJanMalek/FetchAssess/blob/main/Screenshots/Screenshot_FetchSearch.png" width="300"><br>

The searched list reversed<br>
<img src="https://github.com/MichalJanMalek/FetchAssess/blob/main/Screenshots/Screenshot_SearchReverse.png" width="300"><br>

Specific search of the list<br>
<img src="https://github.com/MichalJanMalek/FetchAssess/blob/main/Screenshots/Screenshot_Specific.png" width="300"><br>

## Installation

1. Clone this repository.
2. Open the project in Android Studio (version  or later).
3. Build and run the app on an Android emulator or device with the latest release mobile OS.

## Usage

Upon launching the app, the list of items retrieved from the JSON URL will be displayed in a user-friendly manner, grouped by "listId" and sorted by "name". Items with blank or null "name" values will be filtered out, ensuring a clean and organized user experience.

## Test Cases

1. Scrolll through list to the bottom
2. Click button on the bottom to reverse the list at any point during the scroll
3. Use the search feature to find specific item
4. Reverse the searched filtered list
