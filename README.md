# REST server example with Collection+Json using Jersey/Grizzly

This example is far from done :)

Features
* gzip encoding
* using a hypermedia media-type like collection+json with jersey
* caching
* conditional PUT



## Domain

The domain chosen for this example is pretty simple. We have two domain objects Sensor and SensorReadings. 

Sensor: name, location and a list of readings
SensorReadings: time, temperature and optionally humidity. 

The domain was chosen for simplicity and also because it highlights the need for not directly exposing your domain 
objects as REST resources. In this particular case when we expose a sensor we also want the latest reading for it in 
the same resource. 


## Discussion

Not really sure on the best way to handle the transformation of data from the Service/DB to the Collection entity that
the collection+json framework needs. 

Where do you declare links and properties? 

I think the best way will be to have a dedicated DTO class that is technology aware. In general I think technology 
agnostic DTO's are good, but in this case it is a very descriptive way of stating the intent. 

So we use DTO's and annotations in them to state the intent of the item's in the Collection. But where do we state
the template/queries and other links for the collection resource? I think the best place would be in the 
controller/resource object. That was you have two places where intent is stored. 

## Testing with httpie

### list sensors
http :8080/sensors

### Add a new sensor
http :8080/sensors Content-Type:"application/vnd.collection+json" Accept:"*/*" < post.json

### delete a sensor

http DELETE :8080/sensors/sensor/TestSensor
