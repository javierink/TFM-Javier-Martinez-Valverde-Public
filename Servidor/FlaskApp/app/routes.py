from app import app
from flask import Flask, request
import json
import database_controller
import check_json
import constants
import externalServices

@app.route('/')
@app.route('/index')
def index():
    return "Health Care server"

'''Flask function that controls the rest register'''
@app.route('/register', methods=['POST']) #GET requests will be blocked
def register():
    # Only json is accepted
    if not request.is_json: 
        return constants.CONTEND_ONLY_JSON, 406
    
    # Check if the DB service is active
    if not database_controller.ok_status():
        return constants.CONTEND_DB_STOPED_SERVICE, 503
    
    req_data = request.get_json()

    # Check that there's content in the json
    if len(req_data) == 0: 
            return constants.CONTEND_ARRAY_JSON, 406

    # Check if it's a json list since they don't accept
    if isinstance(req_data, list):
        return constants.CONTEND_NO_ALLOWED_LIST, 406
    
    # Check the json fields
    if not check_json.check_fields_register(req_data):
        return constants.CONTEND_BAD_FIELD, 406

    # Check the content of json fields
    if not check_json.check_regex_register(req_data):
        return constants.CONTEND_BAD_REGEX, 406

    # It checks if the user's database not exists. If return none, then the information is recorded and created a new database
    try:
        database = database_controller.exist(req_data)

        if database != None:
            return constants.CONTEND_USER_EXIST.format(req_data['username']), 406

        database_controller.insert_user_data(req_data)
    except Exception as e:
        print(str(e))
        return constants.CONTEND_DB_ERROR_SERVICE, 503

    # Return a correct message if everything went well
    return constants.CONTEND_USER_CREATE.format(req_data['username']), 201

'''Flask function that controls the rest login'''
@app.route('/login', methods=['POST']) #GET requests will be blocked
def login():
    if not request.is_json: 
        return constants.CONTEND_ONLY_JSON, 406
    
    if not database_controller.ok_status():
        return constants.CONTEND_DB_STOPED_SERVICE, 503
    
    req_data = request.get_json()

    if len(req_data) == 0: 
            return constants.CONTEND_ARRAY_JSON, 406

    if isinstance(req_data, list):
        return constants.CONTEND_NO_ALLOWED_LIST, 406
    
    if not check_json.check_fields_login(req_data):
        return constants.CONTEND_BAD_FIELD, 406

    if not check_json.check_regex_login(req_data):
        return constants.CONTEND_BAD_REGEX, 406

    # It checks if the user's database exists. If the database is returned, then the information is read from the database
    try:
        database = database_controller.exist(req_data)

        if database == None:
            return constants.CONTEND_BAD_USER.format(req_data['username']), 404
            
        dict_pass = database_controller.read_user_data(database)
        del dict_pass["_id"]
        
        if dict_pass["password"] != req_data['password']:
            return constants.CONTEND_BAD_PASS, 401

        return json.dumps(dict_pass), 200

    except:
        return constants.CONTEND_DB_ERROR_SERVICE, 503
    
'''Flask function that controls the rest device'''
@app.route('/device', methods=['POST']) #GET requests will be blocked
def device():
    if not database_controller.ok_status():
        return constants.CONTEND_DB_STOPED_SERVICE, 503
    
    if not request.is_json: 
        return constants.CONTEND_ONLY_JSON, 406
    
    req_data = request.get_json()

    if len(req_data) == 0: 
            return constants.CONTEND_ARRAY_JSON, 406

    # The json list is treated differently than the single json
    if isinstance(req_data, list):

        last_username = None

        for data in req_data:
            if not check_json.check_fields_device(data):
                return constants.CONTEND_BAD_FIELD, 406

            if not check_json.check_regex_device(data):
                return constants.CONTEND_BAD_REGEX, 406

            if last_username == None:
                last_username = data["username"]

            if last_username != data["username"]:
                return constants.CONTEND_DIFERENT_USER, 406
            
            last_username = data["username"]

    else:
        if not check_json.check_fields_device(req_data):
            return constants.CONTEND_BAD_FIELD, 406

        if not check_json.check_regex_device(req_data):
            return constants.CONTEND_BAD_REGEX, 406

    # It checks if the user's database exists. If the database is returned, then the information is inserted in the database
    try:
        database = database_controller.exist(req_data)

        if database == None:
            return constants.CONTEND_BAD_USER.format(database_controller.get_user_json(req_data)), 404

        database_controller.insert_database(req_data, database, "device")
    except:
        return constants.CONTEND_DB_ERROR_SERVICE, 503

    return constants.CONTEND_INSERTED_DEVICE, 201

'''Flask function that controls the rest weather'''
@app.route('/weather', methods=['POST']) #GET requests will be blocked
def weather():
    if not database_controller.ok_status():
        return constants.CONTEND_DB_STOPED_SERVICE, 503
    
    if not request.is_json: 
        return constants.CONTEND_ONLY_JSON, 406
    
    req_data = request.get_json()

    if len(req_data) == 0: 
            return constants.CONTEND_ARRAY_JSON, 406

    if isinstance(req_data, list):
        return constants.CONTEND_ONLY_ONE_JSON, 406

    else:
        if not check_json.check_fields_weather(req_data):
            return constants.CONTEND_BAD_FIELD, 406

        if not check_json.check_regex_weather(req_data):
            return constants.CONTEND_BAD_REGEX, 406

    try:
        database = database_controller.exist(req_data)

        if database == None:
            return constants.CONTEND_BAD_USER.format(database_controller.get_user_json(req_data)), 404
        
        externalServices.insert_weather_pollution(req_data, database)
        #database_controller.insert_database(req_data, database, "weather_polution")
    except Exception as e:
	with open('error.txt', 'a') as writer:
		writer.write(str(e)+'\n')
        return constants.CONTEND_DB_ERROR_SERVICE, 503
            
    return constants.CONTEND_INSERTED_WEATHER, 201

'''Flask function that controls the rest surveys'''
@app.route('/surveys', methods=['POST']) #GET requests will be blocked
def surveys():
    
    if not database_controller.ok_status():
        return constants.CONTEND_DB_STOPED_SERVICE, 503
    
    if not request.is_json: 
        return constants.CONTEND_ONLY_JSON, 406
    
    req_data = request.get_json()

    if len(req_data) == 0: 
            return constants.CONTEND_ARRAY_JSON, 406

    if isinstance(req_data, list):

        last_username = None

        for data in req_data:
            if not check_json.check_fields_survey(data):
                return constants.CONTEND_BAD_FIELD, 406

            if not check_json.check_regex_survey(data):
                return constants.CONTEND_BAD_REGEX, 406
            
            if last_username == None:
                last_username = data["username"]

            if last_username != data["username"]:
                return constants.CONTEND_DIFERENT_USER, 406
            
            last_username = data["username"]
    else:
        if not check_json.check_fields_survey(req_data):
            return constants.CONTEND_BAD_FIELD, 406

        if not check_json.check_regex_survey(req_data):
            return constants.CONTEND_BAD_REGEX, 406

    database = None

    try:
        database = database_controller.exist(req_data)

        if database == None:
            return constants.CONTEND_BAD_USER.format(database_controller.get_user_json(req_data)), 404

        database_controller.insert_database(req_data, database, "surveys")
    except:
        return constants.CONTEND_DB_ERROR_SERVICE, 503
        
    return constants.CONTEND_INSERTED_SURVEY, 201

