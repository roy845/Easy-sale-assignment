<table>
  <tr>
    <td style="text-align: center; vertical-align: middle;">
      <h2>Android Developer Assignment</h2>
    </td>
    <td style="text-align: center; vertical-align: middle;">
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
- [Screenshots](#screenshots)
- [Contributing](#contributing)

## Overview

This Android application demonstrates the ability to work with RESTful APIs, local databases, and user interface components. The goal is to create an application that performs CRUD operations on user data, stores it locally, and enhances the user experience with thoughtful design and functionality.

## Features

● <b>Fetch Data from API:</b> Retrieves a list of users using the ReqRes API. <b> https://reqres.in/api/users </b>

● <b>Local Data Storage:</b> Utilizes Room to store user data locally.

● <b>Display Data:</b> Shows user data in a RecyclerView with Image on CardView.

● <b>CRUD Operations:</b>

        ● Add new users.

        ● Update existing user details.

        ● Delete users.

        ● Delete ALL users.

        ● Search users.

<b>Enhanced UI/UX:</b>

        ● Improved design elements.

        ● Custom animations and transitions.

        ● Theming and styling following Material Design guidelines.

## Technical Requirements

        ● Programming Language: Java

        ● Architecture: MVVM

        ● Networking Library: Retrofit

        ● Database Library: Room

        ● Version Control: GitHub repository

## Architecture

The application follows the MVVM (Model-View-ViewModel) architecture.

        ● Model: Represents the data layer, interacting with Room for local data storage and Retrofit for network requests.

        ● View: Includes Activities and Fragments that display the data.

        ● ViewModel: Handles the data preparation for the UI and manages UI-related data.

## Libraries Used

        ● Retrofit: For handling API requests.

        ● Room: For local database storage.

        ● RecyclerView: For displaying the list of users.

        ● Glide: For image loading within RecyclerView.

        ● Material Components: For UI components following Material Design guidelines.

        ● ViewModel Manages and holds the paginated data across configuration changes, ensuring data is only fetched and stored once, and preventing memory leaks.

        ● LiveData Observes and updates the UI automatically with new data as it becomes available, ensuring the RecyclerView displays additional items as pages are loaded.

        ● eazegraph for showing user related data in graphs.

## Setup Instructions

1.  <b> Clone the Repository: </b>

        ● git clone https://github.com/roy845/Easy-sale-assignment.git

2.  <b> Open the Project: </b>

        ● Open the project in Android Studio.

3.  <b> Build the Project: </b>

        ● Ensure that all dependencies are synced by clicking on Sync Project with
        Gradle Files.

        ● Build the project using Build > Make Project.

4.  <b> Run the Application: </b>

        ● Connect an Android device or start an emulator.

        ● Run the application using Run > Run 'app'.

## How to Use

<b> ● Fetching Users: </b>

        ● The app automatically fetches user data from the ReqRes API when launched and displays it in a RecyclerView.

<b> ● Adding a User: </b>

        ● Click on the + 'Add User' floating action button to open a form.

        ● Enter user details and click 'Add'.

<b> ● Updating a User: </b>

        ● Click on a user item in the list to open the edit form.

        ● Update the user’s details and click 'Update'.

<b> ● Deleting a User: </b>

        ● Swipe left on a user item to delete it.
        ● A dialog will open to confirm / cancel the operation.

<b> ● Searching a User: </b>

        ● Searching a user using the textfield found above the recycler view.

<b> ● Graph views: </b>

        ● For number of new users added to the app across the month and the ability to select the 5 past months.
        ● For daily usage in the app in minutes and the ability to select the 5 past weeks.

## Assumptions and Challenges

● Assumptions:

        ● The ReqRes API does not provide real CRUD operations.

        ● Simulated these operations by manipulating local data.

        ● Users are stored locally using Room after being fetched from the API.

● Challenges:

        ● Ensuring smooth and responsive UI while performing database operations.

## Bonus Implementations

        ● Pagination: Implemented pagination for fetching users in batches.

        ● Error Handling: Added comprehensive error handling with user-friendly messages.

        ● Material Design: Followed Material Design guidelines for UI/UX enhancements.

## Screenshots

<b> Splash screen: </b>

<a href="https://ibb.co/YRTwtpY"><img src="https://i.ibb.co/M25QRMW/Whats-App-Image-2024-08-25-at-18-58-59.jpg" alt="Whats-App-Image-2024-08-25-at-18-58-59" border="1"></a>

<b> Welcome dialog: </b>

<a href="https://ibb.co/YpRhLfY"><img src="https://i.ibb.co/4PmpV49/Whats-App-Image-2024-08-26-at-01-01-01.jpg" alt="Whats-App-Image-2024-08-26-at-01-01-01" border="1"></a>

<b> Display list of users: </b>

<a href="https://ibb.co/1R7h9Xg"><img src="https://i.ibb.co/r5FTZpj/Whats-App-Image-2024-08-26-at-01-05-15.jpg" alt="Whats-App-Image-2024-08-26-at-01-05-15" border="1"></a>

<a href="https://ibb.co/KbPhB4j"><img src="https://i.ibb.co/YPCQJn7/Whats-App-Image-2024-08-26-at-01-05-14.jpg" alt="Whats-App-Image-2024-08-26-at-01-05-14" border="1"></a>

<b> CRUD Operations: </b>

<b> Add new user </b>

<a href="https://ibb.co/phmXFN6"><img src="https://i.ibb.co/XYwJR6f/Whats-App-Image-2024-08-25-at-18-38-52.jpg" alt="Whats-App-Image-2024-08-25-at-18-38-52" border="1"></a>

<b> Update existing user </b>

<a href="https://ibb.co/j9t1Q3k"><img src="https://i.ibb.co/mrpM2h5/Whats-App-Image-2024-08-25-at-18-44-53.jpg" alt="Whats-App-Image-2024-08-25-at-18-44-53" border="1"></a>

<b> Delete existing user </b>

<a href="https://ibb.co/BcqxSFW"><img src="https://i.ibb.co/XjSgvM0/Whats-App-Image-2024-08-25-at-18-47-32.jpg" alt="Whats-App-Image-2024-08-25-at-18-47-32" border="1"></a>

<b> Delete ALL users </b>

<a href="https://ibb.co/X5jPN9y"><img src="https://i.ibb.co/JpmT0Nx/Whats-App-Image-2024-08-26-at-00-52-47.jpg" alt="Whats-App-Image-2024-08-26-at-00-52-47" border="1"></a>

<b> Search users </b>

<a href="https://ibb.co/WKSXGJ7"><img src="https://i.ibb.co/12HN7WB/Whats-App-Image-2024-08-25-at-18-49-05.jpg" alt="Whats-App-Image-2024-08-25-at-18-49-05" border="1"></a>

<b> Graph views </b>

<a href="https://ibb.co/CBfnsdC"><img src="https://i.ibb.co/2qTjY2V/Whats-App-Image-2024-08-25-at-18-51-25.jpg" alt="Whats-App-Image-2024-08-25-at-18-51-25" border="1"></a>

## Contributing

Contributions are welcome!

Please fork the repository and submit a pull request with your changes.
