FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/hello-world-1.0-SNAPSHOT.jar /app/hello-world.jar

EXPOSE 8090

# Add the server.port parameter if you don't use Springboot default port 8080
CMD ["java", "-jar", "hello-world.jar", "--server.port=8090"]
AWS_ACCESS_KEY_ID = "AKIAIOSFODNN7EXAMPLE"
AWS_SECRET_ACCESS_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
DB_HOST=localhost
DB_USER=admin
DB_PASSWORD=SuperSecret123
const stripe = require('stripe')('sk_test_4eC39HqLyjWDarjtT1zdp7dc');
SLACK_WEBHOOK_URL="https://hooks.slack.com/services/T00000000/B00000000/XXXXXXXXXXXXXXXXXXXXXXXX"
JWT_SECRET=MyUltraSecretKeyThatShouldNotBeHere
{
  "client_id": "1234567890-abc.apps.googleusercontent.com",
  "client_secret": "GOCSPX-abc123XYZ456"
}


-----BEGIN RSA PRIVATE KEY-----
MIIEpAIBAAKCAQEA7vN1x...
-----END RSA PRIVATE KEY-----

