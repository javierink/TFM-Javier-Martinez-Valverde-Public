import pymongo
import json
import collections
import csv
import datetime

# This function search a survey and complete the dict ofa measurement
def fillWithGeneralSurvey(prefix, nQ, type, dict, surveyscol, ms):
    

    query = { "$and": [ { "datetime": { "$gt": ms } }, { "type": type }]}

    surveys = surveyscol.find(query)

    selectedSurvey = None
    timeMark = None

    for s in surveys:
        if(timeMark == None):
            timeMark = s['datetime']
            selectedSurvey = s
        
        if(timeMark > s['datetime']):
            timeMark = s['datetime']
            selectedSurvey = s
        
    
    # If exist the survey, the data is completed
    if(selectedSurvey != None):
        for i in range(nQ):
            dict[prefix + str(i+1)] = selectedSurvey['q'+ str(i+1)]

def write_to_csv_file(file_name, line, mode):
    with open(file_name, mode) as csv_file:
        writer = csv.writer(csv_file, delimiter=';')
        writer.writerow(line)

'''Delete minute and second in a date in millis'''
def zero_min_millis_date(ms):

    # Change time to datetime format
    date = datetime.datetime.fromtimestamp(ms/1000.0)

    # Put 0 in the min and millis
    date = date.replace(minute=0, second=0, microsecond=0)
    
    # Return date in millis
    return int(date.strftime("%s")) * 1000 

print('The program is start')

myclient = pymongo.MongoClient("mongodb://'user':'pass'@localhost:27017/")

generalweatherpollutiondb = myclient["general_weather_pollution_database"]
weathergeneralcol = generalweatherpollutiondb["general"]

arrayDatabases = ["usuario1","usuario2","usuario3","usuario4","usuario5","usuario6" ,"usuario7"]


baseDict = {'gender' : ' ', 'birthdate' : 0, 'birthcountry' : ' ', 'homecountry' : ' ', 'educationlevel' : ' ', 'measurementtime' : 0, 'diastolic' : 0, 'systolic' : 0, 
'pulse' : 0, 'steps' : 0, 'weight' : 0, 'height' : 0, 'physical_q1' : -10, 'physical_q2' : -10, 'physical_q3' : -10, 'physical_q4' : -10, 'physical_q5' : -10, 
'physical_q6' : -10, 'physical_q7' : -10, 'diet_q1' : -10, 'diet_q2' : -10, 'diet_q3' : -10, 'diet_q4' : -10, 'diet_q5' : -10, 'diet_q6' : -10, 'diet_q7' : -10, 
'diet_q8' : -10, 'diet_q9' : -10, 'diet_q10' : -10, 'diet_q11' : -10, 'diet_q12' : -10, 'diet_q13' : -10, 'diet_q14' : -10, 'social_q1' : -10, 'social_q2' : -10, 
'social_q3' : -10, 'social_q4' : -10, 'social_q5' : -10, 'social_q6' : -10, 'social_q7' : -10, 'stress_q1' : -10, 'stress_q2' : -10, 'stress_q3' : -10, 'stress_q4' : -10, 
'stress_q5' : -10, 'stress_q6' : -10, 'stress_q7' : -10, 'stress_q8' : -10, 'stress_q9' : -10, 'stress_q10' : -10, 'stress_q11' : -10, 'stress_q12' : -10, 'stress_q13' : -10, 
'stress_q14' : -10, 'depression_q1' : -10, 'depression_q2' : -10, 'depression_q3' : -10, 'depression_q4' : -10, 'depression_q5' : -10, 'depression_q6' : -10, 
'depression_q7' : -10, 'depression_q8' : -10, 'depression_q9' : -10, 'depression_q10' : -10, 'alcohol_beer' : 0, 'alcohol_wine' : 0, 'alcohol_winemix' : 0, 
'alcohol_destilled' : 0, 'alcohol_destilledmix' : 0, 'alcohol_otherfermented' : 0, 'alcohol_other' : 0, 'smoke_cigarettes' : 0, 'smoke_electronic' : 0, 'smoke_other' : 0, 
'med_Paracetamol' : 0, 'med_Ibuprofeno' : 0, 'med_Naproxeno' : 0, 'med_Dexketoprofeno' : 0, 'med_Acido acetil salicilico' : 0, 'med_Metamizol' : 0, 'med_Morfina' : 0, 
'med_Fentanil' : 0, 'med_Tramadol' : 0, 'med_Nitroglicerina' : 0, 'med_Verapamilo' : 0, 'med_Nifedipino' : 0, 'med_Porpanolol' : 0, 'med_Atenolol' : 0, 'med_Labetalol' : 0, 
'med_Amiodarona' : 0, 'med_Lidocaina' : 0, 'med_Adenosina' : 0, 'med_Digoxina' : 0, 'med_Sulfato de magnesio' : 0, 'med_Insulina' : 0, 'med_Glucagon' : 0, 
'med_Lercanidipino' : 0, 'med_Acenocumarol' : 0, 'med_Alprazolam' : 0, 'med_Midazolam' : 0, 'med_Diazepam' : 0, 'med_Lorazepam' : 0, 'med_Lormetazepam' : 0, 
'med_Clorazepato dipotasico' : 0, 'med_Sertralina' : 0, 'med_Zolpidem' : 0, 'med_Dimenhidrinato' : 0, 'med_Doxilamina' : 0, 'med_Difenhidramina' : 0, 
'med_Dimenhidrinato' : 0, 'med_Hidroxicina' : 0, 'med_Cetirizina' : 0, 'med_Hidroclorotiazida' : 0, 'weather_windspeed' : -1.0, 'weather_clouds' : -1.0, 'weather_temp' : -1.0, 
'weather_tempmax' : -1.0, 'weather_humidity' : -1.0, 'weather_pressure' : -1.0, 'weather_tempmin' : -1.0, 'weather_feels' : -1.0, 'pollution_pm10' : -1.0, 'pollution_co' : -1.0, 
'pollution_so2' : -1.0, 'pollution_o3' : -1.0, 'pollution_no2' : -1.0}

