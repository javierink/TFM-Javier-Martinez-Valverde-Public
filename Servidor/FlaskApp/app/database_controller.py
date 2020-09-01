import pymongo

'''Service address'''
'''UserYPassNoDisponibleEnRepositorioPublico'''
host_name = "mongodb://user:pass0@localhost:27017/"

'''Function returned by the user of a json, either a list or a single json'''
def get_user_json(json):
    if isinstance(json, list):
        return json[0]["username"]
    else:
        return json["username"]

'''Function that returns a Boolean, queries the service status of the database'''
def ok_status():
    try:
        # ther server is active?
        myclient = pymongo.MongoClient(host_name,serverSelectionTimeoutMS = 2000)
        myclient.server_info()
        return True
    except pymongo.errors.ServerSelectionTimeoutError as err:
        # the server is stop
        return False

'''
    Function that returns a database object or none, queries the existence of a database created for a user, if exist return db.
    The database is returned because it is more efficient to pick it up only once
'''
def exist(json):
    
    if isinstance(json, list):
        database_name = "database_" + json[0]["username"]
    else:
        database_name = "database_" + json["username"]

    myclient = pymongo.MongoClient(host_name)

    dblist = myclient.list_database_names()

    if database_name in dblist:
        return myclient[database_name]
    else:
        return None

'''Function that inserts a new user into a database, which should be completely new'''
def insert_user_data(json):

    database_name = "database_" + json["username"]

    myclient = pymongo.MongoClient(host_name)

    mydb = myclient[database_name]

    mycol = mydb["user_data"]

    return mycol.insert_one(json)

'''Function that reads a user's data in your database'''
def read_user_data(database):

    mycol = database["user_data"]

    return mycol.find_one()

'''Function that inserts json data into the user's database as either a list or a single json. It adds it to the collection specified by arguments'''
def insert_database(json, database, collection):

    mycol = database[collection]

    if isinstance(json, list):
        mycol.insert_many(json)
    else:
        mycol.insert_one(json)

def exist_in_weather_pollution(ms, country, city):

    myclient = pymongo.MongoClient(host_name)

    mydb = myclient["general_weather_pollution_database"]

    mycol = mydb["general"]

    myquery = { "datetime": ms,  "country": country, "city": city}

    mydoc = mycol.find(myquery)

    return mydoc.count() > 0

def insert_weather_pollution(user_json, user_database, user_collection, general_json):

    myclient = pymongo.MongoClient(host_name)

    general_db = myclient["general_weather_pollution_database"]

    general_col = general_db["general"]

    mycol = user_database[user_collection]

    if general_json != None:
        general_col.insert_one(general_json)
    
    mycol.insert_one(user_json)
