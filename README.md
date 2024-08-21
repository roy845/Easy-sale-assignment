<table>
<tr>
<td>

## Android Developer Assignment

</td>
<td>

<img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT2oS8IZc-LuWk9ZsLUx8_de1AnL47_vxjS1abc-b5fx1jd8kFP_FKwayC0gtJtLxowcYo&usqp=CAU" alt="Android Developer Assignment Image" />

</td>
</tr>
</table>

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technical Requirements](#technical-requirements)
- [Architecture](#architecture)
- [Libraries Used](#libraries-used)
- [Setup Instructions](#setup-instructions)
- [How to Use](#how-to-use)
- [Assumptions and Challenges](#assumptions-and-challenges)
- [Bonus Implementations](#bonus-implementations)
- [Testing](#testing)
- [Future Improvements](#future-improvements)
- [Contributing](#contributing)

## Overview

This Android application demonstrates the ability to work with RESTful APIs, local databases, and user interface components. The goal is to create an application that performs CRUD operations on user data, stores it locally, and enhances the user experience with thoughtful design and functionality.

## Features

● <b>Fetch Data from API:</b> Retrieves a list of users using the ReqRes API.

● <b>Local Data Storage:</b> Utilizes Room to store user data locally.

● <b>Display Data:</b> Shows user data in a RecyclerView with Image on CardView.

● <b>CRUD Operations:</b>

        ● Add new users.

        ● Update existing user details.

        ● Delete users.

        ● Search users.

<b>Enhanced UI/UX:</b>

        ● Improved design elements.

        ● Custom animations and transitions.

        ● Theming and styling following Material Design guidelines.

## Technical Requirements

        ● Programming Language: Java

        ● Architecture: MVVM or MVP pattern

        ● Networking Library: Retrofit

        ● Database Library: Room

        ● Version Control: GitHub repository

## Architecture

The application follows the MVVM (Model-View-ViewModel) architecture.

        ● Model: Represents the data layer, interacting with Room for local data storage and Retrofit for network requests.

        ● ViewModel: Handles the data preparation for the UI and manages UI-related data.

        ● View: Includes Activities and Fragments that display the data.

## Libraries Used

        ● Retrofit: For handling API requests.

        ● Room: For local database storage.

        ● RecyclerView: For displaying the list of users.

        ● Glide: For image loading within RecyclerView.

        ● Material Components: For UI components following Material Design guidelines.

        ● Paging3 for pagination.

        ● ViewModel Manages and holds the paginated data across configuration changes, ensuring data is only fetched and stored once, and preventing memory leaks..

        ● LiveData Observes and updates the UI automatically with new data as it becomes available, ensuring the RecyclerView displays additional items as pages are loaded.

## Setup Instructions

1.  Clone the Repository:

        git clone https://github.com/roy845/Easy-sale-assignment.git

2.  Open the Project:

    ● Open the project in Android Studio.

3.  Build the Project:

    ● Ensure that all dependencies are synced by clicking on Sync Project with
    Gradle Files.

    ● Build the project using Build > Make Project.

4.  Run the Application:

        ● Connect an Android device or start an emulator.

        ● Run the application using Run > Run 'app'.

## How to Use

● Fetching Users:

        ● The app automatically fetches user data from the ReqRes API when launched and displays it in a RecyclerView.

● Adding a User:

        ● Click on the 'Add User' floating action button to open a form.

        ● Enter user details and click 'Add'.

● Updating a User:

        ● Click on a user item in the list to open the edit form.

        ● Update the user’s details and click 'Update'.

● Deleting a User:

        ● Swipe left on a user item to delete it.

● Searching a User:

        ● Searching a user using the textfield found above the recycler view.

## Assumptions and Challenges

● Assumptions:

        ● The ReqRes API does not provide real CRUD operations.

        ● Simulated these operations by manipulating local data.

        ● Users are stored locally using Room after being fetched from the API.

● Challenges:

        ● Handling large data sets and pagination effectively.

        ● Ensuring smooth and responsive UI while performing database operations.

## Bonus Implementations

        ● Pagination: Implemented pagination for fetching users in batches.

        ● Error Handling: Added comprehensive error handling with user-friendly messages.

        ● Material Design: Followed Material Design guidelines for UI/UX enhancements.

## Contributing

Contributions are welcome!

Please fork the repository and submit a pull request with your changes.
