## Description
Just a fun project where I explore effect systems (in this case, cats-effect), tagless final and hexagonal architecture in Scala 3 in order to stay up-to-date on the latest development practices in Scala.

I'm not using Scala in professional capacity at the moment, so please don't expect this repo to be a perfect representation of the current conventions.

Also, this repo only gets updated every now and then, so some features might be incomplete or outright broken. 

## Running
The project includes a Docker compose stack to run infrastructural components. Make sure that this stack is up and running before launching the project.
```bash
$ docker-compose up -d
```

Once the Docker compose stack is up, you can either run individual projects from your favorite IDE, or from the command line using sbt commands.

If you'd like to see what's stored in the database you can connect to it at `postgres://localhost:5432/postgres` with the username `postgres` and password `postgres`.

## API Requests
As of right now there's a single API endpoint that is operational and that's the account registration endpoint.

To test it out, run the following request:

```
POST http://localhost:9000/api/v1/auth/register
```

Request Body:
```json
{
  "firstName": "Foo",
  "lastName": "Bar",
  "organizationName": "Foo Bar Inc.",
  "gsmNumber": "905551111111",
  "email": "foo@bar.com",
  "password": "123",
  "passwordRepeat": "123"
}
```

Once you've sent out the request you should receive an HTTP 200 response with an empty JSON body (e.g. `{}`), and you should be able to see the newly inserted row in the `account` table.

## License
MIT
