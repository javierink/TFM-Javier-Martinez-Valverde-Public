import requests
import json
import datetime
import sys
import database_controller
import math

import pymongo

APPID_OPEN_WEATHER = 'NoDisponibleEnRepositorioPublico'
TOKEN_WAQI = 'NoDisponibleEnRepositorioPublico'
MAX_METRES = 5000.0


'''Query to the weather service'''
def get_pollution_waqi(coord):

    lat, lon = coord
    url = "http://api.waqi.info/feed/geo:" + str(lat) + ";" + str(lon) + "/"

    payload = {'token': TOKEN_WAQI}

    return requests.get(url, params=payload)

'''Query to the pollution service'''
def get_open_weather(coord):

    lat, lon = coord
    url = "http://api.openweathermap.org/data/2.5/weather"

    payload = {'lat' : lat, 'lon' : lon, 'units': 'metric', 'appid': APPID_OPEN_WEATHER}

    return requests.get(url, params=payload)

'''Delete minute and second in a date in millis'''
def zero_min_millis_date(ms):

    # Change time to datetime format
    date = datetime.datetime.fromtimestamp(ms/1000.0)

    # Put 0 in the min and millis
    date = date.replace(minute=0, second=0, microsecond=0)
    
    # Return date in millis
    return int(date.strftime("%s")) * 1000 

def haversine(coord1, coord2):
    R = 6372800  # Earth radius in meters
    lat1, lon1 = coord1
    lat2, lon2 = coord2
    
    phi1, phi2 = math.radians(lat1), math.radians(lat2) 
    dphi       = math.radians(lat2 - lat1)
    dlambda    = math.radians(lon2 - lon1)
    
    a = math.sin(dphi/2)**2 + \
        math.cos(phi1)*math.cos(phi2)*math.sin(dlambda/2)**2
    
    return 2*R*math.atan2(math.sqrt(a), math.sqrt(1 - a))


'''Insert in database the json user and a json in general database of weather, this last must be query to external service with data of the user json'''
def insert_weather_pollution(user_json, user_database):

    ms = user_json["datetime"]
    coord = user_json["lat"] , user_json["lon"]

    # Delete min and sec in the time
    lessMinMs= zero_min_millis_date(ms)

    # It's possible that the user_json doesn't contain a city, in this case always must send de query to weather service. This one will give us a city through the coordinates.
    # This is because android is not possible to give us a city.
    city_exist = False
    if "city" in user_json:
        city_exist = True

    # If exist a city field and exist document in the same time slot, only insert user json
    if city_exist and database_controller.exist_in_weather_pollution(lessMinMs, user_json["country"], user_json["city"]):
        database_controller.insert_weather_pollution(user_json, user_database, "weather_pollution", None)
    # If not exist maked a query to external service and create general weather pollution json
    else:
        try:
            # weather query
            r = get_open_weather(coord)
            r.raise_for_status()

            json_weather = r.json()

            # pollution query
            r = get_pollution_waqi(coord)
            r.raise_for_status()

            json_pollution = r.json()

            # Created a general weather pollution json as from weather json and pollution json
            general_json = {}

            if json_weather["cod"] == 200:
                # General info
                general_json['datetime'] = lessMinMs
                general_json['country'] = user_json["country"]

                # In case that no exist city in the recived json, puts the name of the area in the json_weather
                if(city_exist):
                    general_json['city'] = user_json["city"]
                else:
                    general_json['city'] = json_weather["name"]
                    user_json["city"] = json_weather["name"]


                # Weather info
                general_json['weather'] = {}
                if "main" in json_weather:
                    general_json['weather'] = json_weather["main"]
                if "weather" in json_weather and len(json_weather["weather"]) != 0 and "description" in json_weather["weather"][0]:
                    general_json['weather'] ['description'] = json_weather["weather"][0]["description"]
                if "wind" in json_weather and "speed" in json_weather["wind"]:
                        general_json['weather'] ['wind_speed'] = json_weather["wind"]['speed']
                if "clouds" in json_weather and "all" in json_weather["clouds"]:
                        general_json['weather'] ['clouds'] = json_weather["clouds"]["all"]

            # Pollution info
            general_json['pollution'] = {}

            # Check if the beacon of the pollution service is near to the user position, if not is near doesn't put the pollution information
            coord_beacon = json_pollution["data"]["city"]["geo"][0], json_pollution["data"]["city"]["geo"][1]
            distance = haversine(coord, coord_beacon)

            if json_pollution["status"] == "ok" and distance < MAX_METRES:
                if "data" in json_pollution and "iaqi" in json_pollution["data"]:
                        for field in json_pollution["data"]["iaqi"]:
                            general_json['pollution'][field] = json_pollution["data"]["iaqi"][field]["v"]

            # Insert in database
            database_controller.insert_weather_pollution(user_json, user_database, "weather_pollution", general_json)

        except requests.exceptions.HTTPError as errh:
            print ("Http Error:",errh)
        except requests.exceptions.ConnectionError as errc:
            print ("Error Connecting:",errc)
        except requests.exceptions.Timeout as errt:
            print ("Timeout Error:",errt)
        except requests.exceptions.RequestException as err:
            print ("OOps: Something Else",err)



