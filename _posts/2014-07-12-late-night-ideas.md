---
layout: post
title: "Late Night Ideas"
description: ""
category: ""
tags: [ideas, booklist, android]
---
{% include JB/setup %}

- Search bar to look up books; have an advanced search button that expands the search bar and reveals extra fields to fill out, like: title, author, publisher, etc.... Advanced search bar should animate when it expands and should animate back to normal state when the search commences.
- [Multiple APIs](http://www.programmableweb.com/news/53-books-apis-google-books-goodreads-and-sharedbook/2012/03/13), with some logic to filter out duplicates. APIs I'm currently thinking of: Google Books, GoodReads, Randomhouse, OpenLibrary
	- Multiple APIs means more search results (not always beneficial) but it also means having to find a way to format and sort all the data received in an effecient and elegant manner. I have to take into account what data type each API returns (XML or JSON) and what actual data they return. I'm sure I can get by with a parent class called Book, and a number of classes that inherit from the Book class, but encapsulate data differently according to the API.
- Possible import of books from Google Play. This would mean an addition of an unsorted list, and the user could then sort the books at his leisure.
- Search should ideally check books currently saved in the lists, before searching the web. One way of doing this would be to provide smart suggestions as the user is typing in the search query. Another would be to split the search result page into two rows, one for books that match the string query, and another for the results of the online look-up. The offline results will show up instantly, while the online result row displays a loading circle (or something cooler) as the data is retrieved. 

I still haven't decided on the theme and design of this app. Soon, though. Soon. Maybe a Material Design mock up?