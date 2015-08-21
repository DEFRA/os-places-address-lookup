# OS places address lookup

A REST based wrapper built using [Dropwizard](http://dropwizard.io/) around the [OS Places API](http://www.ordnancesurvey.co.uk/business-and-government/products/os-places/index.html).

Not all features of the API are exposed with this service. It was built to support address lookup using a postcode for another service, and to provide the latitude and longitude for a given address.

For further details about the OS Places API check out their [user guide](http://www.ordnancesurvey.co.uk/docs/user-guides/os-places-user-guide-technical-specification.pdf).

## Prerequisites

* [Java 7 JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)
* [Maven](http://maven.apache.org/) (version 3.0 or above) - for building the service
* Key and URL for [OS Places service](http://www.ordnancesurvey.co.uk/business-and-government/products/os-places/index.html)

## Configuration

The file *configuration.yml* represents a template configuration. If you are happy to use the defaults the only value you'll need to replace is `WCRS_ADDRESS_OSPLACES_KEY`.

## Installation

Clone the repository to your target machine then build it using Maven.

```bash
$ mvn clean package
```

This will automatically run the tests as well which require a working connection to the OS Places API.


## Start the service

Once built execute the jar file, providing 'server' and the name of the configuration file as arguments.

From the command line:

```bash
$ java -jar target/address-lookup-osplaces-0.1.jar server configuration.yml
```

Once the application server is started you should be able to access the service in your browser. Confirm this by going to [http://localhost:9191](http://localhost:9191) and the operational menu should be visible.

## Using the service

The following provide examples of how to use the service.

1) Get a list of all addresses for a given postcode

```bash
$ curl -GET localhost:9190/addresses.json?postcode=BS15AH
```

2) Get the detailed address info for an address given by its unique identifier (moniker or UPRN)

```bash
$ curl -GET localhost:9190/addresses/340116.json?
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
