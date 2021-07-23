# Start project

### Install redis

### On Mac
brew install redis
brew services start redis

redis-cli

paste this string

SET yesterday "[{\"stationId\":145,\"temperature\":256}]

Other Os
Feel free to google

### Signup
curl --location --request POST 'https://localhost:9443/user/login' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--header 'Cookie: X-Auth=eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImlzcyI6ImlkZW50aXR5IiwiZXhwIjoxNjI3MTMxNjQwLCJpYXQiOjE2MjcxMzA3NDB9.rBXV4iykSFTCRheZhLxV8cWRk8u-FLwBBEReW5JA8dhUg-d3dg2phpkbhBAmBVaxzbknku0wJEs3eLbujVPZrHmIa8zyvLdIZ8_ACx--h1v4nRuot9i-DLxgsF2lAKOpFVT4a5b1I19Xchej2hvXxa-8gfqKRxKoFbg66Y3oS-kX4A2mIvKdqIK3tKAvDPhX2UmlwHCjdcoQ7Woqe0udHjsJXMZN3uZ_HvSM6Y2Ec39-fQNtK6eQ6WX12jzW_FhrECGgG2mQdHljPTXfvpEcEjtmT5bgLCqECE-m8BJ_19Q8aial5s7OsYdwRIHSyTpaLK7IhCB9JLFaC_wuB9Qrcg' \
--data-raw '{
"email": "test@gmail.com",
"password": "secret"
}'