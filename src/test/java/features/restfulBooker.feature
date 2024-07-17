
Feature: Validating the restfulBooker APIs

@CreateToken
Scenario: Verify if token is successfully created using CreateToken API
Given user credentials 
|admin|
|password123|
When  User calls "CreateToken" API with "POST" http request
Then  The API call is successful with status code 200 and response time less than 20000L
And "token" is successfully created


@CreateBooking
Scenario: Verify if booking is successfully created using CreateBooking API
Given "CreateBooking"API payload
When User calls "CreateBooking" API with "POST" http request
Then The API call is successful with status code 200 and response time less than 20000L
And "bookingid" is successfully created


@GetBooking
Scenario: Verify if specific bookingID data successfully exists using GetBooking API
Given The BookingID and Access token
When User calls "GetBooking" API with "GET" http request
Then The API call is successful with status code 200 and response time less than 20000L

And The data in the response body is the same as the previously added data


@PartialUpdateBooking
Scenario: Update specific bookingID data and verify if the data is updated successfully
Given "PartialUpdateBooking"API payload
When User calls "PartialUpdateBooking" API with "PATCH" http request
Then The API call is successful with status code 200 and response time less than 20000L
And data is updated successfully


@DeleteBooking
Scenario: Delete specific bookingID and verify if the data is deleted successfully
Given The BookingID and Access token
When User calls "DeleteBooking" API with "DELETE" http request
Then The API call is successful with status code 201 and response time less than 20000L
And  Data is deleted successfully
