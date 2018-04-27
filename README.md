# OS Places address lookup

[![Build Status](https://travis-ci.org/DEFRA/os-places-address-lookup.svg?branch=master)](https://travis-ci.org/DEFRA/os-places-address-lookup)

A REST based wrapper built using [Dropwizard](http://dropwizard.io/) around the [OS Places API](http://www.ordnancesurvey.co.uk/business-and-government/products/os-places/index.html).

Not all features of the API are exposed with this service. It was built to support address lookup using a postcode for another service, and to provide the latitude and longitude for a given address.

For further details about the OS Places API check out their [user guide](http://www.ordnancesurvey.co.uk/docs/user-guides/os-places-user-guide-technical-specification.pdf).

## Prerequisites

* [Java Open JDK 8](http://openjdk.java.net/install/)
* Key and URL for [OS Places service](http://www.ordnancesurvey.co.uk/business-and-government/products/os-places/index.html)

## Installation

Clone the repo

```bash
git clone https://github.com/DEFRA/os-places-address-lookup && cd os-places-address-lookup
```

## Configuration

The file *configuration.yml* represents a template configuration. If you are happy to use the defaults the only value you'll need to replace is `WCRS_OSPLACES_KEY`.

## Build

The project uses [Maven](https://maven.apache.org/) as its build tool, and [Maven Wrapper](https://github.com/takari/maven-wrapper) to handle getting a version of Maven on your machine to download the project.

So to build the project call

```bash
./mvnw clean package
```

The normal command on a machine where Maven is installed is `mvn clean package`.

## Run tests

Currently the unit tests included in the project rely on an actual connection to OS Places as it has not been mocked. Because of this it can cause issues during a typical Maven build as by default it will attempt to run any unit tests it finds.

Also the unit tests cannot read from the [configuration.yml](configuration.yml) as the app would when started, so you must set some environment variables before attempting to run the tests

- `WCRS_OSPLACES_KEY`
- `WCRS_OSPLACES_URL`

For these reasons we have configured Maven not to automatically run tests when a build takes place. Instead those working on the project should manually run the tests as and when required

```bash
./mvnw test
```

## Start the service

Once built execute the jar file, providing 'server' and the name of the configuration file as arguments.

```bash
java -jar target/os-places-address-lookup-*.jar server configuration.yml
```

Once the application server is started you should be able to access the service in your browser. Confirm this by going to [http://localhost:9191](http://localhost:9191) and the operational menu will be visible.

## Using the service

The following provides examples of how to use the service.

1) Get a list of all addresses for a given postcode

```bash
$ curl -GET localhost:8006/addresses.json?postcode=BS15AH
```

2) Get the detailed address info for an address given by its unique identifier (moniker or UPRN)

```bash
$ curl -GET localhost:8006/addresses/340116.json?
```

## Contributing to this project

If you have an idea you'd like to contribute please log an issue. Else if you would like to contribute but are stuck for ideas check out the issue log.

All contributions should be submitted via a pull request.

## License

THIS INFORMATION IS LICENSED UNDER THE CONDITIONS OF THE OPEN GOVERNMENT LICENCE found at:

http://www.nationalarchives.gov.uk/doc/open-government-licence/version/3

The following attribution statement MUST be cited in your products and applications when using this information.

>Contains public sector information licensed under the Open Government license v3

### About the license

The Open Government Licence (OGL) was developed by the Controller of Her Majesty's Stationery Office (HMSO) to enable information providers in the public sector to license the use and re-use of their information under a common open licence.

It is designed to encourage use and re-use of information freely and flexibly, with only a few conditions.