blankDict = collections.OrderedDict(sorted(baseDict.items()))

write_to_csv_file('general_database.csv', ['user'] + blankDict.keys(), 'w')

number = 1

for name in arrayDatabases:

    userdb = myclient["database_" + name]

    surveyscol = userdb["surveys"]
    devicecol = userdb["device"]
    userdatacol = userdb["user_data"]
    weatherpollutioncol = userdb["weather_pollution"]

    write_to_csv_file('database_' + name + '.csv', blankDict.keys(), 'w')

    userdatajson = userdatacol.find_one()

    print("-- " + "Processing database of " + name + ' user')

    devicemeasurements = devicecol.find()

    print("- " + str(devicemeasurements.count()) + " device measurements to be processed")

    for measurementjson in devicemeasurements:

        #print(measurementjson) 
        measurementdict = collections.OrderedDict(blankDict)

        ms = measurementjson['datetime']
        #print(ms)

        # The user's personal data is always the same
        measurementdict['gender'] = userdatajson['gender']
        measurementdict['birthdate'] = userdatajson['birthdate'] 
        measurementdict['birthcountry'] = userdatajson['birthcountry']
        measurementdict['homecountry'] = userdatajson['homecountry'] 
        measurementdict['educationlevel'] = userdatajson['educationlevel']

        # The info of the measurement
        measurementdict['measurementtime'] = measurementjson['datetime']
        measurementdict['diastolic'] = measurementjson['pressuredia'] 
        measurementdict['systolic'] = measurementjson['pressuresys']
        measurementdict['pulse'] = measurementjson['pulse'] 
        measurementdict['steps'] = measurementjson['steps']

        # Need a physical survey to the measurement. One is wanted.

        fillWithGeneralSurvey("physical_q", 7, 1, measurementdict, surveyscol, ms)
        
        # Need a diet survey to the measurement. One is wanted.

        fillWithGeneralSurvey("diet_q", 14, 2, measurementdict, surveyscol, ms)

        
        # Need a social survey to the measurement. One is wanted.

        fillWithGeneralSurvey("social_q", 7, 4, measurementdict, surveyscol, ms)

        # Need a stress survey to the measurement. One is wanted.

        fillWithGeneralSurvey("stress_q", 14, 5, measurementdict, surveyscol, ms)

        # Need a depression survey to the measurement. One is wanted.

        fillWithGeneralSurvey("depression_q", 10, 7, measurementdict, surveyscol, ms)

        # Need a physicalAtrb survey to the measurement. One is wanted.

        query = { "$and": [ { "datetime": { "$lt": ms } }, { "type": 8 }]}

        surveys = surveyscol.find(query)

        selectedSurvey = None
        timeMark = None

        for s in surveys:
            if(timeMark == None):
                timeMark = s['datetime']
                selectedSurvey = s
            
            if(timeMark < s['datetime']):
                selectedSurvey = s
            
        
        # If exist the survey, the data is completed
        if(selectedSurvey != None):
            measurementdict['weight'] = selectedSurvey['weight'] 
            measurementdict['height'] = selectedSurvey['height']
        
        # Need a smoke survey to the measurement. One is wanted.

        # Time 24 hours less in millis
        ms24h = ms - 86400000


        query = { "$and": [ { "datetime": { "$lt": ms } }, { "datetime": { "$gt": ms24h} }, { "type": 9 }]}

        surveys = surveyscol.find(query)

        cigarettes = 0
        electronic = 0
        other = 0

        # Add the units in the surveys
        for s in surveys:
            cigarettes += s['cigarettes']
            electronic += s['electronic']
            other += s['other']
            
        measurementdict['smoke_cigarettes'] = cigarettes
        measurementdict['smoke_electronic'] = electronic
        measurementdict['smoke_other'] = other


        # Need a alcohol survey to the measurement. One is wanted.

        query = { "$and": [ { "datetime": { "$lt": ms } }, { "datetime": { "$gt": ms24h} }, { "type": 10 }]}

        surveys = surveyscol.find(query)

        beer = 0
        wine = 0
        winemix = 0
        destilled = 0
        destilledmix = 0
        otherfermented = 0
        other = 0

        for s in surveys:
            beer += s['beer']
            wine += s['wine']
            winemix += s['wineMix']
            destilled += s['destilled']
            destilledmix += s['destilledMix']
            otherfermented += s['otherFermented']
            other += s['other']
            
        measurementdict['alcohol_beer'] = beer
        measurementdict['alcohol_wine'] = wine
        measurementdict['alcohol_winemix'] = winemix
        measurementdict['alcohol_destilled'] = destilled
        measurementdict['alcohol_destilledmix'] = destilledmix
        measurementdict['alcohol_otherfermented'] = otherfermented
        measurementdict['alcohol_other'] = other

        
        # Need a medication surveys (cronic and occasional) to the measurement. One is wanted.

        query = { "$and": [ { "datetime": { "$lt": ms } }, { "datetime": { "$gt": ms24h} }, { "type": 3 }]}

        surveys = surveyscol.find(query)

        for s in surveys:
            for m in s['medication']:
                if 'med_' + m in measurementdict.keys():
                    measurementdict['med_' + m] = 1
        
        # In the case of cronic, is needed take de most near survey
        query = { "$and": [ { "datetime": { "$lt": ms } }, { "type": 0 }]}

        surveys = surveyscol.find(query)
        
        selectedSurvey = None
        timeMark = None

        for s in surveys:
            if(timeMark == None):
                timeMark = s['datetime']
                selectedSurvey = s
            
            if(timeMark < s['datetime']):
                timeMark = s['datetime']
                selectedSurvey = s
        
        if(selectedSurvey != None):
            for m in selectedSurvey['medication']:
                if 'med_' + m in measurementdict.keys():
                    measurementdict['med_' + m] = 1
        
        # Need a weather pollution to the measurement. One is wanted.

        query = { "datetime": { "$lt": ms } }

        weathers = weatherpollutioncol.find(query)
        
        selectedWeather = None
        timeMark = None

        # we look for the closest climate shot to the measurement
        for w in weathers:
            if(timeMark == None):
                timeMark = w['datetime']
                selectedWeather = w
            
            if(timeMark < w['datetime']):
                timeMark = w['datetime']
                selectedWeather = w
        
        # Once we have the climate shot we have to look for its information in the general climate database, because the data is shared among users
        if(selectedWeather != None):
            # The documents are registered without the minutes and sec so as not to saturate the services
            msWeather = zero_min_millis_date(selectedWeather['datetime'])

            query = { "$and": [ { "datetime": msWeather}, { "city": selectedWeather['city'] }]}
            
            weathers = weathergeneralcol.find(query)
            selectedDefWeather = None

            for w in weathers:
                selectedDefWeather = w
            
            # If exist, complete de data measurement
            if(selectedDefWeather != None):
                if 'wind_speed' in selectedDefWeather['weather']:
                    measurementdict['weather_windspeed'] = selectedDefWeather['weather']['wind_speed']
                if 'clouds' in selectedDefWeather['weather']:
                    measurementdict['weather_clouds'] = selectedDefWeather['weather']['clouds']
                if 'temp' in selectedDefWeather['weather']:
                    measurementdict['weather_temp'] = selectedDefWeather['weather']['temp']
                if 'temp_max' in selectedDefWeather['weather']:
                    measurementdict['weather_tempmax'] = selectedDefWeather['weather']['temp_max']
                if 'humidity' in selectedDefWeather['weather']:
                    measurementdict['weather_humidity'] = selectedDefWeather['weather']['humidity']
                if 'pressure' in selectedDefWeather['weather']:
                    measurementdict['weather_pressure'] = selectedDefWeather['weather']['pressure']
                if 'temp_min' in selectedDefWeather['weather']:
                    measurementdict['weather_tempmin'] = selectedDefWeather['weather']['temp_min']
                if 'feels_like' in selectedDefWeather['weather']:
                    measurementdict['weather_feels'] = selectedDefWeather['weather']['feels_like']


                if 'pm10' in selectedDefWeather['pollution']:
                    measurementdict['pollution_pm10'] = selectedDefWeather['pollution']['pm10']
                if 'co' in selectedDefWeather['pollution']:
                    measurementdict['pollution_co'] = selectedDefWeather['pollution']['co']
                if 'o3' in selectedDefWeather['pollution']:
                    measurementdict['pollution_o3'] = selectedDefWeather['pollution']['o3']
                if 'no2' in selectedDefWeather['pollution']:
                    measurementdict['pollution_no2'] = selectedDefWeather['pollution']['no2']
                if 'so2' in selectedDefWeather['pollution']:
                    measurementdict['pollution_so2'] = selectedDefWeather['pollution']['so2']    

        # Save data into file
        write_to_csv_file('database_' + name + '.csv', measurementdict.values(), 'a')
        write_to_csv_file('general_database.csv', [str(number)] + measurementdict.values(), 'a')
    
    # Add 1 to the number of user, not save the name
    number += 1

print('The program is finish')



