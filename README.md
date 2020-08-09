# Introduction

The goal of this documentation is to provide a step-by-stop guide for understanding everything that you can find in this repo. This includes all the dependencies and architecture used; along with the how and why. I won't be going into much detail of what they are but will still provide a brief overview of what each of them are.

### Architecture - A Clean approach

As you might have already noticed this repo is based on Clean Architecture. This implementation of Clean Architecture is based off of great work done by these magnificent developers.

- [Uncle Bob - The Clean Architecture](https://blog.8thlight.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Fernando Cejas - Clean Architecture Sample](https://github.com/android10/Android-CleanArchitecture-Kotlin)
                                                     
The core principles of the clean approach can be summarized as followed:

- **The application code is separated into layers.**

    These layers define the separation of concerns inside the code base.

- **The layers follow a strict dependency rule.**

    Each layer can only interact with the layers below it.

-  **As we move toward the bottom layer — the code becomes generic.**

    The bottom layers dictate policies and rules, and the upper layers dictate implementation details such as the database, networking manager, and UI.
    
### The Model-View-ViewModel Pattern

The main players in the MVVM pattern are:

- The View — that informs the ViewModel about the user’s actions
- The ViewModel — exposes streams of data relevant to the View
- The DataModel — abstracts the data source. The ViewModel works with the DataModel to get and save the data.

#### Why not MVP 

MVP requires that for each Activity/Fragment (View) we define a Presenter. This is a hard bound rule. Presenter holds the reference to the View and View Holds the reference to presenter (1:1 relationship) and that's where the biggest issue lies.
 
ViewModels are simples classes that interacts with the logic/model layer and just exposes states/data and actually has no idea by whom or how that data will be consumed. 

Only View(Activity) holds the reference to ViewModel and not vice versa, this solves our tight coupling issue. Even though this project doesn't take use of this, a single view can hold reference to multiple ViewModels.
                                                                                                                                                                                                                                                     
### Why MVVM with Clean Architecture?

MVVM separates our view (i.e. Activities and Fragments) from our business logic. MVVM is enough for small projects, but when our codebase becomes huge, our ViewModels start bloating and separating responsibilities becomes hard.

MVVM with Clean Architecture is pretty good in such cases. It goes one step further in separating the responsibilities of our code base. It clearly abstracts the logic of the actions that can be performed in our app.

Thus with both of these in action

- Our code is even more easily testable than with plain MVVM.
- Our code is decoupled into more than one layers 
- The package structure is easier to navigate
- The project is even easier to maintain.

The project is further divided into packages based on features (We only have one feature as this is a small project). These feature modules contain specific features of our application which can help us to again decouple different responsibilities to achieve the same benefits as we’ve previously looked at.
                                                     
### Technologies Used
Here’s a short list of technologies used in this project:

- **Dagger 2** ‒ Helps with DI design pattern with implementation in compile-time. Also with the last talk we had and a requirement for this task.
- **RxJava 2** ‒ Most of the core I use required this and also the use of Subject to implement infinite scrolling is just to easy.  
- **Retrofit 2** ‒ It makes it relatively easy to retrieve and upload JSON (or other structured data) via a REST based Webservice. Also you configure which converter is used for the data serialization and custom adapters for data types with ease.
- **Android Architecture Components** - Makes it easier to create lifecycle-aware components, avoid memory leaks, etc
- **Timber** - Easy setup so that logs prints the logs when our application type is debug application.

## Future Extensions

As of writing this I still haven't managed to write a meaningful comment and that's one of the most time consuming parts. Besides that I would like to mark down a few things I would like to have added.

- Add a bit of animation overall. Everything just appears/disappears at the moment
- Add a lot of documentation as we all know how much of a help that is.