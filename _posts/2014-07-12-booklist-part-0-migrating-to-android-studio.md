---
layout: post
title: "BookList, Part 0: Migrating to Android Studio"
description: ""
category: ""
tags: [ide, booklist, android]
---
{% include JB/setup %}

##What am I talking about?

Ok, I'm actually doing this. I am making another Android app and I'm going to use this blog to keep track of what I do, how I do it and neat and nifty things I've discovered. I finished and published my first app about a month or so ago. It was my resume as an app and it was called [Faisal Alqadi Resume](https://play.google.com/store/apps/details?id=com.faisalalqadi.resume). I think it's pretty cool and if you've got the time, I'd appreciate some feedback on it.

Anywho, this second app was actually the very first project I started working on. I took a huge break to finish off the resume app though, but now I'm back on it. I found that I needed something to keep track of the books that I wanted to read. Some sort of list, with the ability to sort by how much I wanted to read each book. I'm fairly certain there are apps out there that allow you to do this, but they all overcomplicate things (GoodReads for one). I thought why not, I'll just make one for myself, and my close friends. And if it'll help out other people, all the better.

What did this app have to do then? For one, it needed to have access to and be able to search a large (massive) database for books. My first iteration of the project actually relied on the free GoodReads api. Once the book was found, the user would be able to rate the book, and the app would then save the book. The user can go back to the book list, which could be sorted by date added (descending or ascending) or by rating given to each book. That way, the user can see the books sorted by priority, or by date and from there can choose which book to read next. 

That would be the very basic functionality. I added two more lists to that, one for already read books and one for books currently being read. A total of three lists: future, past and present. Coolio. The user would be able to swipe a book in the future list (books to read) and the book will show up in the present list (books currently being read). Swiping a book in the present list will send it to the past list (books already read).

At that stage, I had something that would actually work and do what I wanted it to do, especially when I was out browsing a bookstore. What I needed to do was style the app, so it didn't look like it was made by a three year old. The experience I got from my resume app will help in this regard. Another thing I needed to do was add some details when searching and looking for a book. All the user saw when he clicked the book was a very small book cover image, the title of the book, the rating bar to rate the book, and an edit text box so that the user can save some notes about the book (e.g. Nathan said this book was hilarious!). I think the note feature is pretty handy, so I'll keep that in there. A friend recommended I perhaps add the Amazon/eBay pricing and a link so that the user could buy the book. That's a pretty cool idea but pretty far off ahead.

##Cool, now what?

Well, I've got a bunch of my old source code that I can reuse, which I probably will do. I am going to look around for another free api, to use with the GoodReads api.

All my app dev experience has been with Eclipse, so I decided I am going to try Android Studio from start to finish for this project. It'll give me some much needed experience with Gradle and Maven. Plus, it isn't Eclipse.

I spent a bunch of yesterday getting my environment set up. I downloaded Android Studio, made sure the dark theme was on (everything needs a dark theme) and downloaded a few plug ins. Plug ins installed include:

- Android ButterKnife Zelezny: because I plan on using Jake Wharton's awesome ButterKnife library for injection and boilerplate removal
- CodeGlance: embeds a code minimap into the editor, similar to Sublime
- Genymotion: because it blows the built in emulator out of the water
- Key promoter: pops up with keyboard shortcuts whenever I do something, to help introduce me to Android Studio's shortcuts
- Otto plugin: because I think I plan on using Otto and its event buses
- Android Parcelable code generator: in case I need to implement the parcelable class, this'll generate all the boilerplate code

I fiddled around with gradle build file and got the project to depend on two external libraries: OkHttp for some awesome networking (I usually use RoboSpice, but I decided to branch out) and AutoTextView, for a text view that automatically resizes the text based on the amount of text.

Hopefully, I'll do something for the next blog post.
