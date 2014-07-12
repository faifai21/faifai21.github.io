---
layout: page
title: Hello Fai!
tagline: I shall blog as I see fit
---
{% include JB/setup %}
 
# Most Recent Post

<div class="blog-index">  
  {% assign page = post %}
  {% assign post = site.posts.first %}
  {% assign content = post.content %}
  {% include post_detail.html %}
</div>

## All Posts

<ul class="posts">
  {% for post in site.posts %}
    <li><span>{{ post.date | date_to_string }}</span> &raquo; <a href="{{ BASE_PATH }}{{ post.url }}">{{ post.title }}</a></li>
  {% endfor %}
</ul>

## Footnote

I started this in November of 2013, made two posts and then kind of forgot about it. I really hope I don't make another two posts and forget about it.





