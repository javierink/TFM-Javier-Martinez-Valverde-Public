import re

'''Function that checks the format of a json to be a document in the database belonging to the user's data for registration'''
def check_fields_register(json):

    # The number of fields is checked
    if (len(json) != 14):
        return False

    # The names and types of the fields are stored in an array
    array_attrs = ["username", "password","name","surname","birthdate","gender","birthcountry","region","city","zp","homecountry","address","email","educationlevel"]
    array_types = [unicode, unicode, unicode, unicode, int, unicode, unicode, unicode, unicode, int, unicode, unicode, unicode, unicode]
    
    # Each field is checked
    for i in range(0,len(array_attrs)):
        if ((array_attrs[i] not in json) or (not isinstance(json[array_attrs[i]], array_types[i]))):
            return False  

    return True

'''Function that checks the content of each json field through regex for user format'''
def check_regex_register(json):

    # The class regex is created with the regular expression and checked, if it does not match it is returned false.
    regex = re.compile(r"^[a-z]{2,}\d*$")
    if not regex.match(json["username"]):
        return False
    
    # In some fields, just check if the chain is not empty
    if json["password"] == "":
        return False
    
    if json["name"] == "":
        return False
        
    if json["surname"] == "":
        return False
    
    regex = re.compile(r"^-?\d+\d*$")
    if not regex.match(str(json["birthdate"])):
        return False
    
    regex = re.compile(r"^(?:m|M|male|Male|f|F|female|Female|o|O|other|Other)$")
    if not regex.match(json["gender"]):
        return False
    
    '''regex = re.compile(r"^(?:Ethnic1|ethnic1|Ethnic2|ethnic2|Ethnic3|ethnic3)$")
    if not regex.match(json["ethnic"]):
        return False'''

    regex = re.compile(r"^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$")
    if not regex.match(json["email"]):
        return False
    
    regex = re.compile(r"^[1-9][0-9]*$")
    if not regex.match(str(json["zp"])):
        return False
    
    regex = re.compile(r"^(?:No\sstudies|no\sstudies|Primary\sstudies|primary\sstudies|Secondary\sstudies|secondary\sstudies|Professional\straining|professional\straining|Bachelor's\sdegree|bachelor's\sdegree|Master's\sdegree|master's\sdegree|Doctoral\sdegree|doctoral\sdegree)$")
    if not regex.match(json["educationlevel"]):
        return False

    # If it passes all checks, it returns true
    return True

'''Function that checks the format of a json to be a document in the database belonging to the login credentials'''
def check_fields_login(json):

    if (len(json) != 2):
        return False

    array_attrs = ["username", "password"]
    array_types = [unicode, unicode]
    
    for i in range(0,len(array_attrs)):
        if ((array_attrs[i] not in json) or (not isinstance(json[array_attrs[i]], array_types[i]))):
            return False  

    return True

'''Function that checks the content of each json field through regex for login format'''
def check_regex_login(json):

    regex = re.compile(r"^[a-z]{2,}\d*$")
    if not regex.match(json["username"]):
        return False
    
    if json["password"] == "":
        return False

    return True

'''Function that checks the format of a json to be a document in the database belonging to the device mesurement'''
def check_fields_device(json):

    if (len(json) != 6):
        return False

    array_attrs =  ["username", "datetime", "pulse", "pressuresys", "pressuredia", "steps"]
    array_types = [unicode, int, int, int, int, int]
    
    for i in range(0,len(array_attrs)):
        if ((array_attrs[i] not in json) or (not isinstance(json[array_attrs[i]], array_types[i]))):
            return False 

    return True

'''Function that checks the content of each json field through regex for device mesurement format'''
def check_regex_device(json):

    regex = re.compile(r"^[a-z]{2,}\d*$")
    if not regex.match(json["username"]):
        return False
    
    regex = re.compile(r"^-?\d+\d*$")
    if not regex.match(str(json["datetime"])):
        return False
    
    regex = re.compile(r"^[1-9][0-9]*$")
    if not regex.match(str(json["pulse"])):
        return False
    
    regex = re.compile(r"^[1-9][0-9]*$")
    if not regex.match(str(json["pressuresys"])):
        return False
    
    regex = re.compile(r"^[1-9][0-9]*$")
    if not regex.match(str(json["pressuredia"])):
        return False

    return True

'''Function that checks the format of a json to be a document in the database belonging to the weather-pollution mesurement'''
def check_fields_weather(json):

    if (len(json) < 4):
        return False

    array_attrs = ["username", "datetime", "lon", "lat"]
    array_types = [unicode, int, float, float]
    
    for i in range(0,len(array_attrs)):
        if ((array_attrs[i] not in json) or (not isinstance(json[array_attrs[i]], array_types[i]))):
            return False  

    return True

'''Function that checks the content of each json field through regex for weather_pollution mesurement format'''
def check_regex_weather(json):

    regex = re.compile(r"^[a-z]{2,}\d*$")
    if not regex.match(json["username"]):
        return False
    
    regex = re.compile(r"^-?\d+\d*$")
    if not regex.match(str(json["datetime"])):
        return False
    
    return True

'''Function that checks the format of a json to be a document in the database belonging to the survey'''
def check_fields_survey(json):

    if (len(json) < 3):
        return False

    if "username" not in json:
        return False

    if "datetime" not in json:
        return False
    
    if "type" not in json:
        return False
    

    array_attrs =  ["username", "datetime", "type"]
    array_types = [unicode, int, int]

    for i in range(0,len(array_attrs)):
        if ((array_attrs[i] not in json) or (not isinstance(json[array_attrs[i]], array_types[i]))):
            return False 


    return True

'''Function that checks the content of each json field through regex for survey format'''
def check_regex_survey(json):

    regex = re.compile(r"^[a-z]{2,}\d*$")
    if not regex.match(json["username"]):
        return False
    
    regex = re.compile(r"^-?\d+\d*$")
    if not regex.match(str(json["datetime"])):
        return False
    
    return True
