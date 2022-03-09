## Readme
The project is constructed in *Spring* framework.

All APIs implemented accepts input as request body in JSON format. Below illustrate the URL and keys accepted.
URL in "./xxx" format, the "./" means the root URL for your *Spring* host.

All APIs can be tested using the *Postman* collection [file](./HSBC_Interview.postman_collection.json).
In the provided collection the root URL is http://localhost:8080. 

 1. ./createUser
    Corresponding to the service requirement No. 1 Create user
    * username - String, representing the username
    * password - String, the plaintext of password, will not be stored in system
 2. ./deleteUser
    Corresponding to the service requirement No. 2 Delete user
     * username - String, representing the username
     * password - String, the plaintext of password, will not be stored in system
 3. ./createRole
    Corresponding to the service requirement No. 3 Create role
    * roleName - String, representing the role name
 4. ./deleteRole
    Corresponding to the service requirement No. 4 Delete role
     * roleName - String, representing the role name
 5. ./assignRole
    Corresponding to the service requirement No. 5 Add role to user
     * username - String, representing the username
     * roleName - String, representing the role name
 6. ./authentication
    Corresponding to the service requirement No. 6 Authenticate
    * username - String, representing the username
    * password - String, the plaintext of password, will not be stored in system
 7. ./invalidate
    Corresponding to the service requirement No. 7 Invalidate
    * token - String, representing the token string to pass authentication
 8. ./checkRole
    Corresponding to the service requirement No. 8 Check role
     * token - String, representing the token string to pass authentication
     * roleName - String, representing the role name
 9. ./allRoles
    Corresponding to the service requirement No. 9 All roles
     * token - String, representing the token string to pass authentication
    
Please note the web service API accepts **different** input values than the Spring service.
In the Spring service class, the code accepts exactly the same input as the document illustrated.
Difference in inputs happens for item 2, 4, 5, 8.

The provided java unit tests are for *Spring* service only.