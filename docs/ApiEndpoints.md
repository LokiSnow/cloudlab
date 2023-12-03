## User endpoints

- `GET /users`: 
Returns a list of all users.

- `POST /users`: 
Creates a new user with the provided details.
Request:
```json
{
    "name": "John Doe",
    "password": "password123",
    "email": "john.doe@example.com"
}
```

- `GET /users/{id}`: 
Returns the details of the user with the specified ID.

- `PUT /users/{id}`: 
Updates the details of the user with the specified ID.

- `DELETE /users/{id}`: 
Deletes the user with the specified ID.


--- 
## User Profile endpoints

- `GET /users/:userId/profile`: 
Returns the profile details of the user with the specified ID.

- `PUT /users/:userId/profile`: 
Updates the profile details of the user with the specified ID.
```json
{
    "userId": "123",
    "firstName": "John",
    "lastName": "Doe",
    "bio": "I am a software engineer",
    "profilePic": "https://example.com/profile-pic.jpg"
}
```

--- 
## Post endpoints

- `GET /posts?last={id}`:  
optional parameter: `last` - the post.id in the current page  
Returns a list of all posts(no order guarantee, just load from dynamodb).  
Adapt to dynamodb continues page solution, `last` is used to load last page

- `POST /posts`: 
Creates a new post with the provided details, don't add tags in this request.
```json
{
    "title": "Test Sharing 3",
    "content": "Sharing something 3",
    "previewContent": "Sharing something 3...",
    "categoryCode": "Sharing",
    "author":"Loki"
}
```
Response:
 ```json
{
    "resultCode": "000",
    "resultMsg": "success",
    "content": {
        "content": "Sharing something 3",
        "previewContent": "Sharing something 3...",
        "title": "Test Sharing 3",
        "categoryCode": "Sharing",
        "author": "Loki",
        "tags": null,
        "views": 0,
        "likes": 0,
        "comments": 0,
        "id": "1701600633000001",
        "createdTime": "2023-12-03T18:50:33.27880491",
        "updatedTime": "2023-12-03T18:50:33.278812522",
        "logicDeleted": false,
        "version": 0
    }
}
```

- `GET /posts/{id}`: 
Returns the details of the post with the specified ID.

- `PUT /posts/{id}`: 
Updates the details of the post with the specified ID.

- `DELETE /posts/{id}`: 
Deletes the post with the specified ID.

- `GET /posts/{categoryCode}?last={id}`: 
optional parameter: `last` - the post.id in the current page  
Returns a list of all posts with specified category id.

- `PUT /posts/{id}/like`: 
Increases the number of likes of the post with the specified ID by 1.

- `POST /posts/{id}/addTag`:
add tags for posts
Request:
```json
{
    "id": "aws",
    "name": "Aws"
}
```

- `Post /posts/search`:
Search by key fields
```json
{
    "title": "aws"
}
```


--- 
## Category endpoints

- `GET /categories`: 
Returns a list of all categories.

- `POST /categories`: 
Creates a new category with the provided details.
```json
{
    "code": "Technology",
    "description": "Articles about technology and gadgets"
}
```

- `PUT /categories/{id}`: 
Updates the details of the category with the specified ID.

- `DELETE /categories/{id}`: 
Deletes the category with the specified ID.

--- 
## Tag endpoints

- `GET /tags`: 
Returns a list of all tags.

- `POST /tags`: 
Creates a new tag with the provided details.
the tag ID will be lower case tag name, and replace space to '-'
```json
{
    "name": "Aws"
}
```
- `GET /tags/{id}`: 
Returns the details of the tag with the specified ID.

- `PUT /tags/{id}`: 
Updates the details of the tag with the specified ID.

- `DELETE /tags/{id}`: 
Deletes the tag with the specified ID.

- `GET /tags/{id}/posts`: 
Returns a list of all posts under the specified tag.


--- 
## Comment endpoints

- `POST /comments`: 
Creates a new comment with the provided details;
increase post comments count.

- `GET /comments/{postId}`: 
Returns the details of the comment with the specified Post ID.

- `PUT /comments/{id}`: 
Updates the details of the comment with the specified ID.

- `DELETE /comments/{id}`: 
Deletes the comment with the specified ID.


--- 
## Other endpoints

- `GET /dashboard`: 
Returns the statistics and initial information for the whole site.

