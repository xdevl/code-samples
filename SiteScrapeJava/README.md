# Grocery site scraper
Simple java application that scrapes a grocery website and returns a JSON array of all the products on the page.
This application uses the following technologies:
* Gradle to build and fetch dependencies from the cloud
* Jsoup to download and parse webpages
* Json Jackson to serialize/deserialize Json
* JUnit for unit tests
* Javadoc for documentation
# Build instruction
In order to buil the application you will need Java 1.7+ installed locally and an internet connection to fetch the project dependencies

To build, simply run at the root of the project:
```markdown
gradlew build
```
To run all unit tests:
```markdown
gradlew test
```
To run the application:
```markdown
gradlew run -PappArgs="['webpage url to fetch the product list from']" 
```
if *-PappArgs* is omitted the default url will be used (ie: http://hiring-tests.s3-website-eu-west-1.amazonaws.com/2015_Developer_Scrape/5_products.html)
