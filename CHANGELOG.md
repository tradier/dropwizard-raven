1.1.0
-----

* Update `raven-logback` dependency to 8.0.2
* Add error-prone
* Inherit junit dependency from Dropwizard

1.0.0-1
-------

* Update `dropwizard` dependency to 1.0.0
* Set Java 1.8 as compilation target
* Add `serverName` configuration to appender
* Add `ravenFactory` configuration to appender
* Add `extraTags` configuration to appender

0.9.3
-----

* Backported adjust DroppingRavenLoggingFilter to default to NEUTRAL (eb2285ddcdb5d78e5fce3be5e539a875ea3d809c) to 0.9.x release branch

0.9.2
-----

* Update `raven-logback` to 7.6.0
* Add environment and release options
* Add checkstyle

0.9.1
-----

* Update `raven-logback` dependency to 7.0.0
* Update `dropwizard` dependency to 0.9.2
* Define appender name
* Drop any log events coming from the `raven` package

0.7.1
-----

* Version bump to sync version numbers with Dropwizard releases, no other changes

0.1.2
-----

* Initial release supporting Dropwizard 0.7.0
